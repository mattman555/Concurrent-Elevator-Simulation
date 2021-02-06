/**
 * 
 */
package elevatorSystems;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Matthew Harris 101073502
 *
 */
class RequestGroupTest {
	
	RequestGroup requestGroup;
	ArrayList<Request> requests;
	Request request1;
	Request request2;
	Request request3;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		requests = new ArrayList<Request>();
		request1 = new Request("10:30:30.0", 2, "UP", 5);
		request2 = new Request("10:31:30.0", 2, "UP", 3);
		request3 = new Request("10:32:30.0", 2, "UP", 4);
		requests.add(request1);
		requests.add(request2);
		requests.add(request3);
		requestGroup = new RequestGroup(requests);
	}

	/**
	 * Test method for {@link elevatorSystems.RequestGroup#RequestGroup(java.util.ArrayList)}.
	 */
	@Test
	void testRequestGroup() {
		assertNotNull(new RequestGroup(requests));
	}

	/**
	 * Test method for {@link elevatorSystems.RequestGroup#getNextDestination()}.
	 */
	@Test
	void testGetNextDestination() {
		assertEquals(2, requestGroup.getNextDestination());
	}

	/**
	 * Test method for {@link elevatorSystems.RequestGroup#removeRequest(elevatorSystems.Request)}.
	 */
	@Test
	void testRemoveRequest() {
		assertTrue(requestGroup.getRequests().contains(request1));
		requestGroup.removeRequest(request1);
		assertFalse(requestGroup.getRequests().contains(request1));
	}

	/**
	 * Test method for {@link elevatorSystems.RequestGroup#removeElevatorFloorLamp(java.lang.Integer)}.
	 */
	@Test
	void testRemoveElevatorFloorLamp() {
		assertTrue(requestGroup.getElevatorFloorLamps().contains(3));
		requestGroup.removeElevatorFloorLamp(3);
		assertFalse(requestGroup.getElevatorFloorLamps().contains(3));
	}

	/**
	 * Test method for {@link elevatorSystems.RequestGroup#getRequests()}.
	 */
	@Test
	void testGetRequests() {
		assertTrue(requestGroup.getRequests().contains(request1));
	}

	/**
	 * Test method for {@link elevatorSystems.RequestGroup#getElevatorFloorLamps()}.
	 */
	@Test
	void testGetElevatorFloorLamps() {
		assertTrue(requestGroup.getElevatorFloorLamps().contains(3));
	}

}