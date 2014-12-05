package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import java.awt.FlowLayout;

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
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JFormattedTextField;
import javax.swing.JEditorPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainWindow {

	private JFrame frame;
	private JPasswordField passwordRoot;
	private JEditorPane consola;
	private boolean activo;
	private JButton btnStartStop;

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
		frame = new JFrame();
		frame.setBounds(100, 100, 1200,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setTitle("Probe Radar");
		
		activo = false;
		
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
		
		JLabel lblTarjetaDeRed = new JLabel("Tarjeta de red wireless:");
		lblTarjetaDeRed.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblTarjetaDeRed, "2, 2, 3, 1");
		
		JLabel lblNewLabel = new JLabel("Password de usuario root:");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblNewLabel, "6, 2");
		
		JButton btnRefresh = new JButton("");
		btnRefresh.setIcon(new ImageIcon(MainWindow.class.getResource("/images/refresh.png")));
		btnRefresh.setBorder(BorderFactory.createEmptyBorder());
		btnRefresh.setContentAreaFilled(false);
		panePrincipal.add(btnRefresh, "2, 4, left, default");
		
		JComboBox tarjetasDisponibles = new JComboBox();
		tarjetasDisponibles.setFont(new Font("Dialog", Font.BOLD, 16));
		tarjetasDisponibles.setModel(new DefaultComboBoxModel(new String[] {"wlan0", "wlan1", "wlan2"}));
		panePrincipal.add(tarjetasDisponibles, "4, 4, fill, default");
		
		passwordRoot = new JPasswordField();
		passwordRoot.setHorizontalAlignment(SwingConstants.LEFT);
		passwordRoot.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(passwordRoot, "6, 4, 2, 1, fill, center");
		
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
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.LIGHT_GRAY);
		panePrincipal.add(separator, "2, 8, 12, 1");
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.LIGHT_GRAY);
		separator_2.setOrientation(SwingConstants.VERTICAL);
		panePrincipal.add(separator_2, "5, 9, 1, 15");
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.LIGHT_GRAY);
		separator_3.setOrientation(SwingConstants.VERTICAL);
		panePrincipal.add(separator_3, "8, 9, 1, 15, right, default");
		
		JCheckBox chkBox1 = new JCheckBox("Enviar APs");
		chkBox1.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(chkBox1, "2, 12, 3, 1, left, default");
		
		JLabel lblTiempoEntre = new JLabel("Tiempo entre paquetes:");
		lblTiempoEntre.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblTiempoEntre, "6, 12");
		
		JSpinner tiempoEntrePaquetes = new JSpinner();
		tiempoEntrePaquetes.setModel(new SpinnerNumberModel(0, 0, 32000, 1));
		tiempoEntrePaquetes.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(tiempoEntrePaquetes, "7, 12");
		
		JLabel lblServerIp = new JLabel("Server IP:");
		lblServerIp.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblServerIp, "10, 12");
		
		JFormattedTextField txtServerIP = new JFormattedTextField(new IPAddressFormatter()	);
		txtServerIP.setColumns(15);
		txtServerIP.setText("190.19.175.174");
		txtServerIP.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(txtServerIP, "12, 12, fill, default");
		
		JCheckBox chkBox2 = new JCheckBox("Enviar todos los paquetes");
		chkBox2.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(chkBox2, "2, 14, 3, 1, left, default");
		
		JLabel lblTiempoEntre_1 = new JLabel("Tiempo entre envios:");
		lblTiempoEntre_1.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblTiempoEntre_1, "6, 14");
		
		JSpinner tiempoEntreEnvios = new JSpinner();
		tiempoEntreEnvios.setModel(new SpinnerNumberModel(0, 0, 32000, 1));
		tiempoEntreEnvios.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(tiempoEntreEnvios, "7, 14");
		
		JLabel lblServerStatus = new JLabel("Server Status:");
		lblServerStatus.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblServerStatus, "10, 14");
		
		JLabel txtServerStatus = new JLabel("-");
		txtServerStatus.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(txtServerStatus, "12, 14");
		
		JCheckBox chkBox3 = new JCheckBox("Fake Ap");
		chkBox3.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(chkBox3, "2, 16, 3, 1, left, default");
		
		JLabel lblltimaActualizacin = new JLabel("Última actualización:");
		lblltimaActualizacin.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblltimaActualizacin, "10, 16");
		
		JLabel txtUltimaActualizacion = new JLabel("-");
		txtUltimaActualizacion.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(txtUltimaActualizacion, "12, 16");
		
		JLabel lblIdscanner = new JLabel("ID-Scanner:");
		lblIdscanner.setFont(new Font("Dialog", Font.BOLD, 16));
		panePrincipal.add(lblIdscanner, "10, 18");
		
		JSpinner idScanner = new JSpinner();
		idScanner.setFont(new Font("Dialog", Font.BOLD, 16));
		idScanner.setModel(new SpinnerNumberModel(14, 0, 32000, 1));
		panePrincipal.add(idScanner, "12, 18");
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.LIGHT_GRAY);
		panePrincipal.add(separator_1, "2, 24, 12, 1");
		
		JScrollPane scrollPane = new JScrollPane();
		panePrincipal.add(scrollPane, "2, 28, 12, 1, fill, fill");
		
		consola = new JEditorPane();
		consola.setFont(new Font("Dialog", Font.BOLD, 16));
		consola.setEditable(false);
		scrollPane.setViewportView(consola);
		
	}
	
	private void botonPlayStopClick() {
		activo = ! activo;
		
		if (activo){
			btnStartStop.setIcon(new ImageIcon(MainWindow.class.getResource("/images/stop.png")));
			
		}else{
			btnStartStop.setIcon(new ImageIcon(MainWindow.class.getResource("/images/play.png")));
			
		}
			
	
		escribirConsola("Rocha gay");
	}

	public void escribirConsola(String texto){
		consola.setText(consola.getText() + texto + "\n");	
	}
	
	
	
}
