

import gui.Updater;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import card.Card;
import card.Config;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageConstants;
import com.alee.managers.language.LanguageManager;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

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
import java.awt.Toolkit;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ProbeRadar {

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
	private JPanel panelCanales;
	private JCheckBox cnTodos;
	private JPanel panelTodosCanales;
	private JToggleButton cn1;
	private JToggleButton cn8;
	private JToggleButton cn9;
	private JToggleButton cn10;
	private JToggleButton cn11;
	private JToggleButton cn12;
	private JToggleButton cn13;
	private JToggleButton cn7;
	private JToggleButton cn6;
	private JToggleButton cn5;
	private JToggleButton cn4;
	private JToggleButton cn3;
	private AbstractButton cn2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//LanguageManager.setDefaultLanguage(LanguageConstants.SPANISH);
					//WebLookAndFeel.install();

					ProbeRadar window = new ProbeRadar();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Inicializa la ventana
	private ProbeRadar() {
		loadTypes();
		initialize();
	}

	private void initialize() {
		// Creo ventana
		frame = new JFrame("Probe Radar");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ProbeRadar.class.getResource("/images/icon.png")));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				for (String c : availableCards.keySet())
					availableCards.get(c).stop();
			}
		});
		frame.setBounds(100, 100, 1200,700);
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
				ColumnSpec.decode("250px:grow"),
				ColumnSpec.decode("75px"),
				ColumnSpec.decode("40px"),
				ColumnSpec.decode("20px"),
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("50px"),
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
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));

		panelCanales = new JPanel();
		panePrincipal.add(panelCanales, "6, 4, 5, 3, fill, fill");
		panelCanales.setLayout(new BorderLayout(0, 0));
		cnTodos = new JCheckBox("Todos los canales");
		cnTodos.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		cambiarTodosLosCanales();
		}
		});
		cnTodos.setSelected(true);
		cnTodos.setFont(new Font("Dialog", Font.BOLD, 16));
		panelCanales.add(cnTodos, BorderLayout.NORTH);
		panelTodosCanales = new JPanel();
		panelCanales.add(panelTodosCanales, BorderLayout.CENTER);
		ActionListener actLis = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		chequearTodosLosCanales();
		}
		};
		cn1 = new JCheckBox("1");
		cn1.addActionListener(actLis);
		cn1.setSelected(true);
		cn2 = new JCheckBox("2");
		cn2.addActionListener(actLis);
		cn2.setSelected(true);
		cn3 = new JCheckBox("3");
		cn3.addActionListener(actLis);
		cn3.setSelected(true);
		cn4 = new JCheckBox("4");
		cn4.addActionListener(actLis);
		cn4.setSelected(true);
		cn5 = new JCheckBox("5");
		cn5.addActionListener(actLis);
		cn5.setSelected(true);
		cn6 = new JCheckBox("6");
		cn6.addActionListener(actLis);
		cn6.setSelected(true);
		cn7 = new JCheckBox("7");
		cn7.addActionListener(actLis);
		cn7.setSelected(true);
		cn8 = new JCheckBox("8");
		cn8.addActionListener(actLis);
		cn8.setSelected(true);
		cn9 = new JCheckBox("9");
		cn9.addActionListener(actLis);
		cn9.setSelected(true);
		cn10 = new JCheckBox("10");
		cn10.addActionListener(actLis);
		cn10.setSelected(true);
		cn11 = new JCheckBox("11");
		cn11.addActionListener(actLis);
		cn11.setSelected(true);
		cn12 = new JCheckBox("12");
		cn12.addActionListener(actLis);
		cn12.setSelected(true);
		cn13 = new JCheckBox("13");
		cn13.addActionListener(actLis);
		cn13.setSelected(true);
		GroupLayout gl_panelTodosCanales = new GroupLayout(panelTodosCanales);
		gl_panelTodosCanales.setHorizontalGroup(
		gl_panelTodosCanales.createParallelGroup(Alignment.LEADING)
		.addGroup(gl_panelTodosCanales.createSequentialGroup()
		.addComponent(cn1)
		.addPreferredGap(ComponentPlacement.UNRELATED)
		.addComponent(cn2)
		.addGap(8)
		.addComponent(cn3)
		.addGap(8)
		.addComponent(cn4)
		.addGap(8)
		.addComponent(cn5)
		.addGap(8)
		.addComponent(cn6)
		.addPreferredGap(ComponentPlacement.UNRELATED)
		.addComponent(cn7)
		.addPreferredGap(ComponentPlacement.UNRELATED)
		.addComponent(cn8)
		.addPreferredGap(ComponentPlacement.UNRELATED)
		.addComponent(cn9)
		.addGap(4)
		.addComponent(cn10)
		.addPreferredGap(ComponentPlacement.UNRELATED)
		.addComponent(cn11)
		.addPreferredGap(ComponentPlacement.RELATED)
		.addComponent(cn12)
		.addPreferredGap(ComponentPlacement.UNRELATED)
		.addComponent(cn13)
		.addContainerGap(42, Short.MAX_VALUE))
		);
		gl_panelTodosCanales.setVerticalGroup(
		gl_panelTodosCanales.createParallelGroup(Alignment.LEADING)
		.addGroup(gl_panelTodosCanales.createSequentialGroup()
		.addContainerGap()
		.addGroup(gl_panelTodosCanales.createParallelGroup(Alignment.BASELINE)
		.addComponent(cn1)
		.addComponent(cn2)
		.addComponent(cn3)
		.addComponent(cn4)
		.addComponent(cn5)
		.addComponent(cn6)
		.addComponent(cn7)
		.addComponent(cn8)
		.addComponent(cn9)
		.addComponent(cn10)
		.addComponent(cn11)
		.addComponent(cn12)
		.addComponent(cn13))
		.addContainerGap(27, Short.MAX_VALUE))
		);
		panelTodosCanales.setLayout(gl_panelTodosCanales);
		JScrollPane scrollPane = new JScrollPane();
		panePrincipal.add(scrollPane, "2, 28, 12, 1, fill, fill");

		// Consola
		JEditorPane console = new JEditorPane();
		console.setForeground(new Color(0, 128, 0));
		console.setBackground(Color.BLACK);
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
		btnRefresh.setIcon(new ImageIcon(ProbeRadar.class.getResource("/images/refresh.png")));
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
					selected.getConfig().channels = obtenerCanalesATratar();
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
		btnStartStop.setIcon(new ImageIcon(ProbeRadar.class.getResource("/images/play.png")));
		btnStartStop.setBorder(BorderFactory.createEmptyBorder());
		btnStartStop.setContentAreaFilled(false);
		btnStartStop.setEnabled( false );
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
					selected.getConfig().delaymac = (int) tiempoEntrePaquetes.getValue()*1000;
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
					selected.getConfig().delaysend = (int) tiempoEntreEnvios.getValue()*1000;
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
				boolean condicion = IPValida(txtServerIP.getText().trim());

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

	private void cambiarTodosLosCanales() {
		boolean condicion = cnTodos.isSelected();
		for (Component c : panelTodosCanales.getComponents()){
			JCheckBox cn = (JCheckBox) c;
			cn.setSelected(condicion);
		}
		btnStartStop.setEnabled( ! obtenerCanalesATratar().isEmpty() );
	}
	
	private void chequearTodosLosCanales(){
		boolean condicion = true;
		
		for (Component c : panelTodosCanales.getComponents()){
			JCheckBox cn = (JCheckBox) c;
			if ( ! cn.isSelected() ){
				condicion = false;
				
			}
		}
		cnTodos.setSelected(condicion);
		btnStartStop.setEnabled( ! obtenerCanalesATratar().isEmpty() );
	}
	
	private void habilitarTodosLosCanales(boolean valor){
		for (Component c : panelTodosCanales.getComponents()){
			if (c instanceof JCheckBox) {
				JCheckBox cn = (JCheckBox) c;
				cn.setEnabled( valor );
			}
		}
	}
	
	private Vector<String> obtenerCanalesATratar(){
		Vector<String> salida = new Vector<String>();
		for (Component c : panelTodosCanales.getComponents()){
			JCheckBox cn = (JCheckBox) c;
			if (cn.isSelected())
				salida.add(cn.getText());
		}
		return salida;
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
	private void killProcess() {
		String command[] = {"bash","./scripts/kill_process.sh"};
		int idtask = taskManager.start(command, null);
		taskManager.waitfor(idtask);
	}

	// Setea el boton de play/stop
	private void setPlayBtn(boolean active) {
		if (active)
			btnStartStop.setIcon(new ImageIcon(ProbeRadar.class.getResource("/images/stop.png")));
		else
			btnStartStop.setIcon(new ImageIcon(ProbeRadar.class.getResource("/images/play.png")));
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
			tiempoEntrePaquetes.setValue(config.delaymac/1000);
			tiempoEntreEnvios.setValue(config.delaysend/1000);
			txtServerIP.setText(config.serverip);
			idScanner.setValue(config.idscanner);
			setPlayBtn(selected.isActive());
			updater.setCard(selected);
			for (Component c : panelTodosCanales.getComponents()){
				if (c instanceof JCheckBox) {
					JCheckBox cn = (JCheckBox) c;
					cn.setSelected(false);
				}
			}
			cnTodos.setSelected(false);
			for (String canal : config.channels){
				JCheckBox cn = (JCheckBox) panelTodosCanales.getComponent(Integer.parseInt(canal));
				cn.setSelected(true);
			}
		}
	}

	private void loadTypes() {
		String[] command = {"bash","-c","cat ./scripts/allowedtypes.txt"};
		int idtask = taskManager.start(command, null);
		InputStreamReader inreader = new InputStreamReader(taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = null;
			while ( (line=buff.readLine()) != null) {
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
		habilitarTodosLosCanales(valor);
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
