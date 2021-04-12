package elevatorSystems.elevatorStateMachine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import elevatorSystems.Elevator;

/**
 * State when to resolve a the doors being stuck error
 * @author Jay McCracken
 *
 */
public class DoorStuck extends ElevatorState {

	private Elevator elevator; 
		
	public DoorStuck(Elevator elevator) {
		this.elevator = elevator;
	}
		
	
	/**
	 * The Doors when stuck need to be restarted, force the thread to wait 5 seconds to simulate
	 * the door restarting
	 */
	@Override
	public void doorWait(DatagramSocket sendSocket) {
		System.out.println("Doors on Elevator " + this.elevator.getId() + " are restarting...");
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
		try {
			Thread.sleep(5000); //sleep the elevator thread for 5 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
		System.out.println("Elevator " + elevator.getId() + " has been restarted");	
	}
}
