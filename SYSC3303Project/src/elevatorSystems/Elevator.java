package elevatorSystems;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * 
 * @author Jay McCracken 101066860
 * @version 3.00
 */
public class Elevator implements Runnable{
	
	private Scheduler scheduler;
	private int elevatorLocation;
	private int floorDestination;
	private boolean isDoorOpen;
	private Direction motor;
	private Hashtable<Integer, Boolean> lamp;
	private static boolean done = false;
	
	public Elevator(Scheduler scheduler) {
		this.scheduler = scheduler;	
		this.elevatorLocation = 1;	
		this.floorDestination = 1;
		this.isDoorOpen = false;
		this.lamp = new Hashtable<Integer, Boolean>();
	}
	
	public int getElevatorLocation(){
		return elevatorLocation;
	}
	
	public Direction getMotor(){ //used to talk to floor subsystem
		return motor;
	}
	
	
	public void toggleDoors() {
		this.isDoorOpen = !isDoorOpen;
	}
	
	
	public void setElevatorLocation(int location){
		this.elevatorLocation = location;
	}
	
	
	public void run() {
		while (!done) {
			Entry<Integer,Direction> destination = scheduler.getRequest(elevatorLocation);
			//check if done
			if(destination == null)
				return;
			
			floorDestination = destination.getKey();
			this.motor = destination.getValue();
			
			System.out.println(
					Thread.currentThread().getName()
					+ " goes to floor " + floorDestination);
			
			scheduler.requestDoorChange();
			
			setElevatorLocation(floorDestination);
			
			//set car button lamps to on
			ArrayList<Integer> lamps = scheduler.getRequestedLamps();
			for(Integer key : lamp.keySet()) {
				lamp.put(key, false);
			}
			
			for(Integer i : lamps) {
				lamp.put(i, true);
			}

			
			scheduler.requestDoorChange();
		}
	}
}
