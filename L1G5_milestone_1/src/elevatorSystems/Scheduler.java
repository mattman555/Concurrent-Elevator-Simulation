package elevatorSystems;
import java.util.ArrayList;
import java.util.Map.Entry;

import elevatorSystems.elevatorStateMachine.ElevatorSM;
import elevatorSystems.schedulerStateMachine.*;

public class Scheduler implements Runnable {
	private SchedulerState[] states;
	private int current;
	private int[][] transitions = {{0,1}, {1,2}, {3}, {3,4}};
	public Elevator elevator;
	private FloorSubsystem floorSubsystem;
	private ArrayList<Request> requests;
	private ArrayList<RequestGroup> requestBuckets;
	private RequestGroup inProgressBucket;
	private ArrayList<Request> completedRequests;
	
	
	/**
	* 
	*/
	public Scheduler() {
		SchedulerState[] statearr =
			{new AwaitingRequests(this), 
			 new UnsortedRequests(this), 
			 new SortedRequests(this), 
			 new InProgress(this),
			 new End()};
		this.states = statearr;
		this.current = 0;
		this.requests = new ArrayList<>();
		this.requestBuckets = new ArrayList<>();
		this.completedRequests = new ArrayList<>();
	}
	
	public void addElevator(Elevator elevator) {
		this.elevator = elevator;
	}
	
	public Elevator getElevator() {
		return this.elevator;
	}
	
	public void addFloorSubsystem(FloorSubsystem floorSubsystem) {
		this.floorSubsystem = floorSubsystem;
	}
	
	public ArrayList<Request> getRequests() {
		return requests;
	}

	public ArrayList<RequestGroup> getRequestBuckets() {
		return requestBuckets;
	}

	public RequestGroup getInProgressBucket() {
		return inProgressBucket;
	}
	
	public void setInProgressBucket(RequestGroup inProgressBucket) {
		this.inProgressBucket = inProgressBucket;
	}

	public ArrayList<Request> getCompletedRequests() {
		return completedRequests;
	}

	
	private void nextState(int nextState) {
		 current = transitions[current][nextState];
   }
	
	public synchronized Entry<Integer,Direction> requestTask(int currLocation) {	
		int curr = current;
		nextState(0);
		return states[curr].requestTask(currLocation);
	}
	
	public void getListOfRequests() {
		boolean gotRequests = states[current].getListOfRequests(floorSubsystem);
		System.out.println("Scheduler request are ready");
		nextState( gotRequests ? 1 : 0);
	}
	
	public void sortRequests() {
		states[current].sortRequests();
		nextState(1);
	}

	public void exit() {
		nextState(1);
		states[current].exit();
		System.out.println(current);
	}
	
	public  void addRequest(Request request) {
		this.requests.add(request);
		System.out.println("Scheduler: Gets Request for floor " + request.getFloor() + " from floor subsystem");
	}
	
	/**
	 * Gets a completed from the list of completed requests
	 * @return a Request that was completed by an elevator
	 */
	public synchronized Request getCompletedRequest() {
		if(completedRequests.isEmpty()) { //floorSubsystem wait until there are completed requests
			return null;
		}
		System.out.println("Scheduler: Sends completed request to " + Thread.currentThread().getName() );
		return completedRequests.remove(0);
	}
	
	/**
	 * Gets the car button lamps that are to be on in the elevator
	 * @return an ArrayList of integers of the car button lamps that are supposed to be on
	 */
	public ArrayList<Integer> getRequestedLamps(){
		System.out.println("Scheduler: Sends " + Thread.currentThread().getName() + " requested car lamps");
		return inProgressBucket.getElevatorFloorLamps();
	}
	
	/**
	 * The elevator requests to have its doors toggled and the scheduler will toggle them
	 */
	public void requestDoorChange() {
		System.out.println("Scheduler: Toggling " + Thread.currentThread().getName() + " Doors...");
		elevator.toggleDoors();
	}
	
	
	@Override
	/**
	 * The running of the elevator, travel to new floor, updating lamps
	 */
	public void run() {		
		while (true) {
			switch(current) {
				case 0:
					//Scheduler waiting for requests
					System.out.println("Scheduler waiting for all requests");
					this.getListOfRequests();
					break;
				case 1:
					//Unsorted requests
					System.out.println("Scheduler sorting requests");
					this.sortRequests();
					break;
				case 2:
					//Sorted requests
					
					break;
				case 3:
					//In Progress State
					break;
			}
		}
	}
	
	/**
	 * Initiates the threads and starts them
	 * @param args
	 */
	public static void main(String[] args) {
		
		Scheduler scheduler = new Scheduler();
		Thread schedulerThread = new Thread(scheduler,"Scheduler");
		FloorSubsystem floorSubsystem = new FloorSubsystem(scheduler, 7);
		Thread floorSubsystemThread = new Thread(floorSubsystem,"FloorSubsystem");
		Elevator elevator = new Elevator(scheduler);
		scheduler.addElevator(elevator);
		scheduler.addFloorSubsystem(floorSubsystem);
		Thread elevatorThread = new Thread(new ElevatorSM(elevator,floorSubsystem),"Elevator");
		
		floorSubsystemThread.start();
		schedulerThread.start();
		elevatorThread.start();
		
	}
	
}