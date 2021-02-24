/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import java.util.Map.Entry;

import elevatorSystems.Direction;
import elevatorSystems.Elevator;
import elevatorSystems.FloorSubsystem;

/**
 * @author Matthew Harris 101073502
 *
 */
public class DoorsClosed extends ElevatorState {

	private Elevator elevator; 
	private FloorSubsystem floorSubsystem;
	
	public DoorsClosed(Elevator elevator, FloorSubsystem floorSubsystem) {
		this.elevator = elevator;
		this.floorSubsystem = floorSubsystem;
	}

	@Override
	public void validRequest(Entry<Integer,Direction> destination) {
		System.out.println("Transition from Doors Closed to Moving Up");
		this.elevator.setFloorDestination(destination.getKey()); //set the floor number to go to
		this.elevator.setMotor(destination.getValue());			 //the direction of the motor to get to that floor
		
		//Flavor text of the elevator moving to new floor
		System.out.println(
				Thread.currentThread().getName()
				+ " goes to floor " + this.elevator.getFloorDestination());
	}
	
	@Override
	public void arrivesAtDestination() {
		System.out.println("Transition from Doors Closed to Arrived");
		System.out.println("Arrived at: "+this.elevator.getElevatorLocation());
		this.floorSubsystem.setFloorLamp(this.elevator.getElevatorLocation(), this.elevator.getMotor(), false);
	}
	
	@Override
	public void invalidRequest() {
		System.out.println("Transition from Doors Closed to End");
	}
}
