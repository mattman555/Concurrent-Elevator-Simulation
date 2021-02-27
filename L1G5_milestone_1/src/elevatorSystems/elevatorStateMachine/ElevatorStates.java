package elevatorSystems.elevatorStateMachine;

/**
 * All of the state that the elevator State Machine has
 * @author Nick Coutts 
 *
 */
public enum ElevatorStates {
	DOORS_CLOSED,
	MOVING,
	ARRIVED,
	DOORS_OPEN,
	UPDATE_LAMPS,
	END	
}
