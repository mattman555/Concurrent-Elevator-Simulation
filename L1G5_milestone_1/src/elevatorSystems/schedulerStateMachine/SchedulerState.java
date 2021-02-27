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

	public Entry<Integer,Direction> requestTask(int currLocation) {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}

	public boolean getListOfRequests(FloorSubsystem floorSubsystem) {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}

	public void sortRequests() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}

	public void exit() {
		System.out.println("Incorrect State");
		throw new IllegalArgumentException();
	}

}