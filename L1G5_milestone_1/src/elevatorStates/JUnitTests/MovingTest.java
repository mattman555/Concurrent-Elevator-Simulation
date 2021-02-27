package elevatorStates.JUnitTests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Hashtable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import elevatorSystems.Direction;
import elevatorSystems.Elevator;
import elevatorSystems.FloorSubsystem;
import elevatorSystems.Request;
import elevatorSystems.Scheduler;
import elevatorSystems.elevatorStateMachine.Moving;


/**
 * @author Jay McCracken 101066860
 *
 */
public class MovingTest {

	private Elevator elevatorMethods;
	private Scheduler scheduler;
	private FloorSubsystem fssystem;
	private Moving state;
	private Request request; 
	
	@org.junit.Before
	public void setUp() throws Exception {
		scheduler = new Scheduler();
		elevatorMethods = new Elevator(scheduler);
		scheduler.addElevator(elevatorMethods);
		fssystem = new FloorSubsystem(scheduler, 10);
		state = new Moving(elevatorMethods,fssystem);
		
		request = new Request("00:00:05.000", 1, "Up", 5); 
		fssystem.addRequest(request);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@org.junit.Test
	public void testActivity() {
		elevatorMethods.setElevatorLocation(1);
		assertEquals(1, elevatorMethods.getElevatorLocation(), "The Elevator should be at floor 1");
		state.activity(Direction.UP);
		assertEquals(2, elevatorMethods.getElevatorLocation(), "The Elevator should be at floor 2");
		state.activity(Direction.DOWN);
		assertEquals(1, elevatorMethods.getElevatorLocation(), "The Elevator should be at floor 2");
	}
	
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
