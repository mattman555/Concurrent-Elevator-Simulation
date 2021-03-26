package elevatorSystems.schedulerStateMachine;

/*
* @author Nick Coutts
* @author Matthew Harris
*/

public class End extends SchedulerState {

	/**
	 * Constructor for the End class
	 */
	public End() {}
	
	/**
	 * Method for the system to exit
	 */
	@Override
	public void exit() {
		System.out.println("No more requests, Terminating");
		System.exit(1);
	}
}
