/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 *
 */
public class DoorsOpen extends ElevatorState {
	
	private Elevator elevator;
	
	public DoorsOpen(Elevator elevator) {
		this.elevator = elevator;
	}

	@Override
	public void getLamps() {
		System.out.println("Transition from Doors Open to Update Lamps");
	}
}
