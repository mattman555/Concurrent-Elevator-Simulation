/**
 * 
 */
package elevatorSystems;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Matthew Harris 101073502
 *
 */
public class FloorSubsystem implements Runnable{

	private Scheduler scheduler;
	private List<Request> requests;
	private final int MAX_FLOORS;
	private Hashtable<String, Boolean> lamp;
	private final String fileName = "TestFile.txt";
	/**
	 * Constructor for the floor subsystem set all the fields
	 */
	public FloorSubsystem(Scheduler scheduler, int maxFloors) {
		this.scheduler = scheduler;
		this.MAX_FLOORS = maxFloors;
		this.requests = new ArrayList<Request>();
		this.lamp = new Hashtable<String, Boolean>();
	}
	
	/**
	 * turns floor lamps on and off based on where elevators are going
	 * @param floor the floor to change the lamp of
	 * @param direction the direction on the floor to change the lamp of
	 * @param on whether to turn the lamp on or off
	 */
	public void setFloorLamp(int floor, Direction direction, boolean on) {
		String floorAndDirection = ((Integer)floor).toString() + direction.toString();
		lamp.put(floorAndDirection, on);
	}
	/**
	 *get the lamp hashtable
	 */
	public Hashtable<String, Boolean> getFloorLamp() {
		return lamp;
	}
	
	/**
	 * ensures a request follows formatting rules and is therefore valid
	 * @param request: the request that the method is validating 
	 * @return a boolean true if the request is valid false if invalid
	 */
	private boolean validateRequest(Request request) {
		int[] time = request.getTime();
		if((time[0]>=0 && time[0]<=24)&&(time[1]>=0 && time[1]<=59) && (time[2]>=0 && time[2]<=59) && (time[3]>=0 && time[3]<=999)) {
			if(request.getFloor()>0 && request.getFloor()<=MAX_FLOORS) {
				if(request.getFloorButtons().equals(Direction.UP) || request.getFloorButtons().equals(Direction.DOWN)) {
					if(request.getCarButton()>0 && request.getCarButton()<=MAX_FLOORS && request.getCarButton() == request.getFloor()) {
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * removes the given request from the request list
	 * @param request the given request to be removed
	 */
	private void removeRequest(Request request) {
		if(requests.contains(request)) {
			requests.remove(request);
		}
	}
	
	/**
	 * reads the in file line by line and generates the request 
	 * that are put in the requests list
	 * @param filename the file to read with extension
	 */
	private void readFile(String filename) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			while (line != null) {
				String[] lineArr = line.split(" "); 
				Request request = new Request(lineArr[0], Integer.parseInt(lineArr[1]), lineArr[2], Integer.parseInt(lineArr[3])); 
				if(validateRequest(request)) requests.add(request);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * the run method that runs when the thread start method is called
	 */
	@Override
	public void run() {
		readFile(fileName);
		scheduler.addRequests(requests);
		while(requests.size()>0) {
			removeRequest(scheduler.getCompletedRequest());
		}
		scheduler.setDone();
		
	}
	

}