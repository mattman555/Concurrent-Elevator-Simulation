package elevatorSystems.elevatorStateMachine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.Map.Entry;

import elevatorSystems.Direction;
import elevatorSystems.Elevator;

/**
 * @author Matthew Harris 101073502
 * @author Jay McCracken 101066860
 * 
 *	State when the elevator doors are closed, from statanory or moving state
 */
public class DoorsClosed extends ElevatorState {

	private Elevator elevator; 
	
	public DoorsClosed(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * When received a valid request set the conditions of the elevation to get to the request
	 * @param The elevators next destination
	 */
	@Override
	public void validRequest(Entry<Integer,Direction> destination) {
		System.out.println("Elevator " + elevator.getId() +": Transition from Doors Closed to Moving Up");
		this.elevator.setFloorDestination(destination.getKey()); //set the floor number to go to
		this.elevator.setMotor(destination.getValue());			 //the direction of the motor to get to that floor
	}
	
	/**
	 * When the elevator reaches the required floor, send a packet 
	 * to the floor subsystem to turn the floor lamp for destination off
	 */
	@Override
	public void arrivesAtDestination(DatagramSocket sendReceiveSocket) {
		System.out.println("Elevator " + elevator.getId() + ": Transition from Doors Closed to Arrived");
		System.out.println("Elevator " + elevator.getId() + ": Arrived at: " + this.elevator.getElevatorLocation());
		
		DatagramPacket sendPacket = this.elevator.generatePacket(RPCRequestType.SET_LAMPS);
		boolean received = false;
		while(!received){
			try {
		         sendReceiveSocket.send(sendPacket);
		         System.out.println("Packet sent to the scheduler with a request to open the doors");
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
    		sendReceiveSocket.setSoTimeout(500);
    		sendReceiveSocket.receive(receivePacket); 
    		System.out.println("Packet recieved from the scheduler with the response the request to open the doors");
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
	
	/**
	 * If the request set is invalid send that it is going to end state
	 */
	@Override
	public void invalidRequest() {
		System.out.println("Elevator " + elevator.getId() + ": Transition from Doors Closed to End");
	}
	
	/**
	 * Error class type 2 occurs, end the elevator
	 */
	@Override
	public void shutdown(DatagramSocket sendSocket) {
		System.out.println("Elevator " + elevator.getId() + ": is shutting down");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutputStream oStream;
		try {
			oStream = new ObjectOutputStream(stream);
			oStream.writeObject(new ElevatorInfo(this.elevator.getId(), this.elevator.getElevatorLocation(),this.elevator.getMotor().toString(),this.elevator.getErrorCode()));
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
