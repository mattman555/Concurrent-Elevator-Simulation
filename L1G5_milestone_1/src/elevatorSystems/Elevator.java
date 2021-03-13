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
 * @author Jay McCracken 101066860 and Matthew Harris 101073502
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
	private static final int SCHEDULER_PORT = 14000;
	private static final int FLOOR_SUB_PORT = 14002;
	
	/**
	 * Constructor, creating a base elevator starting
	 * on floor 1 with no lamps turned on and door closed
	 * connected to a scheduler object
	 */
	public Elevator( int elevId) {
		this.elevatorLocation = 1;	
		this.floorDestination = 1;
		this.isDoorOpen = false;
		this.lamp = new Hashtable<Integer, Boolean>();
		this.id = elevId;
	}
	
	public int getId() {
		return this.id;
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
	 * updating the elevators current location
	 * @param location is the floor that the elevator just stopped on
	 */
	public void setElevatorLocation(int location){
		this.elevatorLocation = location;
	}
	
	public DatagramPacket generatePacket(RPCRequestType requestType) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutputStream oStream;
		int port = SCHEDULER_PORT;
		try {
			oStream = new ObjectOutputStream(stream);
			switch(requestType) {
				case TOGGLE_DOORS:
					oStream.writeObject(new ElevatorRPCRequest(this.isDoorOpen, this.id));
					break;
				case GET_REQUEST:
					oStream.writeObject(new ElevatorRPCRequest(this.elevatorLocation, this.id));
					break;
				case GET_LAMPS:
					oStream.writeObject(new ElevatorRPCRequest(this.id));
					break;
				case SET_LAMPS:
					oStream.writeObject(new ElevatorRPCRequest(this.elevatorLocation, this.motor));
					port = FLOOR_SUB_PORT;
					break;
				default:
					System.exit(1);	
				
			}
			stream.close();
			oStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] response = stream.toByteArray();
		
		try {
			return new DatagramPacket(response, response.length, InetAddress.getLocalHost(), port);
		}
		catch(UnknownHostException e){
			e.printStackTrace();
			System.exit(1);
		}
		return null; //never called, needed for structure
	}
	
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
