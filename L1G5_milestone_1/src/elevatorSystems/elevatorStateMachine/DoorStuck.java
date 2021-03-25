package elevatorSystems.elevatorStateMachine;

import elevatorSystems.Elevator;

public class DoorStuck extends ElevatorState {

	private Elevator elevator; 
		
	public DoorStuck(Elevator elevator) {
		this.elevator = elevator;
	}
		
	
	public void doorWait() {
		System.out.println("Doors on Elevator " + this.elevator.getId() + " are restarting...");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
		System.out.println("Elevator " + elevator.getId() + " has been restarted");	
	}
}
