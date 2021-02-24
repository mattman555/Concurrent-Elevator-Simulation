/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Direction;
import elevatorSystems.Elevator;
import elevatorSystems.FloorSubsystem;

/**
 * @author Matthew Harris 101073502
 *
 */
public class Moving extends ElevatorState {

	private Elevator elevator;
	private FloorSubsystem floorSubsystem;
	
	public Moving(Elevator elevator, FloorSubsystem floorSubsystem) {
		this.elevator = elevator;
		this.floorSubsystem = floorSubsystem;
	}

	@Override
	public void activity(Direction direction) {
		System.out.println("Moving "+direction.toString().toLowerCase()+" Elevator on: "+this.elevator.getElevatorLocation());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		if(direction==Direction.UP) {
		this.elevator.setElevatorLocation(this.elevator.getElevatorLocation()+1);
		}
		else if(direction==Direction.DOWN) {
			this.elevator.setElevatorLocation(this.elevator.getElevatorLocation()-1);
		}
	}
	
	@Override
	public void arrivesAtDestination() {
		System.out.println("Transition from Moving to Arrived");
		System.out.println("Arrived at: "+this.elevator.getElevatorLocation());
		this.floorSubsystem.setFloorLamp(this.elevator.getElevatorLocation(), this.elevator.getMotor(), false);
		
	}
}
