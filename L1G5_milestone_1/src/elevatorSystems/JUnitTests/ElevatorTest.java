package elevatorSystems.JUnitTests;

import static org.junit.jupiter.api.Assertions.*;

import elevatorSystems.Elevator;
import elevatorSystems.Logger;
import elevatorSystems.Scheduler;
/**
 * 
 * @author Jay McCracken 101066860
 *
 */
public class ElevatorTest {
	
	private Elevator elevatorMethods;
	private Scheduler scheduler;
	
	@org.junit.Before
	public void setUp() throws Exception {
		elevatorMethods = new Elevator(new Logger("elevatorTestOutput.txt"),1);
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
