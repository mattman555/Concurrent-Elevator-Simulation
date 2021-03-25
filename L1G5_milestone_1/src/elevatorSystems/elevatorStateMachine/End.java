package elevatorSystems.elevatorStateMachine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 *
 * All requests have been achieved ending state machine
 */
public class End extends ElevatorState {
	
	private Elevator elevator;
	
	public End(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * saying that it is in its final state
	 */
	@Override
	public void exit() {
		System.out.println("Elevator " + elevator.getId() + ": All requests processed Transition to Final state");
	}
	
	/**
	 * saying that it is in its final state
	 */
	@Override
	public void errorExit(DatagramSocket sendReceiveSocket) {
		sendShutdown(sendReceiveSocket);
		System.out.println("Elevator " + elevator.getId() + ": Has Shutdown, cannot be used");
	}
	
	/**
	 * When the elevator receives a fatal error code 2, send a packet 
	 * to the scheduler to notify that the elevator is out of service
	 */
	public void sendShutdown(DatagramSocket sendReceiveSocket) {		
		DatagramPacket sendPacket = this.elevator.generatePacket(RPCRequestType.ELEVATOR_SHUTDOWN);
		try {
	         sendReceiveSocket.send(sendPacket);
	         System.out.println("Packet sent to the schduler with a notification this elevator has shutdown");
	    }
		catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	    }		
	}
}
