/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 *
 */
public class End extends ElevatorState {
	
	private Elevator elevator;
	
	public End(Elevator elevator) {
		this.elevator = elevator;
	}

	@Override
	public void exit() {
		System.out.println("All requests processed Transition to Final state");
	}
}
