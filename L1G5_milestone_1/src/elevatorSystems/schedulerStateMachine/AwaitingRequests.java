package elevatorSystems.schedulerStateMachine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map.Entry;
import elevatorSystems.Direction;
import elevatorSystems.Request;
import elevatorSystems.Scheduler;

/**
 * @author Ambar Mendez Jimenez 
 * @author Kevin Belanger 101121709
 * @author Nick Coutts
 * @author Matthew Harris
 */
public class AwaitingRequests extends SchedulerState {

	private Scheduler scheduler; 

	/**
	 * Constructor for the class
	 * @param scheduler the scheduler this state belongs to
	 */
	public AwaitingRequests(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * Gets the current list of requests from the floorSubsystem
	 * @param floorSubsystem the Scheduler's floorSubsystem
	 */
	@Override
	public boolean getListOfRequests(DatagramSocket floorSocket) {
		ArrayList<Request> requests = requestList(floorSocket);	
		if(requests == null)
			return false;
		System.out.println("Scheduler received list of requests.");
		for(Request request : requests) {
			this.scheduler.addRequest(request);
		}
		System.out.println("Scheduler transitions from awaiting requests to unsorted requests.");

		return true;
	}
	
	/**
	 * Method waiting for requests of data through the sockets, sends datagram to floor subsystem requesting 
	 * the requests. Waits for list for the floor subsystem
	 * @param floorSocket socket used for transporting UDP
	 * @return the list of requests
	 */
	private ArrayList<Request> requestList(DatagramSocket floorSocket){
		byte[] data = {1};
		DatagramPacket sendPacket;
		try {
			sendPacket = new DatagramPacket(data, data.length, scheduler.getFloorIP(), scheduler.getSchedulerToFloorPort());
			floorSocket.send(sendPacket);
			System.out.println("Packet Sent to Floor subsystem requesting list of requests");
	    }
		catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	    }
		byte response[] = new byte[10000];
	    DatagramPacket receivePacket = new DatagramPacket(response, response.length);

	    try {
	         // Block until a datagram is received via floorSocket.  
	    	floorSocket.receive(receivePacket);
	    	System.out.println("Packet recieved from Floor subsystem with list of requests");
	    } catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    }
	    
	    ArrayList<Request> requestList = new ArrayList<>();		 
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(receivePacket.getData());
			ObjectInputStream oStream = new ObjectInputStream(stream);
			requestList = (ArrayList<Request>) oStream.readObject();
			oStream.close();
			stream.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return requestList;
	}	
	
	/**
	 * Returns null since the scheduler isnt in a state to give an elevator a task
	 */
	@Override
	public Entry<Integer,Direction> requestTask(int id, int destination) {
		return null;
	}
}