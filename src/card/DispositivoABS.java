package card;

public abstract class DispositivoABS {
	
	// Statics
	public static String TITLE;
	public static String[] TYPES;
	
	// Variables propias de un dispositivo
	protected int power;
	protected int packets;
	protected long last;
	protected String mac;
	protected long delta;
	
	// Constructor
	public DispositivoABS(Packet packet, long delta) {
		last = System.currentTimeMillis();
		mac = packet.origen;
		packets = 1;
		power = packet.power;
		this.delta = delta;
		initialize(packet);
		analyzePacket(packet);
	}
	
	// Verificador de tiempo cumplido
	public boolean needUpdate() {
		long now = System.currentTimeMillis();
		if (now - last > delta) {
			last = now;
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
