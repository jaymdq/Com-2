package taskmanager;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.HashMap;


public class taskManager {

	private static HashMap<Integer, Process> taskmap = new HashMap<Integer, Process>();
	private static int globalid = 0;
	
	public synchronized static int start(String[] command, PrintStream out, PrintStream err, InputStream in, HashMap<String,String> environment) {
		
		// Creo el process builder
		ProcessBuilder processbuilder = new ProcessBuilder();		
		processbuilder.command(command);
		
		// Guardo los streams
		PrintStream realout = System.out;
		PrintStream realerr = System.err;
		InputStream realin = System.in;
		
		// Seteo contexto
		if (environment != null) {
			for (String key: environment.keySet())
				processbuilder.environment().put(key, environment.get(key));
		}

		// Redirecciono el out
		if (out != null)
			System.setOut(out);
		processbuilder.redirectOutput(Redirect.INHERIT);

		// Redirecciono el in
		if (in != null)
			System.setIn(in);
		processbuilder.redirectInput(Redirect.INHERIT);

		// Redirecciono el err
		if (err != null)
			System.setErr(err);
		processbuilder.redirectError(Redirect.INHERIT);
		
		// Creo el proceso
		Process process = null;
		try {
			// Inicio el proceso
			process = processbuilder.start();
			globalid++;
			taskmap.put(globalid, process);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			// Recupero los streams
			System.setOut(realout);
			System.setErr(realerr);
			System.setIn(realin);
			if (process == null)
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

}