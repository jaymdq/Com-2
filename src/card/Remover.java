package card;

import java.util.HashMap;
import java.util.Vector;

public class Remover implements Runnable {

	private HashMap<String,DispositivoABS> aps;
	private HashMap<String,DispositivoABS> clients;
	
	public Remover(HashMap<String,DispositivoABS> aps, HashMap<String,DispositivoABS> clients) {
		this.aps = aps;
		this.clients = clients;
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
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(4000);
				deleteObsolete(aps);
				deleteObsolete(clients);
			}
			catch (Exception e) {}
		}
		// TODO Auto-generated method stub

	}

}
