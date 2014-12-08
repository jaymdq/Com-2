package taskmanager;

import gui.MainWindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;

import javax.swing.JEditorPane;

public class Card extends Observable{


	public class Config {

		public boolean sendAP;
		public boolean sendAll;
		public boolean fakeAp;
		public int timePaq;
		public int timeSend;
		public String serverIP;
		public int idScanner;
		public Boolean serverStatus;	

		public Config() {
			sendAP = false;
			sendAll = false;
			fakeAp = false;
			timePaq = 0;
			timeSend = 0;
			serverIP = "190.19.175.174";
			idScanner = 14;
			serverStatus = null;
		}
	}

	private String card;
	private String monitor;
	private int idChannels;
	private int idTshark;
	private boolean active;
	private Config config;
	private Thread listenerThread;

	public Card(String card) {
		this.card = card;
		this.monitor = null;
		idChannels = 0;
		idTshark = 0;
		active = false;
		setConfig(new Config());
	}

	public void StartStop() {
		if (!active) {
			initCard();
			activateMonitor();
			tshark();
		}
		else {
			listenerThread.interrupt();
			desactivateMonitor();
		}
		active = !active;
	}

	private void initCard() {
		String command[] = {"bash","./scripts/init_card.sh",card};
		int idtask = taskManager.start(command,null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = null;
			while ( (line=buff.readLine()) != null){
				escribirPorConsola(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		taskManager.waitfor(idtask);
	}

	private void activateMonitor() {

		System.out.println(card);

		String command[] = {"bash","./scripts/activate_monitor.sh",card};
		int idtask = taskManager.start(command, null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = null;
			while ( (line=buff.readLine()) != null){
				System.out.println(line);
				if (line.contains(":")){
					escribirPorConsola(line);
					monitor = line.split(": ")[1];
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void tshark() {
		String command[] = {"bash","./scripts/tshark.sh",monitor};
		idTshark = taskManager.start(command,null);
		Listener listener = new Listener(taskManager.getInputStream(idTshark));
		listener.addObserver(MainWindow.consola);
		listenerThread = new Thread(listener);
		listenerThread.start();
		String command2[] = {"bash","./scripts/change_channels.sh"};
		idChannels = taskManager.start(command2, null);
	}

	private void desactivateMonitor() {
		taskManager.stop(idChannels);
		taskManager.stop(idTshark);
		String command[] = {"bash","./scripts/desactivate_monitor.sh",monitor};
		int idtask = taskManager.start(command, null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = null;
			while ( (line=buff.readLine()) != null){
				escribirPorConsola(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Boolean isActive() {
		return active;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	private void escribirPorConsola(String line){
		setChanged();
		notifyObservers(line);
	}
}
