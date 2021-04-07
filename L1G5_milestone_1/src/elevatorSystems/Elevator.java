package elevatorSystems;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;

import elevatorSystems.elevatorStateMachine.ElevatorRPCRequest;
import elevatorSystems.elevatorStateMachine.RPCRequestType;

/**
 * Elevator Thread, keeps track of current location, grabs motor direction
 * and floor destination from scheduler.
 * Keeps track on in car button lamps, turns on and off requested lamps.
 * 
 * @author Jay McCracken 101066860 
 * @author Matthew Harris 101073502
 * @author Nick Coutts 101072875
 * @version 4.00
 */
public class Elevator{
	
	/*
	 * Variables elevator needs to store
	 */
	private int elevatorLocation;
	private int floorDestination;
	private boolean isDoorOpen;
	private Direction motor;
	private Hashtable<Integer, Boolean> lamp;
	private int id;
	private int schedulerPort;
	private int floorPort;
	private int guiPort;
	private int errorCode;
	private InetAddress schedulerIp;
	private InetAddress floorIp;
	private InetAddress guiIp;
	
	/**
	 * Constructor, creating a base elevator starting
	 * on floor 1 with no lamps turned on and door closed
	 * connected to a scheduler object
	 */
	public Elevator(int elevId, int schedulerPort, int floorPort, InetAddress schedulerIp, InetAddress floorIp, int guiPort, InetAddress guiIp) {
		this.schedulerPort = schedulerPort;
		this.floorPort = floorPort;
		this.guiPort = guiPort;
		this.elevatorLocation = 1;	
		this.floorDestination = 1;
		this.isDoorOpen = false;
		this.lamp = new Hashtable<Integer, Boolean>();
		this.id = elevId;
		this.schedulerIp = schedulerIp;
		this.floorIp = floorIp;
		this.guiIp = guiIp;
		this.errorCode = 0;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public int getFloorDestination() {
		return floorDestination;
	}

	public void setFloorDestination(int floorDestination) {
		this.floorDestination = floorDestination;
	}

	public boolean getIsDoorOpen() {
		return isDoorOpen;
	}
	
	public Hashtable<Integer, Boolean> getLamp() {
		return lamp;
	}

	/**
	 * where the elevator currently is
	 * @return	the floor number that the elevator is on
	 */
	public int getElevatorLocation(){
		return elevatorLocation;
	}
	
	/**
	 * Which why the elevator needs to more
	 * @return the Direction the motor in the elevator is running
	 */
	public Direction getMotor(){ //used to talk to floor subsystem
		return motor;
	}
	
	public void setMotor(Direction motor){ //used to talk to floor subsystem
		this.motor = motor;
	}
	/**
	 * Function to toggle if the doors are open
	 * if open, close
	 * if closed, open
	 */
	public void setIsDoorOpen(boolean isDoorOpen) {
		this.isDoorOpen = isDoorOpen;
		System.out.println("Elevator " + this.getId()+" door is "+ (this.isDoorOpen ? "open" : "closed"));
	}
	
	/**
	 * Check if the doors are currently open or not
	 * @return a boolean value if doors open
	 */
	public boolean getIsDoorsOpen() {
		return this.isDoorOpen;
	}
	
	/**
	 * @return the guiPort the port the gui is on
	 */
	public int getGuiPort() {
		return guiPort;
	}
	/**
	 * @return the guiIp the ip of the gui
	 */
	public InetAddress getGuiIp() {
		return guiIp;
	}

	/**
	 * updating the elevators current location
	 * @param location is the floor that the elevator just stopped on
	 */
	public void setElevatorLocation(int location){
		this.elevatorLocation = location;
	}
	
	/**
	 * makes a udp packet to send for the elevator
	 * @param requestType the type of request the packet is for
	 * @return the packet with it's destination and data
	 */
	public DatagramPacket generatePacket(RPCRequestType requestType) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutputStream oStream;
		int port = schedulerPort;
		InetAddress sendToAddress = null; //have to initialize it to null, but it WILL be set in the switch
		try {
			oStream = new ObjectOutputStream(stream);
			switch(requestType) {
				case TOGGLE_DOORS:
					oStream.writeObject(new ElevatorRPCRequest(this.isDoorOpen, this.id));
					sendToAddress = this.schedulerIp;
					break;
				case GET_REQUEST:
					oStream.writeObject(new ElevatorRPCRequest(this.elevatorLocation, this.id));
					sendToAddress = this.schedulerIp;
					break;
				case GET_LAMPS:
					oStream.writeObject(new ElevatorRPCRequest(this.id, RPCRequestType.GET_LAMPS));
					sendToAddress = this.schedulerIp;
					break;
				case SET_LAMPS:
					oStream.writeObject(new ElevatorRPCRequest(this.elevatorLocation, this.motor));
					sendToAddress = this.floorIp;
					port = floorPort;
					break;
				case ELEVATOR_SHUTDOWN:
					oStream.writeObject(new ElevatorRPCRequest(this.id, RPCRequestType.ELEVATOR_SHUTDOWN));
					sendToAddress = this.schedulerIp;
					break;
				default:
					System.out.println("Invalid Request Type: " + requestType);
					System.exit(1);	
				
			}
			stream.close();
			oStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] response = stream.toByteArray();
		
		return new DatagramPacket(response, response.length, sendToAddress, port);
	}
	
	/**
	 * Reads the response that was sent to and 
	 * recreates it to be a RPC request for the elevator to read
	 * @param receivePacket
	 * @return recreated request that the elevator can read
	 */
	public ElevatorRPCRequest readResponse(DatagramPacket receivePacket) {
		ByteArrayInputStream stream = new ByteArrayInputStream(receivePacket.getData());
        ObjectInputStream oStream;
        ElevatorRPCRequest response = null;
		try {
			oStream = new ObjectInputStream(stream);
			response = (ElevatorRPCRequest) oStream.readObject();
			stream.close();
	        oStream.close();
	        return response;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
        
        return null;  //never called, needed for structure
	}
}
