package elevatorSystems;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

import elevatorSystems.elevatorStateMachine.ElevatorRPCRequest;
import elevatorSystems.schedulerStateMachine.*;

public class Scheduler implements Runnable {
	private SchedulerState[] states;
	private int current;
	private int[][] transitions = {{0,1}, {1,2}, {3}, {3,4}};
	private ArrayList<Request> requests;
	private ArrayList<RequestGroup> requestBuckets;
	private Hashtable<Integer, RequestGroup> inProgressBuckets;
	private ArrayList<Request> completedRequests;
	public static final int FLOOR_SUB_PORT = 14001;
	private DatagramSocket elevatorSocket, floorSocket;
	
	/**
	 * Constructor for the scheduler class
	 */
	public Scheduler() {
		SchedulerState[] statearr =
			{new AwaitingRequests(this), 
			 new UnsortedRequests(this), 
			 new SortedRequests(this), 
			 new InProgress(this),
			 new End(this)};
		this.states = statearr;
		this.current = 0;
		this.requests = new ArrayList<Request>();
		this.requestBuckets = new ArrayList<>();
		this.completedRequests = new ArrayList<>();
		this.inProgressBuckets = new Hashtable<>();
		try {
			elevatorSocket = new DatagramSocket(14000);
			floorSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Gets the requests
	 * @return an ArrayList of Requests that is the requests
	 */
	public ArrayList<Request> getRequests() {
		return requests;
	}

	/**
	 * Gets the requestBuckets
	 * @return an ArrayList of RequestGroups that is the requestBuckets
	 */
	public ArrayList<RequestGroup> getRequestBuckets() {
		return requestBuckets;
	}

	/**
	 * Gets the inProgressBucket
	 * @return a RequestGroup that is the inProgressBucket
	 */
	public RequestGroup getInProgressBucket(int id) {
		return inProgressBuckets.get(id);
	}
	
	/**
	 * Sets the in progress bucket to another RequestGroup
	 * @param inProgressBucket the new in progress bucket 
	 */
	public void setInProgressBucket(int id, RequestGroup inProgressBucket) {
		this.inProgressBuckets.put(id, inProgressBucket);
	}

	/**
	 * Gets the completed requests
	 * @return an arraylist containing the completed requests
	 */
	public ArrayList<Request> getCompletedRequests() {
		return completedRequests;
	}

	/**
	 * Moving the scheduler to the next state 
	 * @param nextState the state it will switch to
	 */
	private void nextState(int nextState) {
		 current = transitions[current][nextState];
   }
	
	/**
	 * Gets the current state to return an elevator task.
	 *         
	 * @param currLocation the elevators current location
	 * @return a null request if no requests are currently available,
	 *         a request to floor 10000 if the elevators can stop running
	 *         or returns a valid destination.
	 */
	public void requestTask(ElevatorRPCRequest request, InetAddress address, int port) {	
		int curr = current;
		Entry<Integer,Direction> destination = states[curr].requestTask(request.getId(), request.getCurrentLocation());
		request.setDestination(destination); //modify the request
		sendRPCRequest(request, address, port); //send the modified request back
		
		nextState(0);
	}
	
	/**
	 * Gets the current state to ask the floorsubsystem for the request list
	 */
	public void getListOfRequests() {
		boolean gotRequests = states[current].getListOfRequests(floorSocket);
		nextState( gotRequests ? 1 : 0); //if scheduler got the requests we go to a new state.
	}
	
	/**
	 * Asks the current state to Sort the requests
	 */
	public void sortRequests() {
		states[current].sortRequests();
		nextState(1);
	}

	/**
	 * Called to signal the program that it can stop
	 */
	public void exit() {
		nextState(1);
		states[current].exit();
	}
	
	/**
	 * Adds a request to the requests list
	 * @param request the request to be added
	 */
	public  void addRequest(Request request) {
		this.requests.add(request);
		System.out.println("Scheduler: Gets Request for floor " + request.getFloor() + " from floor subsystem");
	}
	
	/**
	 * Gets a completed from the list of completed requests
	 * @return a Request that was completed by an elevator
	 */
	public synchronized Request getCompletedRequest() {
		if(completedRequests.isEmpty()) { //floorSubsystem wait until there are completed requests
			return null;
		}
		System.out.println("Scheduler: Sends completed request to " + Thread.currentThread().getName() );
		return completedRequests.remove(0);
	}
	
	/**
	 * Gets the car button lamps that are to be on in the elevator
	 * @return an ArrayList of integers of the car button lamps that are supposed to be on
	 */
	public void getRequestedLamps(ElevatorRPCRequest request, InetAddress address, int port){
		int id = request.getId();
		System.out.println("Scheduler: Sends Elevator " + id + " requested car lamps");
		request.setLamps(inProgressBuckets.get(id).getElevatorFloorLamps());
		sendRPCRequest(request, address, port);
	}
	
	/**
	 * The elevator requests to have its doors toggled and the scheduler will toggle them
	 */
	public void toggleDoors(ElevatorRPCRequest request, InetAddress address, int port) {
	    request.setDoor(!request.getIsDoorOpen()); //change the doors state
	    sendRPCRequest(request, address, port);
	    System.out.println("Scheduler: Toggling Elevator " + request.getId() + " Doors");
	    
	   
	}
	
	/**
	 * Receives and processes the different type of request packet
	 * the scheduler recieves
	 */
	private void receiveRequest() {
		byte data[] = new byte[1000];
	    DatagramPacket receivePacket = new DatagramPacket(data, data.length);
		try {
	         // Block until a datagram is received via elevatorSocket.  
			System.out.println("waiting for a new request packet");
			elevatorSocket.receive(receivePacket);
			System.out.println("Packet recieved with a request for a new destination");
	    } catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    }
		//construct ElevatorRPCRequest
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
		
		System.out.println(request.getRequestType());
		switch(request.getRequestType()) {
			case GET_LAMPS:
				getRequestedLamps(request, receivePacket.getAddress(),receivePacket.getPort());
				break;
			case GET_REQUEST:
				requestTask(request, receivePacket.getAddress(),receivePacket.getPort());
				sendCompletedRequests();
				break;
			case TOGGLE_DOORS:
				toggleDoors(request, receivePacket.getAddress(),receivePacket.getPort());
				break;
			default:
				break;
		}
	}
	
	/**
	 * sends a packet with the list of completed 
	 * requests to the floor subsystems
	 */
	private void sendCompletedRequests() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oStream = new ObjectOutputStream(stream);
			oStream.writeObject(completedRequests);
			oStream.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		byte[] sendData = stream.toByteArray();
		try {
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getLocalHost(), Scheduler.FLOOR_SUB_PORT);
			floorSocket.send(sendPacket);
			System.out.println("Packet sent to floor subsystem with the list of completed requests");
	    }
		catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	    }
		
		byte data[] = new byte[1];
	    DatagramPacket receivePacket = new DatagramPacket(data, data.length);

	    try {
	         // Block until an acknowledgment is received via floorSocket.  
	    	floorSocket.receive(receivePacket);
	    	System.out.println("Packet recieved from floor subsystem with acknowledgement of the reciept of the list");
	    } catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    }
	    int remaining = receivePacket.getData()[0];
	    if(remaining == 0) {
	    	floorSocket.close();
	    	elevatorSocket.close();
	    	System.out.println("Completed all requests");
	    	System.exit(2);
	    }
	    completedRequests.clear(); //remove those completed requests		
		
	}

	private void sendRPCRequest(ElevatorRPCRequest request, InetAddress address, int port) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oStream = new ObjectOutputStream(stream);
			oStream.writeObject(request);
			oStream.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] response = stream.toByteArray();
		DatagramPacket sendPacket = new DatagramPacket(response, response.length, address, port);
		try {
			elevatorSocket.send(sendPacket);
			System.out.println("Packet sent to elevator with its new destination");
	    }
		catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	    }
	}
	
	
	@Override
	/**
	 * The running of the elevator, travel to new floor, updating lamps
	 */
	public void run() {		
		
		while (true) {
			switch(current) {
				case 0:
					//Scheduler waiting for requests
					this.getListOfRequests();
					break;
				case 1:
					//Unsorted requests
					System.out.println("Scheduler: Sorting requests");
					this.sortRequests();
					break;
				case 2:
					//Sorted requests
					receiveRequest();
					break;
				case 3:
					//In Progress State
					receiveRequest();
					break;
			}
		}
	}
	
	/**
	 * Initiates the threads and starts them
	 * @param args
	 */
	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		Thread schedulerThread = new Thread(scheduler,"Scheduler");
		schedulerThread.start();	
	}
	
}
