package elevatorSystems.elevatorStateMachine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Set;

import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 * @author Jay McCracken 101066860
 *
 *	Turning on and off car lamps
 */
public class UpdateLamps extends ElevatorState {

	private Elevator elevator;
	
	public UpdateLamps(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * Turns on all requested lamps, turning off the lamp of the elevators location
	 * @param The list of lamps that there are
	 */
	@Override
	public void action(ArrayList<Integer> lamps) {
		System.out.println("Elevator " + elevator.getId() + ": Updating lamps");
		int location = this.elevator.getElevatorLocation();
		for(Integer i : lamps) {
			this.elevator.getLamp().put(i, true);
		}
		this.elevator.getLamp().put(location, false);
		this.elevator.getLamp().remove(location);

		System.out.println("Elevator " + elevator.getId() + ": Car Lamps: " + this.elevator.getLamp());
	}
	
	/**
	 * Toggling the doors of the elevator to open or closed based on what it last was
	 * @param next the state of the door that was switched too
	 */
	@Override
	public void toggleDoors(DatagramSocket sendReceiveSocket) {
		System.out.println("Elevator " + elevator.getId() + ": Transition from Update Lamps to Doors Closed");
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
	         sendReceiveSocket.receive(receivePacket);
	         System.out.println("Packet recieved from the scheduler with the response to request to open the doors");
	    } catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    }
		boolean isDoorOpen = this.elevator.readResponse(receivePacket).getIsDoorOpen();
		this.elevator.setIsDoorOpen(isDoorOpen);
		
	}
	
}
