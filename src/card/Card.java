package card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import parser.Parser;
import taskmanager.taskManager;

public class Card {
	
	private String card;
	private int idChannels;
	private int idTshark;
	private int idAP;
	private boolean active;
	private Config config;
	private Thread listenerThread;
	private Thread removerThread;
	private Thread parserThread;
	private Remover remover;
	private Listener listener;
	private Parser parser;
	private String status;
	private HashMap<String, DispositivoABS> aps;
	private HashMap<String, DispositivoABS> clients;
	private Vector<String> allowedChannels;
	private static Vector<String> allowedTypes = new Vector<String>();
	
	// Agreaga un tipo aceptado por el programa
	public static void addTypes(String type) {
		if (!allowedTypes.contains(type))
			allowedTypes.add(type);
	}
	
	// Create a new card
	public Card(String card) {
		this.card = card;
		idChannels = 0;
		idTshark = 0;
		idAP = 0;
		setStatus("");
		config = new Config();
		listenerThread = null;
		removerThread = null;
		active = false;
		aps = new HashMap<String,DispositivoABS>();
		clients = new HashMap<String,DispositivoABS>();
		remover = new Remover(aps,clients);
		listener = new Listener(this);
		readChannels();
	}

	private void readChannels() {
		allowedChannels = new Vector<String>();
		String command[] = {"bash","./scripts/get_channels.sh",card};
		int idtask = taskManager.start(command,null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		String line = null;
		try {
			while ( (line=buff.readLine()) != null) {
				if (line.startsWith("0"))
					line = line.substring(1);
				allowedChannels.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Start
	public synchronized void start() {
		if (!active) {
			setStatus("");
			if (toMonitor()) {
				if (!config.onlyap)
					startTshark();
				if (config.onlyap || config.fakeap)
					startFakeAP();
				active = true;
			}
		}
	}

	// Stop
	public synchronized void stop() {
		if (active) {
			active = false;
			setStatus("");
			if (!config.onlyap)
				stopTshark();
			if (config.onlyap || config.fakeap)
				stopFakeAP();
			toManaged();	
			aps.clear();
			clients.clear();
		}
	}
	
	// Inicia la tarjeta
	private boolean toMonitor() {
		String command[] = {"bash","./scripts/to_monitor.sh",card};
		int idtask = taskManager.start(command,null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		boolean toreturn = true;
		try {
			String line = null;
			while ( (line=buff.readLine()) != null){
				setStatus(line);
				toreturn = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return toreturn;
	}

	// Tshark y cambio de canales
	private void startTshark() {
		// Comando para ejecutar tshark
		String commandtshark[] = {"bash","./scripts/tshark.sh", card};
		// Comando para cambiar de canales		
		String channelsList = "";
		for (String channel : config.channels)
			channelsList += channel + " ";
		String commandchannels[] = {"bash","./scripts/change_channels.sh", card, channelsList};
		// Lanzo a ejecutar tshark
		idTshark = taskManager.start(commandtshark,null);
		// Lanzo a ejecutar el cambio de canales
		idChannels = taskManager.start(commandchannels, null);
		// Thread escucha de tshark
		listener.setInputStream(taskManager.getInputStream(idTshark));
		listenerThread = new Thread(listener);
		listenerThread.start();
		// Thread removedor
		removerThread = new Thread(remover);
		removerThread.start();
		remover.start();
		// Thread parser para enviar info al servidor
		parser = new Parser(config);
		parser.start();
		parserThread = new Thread(parser);
		parserThread.start();
	}
	
	private void startFakeAP() {
		String command[] = {"bash","./scripts/fake_ap.sh", card};
		idAP = taskManager.start(command, null);
		if (config.onlyap) {
			setStatus("");
			setStatus("La tarjeta " + card + " unicamente esta ejecutando un soft-AP.");
			setStatus("No se estan registrando paquetes.");
		}
	}
	
	private void stopFakeAP() {
		taskManager.stop(idAP);
		idAP = 0;
	}
	
	private void stopTshark() {
		taskManager.stop(idChannels);
		taskManager.stop(idTshark);
		idChannels = 0;
		idTshark = 0;
		if (listenerThread != null) {
			listenerThread.interrupt();
			listenerThread = null;
		}
		if (removerThread != null) {
			remover.stop();
			removerThread.interrupt();
			removerThread = null;
		}
		if (parserThread != null) {
			parser.stop();
			parserThread.interrupt();
			parserThread = null;
		}
	}
	
	// Desactiva el modo monitor
	private void toManaged() {
		String command[] = {"bash","./scripts/to_managed.sh",card};
		int idtask = taskManager.start(command, null);
		taskManager.waitfor(idtask);
	}

	// Indica si el monitor esta activo o no
	public Boolean isActive() {
		return active;
	}

	// Devuelve al configuración actual
	public Config getConfig() {
		return config;
	}

	// String que iria a la consola
	public synchronized String toString() {
		// String a retornar
		String toreturn = new String();
		// Si la tarjeta esta inactiva se devuelve el mensaje de status
		if (idTshark == 0)
			toreturn = status;
		// Si la tarjeta esta activa se devuelven los dispositivos
		else {
			toreturn = "Running on [" + card + "]	Current Channel [" + getChannel() + "] \n\n";
			toreturn = toreturn + AP.TITLE + "\n";
			for (DispositivoABS ap : aps.values())
				toreturn = toreturn + ap.toString() + "\n";
			toreturn= toreturn + "\n";
			toreturn = toreturn + Client.TITLE + "\n";
			for (DispositivoABS client : clients.values())
				toreturn = toreturn + client.toString() + "\n";
		}
		// Retorno el string
		return toreturn;
	}
	
	// Procesado de packet
	public synchronized void listen(String line) {
		// Parseo el string
		String[] parseado = line.split("	");
		// Creo el packet a partir del string
		Packet packet = new Packet(parseado);
		// Si es un packet permitido
		if (allowedTypes.contains(packet.type)) {
			// Si corresponde a un AP
			if (isAP(packet))
				processAP(packet);
			// Si corresopnde a un client
			else if (isClient(packet))
				processClient(packet);
		}
	}
	
	// Verifica si el packet corresponde a un AP
	private boolean isAP(Packet packet) {
		return (aps.containsKey(packet.origen) || Arrays.asList(AP.TYPES).contains(packet.type));
	}
	
	// Verifica si el packet corresponde a un Cliente
	private boolean isClient(Packet packet) {
		return (clients.containsKey(packet.origen) || Arrays.asList(Client.TYPES).contains(packet.type));
	}
	
	// Si corresponde, envia el packet al servidor
	private void sendPacket(Packet packet) {
		parser.addPacket(packet);
	}
	
	// Procesa el packet en caso de que sea de un AP
	private void processAP(Packet packet) {
		DispositivoABS ap = aps.get(packet.origen);
		// Si ya estaba registrado actualizo
		if (ap != null) 
			ap.update(packet);
		// Sino, lo agrego
		else {
			ap = new AP(packet);
			aps.put(packet.origen, ap);
		}
		// Si se cumplen las condiciones, enviar
		if (ap.needUpdate(config.delaymac) && config.sendap)
			sendPacket(packet);
	}
	
	// Procesa el packet en caso de que sea de un Cliente
	private void processClient(Packet packet) {
		DispositivoABS client = clients.get(packet.origen);
		// Si ya estaba registrado actualizo
		if (client != null) 
			client.update(packet);
		// Sino, lo agrego
		else {
			client = new Client(packet);
			clients.put(packet.origen, client);
		}
		// Si se cumplen las condiciones, enviar
		if ( client.needUpdate(config.delaymac) && (packet.type.equals(Packet.PROBEREQ) || config.sendall))
			sendPacket(packet);
	}
	
	// Setea el estado de la tarjeta
	public void setStatus(String line) {
		if (line.equals(""))
			status = "";
		else
			status = status + line + "\n";
	}

	private int getChannel() {			
		String command[] = {"bash","./scripts/get_channel.sh",card};
		int idtask = taskManager.start(command, null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = buff.readLine();
			return Integer.parseInt(line);
		} catch (IOException e) {
			return -1;
		}
	}

	public Vector<String> getAllowedchannels() {
		return allowedChannels;
	}

	public void setAllowedchannels(Vector<String> allowedChannels) {
		this.allowedChannels = allowedChannels;
	}
}
