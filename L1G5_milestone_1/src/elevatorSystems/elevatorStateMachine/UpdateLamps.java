/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import java.util.ArrayList;

import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502 && Jay McCracken 101066860
 *
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
	
	@Override
	public void toggleDoors() {
		System.out.println("Transition from Update Lamps to Doors Closed");
		this.elevator.scheduler.requestDoorChange();
		
	}
	
}
