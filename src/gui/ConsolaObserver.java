package gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JEditorPane;

public class ConsolaObserver implements Observer {

	private JEditorPane consola;

	public ConsolaObserver(JEditorPane consola){
		this.consola = consola;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 != null){
			String text = (String) arg1;
			consola.setText(text);
		}
	}

}
