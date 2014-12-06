package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JFormattedTextField;
import javax.swing.JEditorPane;

import taskmanager.Card;
import taskmanager.taskManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class MainWindow {

	private JFrame frame;
	private JPasswordField passwordRoot;
	private JEditorPane consola;
	private boolean activo;
	private JButton btnStartStop;
	private JComboBox<Object> tarjetasDisponibles;
	private HashMap<String,Card> availableCards;
	private Card selected;
	private JCheckBox chkBoxAP;
	private JCheckBox chkBoxAll;
	private JCheckBox chkBoxFakeAP;
	private JSpinner tiempoEntrePaquetes;
	private JSpinner tiempoEntreEnvios;
	private JFormattedTextField txtServerIP;
	private JSpinner idScanner;
	
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

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Creo ventana
		frame = new JFrame();
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
		consola = new JEditorPane();
		consola.setFont(new Font("Dialog", Font.BOLD, 16));
		consola.setEditable(false);
		scrollPane.setViewportView(consola);
		
		// Creo label de tarjeta de red
		JLabel lblTarjetaDeRed = new JLabel("Tarjeta de red wireless:");
		lblTarjetaDeRed.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblTarjetaDeRed, "2, 2, 3, 1");
		
		// Obtengo todas las tarjetas disponibles
		availableCards = new HashMap<String,Card>();
		for (String card: getCards())
			availableCards.put(card, new Card(card,consola));
		
		// Lista de tarjetas disponibles
		tarjetasDisponibles = new JComboBox<Object>();
		tarjetasDisponibles.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(tarjetasDisponibles, "4, 4, fill, default");
		// Seteo las tarjetas iniciales
		tarjetasDisponibles.setModel(new DefaultComboBoxModel<Object>(availableCards.keySet().toArray()));
		selected = availableCards.get(tarjetasDisponibles.getSelectedItem().toString());
		
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
				for (String card : cards)
					availableCards.put(card, new Card(card,consola));
				tarjetasDisponibles.setModel(new DefaultComboBoxModel<Object>(availableCards.keySet().toArray()));
				selected = availableCards.get(tarjetasDisponibles.getSelectedItem().toString());
			}
		});
		
		// Boton start/stop
		btnStartStop = new JButton("");
		btnStartStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				botonPlayStopClick();
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
		
		// Checkbox de enviar todo
		chkBoxAll = new JCheckBox("Enviar todos los paquetes");
		chkBoxAll.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(chkBoxAll, "2, 14, 3, 1, left, default");
		
		// CheckBox de ap falso
		chkBoxFakeAP = new JCheckBox("Fake Ap");
		chkBoxFakeAP.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(chkBoxFakeAP, "2, 16, 3, 1, left, default");
		
		// Label de tiempo entre paquetes
		JLabel lblTiempoEntre = new JLabel("Tiempo entre paquetes:");
		lblTiempoEntre.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblTiempoEntre, "6, 12");
		// Spinner de tiempo entre paquetes
		tiempoEntrePaquetes = new JSpinner();
		tiempoEntrePaquetes.setModel(new SpinnerNumberModel(0, 0, 32000, 1));
		tiempoEntrePaquetes.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(tiempoEntrePaquetes, "7, 12");
			
		// Label de tiempo entre envios
		JLabel lblTiempoEntre_1 = new JLabel("Tiempo entre envios:");
		lblTiempoEntre_1.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblTiempoEntre_1, "6, 14");
		// Spinner de tiempo entre envios
		tiempoEntreEnvios = new JSpinner();
		tiempoEntreEnvios.setModel(new SpinnerNumberModel(0, 0, 32000, 1));
		tiempoEntreEnvios.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(tiempoEntreEnvios, "7, 14");
		
		// Label de ip de servidor
		JLabel lblServerIp = new JLabel("Server IP:");
		lblServerIp.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblServerIp, "10, 12");
		// IP del servidor
		txtServerIP = new JFormattedTextField(new IPAddressFormatter()	);
		txtServerIP.setColumns(15);
		txtServerIP.setText("190.19.175.174");
		txtServerIP.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(txtServerIP, "12, 12, fill, default");
		
		// Label de estatus del servidor
		JLabel lblServerStatus = new JLabel("Server Status:");
		lblServerStatus.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblServerStatus, "10, 14");
		// Status del servidor
		JLabel txtServerStatus = new JLabel("-");
		txtServerStatus.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(txtServerStatus, "12, 14");
		
		// Label de ultimo update
		JLabel lblltimaActualizacin = new JLabel("Última actualización:");
		lblltimaActualizacin.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblltimaActualizacin, "10, 16");
		// Ultimo update
		JLabel txtUltimaActualizacion = new JLabel("-");
		txtUltimaActualizacion.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(txtUltimaActualizacion, "12, 16");
		
		// Label de id de scanner
		JLabel lblIdscanner = new JLabel("ID-Scanner:");
		lblIdscanner.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblIdscanner, "10, 18");
		// Id de scanner
		idScanner = new JSpinner();
		idScanner.setFont(new Font("Dialog", Font.BOLD, 16));
		idScanner.setModel(new SpinnerNumberModel(14, 0, 32000, 1));
		panePrincipal.add(idScanner, "12, 18");
			
	}

	protected Vector<String> getCards() {
		// TODO
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

	private void botonPlayStopClick() {
		// TODO
		selected.StartStop();
		setPlayBtn(selected.isActive());
	}

	
	private void killProcess() {
		// TODO
		String command[] = {"bash","./scripts/kill_process.sh"};
		int idtask = taskManager.start(command, null);
		taskManager.waitfor(idtask);
	}
	
	public void escribirConsola(String texto){
		consola.setText(consola.getText() + texto + "\n");
		System.out.println(texto);
		//System.out.println(texto);
		//if (texto == "clear")
		//	consola.setText("");
		//else
	}
	
	private void setPlayBtn(boolean active) {
		if (active)
			btnStartStop.setIcon(new ImageIcon(MainWindow.class.getResource("/images/stop.png")));
		else
			btnStartStop.setIcon(new ImageIcon(MainWindow.class.getResource("/images/play.png")));

	}
	
}
