/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

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
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	/**
	 * Default Method for the action event
	 * @param lamps list of lamps to be turned on
	 */
	public void action(ArrayList<Integer> lamps) {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	/**
	 * Default Method for the validRequest event
	 * @param destination where the elevator should go next
	 */
	public void validRequest(Entry<Integer,Direction> destination) {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	/**
	 * Default Method for the invalidRequest event
	 */
	public void invalidRequest() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	/**
	 * Default Method for the arrivesAtDestination event
	 */
	public void arrivesAtDestination() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	/**
	 * Default Method for the toggleDoors event
	 */
	public void toggleDoors() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	/**
	 * Default Method for the getLamps event
	 * @return
	 */
	public ArrayList<Integer> getLamps() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}

	/**
	 * Default Method for the exit event
	 */
	public void exit() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
}
