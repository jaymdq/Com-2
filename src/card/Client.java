package card;

import java.util.Date;
import java.util.Vector;

public class Client {

	public static final String TITLE = "BSSID			STATION			PWR	Packets		Probes";
	public String bssid;
	public int power;
	public String station;
	public int packets;
	public Vector<String> probes;
	public Date last;
	
	
	public Client(String[] parseado) {
		// TODO
		probes = new Vector<String>();
		station = parseado[1];
		bssid = parseado[2];
		power = Integer.parseInt(parseado[3]);
		packets = 1;
	}

	// String que iria en la consola
	public String toString() {
		// TODO Aca deberia imprimri los datos del cliente de forma tabulada 
		return "(not associated)"+ "	" + station + "	" +  power + "	" +  packets + "		" +  probes.toString();
	}
	
	public void update(String[] parseado) {
		// TODO
		power = Integer.parseInt(parseado[3]);
		packets++;
	}

}
