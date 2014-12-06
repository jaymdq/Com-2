package taskmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JEditorPane;

public class Listener implements Runnable {

	private InputStream input;
	public JEditorPane console;
	
	public Listener(InputStream input, JEditorPane console) {
		this.input = input;
		this.console = console;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {	}
		
		console.setText("Escuchando paquetes..." + "\n");
		InputStreamReader inreader = new InputStreamReader(input);
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = null;
			while ( (line=buff.readLine()) != null){
				line = line.replaceAll("\t", "  ");
				console.setText(console.getText() + line + "\n");
			}
		} catch (IOException e) {}
	}

}
