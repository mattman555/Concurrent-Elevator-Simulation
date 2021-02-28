package elevatorStates.JUnitTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import elevatorSystems.Direction;
import elevatorSystems.Elevator;
import elevatorSystems.FloorSubsystem;
import elevatorSystems.Request;
import elevatorSystems.Scheduler;
import elevatorSystems.elevatorStateMachine.ElevatorSM;
import elevatorSystems.elevatorStateMachine.ElevatorStates;

/**
 * 
 * @author Jay McCracken 101066860
 *
 */
public class ElevatorSMTest {

	private Elevator elevatorMethods;
	private Scheduler scheduler;
	private FloorSubsystem fssystem;
	private ElevatorSM state;
	private Request request; 
	private ArrayList<Integer> lamps;
	private List<Request> requests;
	
	@org.junit.Before
	public void setUp() throws Exception {
		scheduler = new Scheduler();
		elevatorMethods = new Elevator(scheduler);
		scheduler.addElevator(elevatorMethods);
		fssystem = new FloorSubsystem(scheduler, 10);
		state = new ElevatorSM(elevatorMethods,fssystem);
		request = new Request("00:00:05.000", 2, "Up", 5); 
		fssystem.addRequest(request);
		lamps = new ArrayList<Integer>();
		elevatorMethods.setMotor(Direction.UP);
		requests = new ArrayList<Request>();
		
		requests.add(request);
		scheduler.addRequest(request);
		scheduler.requestTask(2);

	}

	/**
	 * testing the movement of the elevator
	 */
	@org.junit.Test
	public void testActivity() {
		state.validRequest(Map.entry(2, Direction.UP));
		elevatorMethods.setElevatorLocation(1);
		assertEquals(1, elevatorMethods.getElevatorLocation(), "The Elevator should be at floor 1");
		state.activity(Direction.UP);
		assertEquals(2, elevatorMethods.getElevatorLocation(), "The Elevator should be at floor 2");
		state.activity(Direction.DOWN);
		assertEquals(1, elevatorMethods.getElevatorLocation(), "The Elevator should be at floor 1");
	}
	
	/**
	 * testing turning on and off car lamps
	 */
	@org.junit.Test
	public void testAction() {
		Hashtable<Integer, Boolean> responce = new Hashtable<Integer, Boolean>();
		state.arrivesAtDestination();
		state.toggleDoors(ElevatorStates.DOORS_OPEN);
		lamps = state.getLamps();
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
		assertEquals(responce,elevatorMethods.getLamp(), "The lamp 1 should be false");
	}
	
	/**
	 * testing switching from doors closed to moving state
	 */
	@org.junit.Test
	public void testValidRequest() {
		assertEquals(ElevatorStates.DOORS_CLOSED, state.getState(), "The Elevator state should be Doors Closed");
		state.validRequest(Map.entry(2, Direction.UP));
		assertEquals(ElevatorStates.MOVING, state.getState(), "The Elevator state should be moving");
	}
	
	/**
	 * testing switching from doors closed to end state
	 */
	@org.junit.Test
	public void testInvalidRequest() {
		assertEquals(ElevatorStates.DOORS_CLOSED, state.getState(), "The Elevator state should be Doors Closed");
		state.invalidRequest();
		assertEquals(ElevatorStates.END, state.getState(), "The Elevator state should be end");
	}
	
	/**
	 * testing switching from doors closed to arrived state
	 */
	@org.junit.Test
	public void testArrivesAtDestination() {
		assertEquals(ElevatorStates.DOORS_CLOSED, state.getState(), "The Elevator state should be Moving");
		state.arrivesAtDestination();
		assertEquals(ElevatorStates.ARRIVED, state.getState(), "The Elevator state should be arrived");
	}
	
	/**
	 * testing switching from doors closed to doors open state
	 */
	@org.junit.Test
	public void testToggleDoors() {
		assertEquals(ElevatorStates.DOORS_CLOSED, state.getState(), "The Elevator state should be Doors Closed");
		state.arrivesAtDestination();
		state.toggleDoors(ElevatorStates.DOORS_OPEN);
		assertEquals(ElevatorStates.DOORS_OPEN, state.getState(), "The Elevator state should be Doors Open");
		lamps = state.getLamps();
		state.toggleDoors(ElevatorStates.DOORS_CLOSED);
		assertEquals(ElevatorStates.DOORS_CLOSED, state.getState(), "The Elevator state should be Doors Closed");
	}
	
	/**
	 * testing switching from doors open to update lamps state
	 */
	@org.junit.Test
	public void testGetLamps() {
		assertEquals(ElevatorStates.DOORS_CLOSED, state.getState(), "The Elevator state should be Doors Closed");
		state.arrivesAtDestination();
		state.toggleDoors(ElevatorStates.DOORS_OPEN);
		lamps = state.getLamps();
		assertEquals(ElevatorStates.UPDATE_LAMPS, state.getState(), "The Elevator state should be update Lamps");
	}
	
	/**
	 * testing switching from doors closed to moving state
	 */
	@org.junit.Test
	public void testExit() {
		assertEquals(ElevatorStates.DOORS_CLOSED, state.getState(), "The Elevator state should be Doors Closed");
		state.invalidRequest();
		state.exit();
		assertEquals(ElevatorStates.END, state.getState(), "The Elevator state should be END");
	}
	


}
