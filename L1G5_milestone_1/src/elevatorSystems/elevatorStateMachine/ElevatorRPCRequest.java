package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Direction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ElevatorRPCRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private RPCRequestType requestType;
	private boolean isDoorOpen;
	private int currentLocation;
	private int id;
	private Direction motorDirection;
	private int destination;
	private ArrayList<Integer> lamps;
	
	
	public ElevatorRPCRequest(boolean isDoorOpen, int id) {
		this.requestType = RPCRequestType.TOGGLE_DOORS;
		this.isDoorOpen = isDoorOpen;
		this.id = id;
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

	public int getDestination() {
		return destination;
	}
	
	public void setDoor(boolean isDoorOpen) {
		this.isDoorOpen = isDoorOpen;
	}
	
	public void setDestination(Entry<Integer, Direction> destination) { //needed becuase map.entry is not serializable
		this.destination = destination.getKey();
		this.motorDirection = destination.getValue();
	}

	public Direction getMotorDirection() {
		return motorDirection;
	}
	
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
