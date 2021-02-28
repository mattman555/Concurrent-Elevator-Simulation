README
Group 5 SYSC 3303 Elevator Project

---------------------
Project Description
---------------------

The system is designed in java to schedule and simulate a specified number of elevators(currently 1 for iteration 2). It will contain a controller for the elevator called "Scheduler" which will communicate with 
a "FloorSubsystem" containing all information for a given number of floors and an "ElevatorSubsystem" which is the same but for individual elevators to simulate the functionality. The Scheduler is also multi-threaded 
as to handle as many elevators as required at the same time. New additions include simulating a state machine for the elevator subsystem and scheduler.


---------------------
Iteration Number
---------------------

Iteration 2


-----------------------
How to run the system
-----------------------

JDK used: Java SE 15

The system is run by running the main() function in Scheduler.java


--------------------------
Important Files Included
--------------------------

Files from previous iteration (.java) -----> Direction, Elevator, ElevatorTest, FloorSubsystem, FloorSubsystemTest, Request, RequestTest, RequestGroup, RequestGroupTest, Scheduler, SchedulerTest

Files from current iteration (.java) ------> Arrived, AwaitingRequests, DoorsClosed, DoorsOpen, ElevatorSM, ElevatorState, ElevatorStates, End, InProgress, Moving, SchedulerState, SortedRequests, UnsortedRequests, UpdateLamps


--------------------------------
Elevator State Machine Diagram
--------------------------------

![Click to see Diagram](https://github.com/mattman555/SYSC_3303_Project/blob/main/Elevator%20Subsystem%20State%20Machine.jpg)


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

ElevatorTest.java: A JUnit Test class for Elevator class.

FloorSubsystem.java: FloorSubsystem is a thread that reads the input file and validates that the input file makes sense in terms of the 4 inputs (time, floor, floor button, and car button). Then it creates a request 
and sends it to the scheduler. It also includes methods for getting/setting the floor lamps.

FloorSubsystemTest.java: A JUnit Test class for FloorSubsystem class.

Request.java: Request creates a "request" which is used by the other 3 threads (Elevator, Floorsubsystem, and Scheduler) which is used for asking the elevator to perform an operation with given parameters of: 
time, floor, floorbuttons, and car button.

RequestTest.java: A JUnit Test class for Request class.

RequestGroup.java: RequestGroup is used for grouping requests by creating an arraylist of requests. It also contains a method for creating an arraylist of floors called setFloorRoute which group all the destinations 
requested and sorts them with bubbleSort to order where the elevator will go. There are also a bunch of get and remove methods for use of other classes.

RequestGroupTest.java: A JUnit Test class for RequestGroup class.

Scheduler.java: Scheduler is a thread meant for scheduling the requests and sending the correct order of operations for the elevator to perform.

SchedulerTest.java: A JUnit Test class for Scheduler class.

TestSuite.java: A class that combines all the test classes.

TestSuiteRunner.java: A class that runs the test suite.



New Files Created: 


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

