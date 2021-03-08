package elevatorSystems;
import java.util.Hashtable;

/**
 * Elevator Thread, keeps track of current location, grabs motor direction
 * and floor destination from scheduler.
 * Keeps track on in car button lamps, turns on and off requested lamps.
 * 
 * @author Jay McCracken 101066860 and Matthew Harris 101073502
 * @version 4.00
 */
public class Elevator{
	
	/*
	 * Variables elevator needs to store
	 */
	public Scheduler scheduler;
	private int elevatorLocation;
	private int floorDestination;
	private boolean isDoorOpen;
	private Direction motor;
	private Hashtable<Integer, Boolean> lamp;
	private Logger logger;
	private int id;
	
	/**
	 * Constructor, creating a base elevator starting
	 * on floor 1 with no lamps turned on and door closed
	 * connected to a scheduler object
	 */
	public Elevator(Scheduler scheduler, Logger logger, int elevId) {
		this.scheduler = scheduler;	
		this.elevatorLocation = 1;	
		this.floorDestination = 1;
		this.isDoorOpen = false;
		this.lamp = new Hashtable<Integer, Boolean>();
		this.logger = logger;
		this.id = elevId;
	}

	public Logger getLogger() {
		return this.logger;
	}
	
	public int getId() {
		return this.id;
	}
	public int getFloorDestination() {
		return floorDestination;
	}

	public void setFloorDestination(int floorDestination) {
		this.floorDestination = floorDestination;
	}

	public boolean isDoorOpen() {
		return isDoorOpen;
	}
	
	public Hashtable<Integer, Boolean> getLamp() {
		return lamp;
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
	
	public void setMotor(Direction motor){ //used to talk to floor subsystem
		this.motor = motor;
	}
	/**
	 * Function to toggle if the doors are open
	 * if open, close
	 * if closed, open
	 */
	public void toggleDoors() {
		this.isDoorOpen = !isDoorOpen;
		logger.println("Elevator " + this.getId()+" door is "+ (this.isDoorOpen ? "open" : "closed"));
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
}
