package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class Parser implements Runnable{

	//Mecanismo para parsear fechas
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss.SSS");


	private Vector<String> listaAParsear;
	private boolean activo;
	private String idScanner;

	public Parser(String idScanner){
		this.listaAParsear = new Vector<String>();
		this.activo = false;
		this.idScanner = idScanner;


	}


	public void addLinea(String linea){

		synchronized (listaAParsear){
			listaAParsear.add(linea);
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

	private String parsearFecha(String fecha){
		String salida = "";

		fecha = fecha.substring(0, fecha.length() - 6);
		try {
			Date datetime = sdf2.parse(fecha);
			salida = sdf.format(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	
		return salida;
	}




	private String parsear(String linea){
		String salida = "";

		//Se splitea la linea por tabs
		String valores[] = linea.split("\t");

		salida += idScanner + "|" ;
		salida += parsearFecha(valores[0]) + "|" ;
		salida += valores[1].replaceAll(":", "").toUpperCase() + "|" ;
		salida += valores[2].replaceAll(":", "").toUpperCase() + "|" ;
		salida += valores[3].split("-")[1] + "|";
		salida += "probe_req" + "|";
		salida += "DISPOSITIVO" + "|";
		salida += "W" + "|";
		if (valores.length == 5)
			salida += valores[4];
		else
			salida += "BROADCAST"; //Preguntar

		System.out.println(salida);

		return salida;
	}

	@Override
	public void run() {

		this.activo = true;

		while (activo){

			//El Parser se duerme en caso de que la lista se encuentra vacía.
			synchronized (listaAParsear) {
				while (listaAParsear.isEmpty())
					try {
						listaAParsear.wait();
					} catch (InterruptedException e) {}


				String lineaResultante = "";
				//Proceso las lineas existentes				
				for (String linea : listaAParsear){
					lineaResultante += parsear(linea) + ";";
				}

				//Sacar ultimo ;
				lineaResultante= lineaResultante.substring(0, lineaResultante.length() - 1);

				System.out.println("Resultado: \n" + lineaResultante);

				listaAParsear.clear();

				//Se manda lo obtenido
				System.out.println("Resultados servicio: \n" + EnviaDatos.getInstancia().enviarDatos(lineaResultante));
			}
		}

	}



}
