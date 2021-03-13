/**
 * 
 */
package elevatorSystems.JUnitTests;

import static org.junit.jupiter.api.Assertions.*;

import elevatorSystems.Direction;
import elevatorSystems.FloorSubsystem;

/**
 * @author Matthew Harris 101073502
 *
 */
public class FloorSubsystemTest {
	private FloorSubsystem floorSubsystemMethods;
	/**
	 * @throws java.lang.Exception
	 */
	@org.junit.Before
	public void setUp() throws Exception {
		floorSubsystemMethods = new FloorSubsystem(7);
	}

	/**
	 * Test method for {@link elevatorSystems.FloorSubsystem#FloorSubsystem(elevatorSystems.Scheduler, int)}.
	 */
	@org.junit.Test
	public void testFloorSubsystem() {
		assertNotNull(new FloorSubsystem(7));
	}

	/**
	 * Test method for {@link elevatorSystems.FloorSubsystem#setFloorLamp(int, elevatorSystems.Direction, boolean)}.
	 */
	@org.junit.Test
	public void testSetFloorLamp() {
		floorSubsystemMethods.setFloorLamp(1, Direction.UP, true);
		assertTrue(floorSubsystemMethods.getFloorLamp().get("1UP"));
		floorSubsystemMethods.setFloorLamp(1, Direction.UP, false);
		assertFalse(floorSubsystemMethods.getFloorLamp().get("1UP"));
		floorSubsystemMethods.setFloorLamp(1, Direction.DOWN, true);
		assertFalse(floorSubsystemMethods.getFloorLamp().get("1UP"));
		assertTrue(floorSubsystemMethods.getFloorLamp().get("1DOWN"));
	}

}
