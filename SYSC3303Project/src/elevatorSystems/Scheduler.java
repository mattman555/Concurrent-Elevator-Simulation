package elevatorSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scheduler implements Runnable {

	private List<Request> requests;
	private List<Request> completedRequests;
	private boolean done;
	
	public Scheduler() {
		this.requests = Collections.synchronizedList(new ArrayList<Request>());
		this.completedRequests = Collections.synchronizedList(new ArrayList<Request>());
		this.done = false;
	}
	
	public synchronized Request getRequest() {
		while(requests.isEmpty()) { //elevator wait until there are requests
			try {
				if(done) {
					//TODO: notify the elevators they are complete
				}
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return requests.remove(0);
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
	}
	
	public synchronized void addRequest(Request request) {
		if (request.isCompleted())
			completedRequests.add(request);
		else {
			requests.add(request);
		}
		notifyAll();
	}
	
	public void addRequests(List<Request> requests) {
		for(Request request : requests) {
			addRequest(request);
		}
	}
	
	
	@Override
	public void run() {
		while(!done) {
			//do something?
		}
		
	}
	public static void main(String[] args) {
		
	}

}
