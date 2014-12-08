package taskmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class Card extends Observable{

	private String card;
	private String monitor;
	private int idChannels;
	private int idTshark;
	private boolean active;
	private Config config;
	private Thread listenerThread;
	private String status;
	private HashMap<String, AP> aps;
	private HashMap<String, Client> clients;
	
	// Create a new card
	public Card(String card) {
		this.card = card;
		this.monitor = null;
		idChannels = 0;
		idTshark = 0;
		active = false;
		setStatus("");
		setConfig(new Config());
	}

	// Start
	public void start() {
		if (!active) {
			setStatus("");
			initCard();
			activateMonitor();
			tshark();
			if (listenerThread != null) {
				if (config.sendAP)
					aps = new HashMap<String,AP>();
				clients = new HashMap<String,Client>();
				active = true;
			}
		}
	}

	// Stop
	public void stop() {
		if (active) {
			active = false;
			setStatus("");
			if (listenerThread != null)
				listenerThread.interrupt();
			desactivateMonitor();
		}
	}
	
	// Inicia la tarjeta
	private void initCard() {
		String command[] = {"bash","./scripts/init_card.sh",card};
		int idtask = taskManager.start(command,null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = null;
			while ( (line=buff.readLine()) != null){
				setStatus(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		taskManager.waitfor(idtask);
	}

	// Activa el modo monitor
	private void activateMonitor() {
		String command[] = {"bash","./scripts/activate_monitor.sh",card};
		int idtask = taskManager.start(command, null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = null;
			while ( (line=buff.readLine()) != null){
				setStatus(line);
				if (line.contains("mon")){
					monitor = line;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Tshark y cambio de canales
	private void tshark() {
		if (monitor != null) {
			String commandtshark[] = {"bash","./scripts/tshark.sh", monitor};
			idTshark = taskManager.start(commandtshark,null);
			Listener listener = new Listener(taskManager.getInputStream(idTshark),this);
			listenerThread = new Thread(listener);
			listenerThread.start();
			String commandchannels[] = {"bash","./scripts/change_channels.sh", monitor};
			idChannels = taskManager.start(commandchannels, null);
		}
	}
	
	// Desactiva el modo monitor
	private void desactivateMonitor() {
		taskManager.stop(idChannels);
		taskManager.stop(idTshark);
		String command[] = {"bash","./scripts/desactivate_monitor.sh",monitor};
		int idtask = taskManager.start(command, null);
		taskManager.waitfor(idtask);
		monitor = null;
	}

	// Indica si el monitor esta activo o no
	public Boolean isActive() {
		return active;
	}

	// Devuelve al configuración actual
	public Config getConfig() {
		return config;
	}

	// Setea una nueva configuracion
	public void setConfig(Config config) {
		this.config = config;
	}

	// String que iria a la consola
	public String toString() {
		String toreturn = new String();
		if (!active)
			toreturn = status;
		else {
			if (config.sendAP) {
				toreturn.concat(AP.TITLE + "\n");
				for (AP ap : aps.values())
					toreturn.concat(ap.toString() + "\n");
				toreturn.concat("\n");
			}
			toreturn.concat(Client.TITLE + "\n");
			for (Client client : clients.values())
				toreturn.concat(client.toString() + "\n");
		}
		return toreturn;
	}
	
	public void listen(String packet) {
		// Esto deberia leer la linea, parsearla y toda la bldes
		// Verificar si debo enviarlo o no al servidor (segun los tiempos entre paq)
		// Si lo debo enviar entonces debo actualizar la consola
		// Si actualizo la consola entonces hago un "notifyObservers"
		setChanged();
		notifyObservers(toString());
	}
	
	public void setStatus(String line) {
		if (line == "")
			status = "";
		else
			status = status + line + "\n";
		setChanged();
		notifyObservers(toString());
	}
	
	public void addObserver(Observer o) {
		super.addObserver(o);
		setChanged();
		notifyObservers(toString());
	}
}
