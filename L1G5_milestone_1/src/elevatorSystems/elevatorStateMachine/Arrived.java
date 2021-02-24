/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 *
 */
public class Arrived extends ElevatorState {
	
	private Elevator elevator;
	
	public Arrived(Elevator elevator) {
		this.elevator = elevator;
	}

	@Override
	public void toggleDoors() {
		System.out.println("Transition from Arrived to Doors Open");
		this.elevator.scheduler.requestDoorChange();
	}
}
