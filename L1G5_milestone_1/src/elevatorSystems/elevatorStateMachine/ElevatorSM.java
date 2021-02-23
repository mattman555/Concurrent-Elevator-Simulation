/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import java.util.ArrayList;
import java.util.Map.Entry;

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
	private int[][] transitions = {{1, 2, 6},{3},{3},{4},{5},{0}};
	private Elevator elevator;
	private FloorSubsystem floorSubsystem;
	/**
	* 
	*/
	public ElevatorSM(Elevator elevator, FloorSubsystem floorSubsystem) {
		this.elevator =  elevator;
		this.floorSubsystem = floorSubsystem;
		ElevatorState[] statearr ={new DoorsClosed(this.elevator), new MovingUp(this.elevator, this.floorSubsystem), new MovingDown(this.elevator, this.floorSubsystem), new Arrived(this.elevator), new DoorsOpen(this.elevator), new UpdateLamps(this.elevator), new End(this.elevator)};
		states = statearr;
		current = 0;
	}
	 
	
	private void next(int nextState) {
		 current = transitions[current][nextState];
    }
	
	public void activity() {
		states[current].activity();
	}
	
	public void action(ArrayList<Integer> lamps) {
		states[current].action(lamps);
	}
	
	public void validUpRequest(Entry<Integer,Direction> destination) {
		states[current].validUpRequest(destination);
		next(0);
	}
	
	public void validDownRequest(Entry<Integer,Direction> destination) {
		states[current].validDownRequest(destination);
		next(1);
	}
	
	public void invalidRequest() {
		states[current].invalidRequest();
		next(2);
	}
	
	public void arrivesAtDestination() {
		states[current].arrivesAtDestination();
		next(0);
	}
	
	public void toggleDoors() {
		states[current].toggleDoors();
		next(0);
	}
	
	public ArrayList<Integer> getLamps() {
		ArrayList<Integer> lamps = states[current].getLamps();
		next(0);
		return lamps;
	}
	
	public void exit() {
		states[current].exit();
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
		ArrayList<Integer> lamps = null;
		while (true) {
			switch(current) {
			case 0:
				//Get the request of the next floor with the motor direction from the scheduler
				Entry<Integer,Direction> destination = this.elevator.scheduler.getRequest(this.elevator.getElevatorLocation());
				destinationFloor = destination.getKey();
				if(destination.getValue() == null) {//no more requests move to end
					this.invalidRequest();
					break;
				}
				else if(destination.getValue()==Direction.UP) {
					this.validUpRequest(destination);
					break;
				}
				else if(destination.getValue()==Direction.DOWN) {
					this.validDownRequest(destination);
					break;
				}
				break;
			case 1:
				if(destinationFloor==this.elevator.getElevatorLocation()) {
					this.arrivesAtDestination();
					break;
				}
				else if(destinationFloor>this.elevator.getElevatorLocation()) {
					this.activity();
					break;
				}
				break;
			case 2:
				if(destinationFloor==this.elevator.getElevatorLocation()) {
					this.arrivesAtDestination();
					break;
				}
				else if(destinationFloor<this.elevator.getElevatorLocation()) {
					this.activity();
					break;
				}
				break;
			case 3:
				this.toggleDoors();
				break;
			case 4:
				lamps = this.getLamps();
				break;
			case 5:
				this.action(lamps);
				this.toggleDoors();
				break;
			case 6:
				this.exit();
				break;
			}
		}
	}
}
