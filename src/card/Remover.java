package card;

import java.util.HashMap;
import java.util.Vector;

public class Remover implements Runnable {

	private HashMap<String,DispositivoABS> aps;
	private HashMap<String,DispositivoABS> clients;
	private boolean active;
	
	public Remover(HashMap<String,DispositivoABS> aps, HashMap<String,DispositivoABS> clients) {
		this.aps = aps;
		this.clients = clients;
		active = false;
	}
	
	private void deleteObsolete(HashMap<String,DispositivoABS> mapa) {
		Vector<String> todelete = new Vector<String>();
		synchronized(mapa) {
			long now = System.currentTimeMillis();
			for (String key : mapa.keySet()) {
				if ( now - mapa.get(key).last.getTime() > 60000 )
					todelete.add(key);				
			}
			for (String key : todelete)
				mapa.remove(key);
		}
	}
	
	public void start() {
		active = true;
	}
	
	public void stop() {
		active = false;
	}
	
	@Override
	public void run() {
		while (active) {
			try {
				Thread.sleep(4000);
				deleteObsolete(aps);
				deleteObsolete(clients);
			}
			catch (Exception e) {
			}
		}
	}

}
