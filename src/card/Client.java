package card;

import java.util.Vector;

public class Client extends DispositivoABS{

	// Statics
	public static final String TITLE = "BSSID			STATION			PWR	Packets		Probes";
	private final String NOASSOCIATED = "(not associated)";
	public static String[] TYPES = {Packet.PROBEREQ,Packet.ASSOCIATIONREQ,Packet.REASSOCIATIONREQ,Packet.AUTHENTICATION,Packet.DESAUTHENTICATION, Packet.DISOCIATION,Packet.FLAGSFRAME};

	// Variables propias de un cliente
	public String bssid;
	public Vector<String> probes;
	
	public Client(Packet packet, long delta) {
		super(packet, delta);
	}

	// String
	public String toString() {
		return bssid + "	" + mac + "	" +  power + "	" +  packets + "		" +  probes.toString();
	}

	@Override
	protected void initialize(Packet packet) {
		// Inicio variables propias de un cliente
		probes = new Vector<String>();
		bssid = NOASSOCIATED;		
	}

	@Override
	protected void analyzePacket(Packet packet) {
		// Intento obtener datos a partir del paquete
		switch (packet.type) {
			// Si es un probe request obtengo el essid por el cual preguntó
			case (Packet.PROBEREQ) :
				if (!packet.essid.equals("") && !probes.contains(packet.essid))
					probes.add(packet.essid);
				break;
			// Si es un paquete de disociación seguro ahroa se encuentra descoenctado
			case (Packet.DISOCIATION) :
				bssid = NOASSOCIATED;
				break;
			// Si es un paquete de datos seguro se encuentre conectado
			case (Packet.FLAGSFRAME) :
				bssid = packet.destino;
				break;
		}
	}
}
