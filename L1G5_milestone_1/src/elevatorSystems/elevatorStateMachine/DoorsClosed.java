/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import java.util.Map.Entry;

import elevatorSystems.Direction;
import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 *
 */
public class DoorsClosed extends ElevatorState {

	private Elevator elevator; 
	
	public DoorsClosed(Elevator elevator) {
		this.elevator = elevator;
	}

	@Override
	public void validUpRequest(Entry<Integer,Direction> destination) {
		System.out.println("Transition from Doors Closed to Moving Up");
		this.elevator.setFloorDestination(destination.getKey()); //set the floor number to go to
		this.elevator.setMotor(destination.getValue());			 //the direction of the motor to get to that floor
		
		//Flavor text of the elevator moving to new floor
		System.out.println(
				Thread.currentThread().getName()
				+ " goes to floor " + this.elevator.getFloorDestination());
	}
	
	@Override
	public void validDownRequest(Entry<Integer,Direction> destination) {
		System.out.println("Transition from Doors Closed to Moving Down");
		this.elevator.setFloorDestination(destination.getKey()); //set the floor number to go to
		this.elevator.setMotor(destination.getValue());			 //the direction of the motor to get to that floor
		
		//Flavor text of the elevator moving to new floor
		System.out.println(
				Thread.currentThread().getName()
				+ " goes to floor " + this.elevator.getFloorDestination());
	}
	
	@Override
	public void invalidRequest() {
		System.out.println("Transition from Doors Closed to End");
	}
}
