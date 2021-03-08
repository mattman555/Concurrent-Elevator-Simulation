package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 *
 * State when elevator arrives at required floor 
 */
public class Arrived extends ElevatorState {
	
	private Elevator elevator;
	
	public Arrived(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * Change the state of the elevator doors
	 */
	@Override
	public void toggleDoors() {
		elevator.getLogger().println("Eleavtor Transition from Arrived to Doors Open");
		this.elevator.scheduler.requestDoorChange();
	}
}
