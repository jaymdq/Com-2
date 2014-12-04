import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.HashMap;


public class taskManager {

	private static HashMap<Integer, Process> taskmap = new HashMap<Integer, Process>();
	private static int globalid = 0;
	
	public synchronized static int start(String[] command, PrintStream out, PrintStream err, InputStream in) {
		
		// Creo el process builder
		ProcessBuilder processbuilder = new ProcessBuilder();		
		processbuilder.command(command);
		
		// Guardo los streams
		PrintStream realout = System.out;
		PrintStream realerr = System.err;
		InputStream realin = System.in;
		
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
		taskmap.get(id).destroy();
	}

}
