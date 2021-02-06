package elevatorSystems;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
		assertEquals(elevatorMethods, scheduler.getElevator(), "Should add the evelvator elevatorMethods");
	}
	
	
	@Test
	void testMultipleRequest() {
		scheduler.addElevator(elevatorMethods);
		ArrayList<Request> requests = new ArrayList<Request>();
		requests.add(request1);
		requests.add(request2);
		scheduler.addRequests(requests);
		
		Map.Entry<Integer, Direction> output = scheduler.getRequest(scheduler.getElevator().getElevatorLocation());
		assertEquals(Map.entry(2, Direction.UP),output , "Should be going UP to floor 2");
		
		output = scheduler.getRequest(scheduler.getElevator().getElevatorLocation());
		assertEquals(Map.entry(7, Direction.UP), output, "Should be going UP to floor 7");
	
	    output = scheduler.getRequest(scheduler.getElevator().getElevatorLocation());
		assertEquals(Map.entry(3, Direction.DOWN), output, "Should be going DOWN to floor 3");

	}
}
