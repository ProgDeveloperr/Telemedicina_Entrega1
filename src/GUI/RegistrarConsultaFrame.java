package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import BLL.Consulta;
import BLL.Profesional;
import BLL.Turno;
import BLL.Usuario;
import DLL.ControllerConsulta;
import DLL.ControllerProfesional;
import DLL.ControllerTurno;

public class RegistrarConsultaFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private DashboardProfesionalFrame dashboard;
	private Usuario usuario;

	private ControllerProfesional controllerProfesional;
	private ControllerTurno controllerTurno;
	private ControllerConsulta controllerConsulta;

	private Profesional profesional;

	private JTable tablaTurnos;
	private DefaultTableModel modeloTabla;

	private JTextArea campoObservaciones;
	private JTextArea campoSeguimiento;

	private JButton botonRegistrar;

	private Turno turnoSeleccionado;

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern("HH:mm");

	public RegistrarConsultaFrame(
			DashboardProfesionalFrame dashboard,
			Usuario usuario) {

		this.dashboard = dashboard;
		this.usuario = usuario;

		controllerProfesional =
				new ControllerProfesional();

		controllerTurno =
				new ControllerTurno();

		controllerConsulta =
				new ControllerConsulta();

		profesional =
				controllerProfesional.obtenerPorUsuario(
						usuario.getIdUsuario()
				);

		if (profesional == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se pudieron obtener los datos del profesional.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);

			dispose();
			dashboard.setVisible(true);

			return;
		}

		configurarVentana();
		inicializarComponentes();
		cargarTurnosPendientes();
	}

	private void configurarVentana() {

		setTitle(
				"Telemedicina - Registrar consulta - "
						+ usuario.getNombre()
						+ " "
						+ usuario.getApellido()
		);

		setDefaultCloseOperation(
				JFrame.DO_NOTHING_ON_CLOSE
		);

		setSize(1000, 650);

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
						"REGISTRAR CONSULTA REALIZADA",
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

		crearTabla();

		add(
				new JScrollPane(
						tablaTurnos
				),
				BorderLayout.CENTER
		);

		add(
				crearPanelFormulario(),
				BorderLayout.EAST
		);

		add(
				crearPanelInferior(),
				BorderLayout.SOUTH
		);
	}

	private void crearTabla() {

		String[] columnas = {
				"ID turno",
				"Paciente",
				"DNI",
				"Fecha",
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

		tablaTurnos =
				new JTable(
						modeloTabla
				);

		tablaTurnos.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION
		);

		tablaTurnos
				.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								cargarTurnoSeleccionado();
							}
						}
				);
	}

	private JPanel crearPanelFormulario() {

		JPanel panel =
				new JPanel(
						new BorderLayout(
								5,
								10
						)
				);

		panel.setBorder(
				BorderFactory.createTitledBorder(
						"Datos de la consulta"
				)
		);

		JPanel panelCampos =
				new JPanel(
						new GridLayout(
								4,
								1,
								5,
								5
						)
				);

		panelCampos.add(
				new JLabel(
						"Observaciones:"
				)
		);

		campoObservaciones =
				new JTextArea(
						6,
						25
				);

		campoObservaciones.setLineWrap(true);
		campoObservaciones.setWrapStyleWord(true);

		panelCampos.add(
				new JScrollPane(
						campoObservaciones
				)
		);

		panelCampos.add(
				new JLabel(
						"Seguimiento (opcional):"
				)
		);

		campoSeguimiento =
				new JTextArea(
						5,
						25
				);

		campoSeguimiento.setLineWrap(true);
		campoSeguimiento.setWrapStyleWord(true);

		panelCampos.add(
				new JScrollPane(
						campoSeguimiento
				)
		);

		panel.add(
				panelCampos,
				BorderLayout.CENTER
		);

		botonRegistrar =
				new JButton(
						"Registrar consulta"
				);

		botonRegistrar.setEnabled(false);

		botonRegistrar.addActionListener(
				e -> registrarConsulta()
		);

		panel.add(
				botonRegistrar,
				BorderLayout.SOUTH
		);

		return panel;
	}

	private JPanel crearPanelInferior() {

		JPanel panel =
				new JPanel();

		JButton botonActualizar =
				new JButton(
						"Actualizar lista"
				);

		JButton botonVolver =
				new JButton(
						"Volver"
				);

		panel.add(botonActualizar);
		panel.add(botonVolver);

		botonActualizar.addActionListener(
				e -> cargarTurnosPendientes()
		);

		botonVolver.addActionListener(
				e -> volverDashboard()
		);

		return panel;
	}

	private void cargarTurnosPendientes() {

		modeloTabla.setRowCount(0);

		LinkedList<Turno> turnos =
				controllerTurno
						.obtenerPorProfesional(
								profesional
										.getIdProfesional()
						);

		for (Turno turno : turnos) {

			if (turno.estaPresente()
					&& !controllerConsulta
							.existePorTurno(
									turno.getIdTurno()
							)) {

				modeloTabla.addRow(
						new Object[] {
								turno.getIdTurno(),

								turno
										.getPaciente()
										.getNombre()
										+ " "
										+ turno
												.getPaciente()
												.getApellido(),

								turno
										.getPaciente()
										.getDni(),

								turno
										.getFecha()
										.format(
												formatoFecha
										),

								turno
										.getHora()
										.format(
												formatoHora
										)
						}
				);
			}
		}

		turnoSeleccionado = null;

		tablaTurnos.clearSelection();

		campoObservaciones.setText("");
		campoSeguimiento.setText("");

		botonRegistrar.setEnabled(false);
	}

	private void cargarTurnoSeleccionado() {

		int fila =
				tablaTurnos
						.getSelectedRow();

		if (fila < 0) {

			turnoSeleccionado = null;

			botonRegistrar.setEnabled(false);

			return;
		}

		int idTurno =
				(int) modeloTabla
						.getValueAt(
								fila,
								0
						);

		Turno turno =
				controllerTurno
						.buscarPorId(
								idTurno
						);

		if (turno == null
				|| !turno.estaPresente()
				|| controllerConsulta
						.existePorTurno(
								idTurno
						)) {

			turnoSeleccionado = null;

			botonRegistrar.setEnabled(false);

			return;
		}

		turnoSeleccionado = turno;

		botonRegistrar.setEnabled(true);
	}

	private void registrarConsulta() {

		if (turnoSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un turno de la tabla.",
					"Turno no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		String observaciones =
				campoObservaciones
						.getText()
						.trim();

		String seguimiento =
				campoSeguimiento
						.getText()
						.trim();

		if (observaciones.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"Las observaciones son obligatorias.",
					"Datos incompletos",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (seguimiento.isEmpty()) {

			seguimiento = null;
		}

		Consulta consultaNueva =
				new Consulta(
						0,
						LocalDateTime.now(),
						observaciones,
						seguimiento,
						turnoSeleccionado
				);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea registrar esta consulta?\n\n"
								+ "Paciente: "
								+ turnoSeleccionado
										.getPaciente()
										.getNombre()
								+ " "
								+ turnoSeleccionado
										.getPaciente()
										.getApellido()

								+ "\nDNI: "
								+ turnoSeleccionado
										.getPaciente()
										.getDni()

								+ "\nFecha del turno: "
								+ turnoSeleccionado
										.getFecha()
										.format(
												formatoFecha
										)

								+ "\nHora: "
								+ turnoSeleccionado
										.getHora()
										.format(
												formatoHora
										)

								+ "\n\nObservaciones: "
								+ observaciones

								+ "\nSeguimiento: "
								+ (seguimiento == null
										? "Sin seguimiento"
										: seguimiento),
						"Confirmar consulta",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean registrada =
				controllerConsulta.registrar(
						consultaNueva
				);

		if (registrada) {

			JOptionPane.showMessageDialog(
					this,
					"Consulta registrada correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarTurnosPendientes();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo registrar la consulta.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	private void volverDashboard() {

		dispose();

		dashboard.setVisible(true);
	}
}