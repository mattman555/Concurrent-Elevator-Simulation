package GUI;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

import elevatorSystems.Direction;

public class gui {
	
	static guiFrame frame = new guiFrame();
	
    public static void main(String[] a){
        frame.setTitle("Elevator Form");
        frame.setVisible(true);
        frame.setBounds(10,10,525,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        

    }
    
    public void update(int elevId, int floor, Direction d, int error) {
    	frame.elevators[elevId-1].removeAll();
    	frame.jlabelsFloor[elevId-1].setText("Floor: " + floor);
    	frame.jlabelsDirection[elevId-1].setText("Direction: " + d);
    	frame.jlabelsError[elevId-1].setText("Error: " + error);
    }

}