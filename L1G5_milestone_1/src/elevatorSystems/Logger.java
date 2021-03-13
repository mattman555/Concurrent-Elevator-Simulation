package elevatorSystems;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
	
	private PrintWriter writer;
	
	public Logger(String filename) {
		try {
			File f = new File(".",filename);
			if(!f.exists())
				f.createNewFile();
			writer = new PrintWriter(filename);
			writer.write("");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(3);
		}
	}
	public void close() {
		writer.flush();
		writer.close();
	}
	public synchronized void print(String s) {
		writer.append(s);
		System.out.print(s);
	}
	
	public synchronized void println(String s) {
		writer.append(s + "\n");
		System.out.println(s);
	}
	

}
