package taskmanager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;


public class taskManager {

	private static HashMap<Integer, Process> taskmap = new HashMap<Integer, Process>();
	private static int globalid = 0;
	
	public synchronized static int start(String[] command, HashMap<String,String> environment) {
		
		// Creo el process builder
		ProcessBuilder processbuilder = new ProcessBuilder();		
		processbuilder.command(command);
		
		// Seteo contexto
		if (environment != null) {
			for (String key: environment.keySet())
				processbuilder.environment().put(key, environment.get(key));
		}

		// Creo el proceso
		Process process = null;
		try {
			// Inicio el proceso
			process = processbuilder.start();
			globalid++;
			taskmap.put(globalid, process);
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		return globalid;
	}
	
	public synchronized static void stop(int id) {
		Process p = taskmap.get(id);
		if (p != null) 
			p.destroy();
		taskmap.remove(id);
	}
	
	public static int waitfor(int id) {
		Process p = taskmap.get(id);
		if (p != null)
			try {
				return p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return -1;
			}
		return 0;
	}
	
	public synchronized static InputStream getInputStream(int id) {
		Process p = taskmap.get(id);
		if (p != null)
			return p.getInputStream();
		return null;
	}
	
	public synchronized static OutputStream getOutputStream(int id) {
		Process p = taskmap.get(id);
		if (p != null)
			return p.getOutputStream();
		return null;
	}
}