package elevatorSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;



public class Scheduler implements Runnable {

	private List<Request> requests;
	private List<RequestGroup> requestBuckets;
	private RequestGroup inProgressBucket;
	private List<Request> completedRequests;
	private boolean done;
	private Elevator elevator;
	
	public Scheduler() {
		this.requests = Collections.synchronizedList(new ArrayList<Request>());
		this.requestBuckets = Collections.synchronizedList(new ArrayList<RequestGroup>());
		this.completedRequests = Collections.synchronizedList(new ArrayList<Request>());
		this.done = false;
	}
	
	public void addElevator(Elevator elevator) {
		this.elevator = elevator;
	}
	
	public Elevator getElevator() {
		return this.elevator;
	}
	
	public synchronized Map.Entry<Integer, Direction> getRequest(int currLocation) {
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
			inProgressBucket.removeFloorLamp(currLocation); //turn off floor lamp 
			for(Request request: inProgressBucket.getRequests()) {
				if(request.getCarButton() == currLocation) {
					inProgressBucket.removeRequest(request);
					completedRequests.add(request);
					requests.remove(request);
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
	
	public void setDone() {
		this.done = true;
		notifyAll();
	}
	
	public synchronized void addRequest(Request request) {
		requests.add(request);
	}
	
	public void addRequests(List<Request> requests) {
		for(Request request : requests) {
			addRequest(request);
		}
		sortRequestsIntoGroups();
		notifyAll();
			
	}
	
	/**
	 * Sorts requests into groups of similar requests 
	 */
	public void sortRequestsIntoGroups() {
		if(requests.isEmpty())
			return;
		Request initial = requests.get(0);
		ArrayList<Request> currGroup = new ArrayList<Request>();
		currGroup.add(initial);
		for(int i = 1; i < requests.size(); i++) {
			Request curr = requests.get(i);
			if(compareTime(initial, curr)) { // check if similar time
				if(similarRequests(initial,curr)) { // check if same direction and within bounds
					currGroup.add(curr);
				}
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
	
	public ArrayList<Integer> getRequestedLamps(){
		return inProgressBucket.getFloorLamps();
	}
	
	public void requestDoorChange() {
		elevator.toggleDoors();
	}
	
	
	private boolean compareTime(Request initial, Request curr) {
		int[] currTime = curr.getTime();
		int[] initialTime = initial.getTime();
		int currTotal = currTime[0] * 360 + currTime[1] * 60 + currTime[2];
		int initialTotal = initialTime[0] * 360 + initialTime[1] * 60 + initialTime[2];
		return (currTotal - initialTotal <= 30);
	}
	
	private boolean similarRequests(Request initial, Request curr) {
		boolean sameDir = curr.getFloorButtons().equals(initial.getFloorButtons()); // compare directions of requests
		boolean sameFloor = curr.getFloor() == initial.getFloor(); // see if the new request is initiated on the same floor as the original floor 
		return  sameDir && sameFloor;
	}
	
	
	@Override
	public void run() {
		while(!done) {
			elevator.getElevatorLocation();
		}
		
	}
	
	public static void main(String[] args) {
		//Thread floor = new 
		//need code first
		Scheduler scheduler = new Scheduler();
		Thread schedulerThread = new Thread(scheduler,"Scheduler");
		Elevator elevator = new Elevator(scheduler);
		scheduler.addElevator(elevator);
		Thread elevatorThread = new Thread(elevator,"Elevator");
		
		//floor.start()
		schedulerThread.start();
		elevatorThread.start();
		
	}

}
