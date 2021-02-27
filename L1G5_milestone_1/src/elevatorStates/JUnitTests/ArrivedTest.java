package elevatorStates.JUnitTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import elevatorSystems.Elevator;
import elevatorSystems.Scheduler;
import elevatorSystems.elevatorStateMachine.Arrived;

/**
 * @author Jay McCracken 101066860
 *
 */
public class ArrivedTest {
	
	private Elevator elevatorMethods;
	private Scheduler scheduler;
	private Arrived state;

	@org.junit.Before
	public void setUp() throws Exception {
		scheduler = new Scheduler();
		elevatorMethods = new Elevator(scheduler);
		scheduler.addElevator(elevatorMethods);
		state = new Arrived(elevatorMethods);
	}

	/**
	 * Do the doors toffle correctly
	 */
	@org.junit.Test
	public void testToggleDoors() {
		assertEquals(elevatorMethods.getIsDoorsOpen(), false, "The Doors should be closed");	
		state.toggleDoors();
		assertEquals(elevatorMethods.getIsDoorsOpen(), true, "The Doors should be open");
	}

}
