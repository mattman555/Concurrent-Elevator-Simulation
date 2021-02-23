/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import java.util.Map.Entry;

import elevatorSystems.Direction;

/**
 * @author Matthew Harris 101073502
 *
 */
public abstract class ElevatorState {

	public void activity() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	public void action() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	public void validUpRequest(Entry<Integer,Direction> destination) {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
	public void validDownRequest(Entry<Integer,Direction> destination) {
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
	
	public void getLamps() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}

	public void exit() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}
	
}
