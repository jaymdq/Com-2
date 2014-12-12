package card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;

public class Listener extends Observable implements Runnable {

	private Card card;
	private InputStream input;
	
	public Listener(InputStream input, Card card) {
		this.input = input;
		this.card = card;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {	}

		InputStreamReader inreader = new InputStreamReader(input);
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = null;
			while ( (line=buff.readLine()) != null && ! Thread.interrupted() ){
				card.listen(line);
			}
		} catch (IOException e) {}
	}

}
