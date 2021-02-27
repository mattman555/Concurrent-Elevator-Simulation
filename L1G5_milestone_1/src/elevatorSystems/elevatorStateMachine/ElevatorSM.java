/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private ElevatorStates curr = ElevatorStates.DOORS_CLOSED;
	private HashMap<ElevatorStates, List<ElevatorStates>> transition;
	private int current;
	private int[][] transitions = {{2,1,5},{2},{3},{4},{0}};
	private Elevator elevator;
	private FloorSubsystem floorSubsystem;
	HashMap<ElevatorStates, ElevatorState> s;
	/**
	* 
	*/
	public ElevatorSM(Elevator elevator, FloorSubsystem floorSubsystem) {
		this.elevator =  elevator;
		this.floorSubsystem = floorSubsystem;
		//wont need
		ElevatorState[] statearr = {new DoorsClosed(this.elevator, this.floorSubsystem), new Moving(this.elevator, this.floorSubsystem), new Arrived(this.elevator), new DoorsOpen(this.elevator), new UpdateLamps(this.elevator), new End(this.elevator)};
		generateTransitionHashmap();
		generateStateHashmap();
		states = statearr;
		current = 0;
	}
	 
	private void generateTransitionHashmap() {
		this.transition = new HashMap<>();
		transition.put(ElevatorStates.DOORS_CLOSED,  List.of(ElevatorStates.ARRIVED, ElevatorStates.MOVING, ElevatorStates.END));
		transition.put(ElevatorStates.MOVING, List.of(ElevatorStates.ARRIVED));
		transition.put(ElevatorStates.ARRIVED, List.of(ElevatorStates.DOORS_OPEN));
		transition.put(ElevatorStates.DOORS_OPEN, List.of(ElevatorStates.UPDATE_LAMPS));
		transition.put(ElevatorStates.UPDATE_LAMPS, List.of(ElevatorStates.DOORS_CLOSED));
	}
	
	private void generateStateHashmap() {
		this.s = new HashMap<>();
		s.put(ElevatorStates.DOORS_CLOSED, new DoorsClosed(this.elevator, this.floorSubsystem));
		s.put(ElevatorStates.MOVING,  new Moving(this.elevator, this.floorSubsystem));
		s.put(ElevatorStates.ARRIVED,  new Arrived(this.elevator));
		s.put(ElevatorStates.DOORS_OPEN, new DoorsOpen(this.elevator));
		s.put(ElevatorStates.UPDATE_LAMPS, new UpdateLamps(this.elevator));
		s.put(ElevatorStates.END, new End(this.elevator));
	}
	
	private void nextState(ElevatorStates nextState) {
		if(transition.get(curr).contains(nextState))
			curr = nextState; //else throw exception?
		 //current = transitions[current][nextState];
    }
	
	public void activity(Direction direction) {
		s.get(curr).activity(direction);
		//states[current].activity(direction);
	}
	
	public void action(ArrayList<Integer> lamps) {
		s.get(curr).action(lamps);
		//states[current].action(lamps);
	}
	
	public void validRequest(Entry<Integer,Direction> destination) {
		s.get(curr).validRequest(destination);
		//states[current].validRequest(destination);
		nextState(ElevatorStates.MOVING);
	}
	public void invalidRequest() {
		s.get(curr).invalidRequest();
		//states[current].invalidRequest();
		nextState(ElevatorStates.END);
	}
	
	public void arrivesAtDestination() {
		s.get(curr).arrivesAtDestination();
		//states[current].arrivesAtDestination();
		nextState(ElevatorStates.ARRIVED);
	}
	
	public void toggleDoors(ElevatorStates next) {
		s.get(curr).toggleDoors();
		//states[current].toggleDoors();
		nextState(next);
	}
	
	public ArrayList<Integer> getLamps() {
		ArrayList<Integer> lamps = s.get(curr).getLamps();
		//ArrayList<Integer> lamps = states[current].getLamps();
		nextState(ElevatorStates.UPDATE_LAMPS);
		return lamps;
	}
	
	public void exit() {
		s.get(curr).exit();
		//states[current].exit();
//		current = 6;
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
			switch(curr) {
			case DOORS_CLOSED:
				//Get the request of the next floor with the motor direction from the scheduler
				Entry<Integer,Direction> destination = this.elevator.scheduler.getRequest(this.elevator.getElevatorLocation());
				if(destination == null) {//no more requests move to end
					this.invalidRequest();
				}
				else if(destination.getValue() == Direction.UP || destination.getValue() == Direction.DOWN) {
					destinationFloor = destination.getKey();
					destinationDirection = destination.getValue();
					this.validRequest(destination);
				}
				else if(destination.getValue() == Direction.STATIONARY) {
					this.arrivesAtDestination();
				}
				break;
			case MOVING:
				if(destinationFloor == this.elevator.getElevatorLocation()) {
					this.arrivesAtDestination();
				}
				else if(destinationFloor > this.elevator.getElevatorLocation() || destinationFloor < this.elevator.getElevatorLocation()) {
					this.activity(destinationDirection);
				}
				break;
			case ARRIVED:
				this.toggleDoors(ElevatorStates.DOORS_OPEN);
				break;
			case DOORS_OPEN:
				lamps = this.getLamps();
				break;
			case UPDATE_LAMPS:
				this.action(lamps);
				this.toggleDoors(ElevatorStates.DOORS_CLOSED);
				break;
			case END:
				this.exit();
				return;
			}
		}
	}
}
