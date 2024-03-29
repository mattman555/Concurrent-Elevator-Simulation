package elevatorSystems.schedulerStateMachine;

import java.net.DatagramSocket;
import java.util.Map.Entry;

import elevatorSystems.Direction;

/**
 * @author Ambar Mendez Jimenez 
 * @author Kevin Belanger 101121709
 * @author Nick Coutts
 * @author Matthew Harris
 */
public abstract class SchedulerState {

	/**
	 * default implementation of the requestTask method
	 * @param currLocation current location of the elevator
	 */
	public Entry<Integer,Direction> requestTask(int id, int currLocation) {
		throw new IllegalArgumentException("Scheduler is in an Incorrect State");
	}

	/**
	 * default implementation of the getListOfRequests method
	 * @param currLocation current location of the elevator
	 */
	public boolean getListOfRequests(DatagramSocket floorSubsystemSocket) {
		throw new IllegalArgumentException("Scheduler is in an Incorrect State");
	}

	/**
	 * default implementation of the sortRequests method
	 */
	public void sortRequests() {
		throw new IllegalArgumentException("Scheduler is in an Incorrect State");
	}

	/**
	 * default implementation of the exit method
	 */
	public void exit() {
		throw new IllegalArgumentException("Scheduler is in an Incorrect State");
	}

}