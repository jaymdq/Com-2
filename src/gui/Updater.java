package gui;

import javax.swing.JEditorPane;
import javax.swing.JLabel;

import card.Card;

public class Updater implements Runnable {

	private Card card;
	private JLabel lastupdate;
	private JLabel serverstatus;
	private JEditorPane consola;
	
	public Updater(JEditorPane consola, JLabel lastupdate, JLabel serverstatus) {
		this.consola = consola;
		this.lastupdate = lastupdate;
		this.serverstatus = serverstatus;
		card = null;
	}
	
	public synchronized void setCard(Card card) {
		this.card = card;
		update();
	}
	
	public synchronized void update() {
		if (card != null) {
			consola.setText(card.toString());
			serverstatus.setText(card.getConfig().serverstatus);
			lastupdate.setText(card.getConfig().lastsend);
		}
	}
	
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
			update();
		}

	}

}
