/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import java.util.ArrayList;

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
	public void action(ArrayList<Integer> lamps) {
		System.out.println("Updating lamps");
		for(Integer key : this.elevator.getLamp().keySet()) {
			this.elevator.getLamp().put(key, false);
		}
		
		for(Integer i : lamps) {
			this.elevator.getLamp().put(i, true);
		}
	}
	
	@Override
	public void toggleDoors() {
		System.out.println("Transition from Update Lamps to Doors Closed");
		this.elevator.toggleDoors();
		
	}
	
}
