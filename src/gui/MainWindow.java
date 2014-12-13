package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import card.Card;
import card.Config;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JEditorPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import taskmanager.taskManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MainWindow {

	private JFrame frame;
	private JButton btnStartStop;
	private JComboBox<Object> tarjetasDisponibles;
	private HashMap<String,Card> availableCards;
	private Card selected;
	private JCheckBox chkBoxAP;
	private JCheckBox chkBoxAll;
	private JCheckBox chkBoxFakeAP;
	private JSpinner tiempoEntrePaquetes;
	private JSpinner tiempoEntreEnvios;
	private JTextField txtServerIP;
	private JSpinner idScanner;
	private static JLabel txtServerStatus;
	private static JLabel txtUltimaActualizacion;
	private Updater updater;
	private JLabel lblTiempoEntre;
	private JLabel lblServerIp;
	private JLabel lblTiempoEntre_1;
	private JLabel lblIdscanner;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Inicializa la ventana
	public MainWindow() {
		loadTypes();
		initialize();
	}

	private void initialize() {
		// Creo ventana
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				for (String c : availableCards.keySet())
					availableCards.get(c).stop();
			}
		});
		frame.setBounds(100, 100, 1200,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setTitle("Probe Radar");

		// Creo panel con columnas
		JPanel panePrincipal = new JPanel();
		frame.getContentPane().add(panePrincipal, BorderLayout.WEST);
		panePrincipal.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("20px"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("20px"),
				ColumnSpec.decode("250px"),
				ColumnSpec.decode("20px"),
				ColumnSpec.decode("250px"),
				ColumnSpec.decode("75px"),
				ColumnSpec.decode("40px"),
				ColumnSpec.decode("20px"),
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		JScrollPane scrollPane = new JScrollPane();
		panePrincipal.add(scrollPane, "2, 28, 12, 1, fill, fill");

		// Consola
		JEditorPane console = new JEditorPane();
		console.setFont(new Font("Monospaced", Font.BOLD, 16));
		console.setEditable(false);
		scrollPane.setViewportView(console);

		// Creo label de tarjeta de red
		JLabel lblTarjetaDeRed = new JLabel("Tarjeta de red wireless:");
		lblTarjetaDeRed.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblTarjetaDeRed, "2, 2, 3, 1");

		// Obtengo todas las tarjetas disponibles
		availableCards = new HashMap<String,Card>();
		for (String card: getCards()){
			Card tarjeta = new Card(card);
			availableCards.put(card, tarjeta);
		}

		// Lista de tarjetas disponibles
		tarjetasDisponibles = new JComboBox<Object>();
		tarjetasDisponibles.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(tarjetasDisponibles, "4, 4, fill, default");
		// Seteo las tarjetas iniciales
		tarjetasDisponibles.setModel(new DefaultComboBoxModel<Object>(availableCards.keySet().toArray()));
		tarjetasDisponibles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadCard(tarjetasDisponibles.getSelectedItem().toString());
			}
		});

		// Creo boton de actualizar tarjetas
		JButton btnRefresh = new JButton("");
		btnRefresh.setIcon(new ImageIcon(MainWindow.class.getResource("/images/refresh.png")));
		btnRefresh.setBorder(BorderFactory.createEmptyBorder());
		btnRefresh.setContentAreaFilled(false);
		panePrincipal.add(btnRefresh, "2, 4, left, default");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Vector<String> cards = getCards();
				Set<String> available = availableCards.keySet();
				available.retainAll(cards);
				cards.removeAll(available);
				for (String card : cards){
					Card tarjeta = new Card(card);
					availableCards.put(card, tarjeta);
				}
				tarjetasDisponibles.setModel(new DefaultComboBoxModel<Object>(availableCards.keySet().toArray()));
				selected = availableCards.get(tarjetasDisponibles.getSelectedItem().toString());	
			}
		});

		// Boton start/stop
		btnStartStop = new JButton("");
		btnStartStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selected != null) {
					if (selected.isActive())
						selected.stop();
					else	
						selected.start();
					updater.update();
					setPlayBtn(selected.isActive());
				}
			}
		});
		btnStartStop.setFont(new Font("Dialog", Font.BOLD, 16));
		btnStartStop.setIcon(new ImageIcon(MainWindow.class.getResource("/images/play.png")));
		btnStartStop.setBorder(BorderFactory.createEmptyBorder());
		btnStartStop.setContentAreaFilled(false);
		panePrincipal.add(btnStartStop, "12, 2, 1, 3, right, center");

		// Separadores
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.LIGHT_GRAY);
		panePrincipal.add(separator, "2, 8, 12, 1");
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.LIGHT_GRAY);
		panePrincipal.add(separator_1, "2, 24, 12, 1");
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.LIGHT_GRAY);
		separator_2.setOrientation(SwingConstants.VERTICAL);
		panePrincipal.add(separator_2, "5, 9, 1, 15");
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.LIGHT_GRAY);
		separator_3.setOrientation(SwingConstants.VERTICAL);
		panePrincipal.add(separator_3, "8, 9, 1, 15, right, default");

		// Checkbox de enviar APs
		chkBoxAP = new JCheckBox("Enviar APs");
		chkBoxAP.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(chkBoxAP, "2, 12, 3, 1, left, default");
		chkBoxAP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selected != null)
					selected.getConfig().sendap = chkBoxAP.isSelected();
			}
		});
		// Checkbox de enviar todo
		chkBoxAll = new JCheckBox("Enviar todos los paquetes");
		chkBoxAll.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(chkBoxAll, "2, 14, 3, 1, left, default");
		chkBoxAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selected != null)
					selected.getConfig().sendall = chkBoxAll.isSelected();
			}
		});

		// CheckBox de ap falso
		chkBoxFakeAP = new JCheckBox("Fake Ap");
		chkBoxFakeAP.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(chkBoxFakeAP, "2, 16, 3, 1, left, default");
		chkBoxFakeAP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selected != null)
					selected.getConfig().fakeap = chkBoxFakeAP.isSelected();
			}
		});

		// Label de tiempo entre paquetes
		lblTiempoEntre = new JLabel("Tiempo entre paquetes:");
		lblTiempoEntre.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblTiempoEntre, "6, 12");
		// Spinner de tiempo entre paquetes
		tiempoEntrePaquetes = new JSpinner();
		tiempoEntrePaquetes.setModel(new SpinnerNumberModel(0, 0, 32000, 1));
		tiempoEntrePaquetes.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(tiempoEntrePaquetes, "7, 12");
		tiempoEntrePaquetes.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (selected != null)
					selected.getConfig().delaymac = (int) tiempoEntrePaquetes.getValue();
			}
		});

		// Label de tiempo entre envios
		lblTiempoEntre_1 = new JLabel("Tiempo entre envios:");
		lblTiempoEntre_1.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblTiempoEntre_1, "6, 14");
		// Spinner de tiempo entre envios
		tiempoEntreEnvios = new JSpinner();
		tiempoEntreEnvios.setModel(new SpinnerNumberModel(0, 0, 32000, 1));
		tiempoEntreEnvios.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(tiempoEntreEnvios, "7, 14");
		tiempoEntreEnvios.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (selected != null)
					selected.getConfig().delaysend = (int) tiempoEntreEnvios.getValue();
			}
		});

		// Label de ip de servidor
		lblServerIp = new JLabel("Server IP:");
		lblServerIp.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblServerIp, "10, 12");
		// IP del servidor
		txtServerIP = new JTextField();
		txtServerIP.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				//Aca se le pasa a la clase card la nueva ip

				//Primero se verifica que sea valida
				boolean condicion = IPValida(txtServerIP.getText());

				//Si es valido se envia
				if (condicion){
					txtServerIP.setForeground( Color.GREEN );
					if (selected != null)
						selected.getConfig().serverip = txtServerIP.getText();
				}else{
					txtServerIP.setForeground( Color.RED );
				}
			}
			@Override
			public void focusGained(FocusEvent e) {

				txtServerIP.setForeground( Color.BLACK );
			}
		});
		txtServerIP.setColumns(15);
		txtServerIP.setText("190.19.175.174");
		txtServerIP.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(txtServerIP, "12, 12, fill, default");

		// Label de estatus del servidor
		JLabel lblServerStatus = new JLabel("Server Status:");
		lblServerStatus.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblServerStatus, "10, 14");
		// Status del servidor
		txtServerStatus = new JLabel("-");
		txtServerStatus.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(txtServerStatus, "12, 14");

		// Label de ultimo update
		JLabel lblltimaActualizacin = new JLabel("Última actualización:");
		lblltimaActualizacin.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblltimaActualizacin, "10, 16");
		// Ultimo update
		txtUltimaActualizacion = new JLabel("-");
		txtUltimaActualizacion.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(txtUltimaActualizacion, "12, 16");

		// Label de id de scanner
		lblIdscanner = new JLabel("ID-Scanner:");
		lblIdscanner.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblIdscanner, "10, 18");
		// Id de scanner
		idScanner = new JSpinner();
		idScanner.setFont(new Font("Dialog", Font.BOLD, 16));
		idScanner.setModel(new SpinnerNumberModel(14, 0, 32000, 1));
		panePrincipal.add(idScanner, "12, 18");
		idScanner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (selected != null)
					selected.getConfig().idscanner = (int) idScanner.getValue();
			}
		});

		// El actualizador de tarjeta
		updater = new Updater(console,txtUltimaActualizacion,txtServerStatus);
		Thread t = new Thread(updater);
		t.start();

		// Leo la primera tarjeta de la lista
		if (tarjetasDisponibles.getSelectedItem() != null)
			loadCard(tarjetasDisponibles.getSelectedItem().toString());

	}

	// Obtengo las tarjetas disponibles
	protected Vector<String> getCards() {
		String command[] = {"bash","./scripts/get_cards.sh"};
		int idtask = taskManager.start(command, null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		Vector<String> cards = new Vector<String>();
		try {
			String line = null;
			while ( (line=buff.readLine()) != null)
				cards.add(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cards;
	}

	// Mata los procesos que puedan molestar
	// TODO que garcha hacemos con esto?
	private void killProcess() {
		String command[] = {"bash","./scripts/kill_process.sh"};
		int idtask = taskManager.start(command, null);
		taskManager.waitfor(idtask);
	}

	// Setea el boton de play/stop
	private void setPlayBtn(boolean active) {
		if (active)
			btnStartStop.setIcon(new ImageIcon(MainWindow.class.getResource("/images/stop.png")));
		else
			btnStartStop.setIcon(new ImageIcon(MainWindow.class.getResource("/images/play.png")));
		intercambiarHabilitacionDeConfiguracion(!active);
	}

	// Cuando se selecciona otra tarjeta se carga su configuración
	private void loadCard(String newcard) {
		Card card = availableCards.get(newcard);
		if (selected == null | selected != card) {
			selected = card;
			Config config = selected.getConfig();
			chkBoxAP.setSelected(config.sendap);
			chkBoxAll.setSelected(config.sendall);
			chkBoxFakeAP.setSelected(config.fakeap);
			tiempoEntrePaquetes.setValue(config.delaymac);
			tiempoEntreEnvios.setValue(config.delaysend);
			txtServerIP.setText(config.serverip);
			idScanner.setValue(config.idscanner);
			setPlayBtn(selected.isActive());
			updater.setCard(selected);
		}
	}

	// Chequea el status del servidor
	// TODO que garcha hacemos con esto?
	private void probarServidor(){
		String command[] = {"sh", "-c","ping -c 1 "+ txtServerIP.getText() +" | grep \"received\" | cut -d\",\" -f\"3\" | grep -o '[0-9]*'"};
		int idtask = taskManager.start(command,null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = null;
			while ( (line=buff.readLine()) != null)
				if (line.equals("0")){
					//Servidor y conexion buena
					txtServerStatus.setText("Buena");
					txtServerStatus.setForeground( Color.GREEN );
					txtUltimaActualizacion.setText( new SimpleDateFormat("dd-mm-yyyy HH:mm:ss").format(new Date()) );
				}else{
					//Servidor o conexion mala
					txtServerStatus.setText("Mala");
					txtServerStatus.setForeground( Color.RED );
				}

		} catch (IOException e) {
			e.printStackTrace();
		}
		taskManager.waitfor(idtask);
	}

	private void loadTypes() {
		String[] command = {"bash","-c","cat ./scripts/allowedtypes.txt"};
		int idtask = taskManager.start(command, null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = null;
			while ( (line=buff.readLine()) != null) {
				// Si no es una linea en blanco (mas de 3 caracteres) y no empieza con #
				// TODO ver si se puede hacer el line.separator en lugar de mas de 3 caracteres
				if (!line.startsWith("#") && line.length()> 3)
					Card.addTypes(line.substring(0,4));
			}
		} catch (IOException e) {}
	}
	
	private boolean IPValida(String ip){

		try {
			if (ip == null || ip.isEmpty()) {
				return false;
			}

			String[] partes = ip.split( "\\." );
			if ( partes.length != 4 ) {
				return false;
			}

			for ( String s : partes ) {
				int i = Integer.parseInt( s );
				if ( (i < 0) || (i > 255) ) {
					return false;
				}
			}
			if(ip.endsWith(".")) {
				return false;
			}

			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	private void intercambiarHabilitacionDeConfiguracion(boolean valor){
		chkBoxAP.setEnabled(valor);
		chkBoxAll.setEnabled(valor);
		chkBoxFakeAP.setEnabled(valor);
		tiempoEntrePaquetes.setEnabled(valor);
		tiempoEntreEnvios.setEnabled(valor);
		txtServerIP.setEnabled(valor);
		idScanner.setEnabled(valor);
		lblTiempoEntre.setEnabled(valor);
		lblServerIp.setEnabled(valor);
		lblTiempoEntre_1.setEnabled(valor);
		lblIdscanner.setEnabled(valor);
	}
	
	public static void actulizarEstadoIP(boolean valor){
		if (valor){
			txtServerStatus.setText("Buena");
			txtServerStatus.setForeground( Color.GREEN );
			txtUltimaActualizacion.setText( new SimpleDateFormat("dd-mm-yyyy HH:mm:ss").format(new Date()) );
		}else{
			txtServerStatus.setText("Mala");
			txtServerStatus.setForeground( Color.RED );
		}
			
	}
}
