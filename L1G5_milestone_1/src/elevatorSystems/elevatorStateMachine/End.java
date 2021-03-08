package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 *
 * All requests have been achieved ending state machine
 */
public class End extends ElevatorState {
	
	private Elevator elevator;
	
	public End(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * saying that it is in its final state
	 */
	@Override
	public void exit() {
		elevator.getLogger().println("Elevator " + elevator.getId() + ": All requests processed Transition to Final state");
	}
}
