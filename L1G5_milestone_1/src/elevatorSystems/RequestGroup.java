package elevatorSystems;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Nick Coutts
 * 
 */
public class RequestGroup {
	private ArrayList<Request> requests;
	private ArrayList<Integer> elevFloorLamps;
	private ArrayList<Integer> route;
	
	/**
	 * Constructor for a request group, meant to be used for similar requests
	 * @param requests the Requests that are to be part of a RequestGroup
	 */
	public RequestGroup(ArrayList<Request> requests) {
		this.requests = requests;
		elevFloorLamps = setElevatorFloorLamps();
		route = setFloorRoute();
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