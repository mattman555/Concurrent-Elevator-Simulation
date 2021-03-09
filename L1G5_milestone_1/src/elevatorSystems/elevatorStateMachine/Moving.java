package elevatorSystems.elevatorStateMachine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import elevatorSystems.Direction;
import elevatorSystems.Elevator;
import elevatorSystems.FloorSubsystem;

/**
 * @author Matthew Harris 101073502
 * @author Jay McCracken 101066860
 *
 *	State of the elevator when transitioning between floors
 */
public class Moving extends ElevatorState {

	private Elevator elevator;
	private static final int TIME_BETWEEN_FLOORS = 1000;
	
	public Moving(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * The movement of the elevator to the next floor by 1 based on the direction given
	 * @param direction, If the elevator is going UP or DOWN
	 */
	@Override
	public void activity(Direction direction) {
		elevator.getLogger().println("Elevator " + elevator.getId() + ": Moving " + direction.toString().toLowerCase() + " on floor " + this.elevator.getElevatorLocation());
		try {
			Thread.sleep(TIME_BETWEEN_FLOORS);
		} catch (InterruptedException e) {}
		if(direction == Direction.UP) {
		this.elevator.setElevatorLocation(this.elevator.getElevatorLocation()+1);
		}
		else if(direction == Direction.DOWN) {
			this.elevator.setElevatorLocation(this.elevator.getElevatorLocation()-1);
		}
	}
	
	/**
	 * When the elevator reaches the required floor, turn the floor lamp for destination to off
	 */
	@Override
	public void arrivesAtDestination(DatagramSocket sendReceiveSocket) {
		elevator.getLogger().println("Elevator " + elevator.getId() + ": Transition from Moving to Arrived");
		elevator.getLogger().println("Elevator " + elevator.getId() + ": Arrived at: " + this.elevator.getElevatorLocation());
		DatagramPacket sendPacket = this.elevator.generatePacket(RPCRequestType.SET_LAMPS);
		boolean received = false;
		while(!received){
			try {
		         sendReceiveSocket.send(sendPacket);
		    }
			catch (IOException e) {
		         e.printStackTrace();
		         System.exit(1);
		    }
			received = receivePacket(sendReceiveSocket);
		}
		
	}
	
	private boolean receivePacket(DatagramSocket sendReceiveSocket) {
		byte data[] = new byte[1];
	    DatagramPacket receivePacket = new DatagramPacket(data, data.length);
    	try {
    		// Block until a datagram is received via sendReceiveSocket.  
    		sendReceiveSocket.setSoTimeout(5000);
    		sendReceiveSocket.receive(receivePacket); 
    		if(receivePacket.getLength() == 1 && receivePacket.getData()[0] == 1) 
    			return true; //return true if receive a packet back with correct data
    		return false;
    	} catch(SocketTimeoutException e) {
    		return false;
    	} catch(IOException e) {
    		e.printStackTrace();
    		System.exit(1);
    	} 
    	return false; //never called, needed for structure
	}
}
