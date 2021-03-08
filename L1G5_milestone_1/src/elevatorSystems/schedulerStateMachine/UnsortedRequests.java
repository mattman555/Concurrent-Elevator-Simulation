package elevatorSystems.schedulerStateMachine;

import java.util.ArrayList;
import java.util.Map.Entry;

import elevatorSystems.Direction;
import elevatorSystems.Request;
import elevatorSystems.RequestGroup;
import elevatorSystems.Scheduler;

/**
 * @author Ambar Mendez Jimenez 
 * @author Kevin Belanger 101121709
 * @author Nick Coutts
 * @author Matthew Harris
 *
 */
public class UnsortedRequests extends SchedulerState {

	private Scheduler scheduler;

	/**
	 * Constructor for the UnsortedRequest class
	 * @param scheduler the scheduler this state belongs to
	 */
	public UnsortedRequests(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * Returns null since the scheduler isnt in a state to send tasks
	 */
	@Override
	public Entry<Integer,Direction> requestTask(int id, int destination) {
		return null;
	}

	/**
	 * Sorts requests into groups of similar requests. 
	 * Similar requests are currently if the request originates from the same floor and is within 30 seconds from the first request in that group
	 */
	@Override
	public void sortRequests() {
		while(!scheduler.getRequests().isEmpty()) {
			Request initial = scheduler.getRequests().get(0);
			ArrayList<Request> currGroup = new ArrayList<Request>();
			currGroup.add(initial);
			for(int i = 1; i < scheduler.getRequests().size(); i++) {
				Request curr = scheduler.getRequests().get(i);
				if(compareTime(initial, curr) && similarRequests(initial,curr)) { // check if similar time
						currGroup.add(curr);
				}
				else { //not within time so all requests after will not be within time 
					break;
				}		
			}
			//add this group and remove  them from requests
			scheduler.getRequestBuckets().add(new RequestGroup((ArrayList<Request>) currGroup.clone())); // after final group, add it
			for(Request r : currGroup) {
				scheduler.getRequests().remove(r); //remove requests
			}
		}
		scheduler.getLogger().println("Scheduler sorted requests");

	}
	
	/**
	 * Compares the time between a request and the initial request in a group
	 * @param initial the initial request in a group
	 * @param curr the request that will be compared to the initial request
	 * @return true if there is 30 seconds or less difference between them, false if there is more than a 30 second difference.
	 */
	private boolean compareTime(Request initial, Request curr) {
		int[] currTime = curr.getTime();
		int[] initialTime = initial.getTime();
		int currTotal = currTime[0] * 360 + currTime[1] * 60 + currTime[2];
		int initialTotal = initialTime[0] * 360 + initialTime[1] * 60 + initialTime[2];
		return (currTotal - initialTotal <= 30);
	}
	
	/**
	 * Compares a Request with an initial of the group to see if they are in the same direction and if they originated from the same floor
	 * @param initial the first request in a group and the Request that the other Request will be compared to
	 * @param curr the request that will be compared to the initial Request
	 * @return true if they are in the same direction and originate from the same floor, false if they are different directions or originate from a different floor
	 */
	private boolean similarRequests(Request initial, Request curr) {
		boolean sameDir = curr.getFloorButton().equals(initial.getFloorButton()); // compare directions of requests
		boolean sameFloor = curr.getFloor() == initial.getFloor(); // see if the new request is initiated on the same floor as the original floor 
		return  sameDir && sameFloor;
	}
	
	
}