package elevatorSystems;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElevatorTest {
	
	private Elevator elevatorMethods;
	private Scheduler scheduler;
	
	@BeforeEach
	void setUp() throws Exception {
		scheduler = new Scheduler();
		elevatorMethods = new Elevator(scheduler);
		scheduler.addElevator(elevatorMethods);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Is the elevator is the right location
	 */
	@Test
	void testElevatorLocation() {
		assertEquals(elevatorMethods.getElevatorLocation(), 1, "Should be on floor 1");	
		elevatorMethods.setElevatorLocation(7);
		assertEquals(elevatorMethods.getElevatorLocation(), 7, "Should get floor 7");	
	}
	
	/**
	 * Do the doors toggle correctly
	 */
	@Test
	void testToggleDoors() {
		assertEquals(elevatorMethods.getIsDoorsOpen(), false, "Doors should be closed");	
		scheduler.requestDoorChange();
		assertEquals(elevatorMethods.getIsDoorsOpen(), true, "Doors should be open");
	}
}