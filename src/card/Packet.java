package card;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	public static final String DATAFRAME = "0x20";
	public static final String ENERGYFRAME = "0x24";
	public static final String BROADCAST = "ff:ff:ff:ff:ff:ff";

	//Mecanismo para parsear fechas
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss.SSS");

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
		time = getDate(parseado[0]);
		power = 0;
		essid = "";
		// Intento obtener los datos del paquete
		try {
			type = parseado[1];
			origen = parseado[2].replaceAll(":", "").toUpperCase();
			destino = parseado[3].replaceAll(":", "").toUpperCase();
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

	public String parsearFecha(){
		return sdf.format(time);
	}

	private Date getDate(String fecha){
		Date salida = null;

		fecha = fecha.substring(0, fecha.length() - 6);
		try {
			salida = sdf2.parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return salida;
	}


}
