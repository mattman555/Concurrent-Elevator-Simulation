package elevatorSystems.elevatorStateMachine;

/**
 * The 4 kinds of requests that could be sent over UDP
 * @author Jay McCracken
 *
 */
public enum RPCRequestType {
	TOGGLE_DOORS,
	GET_REQUEST,
	GET_LAMPS,
	SET_LAMPS,
	ELEVATOR_SHUTDOWN
}
