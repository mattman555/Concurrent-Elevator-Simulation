/**
 * 
 */
package elevatorSystems;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Hashtable;

import elevatorSystems.elevatorStateMachine.ElevatorRPCRequest;

/**
 * @author Matthew Harris 101073502
 *
 */
public class FloorSubsystem implements Runnable{

	private ArrayList<Request> requests;
	private final int MAX_FLOORS;
	private Hashtable<String, Boolean> lamp;
	private final String FILENAME = "TestFile.txt";
	DatagramSocket schedulerSocket, elevatorSocket;
	/**
	 * Constructor for the floor subsystem set all the fields
	 */
	public FloorSubsystem(int maxFloors) {
		this.requests = new ArrayList<Request>();
		this.MAX_FLOORS = maxFloors;
		this.lamp = new Hashtable<String, Boolean>();
		try {
			this.schedulerSocket = new DatagramSocket(14001);
			this.elevatorSocket = new DatagramSocket(14002);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * turns floor lamps on and off based on where elevators are going
	 * @param floor the floor to change the lamp of
	 * @param direction the direction on the floor to change the lamp of
	 * @param on whether to turn the lamp on or off
	 */
	public void setFloorLamp(int floor, Direction direction, boolean on) {
		String floorAndDirection = ((Integer)floor).toString() + direction.toString();
		lamp.put(floorAndDirection, on);
	}
	/**
	 *get the lamp hashtable
	 */
	public Hashtable<String, Boolean> getFloorLamp() {
		return lamp;
	}
	
	/**
	 * ensures a request follows formatting rules and is therefore valid
	 * @param request: the request that the method is validating 
	 * @return a boolean true if the request is valid false if invalid
	 */
	private boolean validateRequest(Request request) {
		int[] time = request.getTime();
		//later change these into smaller checking methods
		if((time[0] >= 0 && time[0] <= 24) && (time[1] >= 0 && time[1] <= 59) && (time[2] >= 0 && time[2] <= 59) && (time[3] >= 0 && time[3] <= 999)) { 
			if(request.getFloor() > 0 && request.getFloor()<=MAX_FLOORS) {
				if(request.getFloorButton().equals(Direction.UP) || request.getFloorButton().equals(Direction.DOWN)) {
					if(request.getCarButton() > 0 && request.getCarButton() <= MAX_FLOORS && request.getCarButton() != request.getFloor()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * removes the given request from the request list
	 * @param request the given request to be removed
	 */
	private void removeRequests(ArrayList<Request> completedRequests) {
		if(completedRequests == null)
			return;
		System.out.println(Thread.currentThread().getName() + ": Receives " + completedRequests.size() + " completed Requests from Scheduler" );
		for(Request completedRequest : completedRequests) {
			System.out.println("Floor subsystem Completed: " + completedRequest.toString());
			for(int i = 0; i < this.requests.size(); i++) { //iterate through and remove the completed ones
				if(this.requests.get(i).equals(completedRequest)) {
					this.requests.remove(i);
					break;
				}
			}
		}
	}
	
	/**
	 * reads the in file line by line and generates the request 
	 * that are put in the requests list
	 * @param filename the file to read with extension
	 */
	private void readFile(String filename) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			ArrayList<Request> requests = new ArrayList<>();
			while (line != null) {
				String[] lineArr = line.split(" "); 
				Request request = new Request(lineArr[0], Integer.parseInt(lineArr[1]), lineArr[2], Integer.parseInt(lineArr[3])); 
				if(validateRequest(request)) 
					requests.add(request);
				line = reader.readLine();
			}
			System.out.println("All requests read from file and validated");
			this.requests = requests;
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * returns the list of requests that were read from the file
	 * @return the list of requests that were read from the file
	 */
	public ArrayList<Request> getListOfRequests(){
		if(this.requests.isEmpty())
			return null;
		return this.requests;
  }
  
	/**
	 * adding request to the request list
	 * @param request
	 */
	public void addRequest(Request request) {
		requests.add(request);
	}
	
	/**
	 * respond too a request for the requests from the schduler
	 */
	private void respondWithRequests() {
		byte data[] = new byte[1];
	    DatagramPacket receivePacket = new DatagramPacket(data, data.length);
		try {
	         // Block until a datagram is received via socket.  
			schedulerSocket.receive(receivePacket);
			System.out.println("Packet recieved with a request for the list of requests");
	    } catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    }
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oStream = new ObjectOutputStream(stream);
			oStream.writeObject(requests);
			oStream.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		byte[] sendData = stream.toByteArray();
		try {
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
			schedulerSocket.send(sendPacket);
			System.out.println("Packet sent with the list of requests");
	    }
		catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	    }
	}
	
	/**
	 * get the list of completed requests from the schdulers packet
	 * and makes a list of the requests that are complete
	 * @return the new list of completed requests
	 */
	private ArrayList<Request> processCompletedRequests(){
		byte data[] = new byte[1000];
	    DatagramPacket receivePacket = new DatagramPacket(data, data.length);
		try {
	         // Block until a datagram is received via schedulerSocket.
			schedulerSocket.setSoTimeout(200);
			schedulerSocket.receive(receivePacket);
			System.out.println("Packet recieved with the list of completed requests");
	    } catch(SocketTimeoutException e) {
	    	return null;
		}catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    }
		
		ArrayList<Request> completedRequests = new ArrayList<>();		 
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(receivePacket.getData());
			ObjectInputStream oStream = new ObjectInputStream(stream);
			completedRequests = (ArrayList<Request>) oStream.readObject();
			oStream.close();
			stream.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		if(completedRequests != null) {
			removeRequests(completedRequests);
			completedRequests.clear();
		}
		byte[] sendData = {(byte) this.requests.size()};
		sendAck(sendData, schedulerSocket, receivePacket.getAddress(), receivePacket.getPort());
		System.out.println("Packet sent with acknowledgement of the update of the list of requests");
		return completedRequests;
		
	}
	
	/**
	 * Receives packet from the elevators and updates the lamps accordingly
	 */
	private void changeFloorLamps() {
		byte data[] = new byte[1000];
	    DatagramPacket receivePacket = new DatagramPacket(data, data.length);
		try {
	         // Block until a datagram is received via elevatorSocket.
			elevatorSocket.setSoTimeout(200);
			elevatorSocket.receive(receivePacket);
	    } catch(SocketTimeoutException e) {
	    	return;
		}catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    }
		
		ElevatorRPCRequest request = null;		 
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(receivePacket.getData());
			ObjectInputStream oStream = new ObjectInputStream(stream);
			request = (ElevatorRPCRequest) oStream.readObject();
			oStream.close();
			stream.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("changing lamps");
		setFloorLamp(request.getCurrentLocation(), request.getMotorDirection(), false);
		byte[] sendData = {1};
		sendAck(sendData, elevatorSocket, receivePacket.getAddress(), receivePacket.getPort());
	}
	
	/**
	 * send the acknowledgement packet to a thread that sent a request packet
	 * @param data the contents of the packet
	 * @param socket the socket that it is sent from
	 * @param address the IP address to send the acknowledgement too
	 * @param port the port to send the acknowledgement too
	 */
	private void sendAck(byte[] data, DatagramSocket socket, InetAddress address, int port) {
		try {
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, port);
			socket.send(sendPacket);
	    }
		catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	    }
	}

	/**
	 * the run method that runs when the thread start method is called
	 */
	@Override
	public void run() {
		readFile(FILENAME);
		respondWithRequests();
		while(requests.size()>0) {
			processCompletedRequests();
			changeFloorLamps();
		}
		schedulerSocket.close();
		elevatorSocket.close();
		System.out.println("Floor subsystem: All requests completed.");
	}

	public static void main(String[] args) {
		FloorSubsystem floorSubsystem = new FloorSubsystem(7);
		Thread floorSubsystemThread = new Thread(floorSubsystem,"FloorSubsystem");
		floorSubsystemThread.start();
	}
	
}
