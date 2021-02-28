package elevatorSystems.schedulerStateMachine;

import java.util.Map.Entry;

import elevatorSystems.Direction;
import elevatorSystems.FloorSubsystem;


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
	public Entry<Integer,Direction> requestTask(int currLocation) {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}

	/**
	 * default implementation of the getListOfRequests method
	 * @param currLocation current location of the elevator
	 */
	public boolean getListOfRequests(FloorSubsystem floorSubsystem) {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}

	/**
	 * default implementation of the sortRequests method
	 */
	public void sortRequests() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}

	/**
	 * default implementation of the exit method
	 */
	public void exit() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}

}