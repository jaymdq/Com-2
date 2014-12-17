
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
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Toolkit;

import javax.swing.GroupLayout;

public class ProbeRadar {

	private JFrame frame;
	private JButton btnStartStop;
	private JComboBox<Object> cardList;
	private HashMap<String, Card> availableCards;
	private Card selected;
	private JCheckBox chkBoxAP;
	private JCheckBox chkBoxAll;
	private JCheckBox chkBoxFakeAP;
	private JCheckBox chkBoxOnlyAP;
	private JSpinner delayPacket;
	private JSpinner delaySend;
	private JTextField txtServerIP;
	private JSpinner idScanner;
	private JLabel txtServerStatus;
	private JLabel txtLastUpdate;
	private Updater updater;
	private JLabel lbldelayPacket;
	private JLabel lblServerIp;
	private JLabel lbldelaySend;
	private JLabel lblIdscanner;
	private JPanel channelsPanel;
	private JToggleButton allChannelsBtn;
	
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
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				ProbeRadar.class.getResource("/images/icon.png")));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				for (String c : availableCards.keySet())
					availableCards.get(c).stop();
			}
		});
		frame.setBounds(100, 100, 1200, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setTitle("Probe Radar");

		// Creo panel
		JPanel principalPanel = new JPanel();
		// Creo grilla del panel
		principalPanel.setLayout(new FormLayout(
			new ColumnSpec[] {
				ColumnSpec.decode("5px"), ColumnSpec.decode("25px"),
				ColumnSpec.decode("35px"), ColumnSpec.decode("275px"),	
				ColumnSpec.decode("75px"),	ColumnSpec.decode("20px"),
				ColumnSpec.decode("185px"), ColumnSpec.decode("15px"),
				ColumnSpec.decode("115px"),	ColumnSpec.decode("55px"),
				ColumnSpec.decode("default:grow"), ColumnSpec.decode("15px"),
				ColumnSpec.decode("165px"), ColumnSpec.decode("25px"),
				ColumnSpec.decode("5px"),
			}, 
			new RowSpec[] {
				RowSpec.decode("5px"),	RowSpec.decode("20px"), 
				RowSpec.decode("5px"),	RowSpec.decode("50px"),	
				RowSpec.decode("15px"),	RowSpec.decode("40px"), 
				RowSpec.decode("5px"), 	RowSpec.decode("40px"), 
				RowSpec.decode("5px"), 	RowSpec.decode("40px"),	
				RowSpec.decode("5px"), 	RowSpec.decode("40px"), 
				RowSpec.decode("15px"), RowSpec.decode("default:grow"),
				RowSpec.decode("5px"), 									
			}
		));
		
		// Agrego panel a la ventana
		frame.getContentPane().add(principalPanel, BorderLayout.CENTER);

		// Creo label de tarjeta de red
		JLabel lblWirelessCard = new JLabel("Tarjeta de red wireless");
		lblWirelessCard.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(lblWirelessCard, "2, 2, 3, 1, center, center");

		// Obtengo todas las tarjetas disponibles
		availableCards = new HashMap<String, Card>();

		for (String cardname: getCards()){ 
			Card card = new Card(cardname);
			availableCards.put(cardname, card); 
		}

		// Lista de tarjetas disponibles
		cardList = new JComboBox<Object>();
		cardList.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(cardList, "4, 4, 1, 1, fill, center");
		// Seteo las tarjetas iniciales
		cardList.setModel(new DefaultComboBoxModel<Object>(availableCards.keySet().toArray()));
		cardList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadCard(cardList.getSelectedItem().toString());
			}
		});

		// Creo boton de actualizar tarjetas
		JButton btnRefresh = new JButton("");
		btnRefresh.setIcon(new ImageIcon(ProbeRadar.class.getResource("/images/refresh.png")));
		btnRefresh.setBorder(BorderFactory.createEmptyBorder());
		btnRefresh.setContentAreaFilled(false);
		principalPanel.add(btnRefresh, "2, 4, 2, 1, center, center");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Vector<String> cards = getCards();
				Set<String> available = availableCards.keySet();
				available.retainAll(cards);
				cards.removeAll(available);
				for (String card : cards) {
					Card tarjeta = new Card(card);
					availableCards.put(card, tarjeta);
				}
				cardList.setModel(new DefaultComboBoxModel<Object>(availableCards.keySet().toArray()));
				selected = availableCards.get(cardList.getSelectedItem().toString());
			}
		});

		// Creo label de canales
		JLabel lblChannels = new JLabel("Canales disponibles");
		lblChannels.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(lblChannels, "7, 2, 4, 1, center, center");

		// Lista de canales
		channelsPanel = new JPanel();
		principalPanel.add(channelsPanel, "7, 4, 4, 1, center, center");
		channelsPanel.setLayout(new BorderLayout(0, 0));

		// Accion de cada boton
		class ChennelClickListener implements ActionListener {
			private JToggleButton button;
			public ChennelClickListener(JToggleButton button) {
				super();
				this.button = button;
			}	
			@Override
			public void actionPerformed(ActionEvent e) {
				clickChannelButton(button);
			}	
		}
		
		// Grupo de botones
		GroupLayout channelsgl = new GroupLayout(channelsPanel);
		GroupLayout.SequentialGroup hgroup = channelsgl.createSequentialGroup();
		GroupLayout.ParallelGroup vgroup = channelsgl.createParallelGroup();
		// Creo los botones
		Component[] aux = new Component[14];
		for (int channel = 1; channel < 15; channel++) {
			JToggleButton btn = new JToggleButton(String.valueOf(channel));
			btn.setMargin(new Insets(2,4,2,4));
			btn.addActionListener(new ChennelClickListener(btn));
			hgroup.addComponent(btn);
			vgroup.addComponent(btn);
			aux[channel-1] = btn;
		}
		allChannelsBtn = new JToggleButton("Todos");
		allChannelsBtn.setMargin(new Insets(2,4,2,4));
		hgroup.addComponent(allChannelsBtn);
		vgroup.addComponent(allChannelsBtn);
		allChannelsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clickAllChannelsButton(allChannelsBtn.isSelected());
			}
		});		
		channelsgl.linkSize(SwingConstants.HORIZONTAL,aux);
		channelsgl.setHorizontalGroup(hgroup);
		channelsgl.setVerticalGroup(vgroup);
		channelsPanel.setLayout(channelsgl);

		// Creo label de inciar/detener
		JLabel lblStartStop = new JLabel("Iniciar/Detener");
		lblStartStop.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(lblStartStop, "13, 2, 2, 1, center, center");
				
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
		btnStartStop.setIcon(new ImageIcon(ProbeRadar.class.getResource("/images/play.png")));
		btnStartStop.setBorder(BorderFactory.createEmptyBorder());
		btnStartStop.setContentAreaFilled(false);
		btnStartStop.setEnabled(false);
		principalPanel.add(btnStartStop, "13, 4, 2, 1, center, center");

		// Separadores
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.LIGHT_GRAY);
		principalPanel.add(separator, "2, 5, 13, 1");
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.LIGHT_GRAY);
		principalPanel.add(separator_1, "2, 13, 13, 1");
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.LIGHT_GRAY);
		separator_2.setOrientation(SwingConstants.VERTICAL);
		principalPanel.add(separator_2, "5, 6, 1, 7, center, fill");
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.LIGHT_GRAY);
		separator_3.setOrientation(SwingConstants.VERTICAL);
		principalPanel.add(separator_3, "10, 6, 1, 7, center, fill");

		// Checkbox de enviar APs
		chkBoxAP = new JCheckBox("Enviar APs");
		chkBoxAP.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(chkBoxAP, "3, 6, 3, 1, left, default");
		chkBoxAP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selected != null)
					selected.getConfig().sendap = chkBoxAP.isSelected();
			}
		});
		
		// Checkbox de enviar todo
		chkBoxAll = new JCheckBox("Enviar todos los paquetes");
		chkBoxAll.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(chkBoxAll, "3, 8, 3, 1, left, default");
		chkBoxAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selected != null)
					selected.getConfig().sendall = chkBoxAll.isSelected();
			}
		});

		// CheckBox de ap falso
		chkBoxFakeAP = new JCheckBox("AP Falso");
		chkBoxFakeAP.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(chkBoxFakeAP, "3, 10, 3, 1, left, default");
		chkBoxFakeAP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selected != null)
					selected.getConfig().fakeap = chkBoxFakeAP.isSelected();
			}
		});
		
		// CheckBox de ap falso
		chkBoxOnlyAP = new JCheckBox("Solo AP Falso");
		chkBoxOnlyAP.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(chkBoxOnlyAP, "3, 12, 3, 1, left, default");
		chkBoxOnlyAP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selected != null)
					selected.getConfig().onlyap = chkBoxOnlyAP.isSelected();
				setMonitorEnabled(!chkBoxOnlyAP.isSelected());
			}
		});
		
		// Label de tiempo entre paquetes
		lbldelayPacket = new JLabel("Tiempo entre paquetes:");
		lbldelayPacket.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(lbldelayPacket, "6, 8, 2, 1, right, center");
		// Spinner de tiempo entre paquetes
		delayPacket = new JSpinner();
		delayPacket.setModel(new SpinnerNumberModel(0, 0, 32000, 1));
		delayPacket.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(delayPacket, "9, 8, left, center");
		delayPacket.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (selected != null)
					selected.getConfig().delaymac = (int) delayPacket
							.getValue() * 1000;
			}
		});

		// Label de tiempo entre envios
		lbldelaySend = new JLabel("Tiempo entre envios:");
		lbldelaySend.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(lbldelaySend, "6, 10, 2, 1, right, center");
		// Spinner de tiempo entre envios
		delaySend = new JSpinner();
		delaySend.setModel(new SpinnerNumberModel(0, 0, 32000, 1));
		delaySend.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(delaySend, "9, 10, left, center");
		delaySend.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (selected != null)
					selected.getConfig().delaysend = (int) delaySend
							.getValue() * 1000;
			}
		});

		// Label de ip de servidor
		lblServerIp = new JLabel("Server IP:");
		lblServerIp.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(lblServerIp, "11, 6, right, center");
		// IP del servidor
		txtServerIP = new JTextField();
		txtServerIP.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				// Aca se le pasa a la clase card la nueva ip

				// Primero se verifica que sea valida
				boolean condicion = validIP(txtServerIP.getText().trim());

				// Si es valido se envia
				if (condicion) {
					txtServerIP.setForeground(Color.BLACK);
					if (selected != null)
						selected.getConfig().serverip = txtServerIP.getText();
					checkCanPlay();
				} else {
					txtServerIP.setForeground(Color.RED);
					btnStartStop.setEnabled(false);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {

				txtServerIP.setForeground(Color.BLACK);
			}
		});
		txtServerIP.setColumns(15);
		txtServerIP.setText("190.19.175.174");
		txtServerIP.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(txtServerIP, "13, 6, center, center");

		// Label de estatus del servidor
		JLabel lblServerStatus = new JLabel("Server Status:");
		lblServerStatus.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(lblServerStatus, "11, 8, right, center");
		// Status del servidor
		txtServerStatus = new JLabel("-");
		txtServerStatus.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(txtServerStatus, "13, 8, center, center");

		// Label de ultimo update
		JLabel lblltimaActualizacin = new JLabel("Última actualización:");
		lblltimaActualizacin.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(lblltimaActualizacin, "11, 10, right, center");
		// Ultimo update
		txtLastUpdate = new JLabel("-");
		txtLastUpdate.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(txtLastUpdate, "13, 10, center, center");

		// Label de id de scanner
		lblIdscanner = new JLabel("ID-Scanner:");
		lblIdscanner.setFont(new Font("Dialog", Font.BOLD, 16));
		principalPanel.add(lblIdscanner, "11, 12, right, center");
		// Id de scanner
		idScanner = new JSpinner();
		idScanner.setFont(new Font("Dialog", Font.BOLD, 16));
		idScanner.setModel(new SpinnerNumberModel(14, 0, 32000, 1));
		principalPanel.add(idScanner, "13, 12, fill, center");
		idScanner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (selected != null)
					selected.getConfig().idscanner = (int) idScanner.getValue();
			}
		});
	
		// Consola
		JScrollPane scrollPane = new JScrollPane();
		principalPanel.add(scrollPane, "2, 14, 13, 1, fill, fill");
		JEditorPane console = new JEditorPane();
		console.setForeground(new Color(0, 128, 0));
		console.setBackground(Color.BLACK);
		console.setFont(new Font("Monospaced", Font.BOLD, 16));
		console.setEditable(false);
		scrollPane.setViewportView(console);
		
		// El actualizador de tarjeta
		updater = new Updater(console, txtLastUpdate, txtServerStatus);
		Thread t = new Thread(updater);
		t.start();

		// Leo la primera tarjeta de la lista
		if (cardList.getSelectedItem() != null)
			loadCard(cardList.getSelectedItem().toString());
		
	}

	private void clickChannelButton(JToggleButton button) {
		if (!button.isSelected()) {
			if (selected != null)
				selected.getConfig().channels.remove(button.getText());
			allChannelsBtn.setSelected(false);
			checkCanPlay();
		}
		else {
			if (selected != null)
				selected.getConfig().channels.add(button.getText());
			if (validIP(txtServerIP.getText()))
				btnStartStop.setEnabled(true);
			checkAllButton();
		}
	}

	private void clickAllChannelsButton(boolean value) {
		for (Component c : channelsPanel.getComponents()) {
			if (c.isEnabled()) {
				JToggleButton btn = (JToggleButton) c;
				btn.setSelected(value);
			}
		}
		btnStartStop.setEnabled(value && validIP(txtServerIP.getText()));
	}

	private void checkAllButton() {
		for (Component c : channelsPanel.getComponents()) {
			JToggleButton btn = (JToggleButton) c;
			if (btn.isEnabled() && !btn.isSelected() && !btn.getText().equals("Todos")) {
				allChannelsBtn.setSelected(false);
				return;
			}
		}
		allChannelsBtn.setSelected(true);
	}

	private void checkCanPlay() {
		if (chkBoxOnlyAP.isSelected()) {
			btnStartStop.setEnabled(true);
			return;
		}
		if (validIP(txtServerIP.getText())) {
			for (Component c : channelsPanel.getComponents()) {
				JToggleButton btn = (JToggleButton) c;
				if (btn.isEnabled() && btn.isSelected()) {
					btnStartStop.setEnabled(true);
					return;
				}
			}
		}
		btnStartStop.setEnabled(false);
	}
	
	// Obtengo las tarjetas disponibles
	protected Vector<String> getCards() {
		String command[] = { "bash", "./scripts/get_cards.sh" };
		int idtask = taskManager.start(command, null);
		InputStreamReader inreader = new InputStreamReader(
				taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		Vector<String> cards = new Vector<String>();
		try {
			String line = null;
			while ((line = buff.readLine()) != null)
				cards.add(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cards;
	}

	// Mata los procesos que puedan molestar
	private void killProcess() {
		String command[] = { "bash", "./scripts/kill_process.sh" };
		int idtask = taskManager.start(command, null);
		taskManager.waitfor(idtask);
	}

	// Setea el boton de play/stop
	private void setPlayBtn(boolean active) {
		if (active)
			btnStartStop.setIcon(new ImageIcon(ProbeRadar.class
					.getResource("/images/stop.png")));
		else
			btnStartStop.setIcon(new ImageIcon(ProbeRadar.class
					.getResource("/images/play.png")));
		setAllEnabled(!active);
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
			chkBoxOnlyAP.setSelected(config.onlyap);
			delayPacket.setValue(config.delaymac / 1000);
			delaySend.setValue(config.delaysend / 1000);
			txtServerIP.setText(config.serverip);
			idScanner.setValue(config.idscanner);
			setPlayBtn(selected.isActive());
			updater.setCard(selected);
			for (Component c : channelsPanel.getComponents()) {
				JToggleButton btn = (JToggleButton) c;
				btn.setEnabled(btn.getText().equals("Todos") || selected.getAllowedchannels().contains(btn.getText()));
				btn.setSelected(selected.getConfig().channels.contains(btn.getText()));
			}
			checkAllButton();
			if (config.onlyap)
				setMonitorEnabled(!config.onlyap);
			checkCanPlay();
		}
	}

	private void loadTypes() {
		String[] command = { "bash", "-c", "cat ./scripts/allowedtypes.txt" };
		int idtask = taskManager.start(command, null);
		InputStreamReader inreader = new InputStreamReader(
				taskManager.getInputStream(idtask));
		BufferedReader buff = new BufferedReader(inreader);
		try {
			String line = null;
			while ((line = buff.readLine()) != null) {
				if (!line.startsWith("#") && line.length() > 3)
					Card.addTypes(line.substring(0, 4));
			}
		} catch (IOException e) {
		}
	}

	private boolean validIP(String ip) {

		try {
			if (ip == null || ip.isEmpty()) {
				return false;
			}

			String[] partes = ip.split("\\.");
			if (partes.length != 4) {
				return false;
			}

			for (String s : partes) {
				int i = Integer.parseInt(s);
				if ((i < 0) || (i > 255)) {
					return false;
				}
			}
			if (ip.endsWith(".")) {
				return false;
			}

			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	private void setMonitorEnabled(boolean value) {
		chkBoxAP.setEnabled(value);
		chkBoxAll.setEnabled(value);
		chkBoxFakeAP.setEnabled(value);
		delayPacket.setEnabled(value);
		delaySend.setEnabled(value);
		txtServerIP.setEnabled(value);
		idScanner.setEnabled(value);
		lbldelayPacket.setEnabled(value);
		lblServerIp.setEnabled(value);
		lbldelaySend.setEnabled(value);
		lblIdscanner.setEnabled(value);
		allChannelsBtn.setEnabled(value);
		for (Component c : channelsPanel.getComponents())
			c.setEnabled(value);
	}
	
	private void setAllEnabled(boolean value) {
		chkBoxAP.setEnabled(value);
		chkBoxAll.setEnabled(value);
		chkBoxFakeAP.setEnabled(value);
		chkBoxOnlyAP.setEnabled(value);
		delayPacket.setEnabled(value);
		delaySend.setEnabled(value);
		txtServerIP.setEnabled(value);
		idScanner.setEnabled(value);
		lbldelayPacket.setEnabled(value);
		lblServerIp.setEnabled(value);
		lbldelaySend.setEnabled(value);
		lblIdscanner.setEnabled(value);
		allChannelsBtn.setEnabled(value);
		for (Component c : channelsPanel.getComponents())
			c.setEnabled(value);
	}

}
