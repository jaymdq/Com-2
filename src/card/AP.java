package card;

public class AP extends DispositivoABS {
	
	// Statics
	public static final String TITLE = "BSSID			PWR		Packets		ESSID";
	public static String[] TYPES = {Packet.BEACON,Packet.ACTION,Packet.ASSOCIATIONRESP,Packet.PROBERESP, Packet.REASSOCIATIONRESP};

	// Variables propias de un AP
	public String essid;
	
	public AP(Packet packet, long delta) {
		super(packet,delta);
	}
	
	// String
	public String toString() {
		return mac + "	" + power + "		" + packets + "		" + essid;
	}

	@Override
	protected void initialize(Packet packet) {
		// Inicio variables propias de un AP
		essid = "";
	}

	@Override
	protected void analyzePacket(Packet packet) {
		// Intento obtener datos a partir del paquete
		switch (packet.type) {
			// Si es un beacon intento obtener el essid
			case (Packet.BEACON) :
				if (!packet.essid.equals(""))
					essid = packet.essid;
				else
					essid = "<red oculta>";
			break;
			// Si es un beacon intento obtener el essid
			case (Packet.PROBERESP) :
				if (!packet.essid.equals(""))
					essid = packet.essid;
				else
					essid = "<red oculta>";
			break;
		}		
	}
}
