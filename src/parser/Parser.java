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

	private Vector<Packet> tosend;
	private Config config;

	public Parser(Config config){
		this.tosend = new Vector<Packet>();
		this.config = config;
	}

	public void addPacket(Packet packet){
		synchronized (tosend){
			tosend.add(packet);
		}
	}

	// Convierte un paquete en una linea de string para enviar al servidor				
	private String toLine(Packet paquete){
		String salida = "";
		salida += config.idscanner + "|" ;
		salida += sdftoserver.format(paquete.time) + "|" ;
		salida += paquete.origen.replaceAll(":", "").toUpperCase() + "|" ;
		salida += paquete.destino.replaceAll(":", "").toUpperCase() + "|" ;
		salida += paquete.power + "|";
		salida += paquete.getName() + "|";
		salida += "DISPOSITIVO" + "|";		//TODO tendria que averiguarse a traves de la MAC
		salida += "W" + "|";
		if ( paquete.essid.equals("") )
			salida += "BROADCAST";
		else
			salida += paquete.essid;
		return salida;
	}

	@Override
	public void run() {
		
		while (true){

			try {
				Thread.sleep(config.delaysend);
			} catch (InterruptedException e1) {}
			
			synchronized (tosend) {
				if (! tosend.isEmpty()){
					String toinsert = "";
					//Proceso los paquetes y los convierto en lineas de string				
					for (Packet paquete: tosend)
						toinsert += toLine(paquete) + ";";
					//Sacar ultimo ";"
					toinsert= toinsert.substring(0, toinsert.length() - 1);

					//Se manda lo obtenido
					String resultado =  EnviaDatos.getInstancia().enviarDatos(toinsert,config.serverip);
					if (resultado.toLowerCase().contains("ok")) {
						config.serverstatus = "Online";
						tosend.clear();
					}else{
						config.serverstatus = "Offline";
					}
					config.lastsend = sdftointerface.format(new Date()).toString();
				}
			}
		}

	}



}
