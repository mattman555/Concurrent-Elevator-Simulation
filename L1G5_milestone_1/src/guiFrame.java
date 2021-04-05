import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class guiFrame extends JFrame implements ActionListener{
	Container base = getContentPane();
	JPanel containerSet = new JPanel();
	JPanel containerRun = new JPanel();
	JPanel setElevator = new JPanel();
	JPanel runElevator = new JPanel();
	JMenuBar mb = new JMenuBar();
    JMenu m1 = new JMenu("FILE");
    JMenuItem set = new JMenuItem("Set");
    JMenuItem run = new JMenuItem("Run");
	JLabel configLabel = new JLabel("Configure the Elevator System");
    JLabel elevatorNumLabel = new JLabel("Number of Elevators: ");
    JLabel floorNumLabel = new JLabel("Number of Floors: ");
    JTextField elevatorNumTextField = new JTextField();
    JTextField floorNumField = new JTextField();
    JButton confirmButton = new JButton("CONFIRM");
    BorderLayout layout = new BorderLayout();
    BorderLayout layout2 = new BorderLayout();
    GridLayout layoutGrid = new GridLayout(0,2);
    Color white = new Color(255, 255, 255);
    int numElevator;
    int numFloor;
    JLabel jlabels[];
    JLabel elevator1 = new JLabel("Elevator #1");
    
    guiFrame()
    {
       //Calling methods inside constructor.
        setLayoutManager();
        setLocationAndSize();
       // elevatorSetUp();
        addComponentsToContainer();
        setBackground();
        
        set.addActionListener(this);
        run.addActionListener(this);
        confirmButton.addActionListener(this);

    }
    
   public void setLayoutManager()
   {
	   layoutGrid.setVgap(10);
       layoutGrid.setHgap(10);
	   setElevator.setLayout(null);
	   containerRun.setLayout(layoutGrid);
       containerSet.setLayout(layout2);
       base.setLayout(layout);
       
   }
   
   public void setBackground()
   {
       containerSet.setBackground(white);
       setElevator.setBackground(white);
       
       containerRun.setBackground(white);
       //containerRun.setBackground(new Color(0,0,0));
   }
   
   public void setLocationAndSize()
   {
       //Setting location and Size of each components using setBounds() method.
	   elevatorNumLabel.setBounds(70,150,200,30);
	   floorNumLabel.setBounds(70,220,200,30);
	   elevatorNumTextField.setBounds(250,150,150,30);
	   floorNumField.setBounds(250,220,150,30);
	   confirmButton.setBounds(200,300,100,30);
	   
	   configLabel.setFont(new Font("Serif", Font.PLAIN, 24));
	   configLabel.setHorizontalAlignment(getWidth()/2);
	   
	   containerRun.setBounds(50, 80, 300, 300);
	   
   }
   
   public void addComponentsToContainer()
   {
	   m1.add(set);
	   m1.add(run);
	   mb.add(m1);
      //Adding each components to the Container
	   setElevator.add(configLabel);
       setElevator.add(elevatorNumLabel);
       setElevator.add(floorNumLabel);
       setElevator.add(elevatorNumTextField);
       setElevator.add(floorNumField);
       setElevator.add(confirmButton);
       containerSet.add(configLabel, BorderLayout.NORTH);
       containerSet.add(setElevator, BorderLayout.CENTER);
       
       
       
       base.add(mb, BorderLayout.NORTH);
       base.add(containerSet, BorderLayout.CENTER);
   }


   private void changePanel(JPanel panel) {
	    base.removeAll();
	    base.add(mb, BorderLayout.NORTH);
	    base.add(panel, BorderLayout.CENTER);
	    base.doLayout();
	    update(getGraphics());
	}
   
   
   private void elevatorSetUp(int i) {
	   layoutGrid.setColumns(numElevator);
	   JLabel label = new JLabel("Elevator #" + i);
//	   for(int i = 1; i != numElevator; i++) {
//		   runElevator.add(new JLabel("Elevator #" + i));
//	   }
	   containerRun.add(label);
	  // containerRun.add(runElevator, BorderLayout.CENTER);
   }
   
   
   
    @Override
    public void actionPerformed(ActionEvent e) {
    	if (e.getActionCommand().equals("Set")) {
    		changePanel(containerSet);
    	}
    	else if (e.getActionCommand().equals("Run")) {
    		containerRun.setLayout(layoutGrid);
    		containerRun.setBounds(50, 80, 300, 300);
    		containerRun.add(new JButton("Button 1"));
    		changePanel(containerRun);
    	}
    	else if (e.getActionCommand().equals("CONFIRM")) {
    		numElevator = Integer.parseInt(elevatorNumTextField.getText());
    		numFloor = Integer.parseInt(floorNumField.getText());
    	}
    }
}