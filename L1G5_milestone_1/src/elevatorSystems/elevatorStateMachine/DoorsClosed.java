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
 * @author Jay McCracken 101066860
 * 
 *	State when the elevator doors are closed, from statanory or moving state
 */
public class DoorsClosed extends ElevatorState {

	private Elevator elevator; 
	private FloorSubsystem floorSubsystem;
	
	public DoorsClosed(Elevator elevator, FloorSubsystem floorSubsystem) {
		this.elevator = elevator;
		this.floorSubsystem = floorSubsystem;
	}

	/**
	 * When received a valid request set the conditions of the elevation to get to the request
	 * @param The elevators next destination
	 */
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
	
	/**
	 * When the elevator reaches the required floor, turn the floor lamp for destination to off
	 */
	@Override
	public void arrivesAtDestination() {
		System.out.println("Transition from Doors Closed to Arrived");
		System.out.println("Arrived at: "+this.elevator.getElevatorLocation());
		this.floorSubsystem.setFloorLamp(this.elevator.getElevatorLocation(), this.elevator.getMotor(), false);
	}
	
	/**
	 * If the request set is invalid send that it is going to end state
	 */
	@Override
	public void invalidRequest() {
		System.out.println("Transition from Doors Closed to End");
	}
}
