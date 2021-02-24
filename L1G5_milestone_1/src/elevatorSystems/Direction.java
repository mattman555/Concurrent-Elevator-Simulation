/**
 * 
 */
package elevatorSystems;

/**
 * @author Matthew Harris 101073502
 *
 */
public enum Direction {
	UP,
	DOWN, 
	STATIONARY;
	
	/**
	 * converts string to direction
	 * @return the direction that was contained by the string
	 */
	public static Direction stringToDirection(String direction) {
		if(direction.toUpperCase().equals("UP"))
		{
			return Direction.UP;
		}
		else if(direction.toUpperCase().equals("DOWN"))
		{
			return Direction.DOWN;
		}
		else {
			return null;	
		}
	}
}
