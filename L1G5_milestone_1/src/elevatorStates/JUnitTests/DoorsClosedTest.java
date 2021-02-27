package elevatorStates.JUnitTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Hashtable;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import elevatorSystems.Direction;
import elevatorSystems.Elevator;
import elevatorSystems.FloorSubsystem;
import elevatorSystems.Request;
import elevatorSystems.Scheduler;
import elevatorSystems.elevatorStateMachine.DoorsClosed;

/**
 * @author Jay McCracken 101066860
 *
 */
public class DoorsClosedTest {

	private Elevator elevatorMethods;
	private Scheduler scheduler;
	private FloorSubsystem fssystem;
	private DoorsClosed state;
	
	@org.junit.Before
	public void setUp() throws Exception {
		scheduler = new Scheduler();
		elevatorMethods = new Elevator(scheduler);
		scheduler.addElevator(elevatorMethods);
		fssystem = new FloorSubsystem(scheduler, 10);
		state = new DoorsClosed(elevatorMethods, fssystem);
		
		Request request = new Request("00:00:05.000", 1, "Up", 5); 
		fssystem.addRequest(request);
	}

	/**
	 * Testing the setting of an elevator destination when passed a valid request
	 */
	@org.junit.Test
	public void testValidRequest() {
		assertEquals(elevatorMethods.getFloorDestination(), 1, "The Elevator should be at floor 1");	
		assertEquals(elevatorMethods.getMotor(), null, "The Elevator should be going Down");
		state.validRequest(Map.entry(2, Direction.UP));
		assertEquals(2, elevatorMethods.getFloorDestination(), "The Elevator should be going to floor 2");	
		assertEquals( Direction.UP, elevatorMethods.getMotor(), "The Elevator should be going UP");
	}
	
	/**
	 * Test is the lamp gets turned off correctly
	 */
	@org.junit.Test
	public void testArrivesAtDestination() {
		Hashtable<String, Boolean> responce = new Hashtable<String, Boolean>();
		responce.put("1DOWN", false);
		fssystem.setFloorLamp(1, Direction.DOWN, true);
		elevatorMethods.setElevatorLocation(1);
		elevatorMethods.setMotor(Direction.DOWN);
		state.arrivesAtDestination();
		assertEquals(responce, fssystem.getFloorLamp(), "The lamp 1 going down should be false");	
	}

}
