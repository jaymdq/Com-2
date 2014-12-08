package taskmanager;

import java.util.Date;
import java.util.Vector;

public class Client {

	public static final String TITLE = "BSSID		STATION		PWR	PACKETS	PROBES";
	public String bssid;
	public int power;
	public String station;
	public int packets;
	public Vector<String> probes;
	public Date last;
	
	// String que iria en la consola
	public String toString() {
		// TODO Aca deberia imprimri los datos del cliente de forma tabulada 
		return bssid + station + power + packets + probes.toString();
	}
	
	public boolean update(String packet) {
		// TODO recibo un nuevo paquete de este dispositivo
		// Actualizo la fecha
		// Si actualizo alguna otra cosa (pwr, probes, etc.. retorno true, sino false)
		// Si retorno true, luego debo updatear la consola
		return false;
	}
}
