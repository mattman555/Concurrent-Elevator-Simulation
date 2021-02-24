/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import java.util.ArrayList;
import java.util.Map.Entry;

import elevatorSystems.Direction;

/**
 * @author Matthew Harris 101073502
 *
 */
public abstract class ElevatorState {

	public void activity(Direction direction) {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	public void action(ArrayList<Integer> lamps) {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	public void validRequest(Entry<Integer,Direction> destination) {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	public void invalidRequest() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	public void arrivesAtDestination() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	public void toggleDoors() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	public ArrayList<Integer> getLamps() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}

	public void exit() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
}
