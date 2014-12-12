package card;

import java.util.Date;

public class Packet {

	public static final String ASSOCIATIONREQ = "0x00";
	public static final String ASSOCIATIONRESP = "0x01";
	public static final String REASSOCIATIONREQ = "0x02";
	public static final String REASSOCIATIONRESP = "0x03";
	public static final String PROBEREQ = "0x04";
	public static final String PROBERESP = "0x05";
	public static final String BEACON = "0x08";
	public static final String DISOCIATION = "0x0a";
	public static final String AUTHENTICATION = "0x0b";
	public static final String DESAUTHENTICATION = "0x0c";
	public static final String ACTION = "0x0d";
	public static final String FLAGSFRAME = "0x24";
	public static final String BROADCAST = "ff:ff:ff:ff:ff:ff";
	
	public String type = "";
	public String origen = "";
	public String destino = "";
	public Date time;
	public int power = 0;
	public String essid = "";
	
	// Constructor
	public Packet(String[] parseado) {
		// Inicializo las variables
		type = "";
		origen = "";
		destino = "";
		time = null;
		power = 0;
		essid = "";
		// Intento obtener los datos del paquete
		try {
			type = parseado[1];
			origen = parseado[2];
			destino = parseado[3];
			if (parseado.length > 4)
				power = Math.abs(Integer.parseInt(parseado[4]));
			if (parseado.length > 5)
				essid = parseado[5];
		}
		catch (Exception e) {}
	}
	
	// String
	public String toString() {
		String s = "Type: " + type + "\n";
		s = s+ "Origen: " + origen + "\n";
		s = s+ "Destino: " + destino + "\n";
		s = s+ "Essid: " + essid;
		return s;
	}
	
}
