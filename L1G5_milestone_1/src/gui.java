import java.awt.Color;

import javax.swing.JFrame;

public class gui {
    public static void main(String[] a){
    	guiFrame frame = new guiFrame();
        frame.setTitle("Elevator Form");
        frame.setVisible(true);
        frame.setBounds(10,10,500,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        

    }

}