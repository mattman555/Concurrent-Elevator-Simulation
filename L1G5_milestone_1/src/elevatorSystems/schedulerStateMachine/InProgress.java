package elevatorSystems.schedulerStateMachine;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import elevatorSystems.Direction;
import elevatorSystems.Request;
import elevatorSystems.Scheduler;


/**
 * @author Ambar Mendez Jimenez 
 * @author Kevin Belanger 101121709
 * @author Nick Coutts
 * @author Matthew Harris
 */
public class InProgress extends SchedulerState {

	private Scheduler scheduler;

	/**
	 * Constructor for the InProgress class
	 * @param scheduler the scheduler this state belongs to
	 */
	public InProgress(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public synchronized Entry<Integer,Direction> requestTask(int id, int currLocation) {
		if(scheduler.getInProgressBucket(id) == null ) { // get a new bucket
			scheduler.setInProgressBucket(id, scheduler.getRequestBuckets().remove(0));
		}
		else { // at a destination floor, a request may have been completed
			scheduler.getInProgressBucket(id).removeElevatorFloorLamp(currLocation); //turn off floor lamp
			ArrayList<Request> removable = new ArrayList<Request>(); //dont want to remove them while iterating over them
			for(Request request: scheduler.getInProgressBucket(id).getRequests()) {
				if(request.getCarButton() == currLocation) {
					removable.add(request);
					scheduler.getCompletedRequests().add(request);
				}
			}
			scheduler.getInProgressBucket(id).removeRequests(removable); // remove finished requests from the current bucket
			if(scheduler.getInProgressBucket(id).getRequests().size() == 0) { //bucket is complete, get a new one
				if(scheduler.getRequestBuckets().size() > 0)
					scheduler.setInProgressBucket(id, scheduler.getRequestBuckets().remove(0));
				else { // all requests have been completed, elevators can stop
					return Map.entry(10000, Direction.UP);
				}
			}
		}
		
		Integer destination = scheduler.getInProgressBucket(id).getNextDestination();
		Direction direction = destination > currLocation ? Direction.UP : Direction.DOWN;
		scheduler.getLogger().println("Scheduler: Sends " + Thread.currentThread().getName() + " to move " + direction + " to floor " + destination);
		return Map.entry(destination, direction);
	}
}