package elevatorSystems;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

/**
 * Elevator Thread, keeps track of current location, grabs motor direction
 * and floor destination from scheduler.
 * Keeps track on in car button lamps, turns on and off requested lamps.
 * 
 * @author Jay McCracken 101066860
 * @version 3.00
 */
public class Elevator implements Runnable{
	
	/*
	 * Variables elevator needs to store
	 */
	private Scheduler scheduler;
	private int elevatorLocation;
	private int floorDestination;
	private boolean isDoorOpen;
	private Direction motor;
	private Hashtable<Integer, Boolean> lamp;
	
	/**
	 * Constructor, creating a base elevator starting
	 * on floor 1 with no lamps turned on and door closed
	 * connected to a scheduler object
	 */
	public Elevator(Scheduler scheduler) {
		this.scheduler = scheduler;	
		this.elevatorLocation = 1;	
		this.floorDestination = 1;
		this.isDoorOpen = false;
		this.lamp = new Hashtable<Integer, Boolean>();
	}
	
	/**
	 * where the elevator currently is
	 * @return	the floor number that the elevator is on
	 */
	public int getElevatorLocation(){
		return elevatorLocation;
	}
	
	/**
	 * Which why the elevator needs to more
	 * @return the Direction the motor in the elevator is running
	 */
	public Direction getMotor(){ //used to talk to floor subsystem
		return motor;
	}
	
	/**
	 * Function to toggle if the doors are open
	 * if open, close
	 * if closed, open
	 */
	public void toggleDoors() {
		this.isDoorOpen = !isDoorOpen;
		System.out.println("Elevator door is "+ (this.isDoorOpen ? "open" : "closed"));
	}
	
	/**
	 * Check if the doors are currently open or not
	 * @return a boolean value if doors open
	 */
	public boolean getIsDoorsOpen() {
		return this.isDoorOpen;
	}
	
	/**
	 * updating the elevators current location
	 * @param location is the floor that the elevator just stopped on
	 */
	public void setElevatorLocation(int location){
		this.elevatorLocation = location;
	}
	
	
	/**
	 * The running of the elevator, travel to new floor, updating lamps
	 */
	public void run() {
		/*
		 * until thread is told there is no more requests
		 */
		while (true) {
			//Get the request of the next floor with the motor direction from the scheduler
			Entry<Integer,Direction> destination = scheduler.getRequest(elevatorLocation);
			//check if is no more requests
			if(destination == null)
				return;
			
			floorDestination = destination.getKey(); 	//the floor number to go to
			this.motor = destination.getValue();		//the direction of the motor to get to that floor
			
			//Flavor text of the elevator moving to new floor
			System.out.println(
					Thread.currentThread().getName()
					+ " goes to floor " + floorDestination);
			
			
			scheduler.requestDoorChange();				//Ask scheduler if it can open doors
			
			setElevatorLocation(floorDestination);		
			
			//set car button lamps to on
			ArrayList<Integer> lamps = scheduler.getRequestedLamps();
			for(Integer key : lamp.keySet()) {
				lamp.put(key, false);
			}
			
			for(Integer i : lamps) {
				lamp.put(i, true);
			}

			
			scheduler.requestDoorChange();				//Ask scheduler if it can close doors
		}
	}
}
