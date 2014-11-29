package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InputStreamReader isReader = new InputStreamReader(System.in);
		BufferedReader bufReader = new BufferedReader(isReader);
		String linea = null;
		
		Parser parser = new Parser("14");
		Thread t_parser = new Thread(parser);
		t_parser.start();
		parser.activar();
		
		try {
			while ( (linea=bufReader.readLine()) != null && parser.estaActivo()){
				
				System.out.println(linea);
				
				if (linea.equalsIgnoreCase("terminar"))
					parser.terminar();
				else
					parser.addLinea(linea);
				
			}
			
			System.out.println("Fin del streaming.");
		}
		catch (Exception e) {}
	}
}