package card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;

public class Listener extends Observable implements Runnable {

	// Variables propias del listener
	private Card card;
	private InputStream input;
	
	// Constructor
	public Listener(InputStream input, Card card) {
		this.input = input;
		this.card = card;
	}
	
	@Override
	public void run() {
		// Obtengo el input de datos
		InputStreamReader inreader = new InputStreamReader(input);
		BufferedReader buff = new BufferedReader(inreader);
		// Mientras no se cierre el input o me interrumpan
		try {
			String line = null;
			while ( (line=buff.readLine()) != null && ! Thread.interrupted() ){
				// Envio los datos a la tarjeta
				card.listen(line);
			}
		} catch (IOException e) {}
	}

}
