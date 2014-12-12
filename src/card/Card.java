package card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Vector;

import taskmanager.taskManager;

public class Card {
	
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
	private static Vector<String> allowedtypes;
	private String serverStatus;
	private String lastUpdate;
	
	// Agreaga un tipo aceptado por el programa
	public static void addTypes(String type) {
		if (allowedtypes == null)
			allowedtypes = new Vector<String>();
		if (!allowedtypes.contains(type))
			allowedtypes.add(type);
	}
	
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
	public synchronized void start() {
		if (!active) {
			setStatus("");
			initCard();
			activateMonitor();
			tshark();
			if (listenerThread != null) {
				aps = new HashMap<String,AP>();
				clients = new HashMap<String,Client>();
				active = true;
			}
		}
	}

	// Stop
	public synchronized void stop() {
		if (active) {
			active = false;
			setStatus("");
			if (listenerThread != null) {
				listenerThread.interrupt();
				listenerThread = null;
			}
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
				if (line.contains("mon")) {
					setStatus("Monitor activado: " + line);
					monitor = line;
				}
				else 
					setStatus(line);
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
	public synchronized String toString() {
		String toreturn = new String();
		if (!active)
			toreturn = status;
		else {
			toreturn = toreturn + AP.TITLE + "\n";
			for (AP ap : aps.values())
				toreturn = toreturn + ap.toString() + "\n";
			toreturn= toreturn + "\n";
			toreturn = toreturn + Client.TITLE + "\n";
			for (Client client : clients.values())
				toreturn = toreturn + client.toString() + "\n";
		}
		return toreturn;
	}
	
	public synchronized void listen(String line) {		
		String[] parseado = line.split("	");
		Packet packet = new Packet(parseado);
		if (allowedtypes.contains(packet.type)) {
			if (aps.containsKey(packet.origen) || packet.type.equals(Packet.BEACON) || packet.type.equals(Packet.PROBERESP) || packet.type.equals(Packet.ASSOCIATIONRESP) || packet.type.equals(Packet.ACTION) || packet.type.equals(Packet.REASSOCIATIONRESP)) {
				updateAP(packet);
				// TODO este if ya no deberia pasar, lo sacamos o lo dejamos?
				if (clients.containsKey(packet.origen))
					clients.remove(packet.origen);
				if (config.sendAP) {
					// TODO Enviar al servidor
				}
			}
			else {
				updateClient(packet);
				if (packet.type.equals(Packet.PROBEREQ) || config.sendAll) {
					// TODO Enviar al servidor
				}
			}
		}
	}
	
	private void updateAP(Packet packet) {
		if (aps.containsKey(packet.origen))
			aps.get(packet.origen).update(packet);
		else
			aps.put(packet.origen, new AP(packet));
	}
	
	private void updateClient(Packet packet) {
		if (clients.containsKey(packet.origen))
			clients.get(packet.origen).update(packet);
		else
			clients.put(packet.origen, new Client(packet));
	}
	
	public void setStatus(String line) {
		if (line.equals(""))
			status = "";
		else
			status = status + line + "\n";
	}

	public String getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(String serverStatus) {
		this.serverStatus = serverStatus;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
