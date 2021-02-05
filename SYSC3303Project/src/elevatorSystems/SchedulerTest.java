package elevatorSystems;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchedulerTest {
	private Elevator elevatorMethods;
	private Scheduler scheduler;
	private Request request;

	@BeforeEach
	void setUp() throws Exception {
		scheduler = new Scheduler();
		elevatorMethods = new Elevator(scheduler);
		request = new Request("0:0:0,01",2,"UP",2);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testAddElevator() {
		scheduler.addElevator(elevatorMethods);
		assertEquals(scheduler.elevator, elevatorMethods, "Should add the evelvator elevatorMethods");
	}
	
	@Test
	void testRequest() {
		scheduler.addRequest(request);
		assertEquals(scheduler., elevatorMethods, "Should add the evelvator elevatorMethods");
	}

}
