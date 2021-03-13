package elevatorSystems.schedulerStateMachine;

import elevatorSystems.Scheduler;

/*
* @author Nick Coutts
* @author Matthew Harris
*/

public class End extends SchedulerState {
	private Scheduler scheduler;

	/**
	 * Constructor for the End class
	 */
	public End(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	/**
	 * Method for the system to exit
	 */
	@Override
	public void exit() {
		System.out.println("No more requests, Terminating");
		System.exit(1);
	}
}
