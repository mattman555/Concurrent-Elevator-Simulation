package elevatorSystems.elevatorStateMachine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 *
 * State for when elevator doors are open after it has arrived at the destination
 */
public class DoorsOpen extends ElevatorState {
	
	private Elevator elevator;
	
	public DoorsOpen(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * Get all the car lamps that need to be turned on 
	 * @return list of all lamps to be turned on
	 */
	@Override
	public ArrayList<Integer> getLamps(DatagramSocket sendReceiveSocket) {
		System.out.println("Elevator " + elevator.getId() + ": Transition from Doors Open to Update Lamps");
		DatagramPacket lampsPacket = this.elevator.generatePacket(RPCRequestType.GET_LAMPS);
		try {
	         sendReceiveSocket.send(lampsPacket);
	         System.out.println("Packet sent to the schduler with request for what state the lamps need to be");
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
	         System.out.println("Packet sent to the schduler with what state the lamps need to be");
	    } catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    }
    
		return this.elevator.readResponse(receivePacket).getLamps();
	}
	
}
