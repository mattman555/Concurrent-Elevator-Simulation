package elevatorSystems;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Nick Coutts 101072875
 *
 */
class RequestTest {
	Request request;

	@BeforeEach
	void setUp() throws Exception {
		request = new Request("11:20:03.000",3,"DOWN",1);
	}

	/**
	 * Test method for {@link  elevatorSystems.Request#getTime()}
	 */
	@Test
	void testGetTime() {
		int[] time = request.getTime();
		assertEquals(11,time[0]);
		assertEquals(20,time[1]);
		assertEquals(3,time[2]);
		assertEquals(0,time[3]);
	}
	
	/**
	 * Test method for {@link  elevatorSystems.Request#getTime()}
	 */
	@Test
	void testGetFloor() {
		assertEquals(3,request.getFloor());
	}

	/**
	 * Test method for {@link  elevatorSystems.Request#getCarButton()}
	 */
	@Test
	void testGetCarButton() {
		assertEquals(1,request.getCarButton());
	}
	
	/**
	 * Test method for {@link  elevatorSystems.Request#getFloorButton()}
	 */
	@Test
	void testGetFloorButton() {
		assertEquals(Direction.DOWN, request.getFloorButton());
	}
	
	/**
	 * Test method for {@link  elevatorSystems.Request#toString()t}
	 */
	@Test
	void testToString() {
		String expected = "Request{Time: 11:20:3:0 | Floor: 3 | Directional button: DOWN | Destination: 1}";
		assertEquals(expected, request.toString());
	}
	
	
}
