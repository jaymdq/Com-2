package card;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
	public static final String DATAFRAME = "0x20";
	public static final String ENERGYFRAME = "0x24";
	public static final String BROADCAST = "ff:ff:ff:ff:ff:ff";

	//Mecanismo para parsear fechas
	private static final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss.SSS",new Locale("en_US"));

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
			setDate(parseado[0]);
			type = parseado[1];
			origen = parseado[2];
			destino = parseado[3];
			if (parseado.length > 4)
				power = Math.abs(Integer.parseInt(parseado[4]));
			if (parseado.length > 5)
				essid = parseado[5];
		}
		catch (Exception e) {
			// Si hubo un error ignoro el paquete
			type = "";
		}
	}

	// String
	public String toString() {
		String s = "Type: " + type + "\n";
		s = s+ "Origen: " + origen + "\n";
		s = s+ "Destino: " + destino + "\n";
		s = s+ "Essid: " + essid;
		return s;
	}

	public String getName(){
		if (type.equals("0x00")){
			return "ASSOCIATIONREQ";
		}
		if (type.equals("0x01")){
			return "ASSOCIATIONRESP";
		}
		if (type.equals("0x02")){
			return "REASSOCIATIONREQ";
		}
		if (type.equals("0x03")){
			return "REASSOCIATIONRESP";
		}
		if (type.equals("0x04")){
			return "PROBEREQ";
		}
		if (type.equals("0x05")){
			return "PROBERESP";
		}
		if (type.equals("0x08")){
			return "BEACON";
		}
		if (type.equals("0x0a")){
			return "DISOCIATION";
		}
		if (type.equals("0x0b")){
			return "AUTHENTICATION";
		}
		if (type.equals("0x0c")){
			return "DESAUTHENTICATION";
		}
		if (type.equals("0x0d")){
			return "ACTION";
		}
		if (type.equals("0x20")){
			return "DATAFRAME";
		}
		if (type.equals("0x24")){
			return "ENERGYFRAME";
		}

		return "UNKNOWNTYPE";
	}

	private void setDate(String fecha){
		time = null;
		fecha = fecha.substring(fecha.length()-31, fecha.length() - 6);
		try {
			time = sdf.parse(fecha);
		} catch (ParseException e) {}

	}


}
