package elevatorSystems.JUnitTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Map;

import elevatorSystems.Direction;
import elevatorSystems.Elevator;
import elevatorSystems.Request;
import elevatorSystems.Scheduler;

public class SchedulerTest {
	private Elevator elevatorMethods;
	private Scheduler scheduler;
	private Request request1, request2;

	@org.junit.Before
	public void setUp() throws Exception {
		scheduler = new Scheduler();
		elevatorMethods = new Elevator(scheduler);
		request1 = new Request("14:05:15.0",2,"UP",7);
		request2 = new Request("14:05:15.0",3,"UP",7);
	}

	@org.junit.Test
	public void testAddElevator() {
		scheduler.addElevator(elevatorMethods);
		assertEquals(elevatorMethods, scheduler.getElevator(), "Should add the evelvator elevatorMethods");
	}
	
	@org.junit.Test
	public void testDoorChange() {
		scheduler.addElevator(elevatorMethods);		
		assertEquals(false, elevatorMethods.getIsDoorsOpen(), "The doors should be closed");
		scheduler.requestDoorChange();
		assertEquals(true, elevatorMethods.getIsDoorsOpen(), "The doors should be open");
		scheduler.requestDoorChange();
		assertEquals(false, elevatorMethods.getIsDoorsOpen(), "The doors should be closed");
	}	
}
