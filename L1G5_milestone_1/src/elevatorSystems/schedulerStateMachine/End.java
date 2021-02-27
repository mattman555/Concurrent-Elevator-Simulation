package elevatorSystems.schedulerStateMachine;

/*
* @author Nick Coutts
* @author Matthew Harris
*/

public class End extends SchedulerState {

	@Override
	public void exit() {
		System.out.println("No more requests, Terminating");
		System.exit(1);
	}
}
