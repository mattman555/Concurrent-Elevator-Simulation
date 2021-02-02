package elevatorSystems;
import java.util.ArrayList;

/**
 * 
 * 
 * @author Jay McCracken 101066860
 * @version 2.00
 */
public class Elevator implements Runnable{
	
	private Scheduler scheduler;
	private int elevatorLocation;
	private boolean isDoorOpen = false;
	private ArrayList<Request> floorsSelected;
	private static final int TIME_BETWEEN_FLOORS = 0;
	private boolean done = false;
	
	public Elevator(Scheduler scheduler) {
		this.scheduler = scheduler;		
	}
	
	public void createRequest(Request elevatorCall){
		Request buttonsPushed = new Request(null, elevatorCall.getCarButton(), null, -1);	
		floorsSelected.add(buttonsPushed);	
	}
	
	public ArrayList<Request> getRequest(){
		return floorsSelected;	
	}
	
	public void run() {
		 while(!done) {
			 
			 Request elevatorCall = scheduler.getRequest();
			 
			 if (elevatorCall.getCarButton() == (-1)) {
				 this.isDoorOpen = true;
				 System.out.println(
							Thread.currentThread().getName()
							+ " goes to floor " + elevatorCall.getFloor() + " to drop off a person");
				 this.isDoorOpen = false;
			 } else {
				 this.isDoorOpen = true;
				 System.out.println(
							Thread.currentThread().getName()
							+ " goes to floor " + elevatorCall.getFloor() + " to pick up a person");
				 this.isDoorOpen = false;
				 createRequest(elevatorCall);
			 }
			 
			 /*
			  *  sleep to add pause to allow everything to register
			  */
			 try {
				 Thread.sleep(1000); 
				 } catch (InterruptedException e) { }
			 
			 
		 }
	 }
}
