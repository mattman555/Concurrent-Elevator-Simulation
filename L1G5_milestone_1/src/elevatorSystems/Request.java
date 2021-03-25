/**
 * 
 */
package elevatorSystems;

import java.io.Serializable;

/**
 * @author Matthew Harris 101073502
 *
 */
public class Request implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1385846343311119897L;
	private int[] time;
	private int floor;
	private Direction floorButton;
	private int carButton;
	private int errorCode;
	
	/**
	 * Constructor for a new Request
	 * @param time A string representation of a time in the format hh:mm:ss.mmm
	 * @param floor the initial floor that a request comes form
	 * @param floorButton the Direction the request wants to go
	 * @param carButton the requested destination
	 */
	public Request(String time, int floor, String floorButton, int carButton, int errorType) {
		this.time = new int[4];
		String[] timeArr = time.split(":");
		this.time[0] = Integer.parseInt(timeArr[0]);
		this.time[1] = Integer.parseInt(timeArr[1]);
		timeArr = timeArr[2].split("\\."); //split on periods
		this.time[2] = Integer.parseInt(timeArr[0]);
		this.time[3] = Integer.parseInt(timeArr[1]);
		this.floor = floor;
		this.floorButton = Direction.stringToDirection(floorButton);
		this.carButton = carButton;
		this.errorCode = errorType;
	}

	/**
	 * Return an array where the first element is the hours, second element is the minutes, third element is seconds, and the fourth element is the milliseconds
	 * @return the time
	 */
	public int[] getTime() {
		return time;
	}

	/**
	 * returns the floor the request was initiated from
	 * @return the floor the request was initiated from
	 */
	public int getFloor() {
		return floor;
	}

	/**
	 * Returns the Direction the request wants to go
	 * @return the floorButton representing the Direction the request is going
	 */
	public Direction getFloorButton() {
		return floorButton;
	}

	/**
	 * Gets the destination of the current request
	 * @return the carButton which is the destination of the request
	 */
	public int getCarButton() {
		return carButton;
	}

	/**
	 * @return the errorType
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorType to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * checks that the request is the same as another
	 * @param req2 the request to compare against
	 * @return whether the 2 requests are the same in all data
	 */
	public boolean equals(Request req2) {
		boolean time = (this.time[0] == req2.time[0]) && (this.time[1] == req2.time[1]) && (this.time[2] == req2.time[2]) && (this.time[3] == req2.time[3]);
		boolean buttons = this.carButton == req2.carButton;
		boolean floor = this.floor == req2.floor;
		boolean direction = this.floorButton == req2.floorButton;
		boolean errorType = this.errorCode == req2.errorCode;
		return (time && buttons && floor && direction && errorType);
		
	}
	/**
	 * Returns a string representation of the request
	 */
	public String toString() {
		String time = Integer.toString(this.time[0]) + ":" + Integer.toString(this.time[1]) + ":"  + Integer.toString(this.time[2]) + "." + Integer.toString(this.time[3]);
		return ("Request{Time: " + time + " | Floor: " + this.floor + " | Directional button: " + this.floorButton + " | Destination: " + this.carButton + "| ErrorCode: "+this.errorCode+"}");
		
	}
}
