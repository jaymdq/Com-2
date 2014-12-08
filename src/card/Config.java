package card;

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