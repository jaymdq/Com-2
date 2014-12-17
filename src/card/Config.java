package card;

import java.util.Vector;

public class Config {

	public boolean sendAP;
	public boolean sendAll;
	public boolean fakeAP;
	public boolean onlyAP;
	public int delayMac;
	public int delaySend;
	public String serverIP;
	public int idScanner;
	public String serverStatus;
	public String lastSend;
	public Vector<String> channels;
	
	public Config() {
		sendAP = false;
		sendAll = false;
		fakeAP = false;
		onlyAP = false;
		delayMac = 0;
		delaySend = 0;
		serverIP = "190.19.175.174";
		idScanner = 14;
		lastSend = "-";
		serverStatus = "N/C";
		channels = new Vector<String>();
	}

}