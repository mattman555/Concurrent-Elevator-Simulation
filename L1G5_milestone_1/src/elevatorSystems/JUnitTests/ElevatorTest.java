package elevatorSystems.JUnitTests;

import static org.junit.jupiter.api.Assertions.*;

import elevatorSystems.Elevator;
import elevatorSystems.Scheduler;
/**
 * 
 * @author Jay McCracken 101066860
 *
 */
public class ElevatorTest {
	
	private Elevator elevatorMethods;
	
	@org.junit.Before
	public void setUp() throws Exception {
		elevatorMethods = new Elevator(1,14000,14002, null, null, 14003, null);
	}

	/**
	 * Is the elevator is the right location
	 */
	@org.junit.Test
	public void testElevatorLocation() {
		assertEquals(elevatorMethods.getElevatorLocation(), 1, "Should be on floor 1");	
		elevatorMethods.setElevatorLocation(7);
		assertEquals(elevatorMethods.getElevatorLocation(), 7, "Should get floor 7");	
	}
}
