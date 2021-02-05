package elevatorSystems;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchedulerTest {
	private Elevator elevatorMethods;
	private Scheduler scheduler;
	private Request request1, request2;

	@BeforeEach
	void setUp() throws Exception {
		scheduler = new Scheduler();
		elevatorMethods = new Elevator(scheduler);
		request1 = new Request("14:05:15.0",2,"UP",7);
		request2 = new Request("14:05:15.0",3,"UP",7);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testAddElevator() {
		scheduler.addElevator(elevatorMethods);
		assertEquals(scheduler.getElevator(), elevatorMethods, "Should add the evelvator elevatorMethods");
	}
	
	@Test
	void testRequest() {
		scheduler.addElevator(elevatorMethods);
		scheduler.addRequest(request1);
		scheduler.sortRequestsIntoGroups();
		Map.Entry<Integer, Direction> output = scheduler.getRequest(scheduler.getElevator().getElevatorLocation());
		
		assertEquals(output, Map.entry(2, Direction.UP), "Should be going UP to floor 2");
	}
	
	@Test
	void testMultipleRequest() {
		scheduler.addElevator(elevatorMethods);
		scheduler.addRequest(request1);
		scheduler.sortRequestsIntoGroups();
		Map.Entry<Integer, Direction> output = scheduler.getRequest(scheduler.getElevator().getElevatorLocation());
		
		assertEquals(output, Map.entry(2, Direction.UP), "Should be going UP to floor 2");
	}

}
