package elevatorSystems;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
	
	@org.junit.Test
	public void testRequestedLamps() {
		scheduler.addElevator(elevatorMethods);
		ArrayList<Request> requests = new ArrayList<Request>();
		requests.add(request1);
		scheduler.addRequests(requests);
		Map.Entry<Integer, Direction> output = scheduler.getRequest(1);
		ArrayList<Integer> floor = new ArrayList<>();
		floor.add(7);
		
		assertEquals(floor, scheduler.getRequestedLamps() , "Should be going UP to floor 2");
		
	}
	
	
	@org.junit.Test
	public void testMultipleRequest() {
		scheduler.addElevator(elevatorMethods);
		ArrayList<Request> requests = new ArrayList<Request>();
		requests.add(request1);
		requests.add(request2);
		scheduler.addRequests(requests);
		
		//testing going to each floor
		Map.Entry<Integer, Direction> output = scheduler.getRequest(1);
		assertEquals(Map.entry(2, Direction.UP),output , "Should be going UP to floor 2");
		
		output = scheduler.getRequest(2);
		assertEquals(Map.entry(7, Direction.UP), output, "Should be going UP to floor 7");
				
	    output = scheduler.getRequest(7);
		assertEquals(Map.entry(3, Direction.DOWN), output, "Should be going DOWN to floor 3");
		
		output = scheduler.getRequest(3);
		assertEquals(Map.entry(7, Direction.UP), output, "Should be going UP to floor 7");
		
		output = scheduler.getRequest(7);
		
		//testing getCompletedRequest
		Request completedRequest = scheduler.getCompletedRequest();
		assertEquals(request1, completedRequest, "Should be the first request completed");
	
		completedRequest = scheduler.getCompletedRequest();
		assertEquals(request2, completedRequest, "Should be the second request completed");
	
		}
	
	
}
