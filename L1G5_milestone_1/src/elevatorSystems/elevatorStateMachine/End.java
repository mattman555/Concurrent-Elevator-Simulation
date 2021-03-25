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
		System.out.println("Elevator " + elevator.getId() + ": All requests processed Transition to Final state");
	}
	
	/**
	 * saying that it is in its final state
	 */
	@Override
	public void errorExit() {
		System.out.println("Elevator " + elevator.getId() + ": Has Shutdown, cannot be used");
	}
}
