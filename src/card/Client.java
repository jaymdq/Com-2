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
	private final String NOASSOCIATED = "(not associated)";
	
	public Client(Packet packet) {
		probes = new Vector<String>();
		station = packet.origen;
		power = packet.pwr;
		packets = 1;
		last = packet.time;
		bssid = NOASSOCIATED;

		switch (packet.type) {
			case (Packet.PROBEREQ) :
				if (!packet.essid.equals(""))
					probes.add(packet.essid);
				break;
			case (Packet.FLAGSFRAME) :
				bssid = packet.destino;
				break;
		}
	}

	// String que iria en la consola
	public String toString() {
		// TODO Aca deberia detectar si esta o no asociado a una red..
		return bssid + "	" + station + "	" +  power + "	" +  packets + "		" +  probes.toString();
	}
	
	// Nuevo paquete que llega
	public void update(Packet packet) {
		// TODO Deberia verificar las diferencias de tiempo y actualizar el tiempo
		power = packet.pwr;
		packets++;
		
		switch (packet.type) {
			case (Packet.PROBEREQ) :
				if (!packet.essid.equals("") && !probes.contains(packet.essid))
					probes.add(packet.essid);
				break;
			case (Packet.DISOCIATION) :
				bssid = NOASSOCIATED;
				break;
			case (Packet.FLAGSFRAME) :
				bssid = packet.destino;
				break;
		}
	}

}
