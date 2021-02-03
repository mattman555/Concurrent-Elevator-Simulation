package elevatorSystems;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * 
 * 
 * @author Jay McCracken 101066860
 * @version 3.00
 */
public class Elevator implements Runnable{
	
	private Scheduler scheduler;
	private FloorSubsystem floorSystem;
	private int elevatorLocation;
	private boolean isDoorOpen = false;
	private Direction motor;
	private ArrayList<Integer> floorsRequested;
	private Hashtable<Integer, Boolean> lamp = new Hashtable<Integer, Boolean>();
	private static boolean done = false;
	
	public Elevator(Scheduler scheduler, FloorSubsystem floorSyste) {
		this.floorSystem = floorSystem;
		this.scheduler = scheduler;	
		this.elevatorLocation = 1;	
	}
	
	public ArrayList<Integer> getFloorsRequested(){
		return floorsRequested;
	}
	
	public int getElevatorLocation(){
		return elevatorLocation;
	}
	
	public Hashtable<Integer, Boolean> getLamp(){
		return lamp;
	}
	
	public Direction getMotor(){
		return motor;
	}
	
	
	
	public void run() {
		while (!done) {
			
			int floorDestination = scheduler.getFloorDestination();
			
			System.out.println(
					Thread.currentThread().getName()
					+ " goes to floor " + floorDestination);
			
			if (scheduler.turnLampOff()) {
				lamp.put(floorDestination, false);
			}
			
			ArrayList<Integer> requestedFloors = floorSystem.getReuestedFloors();
			
			for (int floor : requestedFloors) {
				lamp.put(floor, true);
				floorsRequested.add(floor);
			}
			
			if(floorSystem.getDone) {
				done = false;
			}
		}
		return;
	}
}
