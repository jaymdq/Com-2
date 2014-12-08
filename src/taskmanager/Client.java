package taskmanager;

import java.util.Vector;

public class Client {

	public static final String TITLE = "BSSID		STATION		PWR	PACKETS	PROBES";
	public String bssid;
	public int power;
	public String station;
	public int packets;
	public Vector<String> probes;
	
	// String que iria en la consola
	public String toString() {
		return "";
	}
}
