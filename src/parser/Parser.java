package parser;

import java.text.SimpleDateFormat;
import java.util.Vector;

import card.Packet;

public class Parser implements Runnable{

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	private Vector<Packet> tosend;
	private String idscanner;
	private int delay;

	public Parser(String idScanner,int segundos){
		this.tosend = new Vector<Packet>();
		this.idscanner = idScanner;
		this.delay = segundos;
	}


	public void addLinea(Packet paquete){
		synchronized (tosend){
			tosend.add(paquete);
		}
	}

	// Convierte un paquete en una linea de string para enviar al servidor				
	private String toLine(Packet paquete){
		String salida = "";
		salida += idscanner + "|" ;
		salida += sdf.format(paquete.time) + "|" ;
		salida += paquete.origen + "|" ;
		salida += paquete.destino + "|" ;
		salida += paquete.power + "|";
		salida += paquete.getName() + "|";
		salida += "DISPOSITIVO" + "|";		//TODO tendria que averiguarse a traves de la MAC
		salida += "W" + "|";
		if ( paquete.essid.equals("") )
			salida += "BROADCAST";
		else
			salida += paquete.essid;

		System.out.println(salida);

		return salida;
	}

	@Override
	public void run() {

		while (true){

			try {
				Thread.sleep(delay);
			} catch (InterruptedException e1) {}

			synchronized (tosend) {
				if (! tosend.isEmpty()){
					String toinsert = "";
					//Proceso los paquetes y los convierto en lineas de string				
					for (Packet paquete: tosend)
						toinsert += toLine(paquete) + ";";
					//Sacar ultimo ";"
					toinsert= toinsert.substring(0, toinsert.length() - 1);

					System.out.println("Resultado: \n" + toinsert);

					//Se manda lo obtenido
					String resultado =  EnviaDatos.getInstancia().enviarDatos(toinsert);
					if (resultado.toLowerCase().contains("ok")) {
						// TODO indicar a la tarjeta que el status del sv es online
						tosend.clear();
					}else{
						// TODO indicar a la tarjeta que el status del sv es offline
					}
				}
			}
		}

	}



}
