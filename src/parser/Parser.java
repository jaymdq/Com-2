package parser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import card.Config;
import card.Packet;

public class Parser implements Runnable{

	private static final SimpleDateFormat sdftoserver = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",new Locale("en_US"));
	private static final SimpleDateFormat sdftointerface = new SimpleDateFormat("HH:mm:ss");
	private boolean active;
	public static final String ONLINE = "Online";
	public static final String OFFLINE = "Offline";

	private Vector<Packet> toSend;
	private Config config;

	public Parser(Config config){
		this.toSend = new Vector<Packet>();
		this.config = config;
		active = false;
	}

	public void addPacket(Packet packet){
		synchronized (toSend){
			toSend.add(packet);
		}
	}
	
	public void start() {
		active = true;
	}
	
	public void stop() {
		active = false;
	}
	// Convierte un packet en una linea de string para enviar al servidor				
	private String toLine(Packet packet){
		String toreturn = "";
		toreturn += config.idScanner + "|" ;
		toreturn += sdftoserver.format(packet.time) + "|" ;
		toreturn += packet.source.replaceAll(":", "").toUpperCase() + "|" ;
		toreturn += packet.destiny.replaceAll(":", "").toUpperCase() + "|" ;
		toreturn += packet.power + "|";
		toreturn += packet.getName() + "|";
		toreturn += "DISPOSITIVO" + "|";		//TODO brian: tendria que averiguarse a traves de la MAC 
		toreturn += "W" + "|";				// viru: la marca, pero como haces para sacar el SO? eso habria que hacerlo con alguan otra herramienta media chitera
		if ( packet.essid.equals("") )
			toreturn += "BROADCAST";
		else
			toreturn += packet.essid;
		return toreturn;
	}

	@Override
	public void run() {
		
		while (active){

			try {
				Thread.sleep(config.delaySend);
			} catch (InterruptedException e1) {}
			
			synchronized (toSend) {
				if (! toSend.isEmpty()){
					String toinsert = "";
					//Proceso los packets y los convierto en lineas de string				
					for (Packet packet: toSend)
						toinsert += toLine(packet) + ";";
					//Sacar ultimo ";"
					toinsert= toinsert.substring(0, toinsert.length() - 1);

					//Se manda lo obtenido
					String resultado =  EnviaDatos.getInstancia().enviarDatos(toinsert,config.serverIP);
					if (resultado.toLowerCase().contains("ok")) {
						config.serverStatus = ONLINE;
						toSend.clear();
					}else{
						config.serverStatus = OFFLINE;
					}
					config.lastSend = sdftointerface.format(new Date()).toString();
				}
			}
		}

	}



}
