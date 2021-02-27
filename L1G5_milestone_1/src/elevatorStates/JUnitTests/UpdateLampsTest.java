package elevatorStates.JUnitTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Hashtable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import elevatorSystems.Elevator;
import elevatorSystems.FloorSubsystem;
import elevatorSystems.Request;
import elevatorSystems.Scheduler;
import elevatorSystems.elevatorStateMachine.UpdateLamps;

/**
 * 
 * @author Jay McCracken 101066860
 *
 */
public class UpdateLampsTest {

	private Elevator elevatorMethods;
	private Scheduler scheduler;
	private FloorSubsystem fssystem;
	private UpdateLamps state;
	private Request request; 
	private ArrayList<Integer> lamps;
	
	@org.junit.Before
	public void setUp() throws Exception {
		scheduler = new Scheduler();
		elevatorMethods = new Elevator(scheduler);
		scheduler.addElevator(elevatorMethods);
		fssystem = new FloorSubsystem(scheduler, 10);
		state = new UpdateLamps(elevatorMethods);
		
		request = new Request("00:00:05.000", 2, "Up", 5); 
		fssystem.addRequest(request);
		lamps = new ArrayList<Integer>();
	}

	/**
	 * testing turning on and off car lamps
	 */
	@org.junit.Test
	public void testAction() {
		Hashtable<Integer, Boolean> responce = new Hashtable<Integer, Boolean>();
		responce.put(2, true);
		lamps.add(2);
		responce.put(5, true);
		responce.put(1, false);
		lamps.add(5);
		state.action(lamps);
		assertEquals(responce,elevatorMethods.getLamp(), "The lamp 1 and 5 should be true");	
		elevatorMethods.setElevatorLocation(2);
		state.action(lamps);
		responce.put(2, false);
		assertEquals(responce,elevatorMethods.getLamp(), "The lamp 1 going  should be false");
	}

}
