package taskmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;

import javax.swing.JEditorPane;

public class Listener extends Observable implements Runnable {

	private InputStream input;
	
	public Listener(InputStream input) {
		this.input = input;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {	}
		
		limpiarConsola();
		escribirPorConsola("Escuchando paquetes..." );
		InputStreamReader inreader = new InputStreamReader(input);
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = null;
			while ( (line=buff.readLine()) != null){
				line = line.replaceAll("\t", "  ");
				escribirPorConsola(line);
			}
		} catch (IOException e) {}
	}

	
	private void escribirPorConsola(String line){
		setChanged();
		notifyObservers(line);
	}
	
	private void limpiarConsola(){
		setChanged();
		notifyObservers();
	}
}
