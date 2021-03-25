package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Direction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.util.ArrayList;

/**
 * @authors Matthew Harris, Nick Coutts, Jay McCracken, Kevin Belanger
 *
 */
public class ElevatorRPCRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private RPCRequestType requestType;
	private boolean isDoorOpen;
	private int currentLocation;
	private int id;
	private int errorCode;
	private Direction motorDirection;
	private int destination;
	private ArrayList<Integer> lamps;
	
	/**
	 * constructor for a open door request
	 * @param isDoorOpen the current state of the elevator door
	 * @param id the id of the elevator making the request
	 */
	public ElevatorRPCRequest(boolean isDoorOpen, int id) {
		this.requestType = RPCRequestType.TOGGLE_DOORS;
		this.isDoorOpen = isDoorOpen;
		this.id = id;
	}
	
	/**
	 * constructor for a request to get a new destination
	 * @param currentLocation the elevators current location
	 * @param id the id of the elevator making the request
	 */
	public ElevatorRPCRequest(int currentLocation, int id) {
		this.requestType = RPCRequestType.GET_REQUEST;
		this.currentLocation = currentLocation;
		this.id = id;
	}
	
	/**
	 * constructor for a request to get the lamps that need to change
	 * @param id the id of the elevator making the request
	 */
	public ElevatorRPCRequest(int id, RPCRequestType type) {
		this.requestType = type;
		this.id = id;
		this.lamps = new ArrayList<>();
	}
	
	/**
	 * constructor for a request that tells the floor subsystem to change the
	 * floor lamps
	 * @param currentLocation the current location of the elevator making the request
	 * @param motorDirection the direction the elevator is going
	 */
	public ElevatorRPCRequest(int currentLocation, Direction motorDirection) {
		this.requestType = RPCRequestType.SET_LAMPS;
		this.currentLocation = currentLocation;
		this.motorDirection = motorDirection;
	}
	
	/**
	 * @return the type of request the elevator is sending
	 */
	public RPCRequestType getRequestType() {
		return requestType;
	}
	
	/**
	 * @return the boolean state of the door
	 */
	public boolean getIsDoorOpen() {
		return isDoorOpen;
	}
	
	/**
	 * @return the current location of the elevator making the request
	 */
	public int getCurrentLocation() {
		return currentLocation;
	}
	
	/**
	 * @return the id of the elevator making the request
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the list of lamps that need to change
	 */
	public ArrayList<Integer> getLamps() {
		return lamps;
	}

	/**
	 * sets the list on lamps that need to change
	 * @param lamps the list of lamps to set
	 */
	public void setLamps(ArrayList<Integer> lamps) {
		this.lamps = lamps;
	}

	/**
	 * @return the destination for the floor the elevator is going too
	 */
	public int getDestination() {
		return destination;
	}
	
	/**
	 * set the variable for the state of the door
	 * @param isDoorOpen the state of the door
	 */
	public void setDoor(boolean isDoorOpen) {
		this.isDoorOpen = isDoorOpen;
	}
	
	/**
	 * sets the variables for the integer destination and the direction
	 * of the elevator
	 * @param destFloor the destination floor
	 * @param direction the direction that floor is in compared to the current elevator location
	 * @param errorCode the error code for that request, either 0,1,2, or 3
	 */
	public void setDestination(int destFloor, Direction direction, int errorCode) { //needed because map.entry is not serializable
		this.destination = destFloor;
		this.motorDirection = direction;
		this.errorCode = errorCode;
	}
	/**
	 * @return the error code
	 */
	public Integer getErrorCode() {
		return this.errorCode;
	}
	
	/**
	 * @return the direction the elevator is traveling
	 */
	public Direction getMotorDirection() {
		return motorDirection;
	}
	
	/**
	 * generates the request from the given packet
	 * @param receivePacket the packet that the request is read and made from
 	 * @return a ElevatorRPCRequest based on the packet
	 */
	public static ElevatorRPCRequest requestFromPacket(DatagramPacket receivePacket) {
		ByteArrayInputStream stream = new ByteArrayInputStream(receivePacket.getData());
        ObjectInputStream oStream;
        ElevatorRPCRequest response = null;
		try {
			oStream = new ObjectInputStream(stream);
			response = (ElevatorRPCRequest) oStream.readObject();
	        oStream.close();
	        return response;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
        
        return null;  //never called, needed for structure
	}

}
