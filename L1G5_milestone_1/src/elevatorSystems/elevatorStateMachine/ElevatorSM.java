/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import java.util.ArrayList;
import java.util.Map.Entry;

import javax.print.attribute.standard.Destination;

import elevatorSystems.Direction;
import elevatorSystems.Elevator;
import elevatorSystems.FloorSubsystem;

/**
 * @author Matthew Harris 101073502
 *
 */
public class ElevatorSM implements Runnable{

	private ElevatorState[] states;
	private int current;
	private int[][] transitions = {{2,1,5},{2},{3},{4},{0}};
	private Elevator elevator;
	private FloorSubsystem floorSubsystem;
	/**
	* 
	*/
	public ElevatorSM(Elevator elevator, FloorSubsystem floorSubsystem) {
		this.elevator =  elevator;
		this.floorSubsystem = floorSubsystem;
		ElevatorState[] statearr ={new DoorsClosed(this.elevator, this.floorSubsystem), new Moving(this.elevator, this.floorSubsystem), new Arrived(this.elevator), new DoorsOpen(this.elevator), new UpdateLamps(this.elevator), new End(this.elevator)};
		states = statearr;
		current = 0;
	}
	 
	
	private void nextState(int nextState) {
		 current = transitions[current][nextState];
    }
	
	public void activity(Direction direction) {
		states[current].activity(direction);
	}
	
	public void action(ArrayList<Integer> lamps) {
		states[current].action(lamps);
	}
	
	public void validRequest(Entry<Integer,Direction> destination) {
		states[current].validRequest(destination);
		nextState(1);
	}
	public void invalidRequest() {
		states[current].invalidRequest();
		nextState(2);
	}
	
	public void arrivesAtDestination() {
		states[current].arrivesAtDestination();
		nextState(0);
	}
	
	public void toggleDoors() {
		states[current].toggleDoors();
		nextState(0);
	}
	
	public ArrayList<Integer> getLamps() {
		ArrayList<Integer> lamps = states[current].getLamps();
		nextState(0);
		return lamps;
	}
	
	public void exit() {
		states[current].exit();
		current = 6;
	}
	
	
	@Override
	/**
	 * The running of the elevator, travel to new floor, updating lamps
	 */
	public void run() {
		/*
		 * until thread is told there is no more requests
		 */
		int destinationFloor = 1;
		Direction destinationDirection = Direction.STATIONARY;
		ArrayList<Integer> lamps = null;
		while (true) {
			switch(current) {
			case 0:
				//Get the request of the next floor with the motor direction from the scheduler
				Entry<Integer,Direction> destination = this.elevator.scheduler.getRequest(this.elevator.getElevatorLocation());
				if(destination == null) {//no more requests move to end
					this.invalidRequest();
					break;
				}
				else if(destination.getValue()==Direction.UP||destination.getValue()==Direction.DOWN) {
					destinationFloor = destination.getKey();
					destinationDirection = destination.getValue();
					this.validRequest(destination);
					break;
				}
				else if(destination.getValue()==Direction.STATIONARY) {
					this.arrivesAtDestination();
					break;
				}
				break;
			case 1:
				if(destinationFloor==this.elevator.getElevatorLocation()) {
					this.arrivesAtDestination();
					break;
				}
				else if(destinationFloor>this.elevator.getElevatorLocation()||destinationFloor<this.elevator.getElevatorLocation()) {
					this.activity(destinationDirection);
					break;
				}
				break;
			case 2:
				this.toggleDoors();
				break;
			case 3:
				lamps = this.getLamps();
				break;
			case 4:
				this.action(lamps);
				this.toggleDoors();
				break;
			case 5:
				this.exit();
				break;
			}
		}
	}
}
