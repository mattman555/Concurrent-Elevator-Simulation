package GUI;
import javax.swing.*;

import elevatorSystems.Elevator;
import elevatorSystems.FloorSubsystem;
import elevatorSystems.Scheduler;
import elevatorSystems.elevatorStateMachine.ElevatorSM;

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
    JButton runButton = new JButton("Run Program");
    BorderLayout layout = new BorderLayout();
    BorderLayout layout2 = new BorderLayout();
    GridLayout layoutGrid = new GridLayout(0,2);
    FlowLayout layoutFlow = new FlowLayout(FlowLayout.LEADING);
    Color white = new Color(255, 255, 255);
    public static int numElevator;
    public int numFloor;
   	public boolean readyToRun = false;
    
	JPanel elevators[];
    JTextArea jlabelsFloor[];
	JTextArea jlabelsError[];
	JTextArea jlabelsDirection[];
    
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
        runButton.addActionListener(this);

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
	   
	containerRun.setBounds(100, 80, 500, 500);
	   
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
	   base.add(containerRun, BorderLayout.CENTER);
	   base.add(containerSet, BorderLayout.CENTER);
	   changePanel(containerSet);
   }


   private void changePanel(JPanel panel) {
	    panel.repaint();
	    base.removeAll();
	    base.add(mb, BorderLayout.NORTH);
	    base.add(panel, BorderLayout.CENTER);
	    if(readyToRun == true) {
	    	base.add(runButton, BorderLayout.SOUTH);
	    }
	    base.doLayout();
	    update(getGraphics());
	}
   
   
   public void elevatorSetUp() {
	   layoutGrid.setColumns(2);
	   containerRun.removeAll();
	   JLabel labels[] = new JLabel[numElevator];
	   jlabelsFloor = new JTextArea[numElevator];
	   jlabelsError = new JTextArea[numElevator];
	   jlabelsDirection = new JTextArea[numElevator];
	   elevators = new JPanel[numElevator];

	   for(int i = 0; i < labels.length; i++) {
		   labels[i] = new JLabel("Elevator #:" + (i + 1));
		   labels[i].setFont(new Font("Serif", Font.PLAIN, 20));
		   containerRun.add(labels[i]);
		   elevators[i] = new JPanel();
		   elevators[i].setLayout(layoutGrid);
		 //  elevators[i].setBackground(Color.DARK_GRAY);
		   jlabelsFloor[i] = new JTextArea("Floor: 1" );
		   jlabelsFloor[i].setBackground(UIManager.getColor("Label.background"));
		   jlabelsDirection[i] = new JTextArea("Direction: Stationary" );
		   jlabelsDirection[i].setBackground(UIManager.getColor("Label.background"));
		   jlabelsError[i] = new JTextArea("Error: None" );
		   jlabelsError[i].setWrapStyleWord(true);
		   jlabelsError[i].setLineWrap(true);
		   jlabelsError[i].setBackground(UIManager.getColor("Label.background"));
		   elevators[i].add(jlabelsFloor[i]);
		   elevators[i].add(jlabelsDirection[i]);
		   elevators[i].add(jlabelsError[i]);
		   elevators[i].doLayout();
		   containerRun.add(elevators[i]);
		   //containerRun.setAlignmentX(CENTER_ALIGNMENT);
	   }
	   containerRun.doLayout();
	   containerRun.revalidate();
	   containerRun.repaint();
   }
   
   public void elevatorMove() {
	   
	   //int elevator = Elevator.getId();
   }
   
   
	@Override
    public void actionPerformed(ActionEvent e) {
    	if (e.getActionCommand().equals("Set")) {
    		readyToRun = false;
    		changePanel(containerSet);
    	}
    	else if (e.getActionCommand().equals("Run")) {
    		elevatorSetUp();
    		readyToRun = true;
    		changePanel(containerRun);
    	}
    	else if (e.getActionCommand().equals("CONFIRM")) {
    		numElevator = Integer.parseInt(elevatorNumTextField.getText());
    		numFloor = Integer.parseInt(floorNumField.getText());
    	}
    	else if (e.getActionCommand().equals("Run Program")) {
    		FloorSubsystem.main(null);
    		Scheduler.main(null);
    		ElevatorSM.main(null);
    	}
    }
}
