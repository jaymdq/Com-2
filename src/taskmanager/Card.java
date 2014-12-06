package taskmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JEditorPane;

public class Card {
	

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
	private JEditorPane console;
	
	public Card(String card, JEditorPane console) {
		this.card = card;
		this.monitor = null;
		idChannels = 0;
		idTshark = 0;
		active = false;
		setConfig(new Config());
		this.console = console;
		
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
			while ( (line=buff.readLine()) != null)
				console.setText(console.getText() + line + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		taskManager.waitfor(idtask);
	}
	
	private void activateMonitor() {
		String command[] = {"bash","./scripts/activate_monitor.sh",card};
		int idtask = taskManager.start(command, null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = null;
			while ( (line=buff.readLine()) != null){
				console.setText(console.getText() + line + "\n");
				monitor = line.split(": ")[1];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void tshark() {
		String command[] = {"bash","./scripts/tshark.sh",monitor};
		idTshark = taskManager.start(command,null);
		listenerThread = new Thread(new Listener(taskManager.getInputStream(idTshark),console));
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
			while ( (line=buff.readLine()) != null)
				console.setText(console.getText() + line + "\n");
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
}
