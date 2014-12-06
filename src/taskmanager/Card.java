package taskmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JEditorPane;

public class Card {
	

	public class Config {
		
		private boolean sendAP;
		private boolean sendAll;
		private boolean fakeAp;
		private int timePaq;
		private int timeSend;
		private String serverIP;
		private int idScanner;
		private Boolean serverStatus;	
		
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
	private int idChannels;
	private int idTshark;
	private boolean active;
	private Config config;
	private Thread listenerThread;
	private JEditorPane console;
	
	public Card(String card, JEditorPane console) {
		this.card = card;
		idChannels = 0;
		idTshark = 0;
		active = false;
		config = new Config();
		this.console = console;
		
	}
	
	public void StartStop() {
		if (!active) {
			active = true;
			initCard();
			activateMonitor();
			tshark();
		}
		else {
			active = false;
			taskManager.stop(idTshark);
			listenerThread.interrupt();
			desactivateMonitor();
		}
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
			while ( (line=buff.readLine()) != null)
				console.setText(console.getText() + line + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void tshark() {
		String command[] = {"bash","./scripts/tshark.sh",card};
		idTshark = taskManager.start(command,null);
		listenerThread = new Thread(new Listener(taskManager.getInputStream(idTshark),console));
		listenerThread.start();
	}
	
	private void desactivateMonitor() {
		String command[] = {"bash","./scripts/desactivate_monitor.sh",card};
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
}
