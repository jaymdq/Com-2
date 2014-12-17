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
	private int idchannels;
	private int idtshark;
	private int idap;
	private boolean active;
	private Config config;
	private Thread listenerthread;
	private Thread removerthread;
	private Thread parserthread;
	private Remover remover;
	private Listener listener;
	private Parser parser;
	private String status;
	private HashMap<String, DispositivoABS> aps;
	private HashMap<String, DispositivoABS> clients;
	private Vector<String> allowedchannels;
	private static Vector<String> allowedtypes = new Vector<String>();
	
	// Agreaga un tipo aceptado por el programa
	public static void addTypes(String type) {
		if (!allowedtypes.contains(type))
			allowedtypes.add(type);
	}
	
	// Create a new card
	public Card(String card) {
		this.card = card;
		idchannels = 0;
		idtshark = 0;
		idap = 0;
		setStatus("");
		config = new Config();
		listenerthread = null;
		removerthread = null;
		active = false;
		aps = new HashMap<String,DispositivoABS>();
		clients = new HashMap<String,DispositivoABS>();
		remover = new Remover(aps,clients);
		listener = new Listener(this);
		readChannels();
	}

	private void readChannels() {
		allowedchannels = new Vector<String>();
		String command[] = {"bash","./scripts/get_channels.sh",card};
		int idtask = taskManager.start(command,null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		String line = null;
		try {
			while ( (line=buff.readLine()) != null) {
				if (line.startsWith("0"))
					line = line.substring(1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] channels = line.split(" ");
		for (int i = 0; i < channels.length; i++) {
			allowedchannels.add(channels[i]);
		}
	}

	// Start
	public synchronized void start() {
		if (!active) {
			setStatus("");
			if (toMonitor()) {					
				startTshark();
				if (config.fakeap)
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
			stopTshark();
			if (config.fakeap)
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
		String channelsiteration = "";
		for (String channel : config.channels)
			channelsiteration += channel + " ";
		String commandchannels[] = {"bash","./scripts/change_channels.sh", card, channelsiteration};
		// Lanzo a ejecutar tshark
		idtshark = taskManager.start(commandtshark,null);
		// Lanzo a ejecutar el cambio de canales
		idchannels = taskManager.start(commandchannels, null);
		// Thread escucha de tshark
		listener.setInputStream(taskManager.getInputStream(idtshark));
		listenerthread = new Thread(listener);
		listenerthread.start();
		// Thread removedor
		removerthread = new Thread(remover);
		removerthread.start();
		// Thread parser para enviar info al servidor
		parser = new Parser(config);
		parserthread = new Thread(parser);
		parserthread.start();
	}
	
	private void startFakeAP() {
		String command[] = {"bash","./scripts/fake_ap.sh", card};
		idap = taskManager.start(command, null);
	}
	
	private void stopFakeAP() {
		taskManager.stop(idap);
		idap = 0;
	}
	
	private void stopTshark() {
		taskManager.stop(idchannels);
		taskManager.stop(idtshark);
		if (listenerthread != null) {
			listenerthread.interrupt();
			listenerthread = null;
		}
		if (removerthread != null) {
			removerthread.interrupt();
			removerthread = null;
		}
		if (parserthread != null) {
			parserthread.interrupt();
			parserthread = null;
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
		if (!active)
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
		if (allowedtypes.contains(packet.type)) {
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
		return allowedchannels;
	}

	public void setAllowedchannels(Vector<String> allowedchannels) {
		this.allowedchannels = allowedchannels;
	}
}
