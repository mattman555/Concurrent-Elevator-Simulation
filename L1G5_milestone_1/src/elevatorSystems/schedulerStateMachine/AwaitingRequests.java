/**
 * 
 */
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

	public AwaitingRequests(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	
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
	

	@Override
	public Entry<Integer,Direction> requestTask(int destination) {
		return null;
	}
}