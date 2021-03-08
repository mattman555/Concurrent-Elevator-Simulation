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
	private Logger logger;
	
	/**
	 * Constructor for the scheduler class
	 */
	public Scheduler(Logger logger) {
		SchedulerState[] statearr =
			{new AwaitingRequests(this), 
			 new UnsortedRequests(this), 
			 new SortedRequests(this), 
			 new InProgress(this),
			 new End(this)};
		this.states = statearr;
		this.current = 0;
		this.requests = new ArrayList<Request>();
		this.requestBuckets = new ArrayList<>();
		this.completedRequests = new ArrayList<>();
		this.logger = logger;
	}
	
	public Logger getLogger() {
		return this.logger;
	}
	
	/**
	 * Adds an elevator to the scheduler
	 * @param elevator the elevator reference to be added to the scheduler
	 */
	public void addElevator(Elevator elevator) {
		this.elevator = elevator;
	}
	
	/**
	 * Returns the current elevator
	 * @return returns the current elevator object
	 */
	public Elevator getElevator() {
		return this.elevator;
	}
	
	/**
	 * Adds a floor subsystem to the scheduler
	 * @param floorSubsystem the floor subsystem reference to be added to the scheduler
	 */
	public void addFloorSubsystem(FloorSubsystem floorSubsystem) {
		this.floorSubsystem = floorSubsystem;
	}
	
	/**
	 * Gets the requests
	 * @return an ArrayList of Requests that is the requests
	 */
	public ArrayList<Request> getRequests() {
		return requests;
	}

	/**
	 * Gets the requestBuckets
	 * @return an ArrayList of RequestGroups that is the requestBuckets
	 */
	public ArrayList<RequestGroup> getRequestBuckets() {
		return requestBuckets;
	}

	/**
	 * Gets the inProgressBucket
	 * @return a RequestGroup that is the inProgressBucket
	 */
	public RequestGroup getInProgressBucket() {
		return inProgressBucket;
	}
	
	/**
	 * Sets the in progress bucket to another RequestGroup
	 * @param inProgressBucket the new in progress bucket 
	 */
	public void setInProgressBucket(RequestGroup inProgressBucket) {
		this.inProgressBucket = inProgressBucket;
	}

	/**
	 * Gets the completed requests
	 * @return an arraylist containing the completed requests
	 */
	public ArrayList<Request> getCompletedRequests() {
		return completedRequests;
	}

	/**
	 * Moving the scheduler to the next state 
	 * @param nextState the state it will switch to
	 */
	private void nextState(int nextState) {
		 current = transitions[current][nextState];
   }
	
	/**
	 * Gets the current state to return an elevator task.
	 *         
	 * @param currLocation the elevators current location
	 * @return a null request if no requests are currently available,
	 *         a request to floor 10000 if the elevators can stop running
	 *         or returns a valid destination.
	 */
	public synchronized Entry<Integer,Direction> requestTask(int currLocation) {	
		int curr = current;
		nextState(0);
		return states[curr].requestTask(currLocation);
	}
	
	/**
	 * Gets the current state to ask the floorsubsystem for the request list
	 */
	public void getListOfRequests() {
		boolean gotRequests = states[current].getListOfRequests(floorSubsystem);
		nextState( gotRequests ? 1 : 0); //if scheduler got the requests we go to a new state.
	}
	
	/**
	 * Asks the current state to Sort the requests
	 */
	public void sortRequests() {
		states[current].sortRequests();
		nextState(1);
	}

	/**
	 * Called to signal the program that it can stop
	 */
	public void exit() {
		nextState(1);
		states[current].exit();
	}
	
	/**
	 * Adds a request to the requests list
	 * @param request the request to be added
	 */
	public  void addRequest(Request request) {
		this.requests.add(request);
		logger.println("Scheduler: Gets Request for floor " + request.getFloor() + " from floor subsystem");
	}
	
	/**
	 * Gets a completed from the list of completed requests
	 * @return a Request that was completed by an elevator
	 */
	public synchronized Request getCompletedRequest() {
		if(completedRequests.isEmpty()) { //floorSubsystem wait until there are completed requests
			return null;
		}
		logger.println("Scheduler: Sends completed request to " + Thread.currentThread().getName() );
		return completedRequests.remove(0);
	}
	
	/**
	 * Gets the car button lamps that are to be on in the elevator
	 * @return an ArrayList of integers of the car button lamps that are supposed to be on
	 */
	public ArrayList<Integer> getRequestedLamps(){
		logger.println("Scheduler: Sends " + Thread.currentThread().getName() + " requested car lamps");
		return inProgressBucket.getElevatorFloorLamps();
	}
	
	/**
	 * The elevator requests to have its doors toggled and the scheduler will toggle them
	 */
	public void requestDoorChange() {
		logger.println("Scheduler: Toggling " + Thread.currentThread().getName() + " Doors...");
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
		Logger logger = new Logger();
		Scheduler scheduler = new Scheduler(logger);
		Thread schedulerThread = new Thread(scheduler,"Scheduler");
		FloorSubsystem floorSubsystem = new FloorSubsystem(scheduler, 7, logger);
		Thread floorSubsystemThread = new Thread(floorSubsystem,"FloorSubsystem");
		Elevator elevator = new Elevator(scheduler, logger);
		scheduler.addElevator(elevator);
		scheduler.addFloorSubsystem(floorSubsystem);
		Thread elevatorThread = new Thread(new ElevatorSM(elevator,floorSubsystem),"Elevator");
		
		floorSubsystemThread.start();
		schedulerThread.start();
		elevatorThread.start();
		
	}
	
}
