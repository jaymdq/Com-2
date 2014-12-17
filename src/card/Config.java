package card;

import java.util.Vector;

public class Config {

	public boolean sendap;
	public boolean sendall;
	public boolean fakeap;
	public boolean onlyap;
	public int delaymac;
	public int delaysend;
	public String serverip;
	public int idscanner;
	public String serverstatus;
	public String lastsend;
	public Vector<String> channels;
	
	public Config() {
		sendap = false;
		sendall = false;
		fakeap = false;
		onlyap = false;
		delaymac = 0;
		delaysend = 0;
		serverip = "190.19.175.174";
		idscanner = 14;
		lastsend = "-";
		serverstatus = "N/C";
		channels = new Vector<String>();
	}

}