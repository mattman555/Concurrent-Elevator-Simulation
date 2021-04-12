package elevatorSystems;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * 
 * @author Nick Coutts
 */

public class ConfigReader {
	private String inputFile;
	private int numElevators;
	private int numFloors;
	private int elevToSchedulerPort;
	private int schedulerToFloorPort;
	private int elevToFloorPort;
	private int guiPort;
	private int timeBetweenFloors;
	private int timeToUnloadPassengers;
	private InetAddress schedulerIp;
	private InetAddress floorIp;
	private InetAddress guiIp;
	private static final String DEFAULT_INPUT_FILENAME = "TestFile.txt";
	private static final int DEFAULT_NUM_ELEVATORS = 4;
	private static final int DEFAULT_NUM_FLOORS = 22;
	private static final int DEFAULT_ELEV_TO_SCHEDULER_PORT = 14000;
	private static final int DEFAULT_SCHEDULER_TO_FLOOR_PORT = 14001;
	private static final int DEFAULT_ELEV_TO_FLOOR_PORT = 14002;
	private static final int DEFAULT_GUI_PORT = 14003;
	private static final int DEFAULT_TIME_BETWEEN_FLOORS = 1000;
	private static final int DEFAULT_TIME_TO_UNLOAD_PASSENGERS = 1000;
	
	/**
	 * Initializes a new ConfigReader object that reads the config file line by line
	 * and sets the instance variables to the values from the config file or to default values if not found
	 * @param configFilename the filename of the config file
	 */
	public ConfigReader(String configFilename) {
		setDefaultValues();// setting default values in case they aren't present
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(configFilename));
			String line = reader.readLine();
			while (line != null) {
				String[] lineArr = line.split(" "); 
				if(lineArr.length != 2) //incorrect number of arguments
					continue;
				String config = lineArr[0]; 
				switch(config) {
					case "InputFile":
						this.inputFile = lineArr[1];
						break;
					case "Num_elevators":
						this.numElevators = Integer.parseInt(lineArr[1]);
						break;
					case "Num_Floors":
						this.numFloors = Integer.parseInt(lineArr[1]);
						break;				
					case "ElevToSchedulerPort":
						this.elevToSchedulerPort = Integer.parseInt(lineArr[1]);
						break;
					case "SchedulerToFloorPort":
						this.schedulerToFloorPort = Integer.parseInt(lineArr[1]);
						break;
					case "ElevToFloorPort":
						this.elevToFloorPort = Integer.parseInt(lineArr[1]);
						break;
					case "GUIPort":
						this.guiPort = Integer.parseInt(lineArr[1]);
						break;
					case "SchedulerIP":
						if(!lineArr[1].equals("localhost")) //If it is localhost the used IP is InetAddress.getLocalHost()
							schedulerIp = InetAddress.getByName(lineArr[1]);
						break;
					case "FloorIP":
						if(!lineArr[1].equals("localhost"))
							this.floorIp = InetAddress.getByName(lineArr[1]);
						break;
					case "guiIP":
						if(!lineArr[1].equals("localhost"))
							this.guiIp = InetAddress.getByName(lineArr[1]);
						break;
					case "timeBetweenFloors":
						this.timeBetweenFloors = Integer.parseInt(lineArr[1]);
						break;
					case "timeToUnloadPassengers":
						this.timeToUnloadPassengers = Integer.parseInt(lineArr[1]);;
						break;
					default:
						break;
				}
				line = reader.readLine();
			}
			reader.close();
			System.out.println("All configurations read from file");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Gets the input file
	 * @return the input file
	 */
	public String getInputFile() {
		return inputFile;
	}

	/**
	 * Gets the number of elevators specified in the config file, or a default value
	 * @return the number of elevators
	 */
	public int getNumElevators() {
		return numElevators;
	}


	/**
	 * Gets the number of floors specified in the config file, or a default value
	 * @return the number of floors
	 */
	public int getNumFloors() {
		return numFloors;
	}

	/**
	 * Gets the port for transferring data from elevator to scheduler from the config file, or a default value
	 * @return the port for transferring data from elevator to schedule
	 */
	public int getElevToSchedulerPort() {
		return elevToSchedulerPort;
	}

	/**
	 * Gets the port for transferring data from elevator to scheduler from the config file, or a default value
	 * @return the port for transferring data from elevator to schedule
	 */
	public int getSchedulerToFloorPort() {
		return schedulerToFloorPort;
	}

	/**
	 * Gets the port for transferring data from elevator to floor subsystem from the config file, or a default value
	 * @return the port for transferring data from elevator to floor subsystem
	 */
	public int getElevToFloorPort() {
		return elevToFloorPort;
	}
	
	/**
	 * Gets the port number of the GUI subsystem from the config file, or a default value
	 * @return the port for transferring data to the GUI subsystem
	 */
	public int getGUIPort() {
		return guiPort;
	}
	
	/**
	 * Gets the InetAddress of the GUI subsystem from the config file, or a default value
	 * @return the InetAddress for transferring data to the GUI subsystem
	 */
	public InetAddress getGUIIP() {
		return guiIp;
	}
	
	/**
	 * Gets the InetAddress of the Scheduler from the config file, or a default value
	 * @return the InetAddress for transferring data to the Scheduler
	 */
	public InetAddress getSchedulerIp() {
		return schedulerIp;
	}

	/**
	 * Gets the InetAddress of the floor subsystem from the config file, or a default value
	 * @return the InetAddress for transferring data to the floor subsystem
	 */
	public InetAddress getFloorIp() {
		return floorIp;
	}
	
	/**
	 * Gets the time to wait between floors in milliseconds from the config file, or a default value
	 * @return the time to wait between floors in milliseconds
	 */
	public int getTimeBetweenFloors() {
		return timeBetweenFloors;
	}
	
	/**
	 * Gets the time to wait after reaching a new floor to unload passengers in milliseconds from the config file, or a default value
	 * @return the time to wait to unload passengers in milliseconds
	 */
	public int getTimeToUnloadPassengers() {
		return timeToUnloadPassengers;
	}

	/**
	 * Method that sets all the instance variables to default values
	 */
	private void setDefaultValues() {
		this.inputFile = DEFAULT_INPUT_FILENAME;
		this.numElevators = DEFAULT_NUM_ELEVATORS;
		this.numFloors = DEFAULT_NUM_FLOORS;
		this.elevToSchedulerPort = DEFAULT_ELEV_TO_SCHEDULER_PORT;
		this.schedulerToFloorPort = DEFAULT_SCHEDULER_TO_FLOOR_PORT;
		this.elevToFloorPort = DEFAULT_ELEV_TO_FLOOR_PORT;
		this.timeBetweenFloors = DEFAULT_TIME_BETWEEN_FLOORS;
		this.timeToUnloadPassengers = DEFAULT_TIME_TO_UNLOAD_PASSENGERS;
		this.guiPort = DEFAULT_GUI_PORT;
		try {
			this.schedulerIp = InetAddress.getLocalHost();
			this.floorIp = InetAddress.getLocalHost();
			this.guiIp = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
}
