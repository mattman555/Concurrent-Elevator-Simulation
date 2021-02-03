package elevatorSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.util.Pair;


public class Scheduler implements Runnable {

	private List<Request> requests;
	private List<ArrayList<Request>> requestBuckets;
	private List<Request> completedRequests;
	boolean sorted;
	private boolean done;
	
	public Scheduler() {
		this.requests = Collections.synchronizedList(new ArrayList<Request>());
		this.requestBuckets = Collections.synchronizedList(new ArrayList<ArrayList<Request>>());
		this.completedRequests = Collections.synchronizedList(new ArrayList<Request>());
		this.sorted = false;
		this.done = false;
	}
	
	public synchronized Pair<ArrayList<Request>, ArrayList<Integer>> getRequest() {
		while(requests.isEmpty()) { //elevator wait until there are requests
			try {
				if(done) {
					return null;
				}
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ArrayList<Request> bucket = requestBuckets.remove(0);
		return new Pair<ArrayList<Request>,ArrayList<Integer>>(bucket, floorRoute(bucket));
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
		if (request.getStatus() == Progress.COMPLETED)
			completedRequests.add(request);
		else if(request.getStatus() == Progress.INCOMPLETE){
			requests.add(request);
		}
		notifyAll();
	}
	
	public void addRequests(List<Request> requests) {
		for(Request request : requests) {
			addRequest(request);
		}
		if(!sorted)
			sortRequestsIntoBuckets();
	}
	
	/**
	 * Sorts requests into buckets of similar requests 
	 */
	private void sortRequestsIntoBuckets() {
		if(requests.isEmpty())
			return;
		Request initial = requests.get(0);
		ArrayList<Request> currBucket = new ArrayList<Request>();
		currBucket.add(initial);
		int upperBound = initial.getCarButton();
		for(int i = 1; i < requests.size(); i++) {
			Request curr = requests.get(i);
			if(compareTime(initial, curr)) { // check if similar time
				if(similarRequests(initial,curr,upperBound)) { // check if same direction and within bounds
					currBucket.add(curr);
					if(curr.getCarButton() > upperBound)
						upperBound = curr.getCarButton(); //set the new upperBound
				}
			}
			else { //not within time so add this bucket and make new bucket
				requestBuckets.add((ArrayList<Request>) currBucket.clone()); //add curr bucket
				//make new bucket
				currBucket.clear();
				initial = requests.get(i);
				currBucket.add(initial);
				upperBound = initial.getCarButton();
			}		
		}
		requestBuckets.add((ArrayList<Request>) currBucket.clone()); // after final bucket, add it
	}
	
	private ArrayList<Integer> floorRoute(ArrayList<Request> requests) {
		ArrayList<Integer> floors = new ArrayList<>();
		for(int i = 0; i < requests.size(); i++) {
			Request request = requests.get(i);
			
			if(!floors.contains(request.getFloor()))
				floors.add(request.getFloor());
			
			if(!floors.contains(request.getCarButton())) //shouldn't add duplicate floors
				floors.add(request.getCarButton());
		}
		bubbleSort(floors);
		return floors;
	}
	
	private void bubbleSort(ArrayList<Integer> arr) {
		int n = arr.size();
		for (int i = 0; i < n-1; i++) {
			for (int j = 0; j < n-i-1; j++) {
				if (arr.get(j) > arr.get(j+1)) { 
					// swap arr[j] and arr[j + 1] 
					int temp = arr.get(j); 
					arr.set(j, arr.get(j + 1));
					arr.set(j + 1, temp);
                }
			}
		}
    }
	
	private boolean compareTime(Request initial, Request curr) {
		int[] currTime = curr.getTime();
		int[] initialTime = initial.getTime();
		int currTotal = currTime[0]*360 + currTime[1] * 60 + currTime[2];
		int initialTotal = initialTime[0]*360 + initialTime[1] * 60 + initialTime[2];
		return (currTotal - initialTotal <= 30);
	}
	
	private boolean similarRequests(Request initial, Request curr, int upperBound) {
		boolean sameDir = curr.getFloorButtons().equals(initial.getFloorButtons()); // compare directions of requests
		boolean withinBound = curr.getFloor() > initial.getFloor() && curr.getFloor() < upperBound; // see if the new request is between the original floor and the upper bound
		return  sameDir && withinBound;
	}
	
	
	@Override
	public void run() {
		while(!done) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	public static void main(String[] args) {
		
	}

}
