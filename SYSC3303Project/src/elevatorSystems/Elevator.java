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
	private int elevatorLocation;
	private boolean isDoorOpen = false;
	private Direction motor;
	private ArrayList<Integer> floorsRequested;
	private Hashtable<Integer, Boolean> lamp;
	private static boolean done = false;
	
	public Elevator(Scheduler scheduler) {
		this.scheduler = scheduler;	
		this.elevatorLocation = 1;	
		this.lamp = new Hashtable<Integer, Boolean>();
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
	
	public void setFloorsRequested(ArrayList<Integer> floors){
		this.floorsRequested = floors;
	}
	
	public void setElevatorLocation(int location){
		this.elevatorLocation = location;
	}
	
	public void setLamp(Hashtable<Integer, Boolean> lamps){
		this.lamp = lamps;
	}
	
	public void setMotor(Direction motor){
		this.motor = motor;
	}
	
	
	
	public void run() {
		while (!done) {
			
			int floorDestination = scheduler.getFloorDestination();
			this.motor = scheduler.getMotorDirection();
			
			System.out.println(
					Thread.currentThread().getName()
					+ " goes to floor " + floorDestination);
			
			setElevatorLocation(floorDestination);
			
			if (scheduler.getOpenDoors() = true) {
				isDoorOpen = true;
			}
			
			if (scheduler.turnLampOff()) {
				lamp.put(floorDestination, false);
			}
			
			ArrayList<Integer> requestedFloors = scheduler.getReuestedFloors();
			
			for (int floor : requestedFloors) {
				lamp.put(floor, true);
				floorsRequested.add(floor);
			}
			
			if (scheduler.getOpenDoors() = false) {
				isDoorOpen = false;
			}
			
			if(scheduler.getDone()) {
				done = false;
			}
		}
		return;
	}
}
