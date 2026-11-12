package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import BLL.Consulta;
import BLL.Disponibilidad;
import BLL.Paciente;
import BLL.Profesional;
import BLL.Usuario;

import DLL.ControllerConsulta;
import DLL.ControllerDisponibilidad;
import DLL.ControllerPaciente;
import DLL.ControllerProfesional;

public class ConsultasInformacionFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private DashboardEmpleadoFrame dashboard;

	private ControllerPaciente controllerPaciente;
	private ControllerConsulta controllerConsulta;
	private ControllerProfesional controllerProfesional;
	private ControllerDisponibilidad controllerDisponibilidad;

	private JTable tablaPacientes;
	private JTable tablaConsultas;
	private JTable tablaRetrasos;

	private DefaultTableModel modeloPacientes;
	private DefaultTableModel modeloConsultas;
	private DefaultTableModel modeloRetrasos;

	private JLabel labelPacienteSeleccionado;

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern("HH:mm");

	private final DateTimeFormatter formatoFechaHora =
			DateTimeFormatter.ofPattern(
					"dd/MM/yyyy HH:mm"
			);

	public ConsultasInformacionFrame(
			DashboardEmpleadoFrame dashboard) {

		this.dashboard = dashboard;

		controllerPaciente =
				new ControllerPaciente();

		controllerConsulta =
				new ControllerConsulta();

		controllerProfesional =
				new ControllerProfesional();

		controllerDisponibilidad =
				new ControllerDisponibilidad();

		configurarVentana();
		inicializarComponentes();

		cargarPacientes();
		cargarRetrasos();
	}

	private void configurarVentana() {

		setTitle(
				"Telemedicina - Consultas e informacion"
		);

		setDefaultCloseOperation(
				JFrame.DO_NOTHING_ON_CLOSE
		);

		setSize(1050, 650);

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

		JLabel titulo =
				new JLabel(
						"CONSULTAS E INFORMACION",
						SwingConstants.CENTER
				);

		titulo.setFont(
				new Font(
						"SansSerif",
						Font.BOLD,
						22
				)
		);

		add(
				titulo,
				BorderLayout.NORTH
		);

		JTabbedPane pestanias =
				new JTabbedPane();

		pestanias.addTab(
				"Historial por paciente",
				crearPanelHistorial()
		);

		pestanias.addTab(
				"Retrasos registrados",
				crearPanelRetrasos()
		);

		add(
				pestanias,
				BorderLayout.CENTER
		);

		JPanel panelInferior =
				new JPanel();

		JButton botonActualizar =
				new JButton(
						"Actualizar informacion"
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
				e -> actualizarTodo()
		);

		botonVolver.addActionListener(
				e -> volverDashboard()
		);

		add(
				panelInferior,
				BorderLayout.SOUTH
		);
	}

	private JPanel crearPanelHistorial() {

		JPanel panel =
				new JPanel(
						new GridLayout(
								2,
								1,
								10,
								10
						)
				);

		crearTablaPacientes();
		crearTablaConsultas();

		JPanel panelPacientes =
				new JPanel(
						new BorderLayout()
				);

		panelPacientes.setBorder(
				BorderFactory.createTitledBorder(
						"Pacientes"
				)
		);

		panelPacientes.add(
				new JScrollPane(
						tablaPacientes
				),
				BorderLayout.CENTER
		);

		JPanel panelConsultas =
				new JPanel(
						new BorderLayout()
				);

		panelConsultas.setBorder(
				BorderFactory.createTitledBorder(
						"Historial de consultas"
				)
		);

		labelPacienteSeleccionado =
				new JLabel(
						"Seleccione un paciente para consultar su historial."
				);

		labelPacienteSeleccionado.setBorder(
				BorderFactory.createEmptyBorder(
						5,
						5,
						8,
						5
				)
		);

		panelConsultas.add(
				labelPacienteSeleccionado,
				BorderLayout.NORTH
		);

		panelConsultas.add(
				new JScrollPane(
						tablaConsultas
				),
				BorderLayout.CENTER
		);

		panel.add(panelPacientes);
		panel.add(panelConsultas);

		return panel;
	}

	private void crearTablaPacientes() {

		String[] columnas = {
				"ID",
				"DNI",
				"Nombre",
				"Apellido"
		};

		modeloPacientes =
				crearModeloNoEditable(
						columnas
				);

		tablaPacientes =
				new JTable(
						modeloPacientes
				);

		tablaPacientes.setSelectionMode(
				ListSelectionModel
						.SINGLE_SELECTION
		);

		tablaPacientes
				.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								cargarHistorialPaciente();
							}
						}
				);
	}

	private void crearTablaConsultas() {

		String[] columnas = {
				"ID",
				"Fecha atencion",
				"Fecha turno",
				"Hora"
		};

		modeloConsultas =
				crearModeloNoEditable(
						columnas
				);

		tablaConsultas =
				new JTable(
						modeloConsultas
				);

		tablaConsultas.setSelectionMode(
				ListSelectionModel
						.SINGLE_SELECTION
		);
	}

	private JPanel crearPanelRetrasos() {

		JPanel panel =
				new JPanel(
						new BorderLayout()
				);

		panel.setBorder(
				BorderFactory.createEmptyBorder(
						10,
						10,
						10,
						10
				)
		);

		String[] columnas = {
				"Profesional",
				"Especialidad",
				"Fecha",
				"Horario",
				"Retraso estimado"
		};

		modeloRetrasos =
				crearModeloNoEditable(
						columnas
				);

		tablaRetrasos =
				new JTable(
						modeloRetrasos
				);

		tablaRetrasos.setSelectionMode(
				ListSelectionModel
						.SINGLE_SELECTION
		);

		panel.add(
				new JScrollPane(
						tablaRetrasos
				),
				BorderLayout.CENTER
		);

		return panel;
	}

	private DefaultTableModel crearModeloNoEditable(
			String[] columnas) {

		return new DefaultTableModel(
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
	}

	private void cargarPacientes() {

		modeloPacientes.setRowCount(0);

		LinkedList<Paciente> pacientes =
				controllerPaciente.buscar("");

		for (Paciente paciente : pacientes) {

			modeloPacientes.addRow(
					new Object[] {
							paciente
									.getIdPaciente(),

							paciente
									.getDni(),

							paciente
									.getNombre(),

							paciente
									.getApellido()
					}
			);
		}

		modeloConsultas.setRowCount(0);

		if (labelPacienteSeleccionado != null) {

			labelPacienteSeleccionado.setText(
					"Seleccione un paciente para consultar su historial."
			);
		}
	}

	private void cargarHistorialPaciente() {

		int fila =
				tablaPacientes
						.getSelectedRow();

		modeloConsultas.setRowCount(0);

		if (fila < 0) {

			labelPacienteSeleccionado.setText(
					"Seleccione un paciente para consultar su historial."
			);

			return;
		}

		int idPaciente =
				(int) modeloPacientes
						.getValueAt(
								fila,
								0
						);

		Paciente paciente =
				controllerPaciente
						.buscarPorId(
								idPaciente
						);

		if (paciente == null) {

			labelPacienteSeleccionado.setText(
					"No se pudo obtener el paciente seleccionado."
			);

			return;
		}

		labelPacienteSeleccionado.setText(
				"Paciente: "
						+ paciente.getNombre()
						+ " "
						+ paciente.getApellido()
						+ " | DNI: "
						+ paciente.getDni()
		);

		LinkedList<Consulta> consultas =
				controllerConsulta
						.obtenerPorPaciente(
								idPaciente
						);

		for (Consulta consulta : consultas) {

			String seguimiento =
					consulta.getSeguimiento();

			if (seguimiento == null
					|| seguimiento
							.trim()
							.isEmpty()) {

				seguimiento =
						"Sin seguimiento";
			}

			modeloConsultas.addRow(
					new Object[] {
							consulta
									.getIdConsulta(),

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
									),

							consulta
									.getObservaciones(),

							seguimiento
					}
			);
		}

		if (consultas.isEmpty()) {

			labelPacienteSeleccionado.setText(
					"Paciente: "
							+ paciente.getNombre()
							+ " "
							+ paciente.getApellido()
							+ " | DNI: "
							+ paciente.getDni()
							+ " | Sin consultas registradas"
			);
		}
	}

	private void cargarRetrasos() {

		modeloRetrasos.setRowCount(0);

		LinkedList<Profesional> profesionales =
				controllerProfesional.buscar("");

		for (Profesional profesional : profesionales) {

			Usuario usuarioProfesional =
					controllerProfesional
							.obtenerUsuarioPorProfesional(
									profesional
											.getIdProfesional()
							);

			if (usuarioProfesional == null) {
				continue;
			}

			LinkedList<Disponibilidad> disponibilidades =
					controllerDisponibilidad
							.obtenerPorProfesional(
									profesional
											.getIdProfesional()
							);

			for (Disponibilidad disponibilidad
					: disponibilidades) {

				if (disponibilidad.tieneRetraso()
						&& !disponibilidad
								.getFecha()
								.isBefore(
										LocalDate.now()
								)) {

					modeloRetrasos.addRow(
							new Object[] {
									usuarioProfesional
											.getNombre()
											+ " "
											+ usuarioProfesional
													.getApellido(),

									profesional
											.getEspecialidad(),

									disponibilidad
											.getFecha()
											.format(
													formatoFecha
											),

									disponibilidad
											.getHoraInicio()
											.format(
													formatoHora
											)
											+ " - "
											+ disponibilidad
													.getHoraFin()
													.format(
															formatoHora
													),

									disponibilidad
											.getRetrasoEstimado()
											+ " minutos"
							}
					);
				}
			}
		}
	}

	private void actualizarTodo() {

		cargarPacientes();
		cargarRetrasos();
	}

	private void volverDashboard() {

		dispose();

		dashboard.setVisible(true);
	}
}