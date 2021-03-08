package elevatorSystems.elevatorStateMachine;

import java.util.ArrayList;

import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 *
 * State for when elevator doors are open after it has arrived at the destination
 */
public class DoorsOpen extends ElevatorState {
	
	private Elevator elevator;
	
	public DoorsOpen(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * Get all the car lamps that need to be turned on 
	 * @return list of all lamps to be turned on
	 */
	@Override
	public ArrayList<Integer> getLamps(int id) {
		elevator.getLogger().println("Elevator " + elevator.getId() + ": Transition from Doors Open to Update Lamps");
		return this.elevator.scheduler.getRequestedLamps(id);
	}
}
