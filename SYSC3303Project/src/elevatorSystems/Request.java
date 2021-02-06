/**
 * 
 */
package elevatorSystems;

/**
 * @author Matthew Harris 101073502
 *
 */
public class Request {

	private int[] time;
	private int floor;
	private Direction floorButton;
	private int carButton;
	
	public Request(String time, int floor, String floorButton, int carButton) {
		this.time = new int[4];
		String[] timeArr = time.split(":");
		this.time[0] = Integer.parseInt(timeArr[0]);
		this.time[1] = Integer.parseInt(timeArr[1]);
		timeArr = timeArr[2].split("\\.");
		this.time[2] = Integer.parseInt(timeArr[0]);
		this.time[3] = Integer.parseInt(timeArr[1]);
		this.floor = floor;
		this.floorButton = Direction.stringToDirection(floorButton);
		this.carButton = carButton;
	}

	/**
	 * @return the time
	 */
	public int[] getTime() {
		return time;
	}

	/**
	 * @return the floor
	 */
	public int getFloor() {
		return floor;
	}

	/**
	 * @return the floorButtons
	 */
	public Direction getFloorButton() {
		return floorButton;
	}

	/**
	 * @return the carButton
	 */
	public int getCarButton() {
		return carButton;
	}
	
	public String toString() {
		String time = Integer.toString(this.time[0]) + ":" + Integer.toString(this.time[1]) + ":"  + Integer.toString(this.time[2]) + ":" + Integer.toString(this.time[3]);
		return ("Request{Time: " + time + "| Floor: " + this.floor + "| Directional button: " + this.floorButton + "| Destination: " + this.carButton + "}");
		
	}

}
