package elevatorSystems.elevatorStateMachine;

/**
 * All of the state that the elevator State Machine has
 * @author Nick Coutts
 * @author Jay McCracken
 * 
 */
public enum ElevatorStates {
	DOORS_CLOSED,
	MOVING,
	ARRIVED,
	DOORS_OPEN,
	DOOR_STUCK,
	UPDATE_LAMPS,
	END	
}
