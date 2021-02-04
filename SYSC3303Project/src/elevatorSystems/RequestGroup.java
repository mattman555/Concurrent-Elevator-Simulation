package elevatorSystems;

import java.util.ArrayList;

/**
 * @author Nick Coutts
 * 
 */
public class RequestGroup {
	private ArrayList<Request> requests;
	private ArrayList<Integer> elevFloorLamps;
	private ArrayList<Integer> route;
	
	public RequestGroup(ArrayList<Request> requests) {
		this.requests = requests;
		elevFloorLamps = setFloorLamps();
		route = setFloorRoute();
	}
	
	private ArrayList<Integer> setFloorLamps() {
		ArrayList<Integer> floors = new ArrayList<Integer>();
		for(Request request : requests) {
			if(!floors.contains(request.getCarButton()))
				floors.add(request.getCarButton());
		}
		return floors;
	}
	
	public Integer getNextDestination() {
		if(route.size() > 0) 
			return route.remove(0);
		else {
			return null;
		}
		
	}
	
	public void removeFloorLamp(Integer floor) {
		elevFloorLamps.remove(floor);
	}
	
	public ArrayList<Request> getRequests(){
		return requests;
	}
	public ArrayList<Integer> getFloorLamps() {
		return elevFloorLamps;
	}

	private ArrayList<Integer> setFloorRoute() {
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
	
}
