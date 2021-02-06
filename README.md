README
Group 5 SYSC 3303 Elevator Project

---------------------
Project Description
---------------------

The system is designed in java to schedule and simulate a specified number of elevators(currently 1 for iteration 1). It will contain a controller for the elevator called "Scheduler" which will communicate with a "FloorSubsystem" containing all information
for a given number of floors and an "ElevatorSubsystem" which is the same but for individual elevators to simulate the functionality. The Scheduler is also multi-threaded as to handle as many elevators as required at the same
time.


---------------------
Iteration Number
---------------------

Iteration 1


-----------------------
How to run the system
-----------------------

JDK used: Java SE 15

The system is run by running the main() function in Scheduler.java


----------------
Important Files Included
----------------

All source files (.java) -----> Direction.java, Elevator.java, ElevatorTest.java, FloorSubsystem.java, FloorSubsystemTest.java, Request.java, RequestTest.java, RequestGroup.java, RequestGroupTest.java, Scheduler.java, SchedulerTest.java

-------------------
Overview of Files
-------------------

The following is a brief overview of the files listed above:

Direction.java: Direction is an enumeration for the direction that the elevator is moving which is used by all the other classes. It is used by request to determine the direction destination of the elevator.

Elevator.java:  Elevator is a thread that contains the lights, buttons, doors, and motors that is responsible for making sure to keep track of the current location of the elevator and will be grabbing the direction of the motor and which floor the elevator's destination is from the Scheduler.

ElevatorTest.java:  A JUnit Test class for Elevator class.

FloorSubsystem.java:  FloorSubsystem is a thread that reads the input file and validates that the input file makes sense in terms of the 4 inputs (time, floor, floor button, and car button). Then it creates a request and sends it to the scheduler. It also includes methods for getting/setting the floor lamps.

FloorSubsystemTest.java:  A JUnit Test class for FloorSubsystem class.

Request.java: Request creates a "request" which is used by the other 3 threads (Elevator, Floorsubsystem, and Scheduler) which is used for asking the elevator to perform an operation with given parameters of: time, floor, floorbuttons, and car button.

RequestTest.java: A JUnit Test class for Request class.

RequestGroup.java:  RequestGroup is used for grouping requests by creating an arraylist of requests. It also contains a method for creating an arraylist of floors called setFloorRoute which group all the destinations requested and sorts them with bubbleSort to order where the elevator will go. There are also a bunch of get and remove methods for use of other classes.


RequestGroupTest.java:  A JUnit Test class for RequestGroup class.

Scheduler.java: Scheduler is a thread meant for scheduling the requests and sending the correct order of operations for the elevator to perform.

SchedulerTest.java: A JUnit Test class for Scheduler class.
