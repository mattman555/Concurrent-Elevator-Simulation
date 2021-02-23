/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Elevator;
import elevatorSystems.FloorSubsystem;

/**
 * @author Matthew Harris 101073502
 *
 */
public class MovingUp extends ElevatorState {

	private Elevator elevator;
	private FloorSubsystem floorSubsystem;
	
	public MovingUp(Elevator elevator, FloorSubsystem floorSubsystem) {
		this.elevator = elevator;
		this.floorSubsystem = floorSubsystem;
	}

	@Override
	public void activity() {
		System.out.println("Moving Up Elevator on: "+this.elevator.getElevatorLocation());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {}
		this.elevator.setElevatorLocation(this.elevator.getElevatorLocation()+1);
	}
	
	@Override
	public void arrivesAtDestination() {
		System.out.println("Transition from Moving Up to Arrived");
		System.out.println("Arrived at: "+this.elevator.getElevatorLocation());
		this.floorSubsystem.setFloorLamp(this.elevator.getElevatorLocation(), this.elevator.getMotor(), false);
		
	}
}
