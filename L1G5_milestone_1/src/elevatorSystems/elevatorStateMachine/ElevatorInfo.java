/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * @author 1999m
 *
 */
public class ElevatorInfo implements Serializable{

	private int elevatorId;
	private int elevatorLocation;
	private String direction;
	private int errorCode;
	private Hashtable<Integer, Boolean> lamps;
	
	public ElevatorInfo(int elevatorId, int elevatorLocation, String direction, int errorCode, Hashtable<Integer,Boolean> lamps) {
		this.elevatorId = elevatorId;
		this.elevatorLocation = elevatorLocation;
		this.direction = direction;
		this.errorCode = errorCode;
		this.lamps = lamps;
	}
	
	/**
	 * @return the elevatorId
	 */
	public int getElevatorId() {
		return elevatorId;
	}

	/**
	 * @return the elevatorLocation
	 */
	public int getElevatorLocation() {
		return elevatorLocation;
	}
	
	public String getDirection() {
		return direction;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public Hashtable<Integer, Boolean> getLamps() {
		return lamps;
	}
}
