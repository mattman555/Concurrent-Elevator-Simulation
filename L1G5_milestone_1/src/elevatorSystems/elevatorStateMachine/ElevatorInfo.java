/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import java.io.Serializable;

/**
 * @author 1999m
 *
 */
public class ElevatorInfo implements Serializable{

	private int elevatorId;
	private int elevatorLocation;
	private String direction;
	private int errorCode;
	
	public ElevatorInfo(int elevatorId, int elevatorLocation, String direction, int errorCode) {
		this.elevatorId = elevatorId;
		this.elevatorLocation = elevatorLocation;
		this.direction = direction;
		this.errorCode = errorCode;
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
}
