package elevatorSystems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author Nick Coutts
 * 
 */
public class RequestGroup {
	private ArrayList<Request> requests;
	private ArrayList<Integer> elevFloorLamps;
	private ArrayList<Integer> route;
	private HashMap<Integer,Integer> errorLookup;
	
	/**
	 * Constructor for a request group, meant to be used for similar requests
	 * @param requests the Requests that are to be part of a RequestGroup
	 */
	public RequestGroup(ArrayList<Request> requests) {
		this.requests = requests;
		this.elevFloorLamps = setElevatorFloorLamps();
		this.route = setFloorRoute();
		this.errorLookup = createErrorLookup();
	}
	
	/**
	 * Used to initialize the car button lamps 
	 * @return an ArrayList of integers, the integers represent which car button lamps should be on
	 */
	private ArrayList<Integer> setElevatorFloorLamps() {
		ArrayList<Integer> floors = new ArrayList<Integer>();
		for(Request request : requests) {
			if(!floors.contains(request.getCarButton()))
				floors.add(request.getCarButton());
		}
		return floors;
	}
	
	/**
	 * Used to initialized the floor route for a set of requests
	 * @return an ordered ArrayList of the floors to be visited
	 */
	private ArrayList<Integer> setFloorRoute() {
		ArrayList<Integer> floors = new ArrayList<>();
		for(int i = 0; i < requests.size(); i++) {
			Request request = requests.get(i);
			
			if(!floors.contains(request.getFloor()))
				floors.add(request.getFloor());
			
			if(!floors.contains(request.getCarButton())) //shouldn't add duplicate floors
				floors.add(request.getCarButton());
		}
		bubbleSort(floors, requests.get(0).getFloorButton());
		return floors;
	}
	
	/**
	 * Generates the error lookup map with each used floor from the requests and mapping it with the highest error from that floor.
	 * @return a HashMap mapping the floors to the highest error code for that floor
	 */
	private HashMap<Integer,Integer> createErrorLookup() {
		 HashMap<Integer,Integer> errors = new HashMap<Integer,Integer>();
		 for(int i = 0; i < requests.size(); i++) { //iterate through the requests 
				Request request = requests.get(i);
				
				if(!errors.containsKey(request.getFloor())) {
					errors.put(request.getFloor(), 0); // floor to pick someone up, always no error
				}
				//already present with smaller error code replace with higher error code, or not present
				if((errors.containsKey(request.getCarButton()) && (errors.get(request.getCarButton()) < request.getErrorCode())) || !errors.containsKey(request.getCarButton())) {
					errors.put(request.getCarButton(), request.getErrorCode());
				}
			}
		return errors;
		
	}
	
	/**
	 * Gets the next destination that needs to be visited to fulfill a request
	 * @return an Integer representing the next destination
	 */
	public Integer getNextDestination() {
		if(route.size() > 0) 
			return route.remove(0);
		else {
			return null;
		}
		
	}
	/**
	 * Gets the error code of the given floor
	 * @param the floor you want the error code of
	 * @return an Integer representing the highest error code of that floor, or null if floor is null or floor isnt a floor handled by this group
	 */
	public int getErrorCode(Integer floor) {
		Integer error = errorLookup.get(floor);
		return (error == null ? -1 : error);
	}
	
	/**
	 * Removes each Request in requests from the RequestGroup since the requests are complete
	 * @param requests An ArrayList containing the requests to be removed
	 */
	public void removeRequests(ArrayList<Request> requests) {
		for(Request request : requests) {
			this.requests.remove(request);
		}
	}
	
	/**
	 * Removes a car button lamp from the list of car button lamps that should be on
	 * @param floor the floor of the car button lamp that should not be on
	 */
	public void removeElevatorFloorLamp(Integer floor) {
		elevFloorLamps.remove(floor);
	}
	
	/**
	 * Gets the unfulfilled requests of the request group
	 * @return an ArrayList<Request> containing requests that have not been completed
	 */
	public ArrayList<Request> getRequests(){
		return requests;
	}
	
	/**
	 * Gets the car button lamps that should be on in the elevator
	 * @return an ArrayList of Integers representing which car buttons lamps should be on
	 */
	public ArrayList<Integer> getElevatorFloorLamps() {
		return elevFloorLamps;
	}

	/**
	 * Method to sort the floor route, used bubble sort since the floor route is never going to be too large
	 * @param arr the ArrayList of integers that is to be sorted
	 */
	private void bubbleSort(ArrayList<Integer> arr, Direction dir) {
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
		if(dir == Direction.DOWN) // going down, sort in reverse order
			Collections.reverse(arr);
    }
	
}
