package GUI;

import elevatorSystems.FloorSubsystem;
import elevatorSystems.Scheduler;
import elevatorSystems.elevatorStateMachine.ElevatorSM;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GuiFrame extends JFrame implements ActionListener{
	public Container base = getContentPane();
    private JPanel containerSet = new JPanel();
	public JPanel containerRun = new JPanel();
	private JPanel setElevator = new JPanel();
	private JPanel runElevator = new JPanel();
	private JMenuBar mb = new JMenuBar();
   	private JMenu m1 = new JMenu("FILE");
    private JMenuItem set = new JMenuItem("Set");
    private JMenuItem run = new JMenuItem("Run");
	private JLabel configLabel = new JLabel("Configure the Elevator System");
    private JLabel elevatorNumLabel = new JLabel("Number of Elevators: ");
    private JLabel floorNumLabel = new JLabel("Number of Floors: ");
    private JTextField elevatorNumTextField = new JTextField();
    private JTextField floorNumField = new JTextField();
    private JButton confirmButton = new JButton("CONFIRM");
    private JButton runButton = new JButton("Run Program");
    private BorderLayout layout = new BorderLayout();
    private BorderLayout layout2 = new BorderLayout();
    private GridLayout layoutGrid = new GridLayout(0,2);
    private FlowLayout layoutFlow = new FlowLayout(FlowLayout.LEADING);
    private Color white = new Color(255, 255, 255);
    public static int numElevator;
    public int numFloor;
   	public boolean readyToRun = false;
    
	public JPanel elevators[];
    public JTextArea jlabelsFloor[];
	public JTextArea jlabelsError[];
	public JTextArea jlabelsDirection[];
	public JTextArea lamps[];
    
    public GuiFrame()
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


   public void changePanel(JPanel panel) {
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
	   lamps = new JTextArea[numElevator];
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
		   lamps[i] = new JTextArea("Car Lamps:" );
		   lamps[i].setWrapStyleWord(true);
		   lamps[i].setLineWrap(true);
		   lamps[i].setBackground(UIManager.getColor("Label.background"));
		   elevators[i].add(jlabelsFloor[i]);
		   elevators[i].add(jlabelsDirection[i]);
		   elevators[i].add(jlabelsError[i]);
		   elevators[i].add(lamps[i]);
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
    		try {
    		      File config = new File("Config.txt");
    		      File temp = new File("temp.txt");
    		      temp.createNewFile();
    		      FileWriter myWriter = new FileWriter("temp.txt");
    		      Scanner myReader = new Scanner(config);
    		      while (myReader.hasNextLine()) {
    		        String data = myReader.nextLine();
    		        String[] lineArr = data.split(" ");
    		        if(lineArr[0].equals("Num_elevators")) {
    		        	lineArr[1] = Integer.toString(numElevator);
    		        	myWriter.write(lineArr[0]+" "+lineArr[1]+"\n");
    		        }
    		        else if(lineArr[0].equals("Num_floors")){
    		        	lineArr[1] = Integer.toString(numFloor);
    		        	myWriter.write(lineArr[0]+" "+lineArr[1]+"\n");
    		        }
    		        else {
    		        	myWriter.write(data+"\n");
    		        }
    		      }
    		      myWriter.close();
    		      myReader.close();
    		      config.delete();
    		      temp.renameTo(config);
    		    } catch (FileNotFoundException e1) {
    		      System.out.println("An error occurred.");
    		      e1.printStackTrace();
    		    } catch (IOException e1) {
					e1.printStackTrace();
				}
    	}
    	else if (e.getActionCommand().equals("Run Program")) {
    		FloorSubsystem.main(null);
    		Scheduler.main(null);
    		ElevatorSM.main(null);
    	}
    }
}
