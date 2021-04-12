package elevatorSystems.elevatorStateMachine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import elevatorSystems.Direction;
import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 * @author Jay McCracken 101066860
 * @author Nick Coutts 101072875
 *
 *	State of the elevator when transitioning between floors
 */
public class Moving extends ElevatorState {

	private Elevator elevator;
	private int timeBetweenFloors;
	
	public Moving(Elevator elevator, int timeBetweenFloors) {
		this.elevator = elevator;
		this.timeBetweenFloors = timeBetweenFloors;
	}

	/**
	 * The movement of the elevator to the next floor by 1 based on the direction given
	 * @param direction, If the elevator is going UP or DOWN
	 */
	@Override
	public void activity(Direction direction, DatagramSocket sendSocket) {
		System.out.println("Elevator " + elevator.getId() + ": Moving " + direction.toString().toLowerCase() + " from floor " + this.elevator.getElevatorLocation());
		try {
			Thread.sleep(this.timeBetweenFloors);
		} catch (InterruptedException e) {}
		if(direction == Direction.UP) {
			this.elevator.setElevatorLocation(this.elevator.getElevatorLocation()+1);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ObjectOutputStream oStream;
			try {
				oStream = new ObjectOutputStream(stream);
				oStream.writeObject(new ElevatorInfo(this.elevator.getId(), this.elevator.getElevatorLocation(),direction.toString(),this.elevator.getErrorCode(),this.elevator.getLamp()));
				stream.close();
				oStream.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
			byte[] message = stream.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(message, message.length,this.elevator.getGuiIp(),this.elevator.getGuiPort());
			try {
				sendSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(direction == Direction.DOWN) {
			this.elevator.setElevatorLocation(this.elevator.getElevatorLocation()-1);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ObjectOutputStream oStream;
			try {
				oStream = new ObjectOutputStream(stream);
				oStream.writeObject(new ElevatorInfo(this.elevator.getId(), this.elevator.getElevatorLocation(),direction.toString(),this.elevator.getErrorCode(),this.elevator.getLamp()));
				stream.close();
				oStream.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
			byte[] message = stream.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(message, message.length,this.elevator.getGuiIp(),this.elevator.getGuiPort());
			try {
				sendSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * When the elevator reaches the required floor, turn the floor lamp for destination to off
	 */
	@Override
	public void arrivesAtDestination(DatagramSocket sendReceiveSocket) {
		System.out.println("Elevator " + elevator.getId() + ": Transition from Moving to Arrived");
		System.out.println("Elevator " + elevator.getId() + ": Arrived at: " + this.elevator.getElevatorLocation());
		DatagramPacket sendPacket = this.elevator.generatePacket(RPCRequestType.SET_LAMPS);
		boolean received = false;
		while(!received){
			try {
		         sendReceiveSocket.send(sendPacket);
		         System.out.println("Packet sent to the floor subsystem with a request to update the floors lamps");
		    }
			catch (IOException e) {
		         e.printStackTrace();
		         System.exit(1);
		    }
			received = receivePacket(sendReceiveSocket);
		}
		
	}
	
	/**
	 * Tries to receive a packet from floor subsystem to see if the floor subsystem has updated its lamps
	 * @param sendReceiveSocket Socket used to pass UDP
	 * @return true if it gets to packet that it updated its lamps, false if it does not
	 */
	private boolean receivePacket(DatagramSocket sendReceiveSocket) {
		byte data[] = new byte[1];
	    DatagramPacket receivePacket = new DatagramPacket(data, data.length);
    	try {
    		// Block until a datagram is received via sendReceiveSocket.  
    		sendReceiveSocket.setSoTimeout(5000);
    		sendReceiveSocket.receive(receivePacket); 
    		System.out.println("Packet received from the floor subsystem with acknowlegement to update its floor lamps");
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
