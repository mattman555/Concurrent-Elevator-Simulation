package elevatorSystems.JUnitTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import elevatorSystems.Request;
import elevatorSystems.RequestGroup;

/**
 * @author Matthew Harris 101073502
 *
 */
public class RequestGroupTest {
	
	RequestGroup requestGroup;
	ArrayList<Request> requests;
	Request request1;
	Request request2;
	Request request3;
	/**
	 * @throws java.lang.Exception
	 */
	@org.junit.Before
	public void setUp() throws Exception {
		requests = new ArrayList<Request>();
		request1 = new Request("10:30:30.0", 2, "UP", 5, 0);
		request2 = new Request("10:31:30.0", 2, "UP", 3, 0);
		request3 = new Request("10:32:30.0", 2, "UP", 4, 0);
		requests.add(request1);
		requests.add(request2);
		requests.add(request3);
		requestGroup = new RequestGroup(requests);
	}

	/**
	 * Test method for {@link elevatorSystems.RequestGroup#RequestGroup(java.util.ArrayList)}.
	 */
	@org.junit.Test
	public void testRequestGroup() {
		assertNotNull(new RequestGroup(requests));
	}

	/**
	 * Test method for {@link elevatorSystems.RequestGroup#getNextDestination()}.
	 */
	@org.junit.Test
	public void testGetNextDestination() {
		assertEquals(2, requestGroup.getNextDestination());
	}

	/**
	 * Test method for {@link elevatorSystems.RequestGroup#removeRequest(elevatorSystems.Request)}.
	 */
	@org.junit.Test
	public void testRemoveRequest() {
		assertTrue(requestGroup.getRequests().contains(request1));
		ArrayList<Request> remove = new ArrayList<>();
		remove.add(request1);
		requestGroup.removeRequests(remove);
		assertFalse(requestGroup.getRequests().contains(request1));
	}

	/**
	 * Test method for {@link elevatorSystems.RequestGroup#removeElevatorFloorLamp(java.lang.Integer)}.
	 */
	@org.junit.Test
	public void testRemoveElevatorFloorLamp() {
		assertTrue(requestGroup.getElevatorFloorLamps().contains(3));
		requestGroup.removeElevatorFloorLamp(3);
		assertFalse(requestGroup.getElevatorFloorLamps().contains(3));
	}

	/**
	 * Test method for {@link elevatorSystems.RequestGroup#getRequests()}.
	 */
	@org.junit.Test
	public void testGetRequests() {
		assertTrue(requestGroup.getRequests().contains(request1));
	}

	/**
	 * Test method for {@link elevatorSystems.RequestGroup#getElevatorFloorLamps()}.
	 */
	@org.junit.Test
	public void testGetElevatorFloorLamps() {
		assertTrue(requestGroup.getElevatorFloorLamps().contains(3));
	}

}
