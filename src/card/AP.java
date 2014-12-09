package card;

import java.util.Date;

public class AP {

	public static final String TITLE = "BSSID			PWR		BEACONS		ESSID";
	public String bssid;
	public int pwr;
	public int beacons;
	public int channel;
	public String encrypt;
	public String essid;
	public Date last;
	
	public AP(Packet packet) {
		bssid = packet.origen;
		pwr = packet.pwr;
		beacons = 0;
		essid = "";
		if (packet.type.equals(Packet.BEACON)) {
			if (!packet.essid.equals(""))
				essid = packet.essid;
			else
				essid = "<red oculta>";
			beacons++;
		}
		// TODO channel?
		// TODO encrypt?
		last = packet.time;
	}
	
	// String que iria en la consola
	public String toString() {
		// TODO Aca deberia imprimri los datos del AP de forma tabulada 
		return bssid + "	" + pwr + "		" + beacons + "		" + essid;
	}
	
	public void update(Packet packet) {
		// TODO Deberia verificar las diferencias de tiempo y actualizar el tiempo
		pwr = packet.pwr;
		if (packet.type.equals(Packet.BEACON)) {
			if (essid.equals("")) {
				if (!packet.essid.equals(""))
					essid = packet.essid;
				else
					essid = "<red oculta>";
			}
			beacons++;
		}
	}
}
