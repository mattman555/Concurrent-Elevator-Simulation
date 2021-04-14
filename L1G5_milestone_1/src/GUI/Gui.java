package GUI;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Hashtable;
import javax.swing.JFrame;
import elevatorSystems.ConfigReader;
import elevatorSystems.elevatorStateMachine.ElevatorInfo;

public class Gui {
	
	private static GuiFrame frame = new GuiFrame();
	private final static String CONFIG = "Config.txt";
	private static int guiPort;
	private static DatagramSocket receiveSocket;
	private static int elevatorsTotal;
	private static int elevatorsFinished = 0;
	private static ElevatorInfo request;
	
	
    public static void main(String[] a){
    	ConfigReader configs = new ConfigReader(CONFIG);
    	guiPort = configs.getGUIPort(); 
        frame.setTitle("Elevator Form");
        frame.setVisible(true);
        frame.setBounds(10,10,525,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        
        
        byte data[] = new byte[1000];
        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        try {
        	receiveSocket = new DatagramSocket(guiPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
        
        
        do{
        	elevatorsTotal = configs.getNumElevators();
        	try {
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
        	

    		try {
    			ByteArrayInputStream stream = new ByteArrayInputStream(receivePacket.getData());
    			ObjectInputStream oStream = new ObjectInputStream(stream);
    			request = (ElevatorInfo) oStream.readObject();
    			oStream.close();
    			stream.close();
    		} catch (IOException | ClassNotFoundException e) {
    			e.printStackTrace();
    			System.exit(1);
    		}
    		update(request.getElevatorId(),request.getElevatorLocation(),request.getDirection(),request.getErrorCode(), request.getLamps());
        } while(elevatorsTotal > elevatorsFinished);
        
        receiveSocket.close();
    }
    
    
    public static void update(int elevId, int floor, String d, int error, Hashtable<Integer, Boolean> lamps) {
    	//frame.elevators[elevId-1].removeAll();
    	frame.jlabelsFloor[elevId-1].setText("Floor: " + floor);
    	frame.jlabelsDirection[elevId-1].setText("Direction: " + d);
    	if (error == 0) {
    		frame.jlabelsError[elevId-1].setText("RUNNING FINE");
    	}else if (error == 1) {
    		frame.jlabelsError[elevId-1].setText("Error: DOOR STUCK");
    	}else if (error == 2) {
    		frame.jlabelsError[elevId-1].setText("RECEIVED FATAL ERROR, ELEVATOR SHUTDOWN");
    	}else {
    		frame.jlabelsError[elevId-1].setText("ELEVATOR COMPLETED");
    		frame.jlabelsFloor[elevId-1].removeAll();
    		frame.jlabelsDirection[elevId-1].removeAll();
    	}
    	
    	
    	frame.lamps[elevId-1].setText("Car Lamps: " + lamps);
    	frame.readyToRun = false;
    	frame.changePanel(frame.containerRun);
    	frame.base.doLayout();
    	frame.doLayout();
    	frame.revalidate();
    	frame.repaint();
    }

}