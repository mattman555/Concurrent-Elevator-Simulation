package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Direction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map.Entry;

public class ElevatorRPCRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private RPCRequestType requestType;
	private boolean isDoorOpen;
	private int currentLocation;
	private int id;
	private Direction motorDirection;
	private Entry<Integer,Direction> destination;
	private ArrayList<Integer> lamps;
	
	
	public ElevatorRPCRequest(boolean isDoorOpen) {
		this.requestType = RPCRequestType.TOGGLE_DOORS;
		this.isDoorOpen = isDoorOpen;
	}
	
	public ElevatorRPCRequest(int currentLocation, int id) {
		this.requestType = RPCRequestType.GET_REQUEST;
		this.currentLocation = currentLocation;
		this.id = id;
	}
	
	public ElevatorRPCRequest(int id) {
		this.requestType = RPCRequestType.GET_LAMPS;
		this.id = id;
		this.lamps = new ArrayList<>();
	}
	
	public ElevatorRPCRequest(int currentLocation, Direction motorDirection) {
		this.requestType = RPCRequestType.SET_LAMPS;
		this.currentLocation = currentLocation;
		this.motorDirection = motorDirection;
	}
	
	public RPCRequestType getRequestType() {
		return requestType;
	}
	
	public boolean getIsDoorOpen() {
		return isDoorOpen;
	}
	
	public int getCurrentLocation() {
		return currentLocation;
	}
	
	public int getId() {
		return id;
	}
	
	public ArrayList<Integer> getLamps() {
		return lamps;
	}

	public void setLamps(ArrayList<Integer> lamps) {
		this.lamps = lamps;
	}

	public Entry<Integer, Direction> getDestination() {
		return destination;
	}
	
	public void setDoorOpen(boolean isDoorOpen) {
		this.isDoorOpen = isDoorOpen;
	}
	
	public void setDestination(Entry<Integer, Direction> destination) {
		this.destination = destination;
	}

	public Direction getMotorDirection() {
		return motorDirection;
	}

}
