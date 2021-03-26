README
Group 5 SYSC 3303 Elevator Project

---------------------
Project Description
---------------------

The system is designed in java to schedule and simulate a specified number of elevators. It will contain a controller for the elevator called "Scheduler" which will communicate through UDP with a "FloorSubsystem" containing all 
information for a given number of floors and an "Elevator" which is the same but for individual elevators to simulate the functionality. The system simulates a state machine for the elevator's and the scheduler. It also has 
the capability for multiple elevators with the use of UDP and individual main functions. New additions this iteration include adding the configuration text file for passing across the whole system where needed. Added a new state
for the elevatorSM, DoorStuck, for when the specific error is passed with the request.


---------------------
Iteration Number
---------------------

Iteration 4


-------------------------------
Breakdown
-------------------------------
ITERATION 4:
  Nick - Made changes to RequestGroup and Scheduler to incorporate error codes, updated sequence diagram and Elevator State Machine.jpg.
  Jay - Updated the ElevatorSM and Elevator classes to handle errors given through the input file.
  Matt - Updated the Request class to have an error code, updated floor subsytem to read the error codes from the input fiel, updated UML class diagram.
  Kevin - Updated ElevatorSM, Scheduler, and FloorSubsystem to read from a config file, updated the Readme.
  Ambar - 
  This isn't strictly adhered to since everyone added methods and changes to other classes 



ITERATION 3:
  Nick - Majority of code for udp communications, helped with designing the udp communications, debugging issues with udp communications
  Jay - Helped with designing udp communications, Documenting code
  Matt - Added logging for the UDP communications, Documenting code, debugging issues with udp communications, helped with designing udp communications
  Kevin - UML, Readme, Sequence diagrams, helped with designing udp communications
  Ambar - 
  This isn't strictly adhered to since everyone added methods and changes to other classes 



ITERATION 2:
  Everyone together - Outline of UML, Outline of State machine diagrams for both state machines
  Nick - Majority of code for Scheduler State machine, Refactored Elevator state machine to use enums
  Jay - Added more logging to the code, Testing for elevator state machine
  Matt - Majority of code for Scheduler State machine, majority of code for elevator State machine
  Kevin - Majority of UML, Outline of Scheduler State machine code, Readme
  Ambar - Outline of Scheduler State machine code
  This isn't strictly adhered to since everyone added methods and changes to other classes 



ITERATION 1:
  Everyone together - UML, Debugged final code as a group
  Nick - RequestGroup.java, Scheduler.java, RequestTest.java
  Jay - Elevator.java, SchedulerTest.java, ElevatorTest.java
  Matt - FloorSubsytem.java, Request.java, FloorSubsystemTest.java, RequestGroupTest.java, Direction.java, TestSuite.java
  Kevin - README, Sequence Diagrams
  Ambar - 
  This isn't strictly adhered to since everyone added methods and changes to other classes 


-----------------------
How to run the system
-----------------------

JDK used: Java SE 15

The system is run by running the main() functions in the order: FloorSubsystem.java, Scheduler.java, and ElevatorSM.java


--------------------------
Important Files Included
--------------------------

Files from previous iteration (.java) -----> Direction, Elevator, ElevatorRPCRequests, ElevatorTest, FloorSubsystem, FloorSubsystemTest, Request, RequestTest, RequestGroup, RequestGroupTest, 
					     RPCRequestType, Scheduler, SchedulerTest, Arrived, AwaitingRequests, DoorsClosed, DoorsOpen, ElevatorSM, ElevatorState, ElevatorStates, End, End, 
					     InProgress, Moving, SchedulerState, SortedRequests, UnsortedRequests, UpdateLamps


Files from current iteration         ------> config.txt, DoorStuck.java

--------------------------------
Elevator State Machine Diagram
--------------------------------

![Click to see Diagram](https://github.com/mattman555/SYSC_3303_Project/blob/main/State_Machine_Elevator_Subsystem.jpg)


---------------------------------
Scheduler State Machine Diagram
---------------------------------

![Click to see Diagram](https://github.com/mattman555/SYSC_3303_Project/blob/main/Scheduler%20State%20Machine.jpg)


-------------------
Overview of Files   
-------------------

The following is a brief overview of the files listed above:



Files from previous iteration that have been updated:


Direction.java: Direction is an enumeration for the direction that the elevator is moving which is used by all the other classes. It is used by request to determine the direction destination of the elevator.

Elevator.java: Elevator is a thread that contains the lights, buttons, doors, and motors that is responsible for making sure to keep track of the current location of the elevator and will be grabbing the direction 
of the motor and which floor the elevator's destination is from the Scheduler.

ElevatorRPCRequests.java: A serializable class for sending information in remote procedure calls.

ElevatorTest.java: A JUnit Test class for Elevator class.

FloorSubsystem.java: FloorSubsystem is a thread that reads the input file and validates that the input file makes sense in terms of the 5 inputs (time, floor, floor button, car button, and error code). Then it creates a request 
and sends it to the scheduler. It also includes methods for getting/setting the floor lamps.

FloorSubsystemTest.java: A JUnit Test class for FloorSubsystem class.

Request.java: Request creates a "request" which is used by the other 3 threads (Elevator, Floorsubsystem, and Scheduler) which is used for asking the elevator to perform an operation with given parameters of: 
time, floor, floorbuttons, car button, and error code.

RequestTest.java: A JUnit Test class for Request class.

RequestGroup.java: RequestGroup is used for grouping requests by creating an arraylist of requests. It also contains a method for creating an arraylist of floors called setFloorRoute which group all the destinations 
requested and sorts them with bubbleSort to order where the elevator will go. There are also a bunch of get and remove methods for use of other classes.

RequestGroupTest.java: A JUnit Test class for RequestGroup class.

RPCRequestType.java : An enumeration class for the different types of remote procedure call request types.

Scheduler.java: Scheduler is a thread meant for scheduling the requests and sending the correct order of operations for the elevator to perform.

SchedulerTest.java: A JUnit Test class for Scheduler class.

TestSuite.java: A class that combines all the test classes.

TestSuiteRunner.java: A class that runs the test suite.

Arrived.java: A class for the Arrived state for the elevator state machine.

AwaitingRequests.java: A class for the AwaitingRequests state for the scheduler state machine.

DoorsClosed.java: A class for the DoorsClosed state for the elevator state machine.

DoorsOpen.java: A class for the DoorsOpen state for the elevator state machine.

ElevatorSM.java: A class that simulates the order of the states of the elevator state machine.

ElevatorState.java: A class that contains a method for each state in the elevator.

ElevatorStates.java: An enumeration class for all 6 states of the elevator.

End.java: A class for the End state for the elevator state machine.

InProgress.java: A class for the InProgress state for the scheduler state machine.

Moving.java: A class for the Moving state for the elevator state machine.

SchedulerState.java: A class that contains a method for each state in the scheduler.

SortedRequests.java: A class for the SortedRequests state for the scheduler state machine.

UnsortedRequests.java: A class for the UnsortedRequests state for the scheduler state machine.

UpdateLamps.java: A class for the UpdateLamps state for the elevator state machine.



New Files Created: 


config.txt: A text file created for the configuration of necessary inputs to other files. ex: Number of Floors set to 22

DoorStuck.java: A new state for the elevator state machine for when the doors get stuck (request with error code 1)
