package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Direction;
import elevatorSystems.Elevator;
import elevatorSystems.FloorSubsystem;

/**
 * @author Matthew Harris 101073502
 * @author Jay McCracken 101066860
 *
 *	State of the elevator when transitioning between floors
 */
public class Moving extends ElevatorState {

	private Elevator elevator;
	private FloorSubsystem floorSubsystem;
	private static final int TIME_BETWEEN_FLOORS = 1000;
	
	public Moving(Elevator elevator, FloorSubsystem floorSubsystem) {
		this.elevator = elevator;
		this.floorSubsystem = floorSubsystem;
	}

	/**
	 * The movement of the elevator to the next floor by 1 based on the direction given
	 * @param direction, If the elevator is going UP or DOWN
	 */
	@Override
	public void activity(Direction direction) {
		elevator.getLogger().println("Elevator " + elevator.getId() + ": Moving " + direction.toString().toLowerCase() + " on floor " + this.elevator.getElevatorLocation());
		try {
			Thread.sleep(TIME_BETWEEN_FLOORS);
		} catch (InterruptedException e) {}
		if(direction == Direction.UP) {
		this.elevator.setElevatorLocation(this.elevator.getElevatorLocation()+1);
		}
		else if(direction == Direction.DOWN) {
			this.elevator.setElevatorLocation(this.elevator.getElevatorLocation()-1);
		}
	}
	
	/**
	 * When the elevator reaches the required floor, turn the floor lamp for destination to off
	 */
	@Override
	public void arrivesAtDestination() {
		elevator.getLogger().println("Elevator " + elevator.getId() + ": Transition from Moving to Arrived");
		elevator.getLogger().println("Elevator " + elevator.getId() + ": Arrived at: " + this.elevator.getElevatorLocation());
		this.floorSubsystem.setFloorLamp(this.elevator.getElevatorLocation(), this.elevator.getMotor(), false);
		
	}
}
