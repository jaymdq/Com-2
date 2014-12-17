package card;

import java.util.Date;

public abstract class DispositivoABS {
	
	// Variables propias de un dispositivo
	protected int power;
	protected int packets;
	protected Date last;
	protected String mac;
	
	// Constructor
	public DispositivoABS(Packet packet) {
		last = packet.time;
		mac = packet.source;
		packets = 1;
		power = packet.power;
		initialize(packet);
		analyzePacket(packet);
	}
	
	// Verificador de tiempo cumplido
	public boolean needUpdate(long delta) {
		long now = System.currentTimeMillis();
		if (now - last.getTime() > delta) {
			last = new Date(now);
			return true;
		}
		return false;
	}
	
	// Update de nuevo paquete
	public void update(Packet packet) {
		packets++;
		power = packet.power;
		analyzePacket(packet);
	}
	
	// Inicializador de variables extras
	protected abstract void initialize(Packet packet);
	
	// Analizis extra de paquetes
	protected abstract void analyzePacket(Packet packet);
	
}
