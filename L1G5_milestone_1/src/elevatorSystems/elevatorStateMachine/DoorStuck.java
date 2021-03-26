package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Elevator;

/**
 * State when to resolve a the doors being stuck error
 * @author Jay McCracken
 *
 */
public class DoorStuck extends ElevatorState {

	private Elevator elevator; 
		
	public DoorStuck(Elevator elevator) {
		this.elevator = elevator;
	}
		
	
	/**
	 * The Doors when stuck need to be restarted, force the thread to wait 5 seconds to simulate
	 * the door restarting
	 */
	public void doorWait() {
		System.out.println("Doors on Elevator " + this.elevator.getId() + " are restarting...");
		try {
			Thread.sleep(5000); //sleep the elevator thread for 5 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
		System.out.println("Elevator " + elevator.getId() + " has been restarted");	
	}
}
