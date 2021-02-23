/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 *
 */
public class UpdateLamps extends ElevatorState {

	private Elevator elevator;
	
	public UpdateLamps(Elevator elevator) {
		this.elevator = elevator;
	}

	@Override
	public void action() {
		
	}
	
	@Override
	public void toggleDoors() {
		
	}
	
}
