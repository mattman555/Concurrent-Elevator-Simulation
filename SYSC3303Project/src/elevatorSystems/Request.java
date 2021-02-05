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
	private Direction floorButtons;
	private int carButton;
	
	public Request(String time, int floor, String floorButtons, int carButton) {
		String[] timeArr = time.split(":");
		this.time[0] = Integer.parseInt(timeArr[0]);
		this.time[1] = Integer.parseInt(timeArr[1]);
		timeArr = timeArr[2].split(".");
		this.time[2] = Integer.parseInt(timeArr[0]);
		this.time[3] = Integer.parseInt(timeArr[1]);
		this.floor = floor;
		this.floorButtons = Direction.stringToDirection(floorButtons);
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
	public Direction getFloorButtons() {
		return floorButtons;
	}

	/**
	 * @return the carButton
	 */
	public int getCarButton() {
		return carButton;
	}

}
