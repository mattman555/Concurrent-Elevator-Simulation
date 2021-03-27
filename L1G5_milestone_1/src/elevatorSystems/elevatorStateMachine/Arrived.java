package elevatorSystems.elevatorStateMachine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 *
 * State when elevator arrives at required floor 
 */
public class Arrived extends ElevatorState {
	
	private Elevator elevator;
	
	public Arrived(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * Change the state of the elevator doors by sending a packet to the
	 * Scheduler to ask
	 */
	@Override
	public void toggleDoors(DatagramSocket sendReceiveSocket) {
		DatagramPacket togglePacket = this.elevator.generatePacket(RPCRequestType.TOGGLE_DOORS);
		try {
	         sendReceiveSocket.send(togglePacket);
	         System.out.println("Packet sent to the scheduler with a request to open the doors");
	    }
		catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	    }
		
		byte data[] = new byte[1000];
	    DatagramPacket receivePacket = new DatagramPacket(data, data.length);

	    try {
	         // Block until a datagram is received via sendReceiveSocket.
	    	sendReceiveSocket.setSoTimeout(0);
	         sendReceiveSocket.receive(receivePacket);
	         System.out.println("Packet recieved from the scheduler with the response to request to open the doors");
	    } catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    }
		boolean isDoorOpen = this.elevator.readResponse(receivePacket).getIsDoorOpen();
		this.elevator.setIsDoorOpen(isDoorOpen);
		System.out.println("Elevator " + elevator.getId() +": Transition from Arrived to Doors Open");
	}
	
	@Override
	public void doorStuckError() {
		System.out.println("Doors on Elevator " + this.elevator.getId() + " Stuck");
	}
	
		
	
}