package elevatorSystems.elevatorStateMachine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
	public void exit(DatagramSocket sendSocket) {
		System.out.println("Elevator " + elevator.getId() + ": All requests processed Transition to Final state");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutputStream oStream;
		try {
			oStream = new ObjectOutputStream(stream);
			oStream.writeObject(new ElevatorInfo(this.elevator.getId(), this.elevator.getElevatorLocation(),this.elevator.getMotor().toString(),this.elevator.getErrorCode(),this.elevator.getLamp()));
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
	         System.out.println("Packet sent to the scheduler with a notification this elevator has shutdown");
	    }
		catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	    }		
	}
}
