package elevatorSystems.elevatorStateMachine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import elevatorSystems.ConfigReader;
import elevatorSystems.Direction;
import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 * @author Jay McCracken 101066860
 * @author Nick Coutts 101072875
 * @author Kevin Belanger 101121709
 *
 *	The State machine for the elevator, switching states based on event
 */
public class ElevatorSM implements Runnable{

	private ElevatorStates current;
	private Hashtable<ElevatorStates, ElevatorState> states;
	private Hashtable<ElevatorStates, List<ElevatorStates>> transitions;
	private final static String CONFIG = "Config.txt";
	private static final int INVALID_FLOOR = 10000;
	private int timeBetweenFloors;
	private int timeToUnloadPassengers;
	private int errorCode;
	private Elevator elevator;
	private DatagramSocket sendReceiveSocket;
	private DatagramSocket sendSocket;
;

	public ElevatorSM(Elevator elevator, int timeBetweenFloors, int timeToUnloadPassengers) {
		this.elevator =  elevator;
		this.timeBetweenFloors = timeBetweenFloors;
		this.timeToUnloadPassengers = timeToUnloadPassengers;
		this.current = ElevatorStates.DOORS_CLOSED;
		generateTransitionHashmap();
		generateStateHashmap();
		try {
			sendReceiveSocket = new DatagramSocket();
			sendSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	 
	/**
	 * Creating the order of states, what state can switch to what state
	 */
	private void generateTransitionHashmap() {
		this.transitions = new Hashtable<>();
		this.transitions.put(ElevatorStates.DOORS_CLOSED,  List.of(ElevatorStates.ARRIVED, ElevatorStates.MOVING, ElevatorStates.END));
		this.transitions.put(ElevatorStates.MOVING, List.of(ElevatorStates.ARRIVED));
		this.transitions.put(ElevatorStates.ARRIVED, List.of(ElevatorStates.DOORS_OPEN));
		this.transitions.put(ElevatorStates.DOOR_STUCK, List.of(ElevatorStates.ARRIVED));
		this.transitions.put(ElevatorStates.DOORS_OPEN, List.of(ElevatorStates.UPDATE_LAMPS));
		this.transitions.put(ElevatorStates.UPDATE_LAMPS, List.of(ElevatorStates.DOORS_CLOSED));
	}
	
	/**
	 * Matching the class states the reference each of the different states
	 */
	private void generateStateHashmap() {
		this.states = new Hashtable<>();
		this.states.put(ElevatorStates.DOORS_CLOSED, new DoorsClosed(this.elevator));
		this.states.put(ElevatorStates.MOVING,  new Moving(this.elevator, this.timeBetweenFloors));
		this.states.put(ElevatorStates.ARRIVED,  new Arrived(this.elevator));
		this.states.put(ElevatorStates.DOORS_OPEN, new DoorsOpen(this.elevator));
		this.states.put(ElevatorStates.DOOR_STUCK, new DoorStuck(this.elevator));
		this.states.put(ElevatorStates.UPDATE_LAMPS, new UpdateLamps(this.elevator));
		this.states.put(ElevatorStates.END, new End(this.elevator));
	}
	
	public Elevator getElevator() {
		return this.elevator;
	}
	
	/**
	 * Moving the elevator to the next state if it can
	 * @param nextState the state it tries to switch to
	 */
	private void nextState(ElevatorStates nextState) {
		if(transitions.get(current).contains(nextState))
			current = nextState; //else throw exception?
    }
	
	/**
	 * The movement of the elevator to the next floor by 1 based on the direction given
	 * @param direction, If the elevator is going UP or DOWN
	 */
	public void activity(Direction direction, DatagramSocket socket) {
		states.get(current).activity(direction, socket);
	}
	
	/**
	 * Turning on and off the car lamps based on the current location and the 
	 * requested new floors
	 * @param lamps, the list of lamps that need to be turned on
	 */
	public void action(ArrayList<Integer> lamps) {
		states.get(current).action(lamps);
	}
	
	/**
	 * When a valid request is made set to go to the new destination
	 * switching to the new state MOVING
	 * @param destination, where the elevator needs to go next
	 */
	public void validRequest(Entry<Integer,Direction> destination) {
		states.get(current).validRequest(destination);
		nextState(ElevatorStates.MOVING);
	}
	
	/**
	 * When a invalid request is sent, switch to the END state
	 */
	public void invalidRequest() {
		states.get(current).invalidRequest();
		nextState(ElevatorStates.END);
	}
	
	/**
	 * When a error shutdown occurs, switch to the END state
	 */
	public void shutdown(DatagramSocket socket) {
		states.get(current).shutdown(socket);
		nextState(ElevatorStates.END);
	}
	
	/**
	 * When the elevator arrives at the required destination
	 * switch to arrived state
	 */
	public void arrivesAtDestination() {
		states.get(current).arrivesAtDestination(sendReceiveSocket);
		nextState(ElevatorStates.ARRIVED);
	}
	
	/**
	 * Toggling the doors of the elevator to open or closed based on what it last was
	 * @param next the state of the door that was switched too
	 */
	public void toggleDoors(ElevatorStates next) {
		states.get(current).toggleDoors(sendReceiveSocket);
		nextState(next);
	}
	
	/**
	 * Getting the car lamps that are being requested, switching to update lamps
	 * @return the list of lamps that need to be turned on
	 */
	public ArrayList<Integer> getLamps() {
		ArrayList<Integer> lamps = states.get(current).getLamps(sendReceiveSocket);
		nextState(ElevatorStates.UPDATE_LAMPS);
		return lamps;
	}
	
	/**
	 * exit state to terminate the state machine
	 */
	public void exit(DatagramSocket socket) {
		states.get(current).exit(socket);
	}
	
	/**
	 * exit state to terminate the state machine
	 */
	public void errorExit() {
		states.get(current).errorExit(sendReceiveSocket);
	}
	
	/**
	 * If an type 1 error occurs, the doors are stuck transition to DoorStuck state
	 * @param next the state to go to after
	 */
	public void doorStuckError(ElevatorStates next) {
		states.get(current).doorStuckError();
		nextState(next);
	}
	
	/**
	 * Make the thread wait 5 seconds then transition back to arrived state
	 * @param next
	 */
	public void doorWait(ElevatorStates next, DatagramSocket socket) {
		states.get(current).doorWait(socket);
		nextState(next);
	}
	
	/**
	 * to get the current state of the elevator
	 * @return current state
	 */
	public ElevatorStates getState() {
		return current;
	}
	
	/**
	 * Sends an packet with a ElevatorRPCRequest type get request, asking for information of where to 
	 * go next, Then waits for a response and returns the received RPCRequest 
	 * @return the new destination and motor direction of the elevator
	 */
	private ElevatorRPCRequest requestTask(){
		DatagramPacket requestPacket = this.elevator.generatePacket(RPCRequestType.GET_REQUEST);
		try {
	         sendReceiveSocket.send(requestPacket);
	    }
		catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	    }
		
		byte data[] = new byte[1000];
	    DatagramPacket receivePacket = new DatagramPacket(data, data.length);

	    try {
	         // Block until a datagram is received via sendReceiveSocket.  
	         sendReceiveSocket.receive(receivePacket);
	    } catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    }
	    return this.elevator.readResponse(receivePacket);	
	}
	
	@Override
	/**
	 * The running of the elevator, travel to new floor, updating lamps
	 */
	public void run() {
		/*
		 * until thread is told there is no more requests
		 */
		int destinationFloor = 1;
		Direction destinationDirection = Direction.STATIONARY;
		ArrayList<Integer> lamps = null;
		while (true) {
			switch(current) {
			case DOORS_CLOSED:
				//Get the request of the next floor with the motor direction from the scheduler
				ElevatorRPCRequest request = requestTask();
				int destFloor = request.getDestination();
				Direction motorDir = request.getMotorDirection();
				errorCode = request.getErrorCode();
				this.elevator.setErrorCode(errorCode);
				System.out.println("Elevator " + elevator.getId() + ": error code " + errorCode + " for next destination");
				if(destFloor == INVALID_FLOOR) {//no more requests move to end
					this.invalidRequest();
				}
				else if (errorCode == 2) {
					this.shutdown(sendSocket);
				}
				else if(motorDir == Direction.UP || motorDir == Direction.DOWN) {
					destinationFloor = destFloor;
					destinationDirection = motorDir;
					this.validRequest(Map.entry(destinationFloor, destinationDirection));
				}
				else if(motorDir == Direction.STATIONARY) {
					destinationDirection = request.getRequestDirection();
					elevator.setMotor(destinationDirection);
					this.arrivesAtDestination();
				}
				break;
			case MOVING:
				if(destinationFloor == this.elevator.getElevatorLocation()) {
					this.arrivesAtDestination();
				}
				else if(destinationFloor > this.elevator.getElevatorLocation() || destinationFloor < this.elevator.getElevatorLocation()) {
					this.activity(destinationDirection,sendSocket);
				}
				break;
			case ARRIVED:
				if (errorCode == 1) { //sending to doors stuck state to restart doors before continuing
					errorCode = 0;
					this.doorStuckError(ElevatorStates.DOOR_STUCK);
					break;
				}
				this.toggleDoors(ElevatorStates.DOORS_OPEN);
				break;
			case DOOR_STUCK:
				this.doorWait(ElevatorStates.ARRIVED,sendSocket);
				break;
			case DOORS_OPEN:
				lamps = this.getLamps();
				break;
			case UPDATE_LAMPS:
				this.action(lamps);
				this.toggleDoors(ElevatorStates.DOORS_CLOSED);
				break;
			case END:
				if (errorCode == 2) { //ending just on elevator when a shutdown error occurs
					errorCode = 0;
					this.errorExit();
					sendReceiveSocket.close();
					sendSocket.close();
					return;
				}
				this.exit(sendSocket);
				sendReceiveSocket.close();
				sendSocket.close();
				return;
			}
		}
	}
	
	public static void main(String[] args) {
		ConfigReader configs = new ConfigReader(CONFIG);
		int numElevators = configs.getNumElevators();
		int schedulerPort = configs.getElevToSchedulerPort();
		int floorPort = configs.getElevToFloorPort();
		int guiPort = configs.getGUIPort();
		InetAddress schedulerIp = configs.getSchedulerIp();
		InetAddress floorIp = configs.getFloorIp();
		InetAddress guiIp = configs.getGUIIP();
		int timeBetweenFloors = configs.getTimeBetweenFloors();
		int timeToUnload = configs.getTimeToUnloadPassengers();
		for (int i=1; i <= numElevators; i++) {
			Thread elevatorThread = new Thread(new ElevatorSM(new Elevator(i,schedulerPort,floorPort,schedulerIp,floorIp,guiPort,guiIp),timeBetweenFloors,timeToUnload),"Elevator " + i);
			elevatorThread.start();
		}
	}
}
