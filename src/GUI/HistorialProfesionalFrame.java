package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import BLL.Consulta;
import BLL.Profesional;
import BLL.Usuario;
import DLL.ControllerConsulta;
import DLL.ControllerProfesional;

public class HistorialProfesionalFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private DashboardProfesionalFrame dashboard;
	private Usuario usuario;

	private ControllerProfesional controllerProfesional;
	private ControllerConsulta controllerConsulta;

	private Profesional profesional;

	private JTable tablaConsultas;
	private DefaultTableModel modeloTabla;

	private JTextArea areaObservaciones;
	private JTextArea areaSeguimiento;

	private JLabel labelResumen;

	private LinkedList<Consulta> consultas;

	private final DateTimeFormatter formatoFechaHora =
			DateTimeFormatter.ofPattern(
					"dd/MM/yyyy HH:mm"
			);

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern(
					"dd/MM/yyyy"
			);

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern(
					"HH:mm"
			);

	public HistorialProfesionalFrame(
			DashboardProfesionalFrame dashboard,
			Usuario usuario) {

		this.dashboard = dashboard;
		this.usuario = usuario;

		controllerProfesional =
				new ControllerProfesional();

		controllerConsulta =
				new ControllerConsulta();

		profesional =
				controllerProfesional.obtenerPorUsuario(
						usuario.getIdUsuario()
				);

		if (profesional == null) {

			javax.swing.JOptionPane.showMessageDialog(
					null,
					"No se pudieron obtener los datos del profesional.",
					"Error",
					javax.swing.JOptionPane.ERROR_MESSAGE
			);

			dispose();
			dashboard.setVisible(true);

			return;
		}

		configurarVentana();
		inicializarComponentes();
		cargarHistorial();
	}

	private void configurarVentana() {

		setTitle(
				"Telemedicina - Historial de consultas - "
						+ usuario.getNombre()
						+ " "
						+ usuario.getApellido()
		);

		setDefaultCloseOperation(
				JFrame.DO_NOTHING_ON_CLOSE
		);

		setSize(1100, 650);

		setLocationRelativeTo(null);

		setResizable(false);

		addWindowListener(
				new java.awt.event.WindowAdapter() {

					@Override
					public void windowClosing(
							java.awt.event.WindowEvent e) {

						volverDashboard();
					}
				}
		);
	}

	private void inicializarComponentes() {

		setLayout(
				new BorderLayout(15, 15)
		);

		((javax.swing.JComponent) getContentPane())
				.setBorder(
						BorderFactory.createEmptyBorder(
								20,
								20,
								20,
								20
						)
				);

		JPanel panelSuperior =
				new JPanel(
						new BorderLayout()
				);

		JLabel titulo =
				new JLabel(
						"HISTORIAL DE CONSULTAS",
						SwingConstants.CENTER
				);

		titulo.setFont(
				new Font(
						"SansSerif",
						Font.BOLD,
						22
				)
		);

		labelResumen =
				new JLabel(
						" ",
						SwingConstants.CENTER
				);

		panelSuperior.add(
				titulo,
				BorderLayout.NORTH
		);

		panelSuperior.add(
				labelResumen,
				BorderLayout.SOUTH
		);

		add(
				panelSuperior,
				BorderLayout.NORTH
		);

		crearTabla();

		JPanel panelCentral =
				new JPanel(
						new BorderLayout(
								10,
								10
						)
				);

		panelCentral.add(
				new JScrollPane(
						tablaConsultas
				),
				BorderLayout.CENTER
		);

		panelCentral.add(
				crearPanelDetalle(),
				BorderLayout.SOUTH
		);

		add(
				panelCentral,
				BorderLayout.CENTER
		);

		JPanel panelInferior =
				new JPanel();

		JButton botonActualizar =
				new JButton(
						"Actualizar historial"
				);

		JButton botonVolver =
				new JButton(
						"Volver"
				);

		panelInferior.add(
				botonActualizar
		);

		panelInferior.add(
				botonVolver
		);

		botonActualizar.addActionListener(
				e -> cargarHistorial()
		);

		botonVolver.addActionListener(
				e -> volverDashboard()
		);

		add(
				panelInferior,
				BorderLayout.SOUTH
		);
	}

	private void crearTabla() {

		String[] columnas = {
				"ID",
				"Paciente",
				"DNI",
				"Fecha atencion",
				"Fecha turno",
				"Hora"
		};

		modeloTabla =
				new DefaultTableModel(
						columnas,
						0
				) {

					private static final long serialVersionUID = 1L;

					@Override
					public boolean isCellEditable(
							int fila,
							int columna) {

						return false;
					}
				};

		tablaConsultas =
				new JTable(
						modeloTabla
				);

		tablaConsultas.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION
		);

		tablaConsultas
				.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								mostrarDetalleSeleccionado();
							}
						}
				);
	}

	private JPanel crearPanelDetalle() {

		JPanel panel =
				new JPanel(
						new GridLayout(
								1,
								2,
								10,
								10
						)
				);

		JPanel panelObservaciones =
				new JPanel(
						new BorderLayout()
				);

		panelObservaciones.setBorder(
				BorderFactory.createTitledBorder(
						"Observaciones"
				)
		);

		areaObservaciones =
				new JTextArea(
						5,
						30
				);

		areaObservaciones.setEditable(false);
		areaObservaciones.setLineWrap(true);
		areaObservaciones.setWrapStyleWord(true);

		panelObservaciones.add(
				new JScrollPane(
						areaObservaciones
				),
				BorderLayout.CENTER
		);

		JPanel panelSeguimiento =
				new JPanel(
						new BorderLayout()
				);

		panelSeguimiento.setBorder(
				BorderFactory.createTitledBorder(
						"Seguimiento"
				)
		);

		areaSeguimiento =
				new JTextArea(
						5,
						30
				);

		areaSeguimiento.setEditable(false);
		areaSeguimiento.setLineWrap(true);
		areaSeguimiento.setWrapStyleWord(true);

		panelSeguimiento.add(
				new JScrollPane(
						areaSeguimiento
				),
				BorderLayout.CENTER
		);

		panel.add(panelObservaciones);
		panel.add(panelSeguimiento);

		return panel;
	}

	private void cargarHistorial() {

		modeloTabla.setRowCount(0);

		areaObservaciones.setText("");
		areaSeguimiento.setText("");

		consultas =
				controllerConsulta
						.obtenerPorProfesional(
								profesional
										.getIdProfesional()
						);

		for (Consulta consulta : consultas) {

			modeloTabla.addRow(
					new Object[] {
							consulta
									.getIdConsulta(),

							consulta
									.getTurno()
									.getPaciente()
									.getNombre()
									+ " "
									+ consulta
											.getTurno()
											.getPaciente()
											.getApellido(),

							consulta
									.getTurno()
									.getPaciente()
									.getDni(),

							consulta
									.getFechaHoraAtencion()
									.format(
											formatoFechaHora
									),

							consulta
									.getTurno()
									.getFecha()
									.format(
											formatoFecha
									),

							consulta
									.getTurno()
									.getHora()
									.format(
											formatoHora
									)
					}
			);
		}

		if (consultas.isEmpty()) {

			labelResumen.setText(
					"No tiene consultas registradas."
			);

		} else {

			labelResumen.setText(
					"Consultas registradas: "
							+ consultas.size()
			);
		}

		tablaConsultas.clearSelection();
	}

	private void mostrarDetalleSeleccionado() {

		int fila =
				tablaConsultas
						.getSelectedRow();

		if (fila < 0) {

			areaObservaciones.setText("");
			areaSeguimiento.setText("");

			return;
		}

		int idConsulta =
				(int) modeloTabla
						.getValueAt(
								fila,
								0
						);

		Consulta consultaSeleccionada = null;

		for (Consulta consulta : consultas) {

			if (consulta.getIdConsulta()
					== idConsulta) {

				consultaSeleccionada =
						consulta;

				break;
			}
		}

		if (consultaSeleccionada == null) {

			areaObservaciones.setText("");
			areaSeguimiento.setText("");

			return;
		}

		areaObservaciones.setText(
				consultaSeleccionada
						.getObservaciones()
		);

		String seguimiento =
				consultaSeleccionada
						.getSeguimiento();

		if (seguimiento == null
				|| seguimiento
						.trim()
						.isEmpty()) {

			seguimiento =
					"Sin seguimiento";
		}

		areaSeguimiento.setText(
				seguimiento
		);
	}

	private void volverDashboard() {

		dispose();

		dashboard.setVisible(true);
	}
}