package elevatorSystems;

public class Logger {
	
	public synchronized void print(String s) {
		System.out.print(s);
	}
	
	public synchronized void println(String s) {
		System.out.println(s);
	}
	

}
