package elevatorSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @author Nick Coutts
 */
public class Scheduler implements Runnable {

	private List<Request> requests;
	private List<RequestGroup> requestBuckets;
	private RequestGroup inProgressBucket;
	private List<Request> completedRequests;
	private boolean done;
	private Elevator elevator;
	
	/**
	 * Constructor for the scheduler class
	 */
	public Scheduler() {
		this.requests = Collections.synchronizedList(new ArrayList<Request>());
		this.requestBuckets = Collections.synchronizedList(new ArrayList<RequestGroup>());
		this.completedRequests = Collections.synchronizedList(new ArrayList<Request>());
		this.done = false;
	}
	
	/**
	 * Adds an elevator to the scheduler
	 * @param elevator the elevator reference to be added to the scheduler
	 */
	public void addElevator(Elevator elevator) {
		this.elevator = elevator;
	}
	
	/**
	 * returns the current elevator
	 * @return returns the current elevator object
	 */
	public Elevator getElevator() {
		return this.elevator;
	}

	/**
	 * Returns the next destination and the direction in which that destination is based on the current location of the elevator
	 * @param currLocation current location of the elevator
	 * @return an entry(key-value pair) containing the floor to go to and the direction that floor is 
	 */	
	public synchronized Map.Entry<Integer, Direction> getRequest(int currLocation) {
		System.out.print(requestBuckets.size() + " ");
		while(requestBuckets.size() == 0 && inProgressBucket == null) { //elevator wait until there are requests
			try {
				if(done)
					return null;
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(inProgressBucket == null ) { // get a new bucket
			inProgressBucket = requestBuckets.remove(0);
		}
		else { // at a destination floor, a request has been completed
			inProgressBucket.removeElevatorFloorLamp(currLocation); //turn off floor lamp 
			for(Request request: inProgressBucket.getRequests()) {
				if(request.getCarButton() == currLocation) {
					inProgressBucket.removeRequest(request);
					completedRequests.add(request);
					requests.remove(request);
					notifyAll();
				}
			}
			if(inProgressBucket.getRequests().size() == 0) {
				if(requestBuckets.size() > 0)
					inProgressBucket = requestBuckets.remove(0);
				else {
					return null;
				}
			}
		}
		Integer destination = inProgressBucket.getNextDestination();
		Direction direction = destination > currLocation ? Direction.UP : Direction.DOWN;
		return Map.entry(destination, direction);
	}
	
	/**
	 * Gets a completed from the list of completed requests
	 * @return a Request that was completed by an elevator
	 */
	public synchronized Request getCompletedRequest() {
		while(completedRequests.isEmpty()) { //floorSubsystem wait until there are completed requests
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return completedRequests.remove(0);
	}
	
	/**
	 * Called when the system is out of requests and all requests have been completed and used to signal the elevators that they can stop running
	 */
	public void setDone() {
		this.done = true;
		notifyAll();
	}
	
	
	public synchronized void addRequests(List<Request> requests) {
		for(Request request : requests) {
			this.requests.add(request);
		}
		sortRequestsIntoGroups();
		notifyAll();
			
	}
	
	/**
	 * Sorts requests into groups of similar requests. 
	 * Similar requests are currently if the request originates from the same floor and is within 30 seconds from the first request in that group
	 */
	private void sortRequestsIntoGroups() {
		if(requests.isEmpty())
			return;
		Request initial = requests.get(0);
		ArrayList<Request> currGroup = new ArrayList<Request>();
		currGroup.add(initial);
		for(int i = 1; i < requests.size(); i++) {
			Request curr = requests.get(i);
			if(compareTime(initial, curr) && similarRequests(initial,curr)) { // check if same direction and within bounds
					currGroup.add(curr);
				
			}
			else { //not within time so add this group and make new group
				RequestGroup group = new RequestGroup((ArrayList<Request>) currGroup.clone());
				requestBuckets.add(group); //add curr group
				//make new bucket
				currGroup.clear();
				initial = requests.get(i);
				currGroup.add(initial);
			}		
		}
		requestBuckets.add(new RequestGroup((ArrayList<Request>) currGroup.clone())); // after final group, add it
	}
	
	/**
	 * Gets the car button lamps that are to be on in the elevator
	 * @return an ArrayList of integers of the car button lamps that are supposed to be on
	 */
	public ArrayList<Integer> getRequestedLamps(){
		return inProgressBucket.getElevatorFloorLamps();
	}
	
	/**
	 * The elevator requests to have its doors toggled and the scheduler will toggle them
	 */
	public void requestDoorChange() {
		elevator.toggleDoors();
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
		boolean sameDir = curr.getFloorButtons().equals(initial.getFloorButtons()); // compare directions of requests
		boolean sameFloor = curr.getFloor() == initial.getFloor(); // see if the new request is initiated on the same floor as the original floor 
		return  sameDir && sameFloor;
	}
	
	/**
	 * The method that runs when starting a Thread containing a Scheduler runnable.
	 */
	@Override
	public void run() {
		while(!done) {
			elevator.getElevatorLocation();
		}
		
	}
	
	/**
	 * Initiates the threads and starts them
	 * @param args
	 */
	public static void main(String[] args) {
		
		Scheduler scheduler = new Scheduler();
		Thread schedulerThread = new Thread(scheduler,"Scheduler");
		Thread floorSubsystemThread = new Thread(new FloorSubsystem(scheduler, 7),"FloorSubsystem");
		Elevator elevator = new Elevator(scheduler);
		scheduler.addElevator(elevator);
		Thread elevatorThread = new Thread(elevator,"Elevator");
		
		floorSubsystemThread.start();
		schedulerThread.start();
		elevatorThread.start();
		
	}

}
