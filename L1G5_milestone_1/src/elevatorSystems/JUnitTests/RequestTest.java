package elevatorSystems.JUnitTests;

import static org.junit.jupiter.api.Assertions.*;

import elevatorSystems.Direction;
import elevatorSystems.Request;

/**
 * 
 * @author Nick Coutts 101072875
 *
 */
public class RequestTest {
	Request request;

	@org.junit.Before
	public void setUp() throws Exception {
		request = new Request("11:20:03.000",3,"DOWN",1,0);
	}

	/**
	 * Test method for {@link  elevatorSystems.Request#getTime()}
	 */
	@org.junit.Test
	public void testGetTime() {
		int[] time = request.getTime();
		assertEquals(11,time[0]);
		assertEquals(20,time[1]);
		assertEquals(3,time[2]);
		assertEquals(0,time[3]);
	}
	
	/**
	 * Test method for {@link   elevatorSystems.Request#getTime()}
	 */
	@org.junit.Test
	public void testGetFloor() {
		assertEquals(3,request.getFloor());
	}

	/**
	 * Test method for {@link   elevatorSystems.Request#getCarButton()}
	 */
	@org.junit.Test
	public void testGetCarButton() {
		assertEquals(1,request.getCarButton());
	}
	
	/**
	 * Test method for {@link  elevatorSystems.Request#getFloorButton()}
	 */
	@org.junit.Test
	public void testGetFloorButton() {
		assertEquals(Direction.DOWN, request.getFloorButton());
	}
	
	/**
	 * Test method for {@link  elevatorSystems.Request#toString()}
	 */
	@org.junit.Test
	public void testToString() {
		String expected = "Request{Time: 11:20:3:0 | Floor: 3 | Directional button: DOWN | Destination: 1}";
		assertEquals(expected, request.toString());
	}
	
	
}
