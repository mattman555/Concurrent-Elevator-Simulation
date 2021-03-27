package elevatorSystems.elevatorStateMachine;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Map.Entry;

import elevatorSystems.Direction;

/**
 * @author Matthew Harris 101073502
 * @author Jay McCracken 101066860
 *
 */
public abstract class ElevatorState {

	/**
	 * Default Method for the activity event
	 * @param direction the elevator should move
	 */
	public void activity(Direction direction) {
		throw new IllegalArgumentException("Elevator is in an Incorrect State");
	}
	
	/**
	 * Default Method for the action event
	 * @param lamps list of lamps to be turned on
	 */
	public void action(ArrayList<Integer> lamps) {
		throw new IllegalArgumentException("Elevator is in an Incorrect State");
	}
	
	/**
	 * Default Method for the validRequest event
	 * @param destination where the elevator should go next
	 */
	public void validRequest(Entry<Integer,Direction> destination) {
		throw new IllegalArgumentException("Elevator is in an Incorrect State");
	}
	
	/**
	 * Default Method for the invalidRequest event
	 */
	public void invalidRequest() {
		throw new IllegalArgumentException("Elevator is in an Incorrect State");
	}
	
	/**
	 * Default Method for the arrivesAtDestination event
	 */
	public void arrivesAtDestination(DatagramSocket sendReceiveSocket) {
		throw new IllegalArgumentException("Elevator is in an Incorrect State");
	}
	
	/**
	 * Default Method for the toggleDoors event
	 */
	public void toggleDoors(DatagramSocket sendReceiveSocket) {
		throw new IllegalArgumentException("Elevator is in an Incorrect State");
	}
	
	/**
	 * Default Method for the getLamps event
	 * @return
	 */
	public ArrayList<Integer> getLamps(DatagramSocket sendReceiveSocket) {
		throw new IllegalArgumentException("Elevator is in an Incorrect State");
	}

	/**
	 * Default Method for the exit event
	 */
	public void exit() {
		throw new IllegalArgumentException("Elevator is in an Incorrect State");
	}

	/**
	 * Default Method for the errorExit event
	 */
	public void errorExit(DatagramSocket sendReceiveSocket) {
		throw new IllegalArgumentException("Elevator is in an Incorrect State");	
	}
	
	/**
	 * Default Method for the doorStuckError event
	 */
	public void doorStuckError() {
		throw new IllegalArgumentException("Elevator is in an Incorrect State");	
	}
	
	/**
	 * Default Method for the doorWaitevent
	 */
	public void doorWait() {
		throw new IllegalArgumentException("Elevator is in an Incorrect State");	
	}
	
	/**
	 * Default Method for the shutdown
	 */
	public void shutdown() {
		throw new IllegalArgumentException("Elevator is in an Incorrect State");	
	}
	
	
}
