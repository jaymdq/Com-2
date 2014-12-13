package parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import card.Packet;

public class Parser implements Runnable{

	private Vector<Packet> listaAParsear;
	private boolean activo;
	private String idScanner;
	private Integer segundos;

	public Parser(String idScanner,Integer segundos){
		this.listaAParsear = new Vector<Packet>();
		this.activo = false;
		this.idScanner = idScanner;
		this.segundos = segundos;
	}


	public void addLinea(Packet paquete){

		synchronized (listaAParsear){
			listaAParsear.add(paquete);
			listaAParsear.notify();
		}

	}

	public void terminar(){
		this.activo = false;
	}

	public void activar(){
		this.activo = true;
	}

	public boolean estaActivo(){
		return this.activo;
	}






	private String parsear(Packet paquete){
		String salida = "";

		salida += idScanner + "|" ;
		salida += paquete.parsearFecha() + "|" ;
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

		this.activo = true;

		while (activo){

			//El parser se duerme
			try {
				Thread.sleep(segundos * 1000);
			} catch (InterruptedException e1) {}

			synchronized (listaAParsear) {

				if (! listaAParsear.isEmpty()){

					String lineaResultante = "";
					//Proceso las lineas existentes				
					for (Packet paquete: listaAParsear){
						lineaResultante += parsear(paquete) + ";";
					}

					//Sacar ultimo ;
					lineaResultante= lineaResultante.substring(0, lineaResultante.length() - 1);

					System.out.println("Resultado: \n" + lineaResultante);

					listaAParsear.clear();

					//Se manda lo obtenido
					String resultado =  EnviaDatos.getInstancia().enviarDatos(lineaResultante);
					if (resultado.toLowerCase().contains("ok")){
						//Se inserto correctamente
						

					}else{
						//No se inserto correctamente


					}
				}
			}
		}

	}



}
