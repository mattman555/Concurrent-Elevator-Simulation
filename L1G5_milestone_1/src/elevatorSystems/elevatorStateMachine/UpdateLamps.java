package elevatorSystems.elevatorStateMachine;

import java.util.ArrayList;

import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 * @author Jay McCracken 101066860
 *
 *	Turning on and off car lamps
 */
public class UpdateLamps extends ElevatorState {

	private Elevator elevator;
	
	public UpdateLamps(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * Turns on all requested lamps, turning off the lamp of the elevators location
	 * @param The list of lamps that there are
	 */
	@Override
	public void action(ArrayList<Integer> lamps) {
		System.out.println("Updating lamps");
		int location = this.elevator.getElevatorLocation();
		for(Integer i : lamps) {
			this.elevator.getLamp().put(i, true);
		}
		this.elevator.getLamp().put(location, false);
		System.out.println("Car Lamps: " + this.elevator.getLamp());
	}
	
	/**
	 * Toggling the doors of the elevator to open or closed based on what it last was
	 * @param next the state of the door that was switched too
	 */
	@Override
	public void toggleDoors() {
		System.out.println("Transition from Update Lamps to Doors Closed");
		this.elevator.scheduler.requestDoorChange();
		
	}
	
}
