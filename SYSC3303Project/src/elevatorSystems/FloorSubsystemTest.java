/**
 * 
 */
package elevatorSystems;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Matthew Harris 101073502
 *
 */
class FloorSubsystemTest {
	private FloorSubsystem floorSubsystemMethods;
	private Scheduler scheduler;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		scheduler = new Scheduler();
		floorSubsystemMethods = new FloorSubsystem(scheduler, 7);
	}

	/**
	 * Test method for {@link elevatorSystems.FloorSubsystem#FloorSubsystem(elevatorSystems.Scheduler, int)}.
	 */
	@Test
	void testFloorSubsystem() {
		assertNotNull(new FloorSubsystem(scheduler,7));
	}

	/**
	 * Test method for {@link elevatorSystems.FloorSubsystem#setFloorLamp(int, elevatorSystems.Direction, boolean)}.
	 */
	@Test
	void testSetFloorLamp() {
		floorSubsystemMethods.setFloorLamp(1, Direction.UP, true);
		assertTrue(floorSubsystemMethods.getFloorLamp().get("1UP"));
		floorSubsystemMethods.setFloorLamp(1, Direction.UP, false);
		assertFalse(floorSubsystemMethods.getFloorLamp().get("1UP"));
		floorSubsystemMethods.setFloorLamp(1, Direction.DOWN, true);
		assertFalse(floorSubsystemMethods.getFloorLamp().get("1UP"));
		assertTrue(floorSubsystemMethods.getFloorLamp().get("1DOWN"));
	}
	@Test
	void testRun() {
		Thread floorSubsystemThread = new Thread(floorSubsystemMethods, "floorSubsystem");
		floorSubsystemThread.start();
		assertEquals(5, scheduler.getRequest(1));
	}

}
