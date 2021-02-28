package elevatorSystems.schedulerStateMachine;

import java.util.ArrayList;
import java.util.Map.Entry;
import elevatorSystems.Direction;
import elevatorSystems.FloorSubsystem;
import elevatorSystems.Request;
import elevatorSystems.Scheduler;

/**
 * @author Ambar Mendez Jimenez 
 * @author Kevin Belanger 101121709
 * @author Nick Coutts
 * @author Matthew Harris
 */
public class AwaitingRequests extends SchedulerState {

	private Scheduler scheduler; 

	/**
	 * Constructor for the class
	 * @param scheduler the scheduler this state belongs to
	 */
	public AwaitingRequests(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * Gets the current list of requests from the floorSubsystem
	 * @param floorSubsystem the Scheduler's floorSubsystem
	 */
	@Override
	public boolean getListOfRequests(FloorSubsystem floorSubsystem) {
		ArrayList<Request> requests = floorSubsystem.getListOfRequests();
		if(requests == null)
			return false;
		System.out.println("Scheduler received list of requests.");
		for(Request request : requests) {
			this.scheduler.addRequest(request);
			System.out.println("Scheduler: Gets Request for floor " + request.getFloor() + " from " + Thread.currentThread().getName() + "...");
		}
		System.out.println("Scheduler transitions from awaiting requests to unsorted requests.");

		return true;
	}
	
	/**
	 * Returns null since the scheduler isnt in a state to give an elevator a task
	 */
	@Override
	public Entry<Integer,Direction> requestTask(int destination) {
		return null;
	}
}