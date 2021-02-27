/**
 * 
 */
package elevatorSystems.elevatorStateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import elevatorSystems.Direction;
import elevatorSystems.Elevator;
import elevatorSystems.FloorSubsystem;

/**
 * @author Matthew Harris 101073502
 * @author Jay McCracken 101066860
 *
 *	The State machine for the elevator, switching states based on event
 */
public class ElevatorSM implements Runnable{

	private ElevatorStates current;
	HashMap<ElevatorStates, ElevatorState> states;
	private HashMap<ElevatorStates, List<ElevatorStates>> transitions;
	private Elevator elevator;
	private FloorSubsystem floorSubsystem;
;

	public ElevatorSM(Elevator elevator, FloorSubsystem floorSubsystem) {
		this.elevator =  elevator;
		this.floorSubsystem = floorSubsystem;
		this.current = ElevatorStates.DOORS_CLOSED;
		generateTransitionHashmap();
		generateStateHashmap();
	}
	 
	/**
	 * Creating the order of states, what state can switch to what state
	 */
	private void generateTransitionHashmap() {
		this.transitions = new HashMap<>();
		this.transitions.put(ElevatorStates.DOORS_CLOSED,  List.of(ElevatorStates.ARRIVED, ElevatorStates.MOVING, ElevatorStates.END));
		this.transitions.put(ElevatorStates.MOVING, List.of(ElevatorStates.ARRIVED));
		this.transitions.put(ElevatorStates.ARRIVED, List.of(ElevatorStates.DOORS_OPEN));
		this.transitions.put(ElevatorStates.DOORS_OPEN, List.of(ElevatorStates.UPDATE_LAMPS));
		this.transitions.put(ElevatorStates.UPDATE_LAMPS, List.of(ElevatorStates.DOORS_CLOSED));
	}
	
	/**
	 * Matching the class states the reference each of the different states
	 */
	private void generateStateHashmap() {
		this.states = new HashMap<>();
		this.states.put(ElevatorStates.DOORS_CLOSED, new DoorsClosed(this.elevator, this.floorSubsystem));
		this.states.put(ElevatorStates.MOVING,  new Moving(this.elevator, this.floorSubsystem));
		this.states.put(ElevatorStates.ARRIVED,  new Arrived(this.elevator));
		this.states.put(ElevatorStates.DOORS_OPEN, new DoorsOpen(this.elevator));
		this.states.put(ElevatorStates.UPDATE_LAMPS, new UpdateLamps(this.elevator));
		this.states.put(ElevatorStates.END, new End(this.elevator));
	}
	
	public Elevator getElevator() {
		return this.elevator;
	}
	/**
	 * Moving the elevator to the next state if it can
	 * @param nextState the state it tries to switch to
	 */
	private void nextState(ElevatorStates nextState) {
		if(transitions.get(current).contains(nextState))
			current = nextState; //else throw exception?
    }
	
	/**
	 * The movement of the elevator to the next floor by 1 based on the direction given
	 * @param direction, If the elevator is going UP or DOWN
	 */
	public void activity(Direction direction) {
		states.get(current).activity(direction);
	}
	
	/**
	 * Turning on and off the car lamps based on the current location and the 
	 * requested new floors
	 * @param lamps, the list of lamps that need to be turned on
	 */
	public void action(ArrayList<Integer> lamps) {
		states.get(current).action(lamps);
	}
	
	/**
	 * When a valid request is made set to go to the new destination
	 * switching to the new state MOVING
	 * @param destination, where the elevator needs to go next
	 */
	public void validRequest(Entry<Integer,Direction> destination) {
		states.get(current).validRequest(destination);
		nextState(ElevatorStates.MOVING);
	}
	
	/**
	 * When a invalid request is sent, switch to the END state
	 */
	public void invalidRequest() {
		states.get(current).invalidRequest();
		nextState(ElevatorStates.END);
	}
	
	/**
	 * When the elevator arrives at the required destination
	 * switch to arrived state
	 */
	public void arrivesAtDestination() {
		states.get(current).arrivesAtDestination();
		nextState(ElevatorStates.ARRIVED);
	}
	
	/**
	 * Toggling the doors of the elevator to open or closed based on what it last was
	 * @param next the state of the door that was switched too
	 */
	public void toggleDoors(ElevatorStates next) {
		states.get(current).toggleDoors();
		nextState(next);
	}
	
	/**
	 * Getting the car lamps that are being requested, switching to update lamps
	 * @return the list of lamps that need to be turned on
	 */
	public ArrayList<Integer> getLamps() {
		ArrayList<Integer> lamps = states.get(current).getLamps();
		nextState(ElevatorStates.UPDATE_LAMPS);
		return lamps;
	}
	
	/**
	 * exit state to terminate the state machine
	 */
	public void exit() {
		states.get(current).exit();
	}
	
	/**
	 * to get the current state of the elevator
	 * @return current state
	 */
	public ElevatorStates getState() {
		return current;
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
			case DOORS_CLOSED:
				//Get the request of the next floor with the motor direction from the scheduler
				Entry<Integer,Direction> destination = this.elevator.scheduler.requestTask(this.elevator.getElevatorLocation());
				if(destination == null) {
					break;
				}
				else if(destination.getKey() == 10000) {//no more requests move to end
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
