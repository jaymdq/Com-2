package taskmanager;

import java.util.Date;

public class AP {

	public static final String TITLE = "BSSID		PWR		BEACONS		CH	ENC	ESSID";
	public String bssid;
	public int power;
	public int beacons;
	public int channel;
	public String encrypt;
	public String essid;
	public Date last;
	
	// String que iria en la consola
	public String toString() {
		// TODO Aca deberia imprimri los datos del AP de forma tabulada 
		return bssid + power + beacons + channel + encrypt + essid;
	}
	
	public boolean update(String packet) {
		// TODO recibo un nuevo paquete de este ap
		// Actualizo la fecha
		// Si actualizo alguna otra cosa (pwr, beacons, ch, etc.. retorno true, sino false)
		// Si retorno true, luego debo updatear la consola
		return false;
	}
}
