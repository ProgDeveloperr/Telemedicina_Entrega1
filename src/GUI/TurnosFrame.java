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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import BLL.Turno;
import DLL.ControllerTurno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import BLL.Disponibilidad;
import BLL.EstadoTurno;
import BLL.Paciente;
import BLL.Profesional;
import BLL.Usuario;

import DLL.ControllerDisponibilidad;
import DLL.ControllerPaciente;
import DLL.ControllerProfesional;

public class TurnosFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private DashboardEmpleadoFrame dashboard;

	private ControllerTurno controllerTurno;

	private JTable tablaTurnos;
	private DefaultTableModel modeloTabla;

	private Turno turnoSeleccionado;

	private JButton botonModificar;
	private JButton botonCancelar;
	private JButton botonConfirmar;
	private JButton botonLlegada;

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern("HH:mm");

	public TurnosFrame(
			DashboardEmpleadoFrame dashboard) {

		this.dashboard = dashboard;

		controllerTurno =
				new ControllerTurno();

		configurarVentana();
		inicializarComponentes();
		cargarTurnos();
	}

	private void configurarVentana() {

		setTitle(
				"Telemedicina - Gestion de turnos"
		);

		setDefaultCloseOperation(
				JFrame.DO_NOTHING_ON_CLOSE
		);

		setSize(1100, 600);

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
						"GESTION DE TURNOS",
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
				crearPanelAcciones(),
				BorderLayout.EAST
		);

		add(
				crearPanelInferior(),
				BorderLayout.SOUTH
		);
	}

	private void crearTabla() {

		String[] columnas = {
				"ID",
				"Paciente",
				"DNI",
				"Fecha",
				"Hora",
				"Estado",
				"Disponibilidad ID"
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
				new JTable(modeloTabla);

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

	private JPanel crearPanelAcciones() {

		JPanel panel =
				new JPanel(
						new GridLayout(
								7,
								1,
								8,
								8
						)
				);

		panel.setBorder(
				BorderFactory.createTitledBorder(
						"Operaciones"
				)
		);

		JButton botonAsignar =
				new JButton(
						"Asignar turno"
				);

		botonModificar =
				new JButton(
						"Modificar turno"
				);

		botonCancelar =
				new JButton(
						"Cancelar turno"
				);

		botonConfirmar =
				new JButton(
						"Confirmar turno"
				);

		botonLlegada =
				new JButton(
						"Registrar llegada"
				);

		JButton botonDetalle =
				new JButton(
						"Ver detalle"
				);

		JButton botonActualizar =
				new JButton(
						"Actualizar lista"
				);

		panel.add(botonAsignar);
		panel.add(botonModificar);
		panel.add(botonCancelar);
		panel.add(botonConfirmar);
		panel.add(botonLlegada);
		panel.add(botonDetalle);
		panel.add(botonActualizar);

		botonAsignar.addActionListener(
				e -> asignarTurno()
		);

		botonModificar.addActionListener(
				e -> modificarTurno()
		);

		botonCancelar.addActionListener(
				e -> cancelarTurno()
		);

		botonConfirmar.addActionListener(
				e -> confirmarTurno()
		);

		botonLlegada.addActionListener(
				e -> registrarLlegada()
		);

		botonDetalle.addActionListener(
				e -> mostrarDetalle()
		);

		botonActualizar.addActionListener(
				e -> cargarTurnos()
		);

		actualizarEstadoBotones();

		return panel;
	}

	private JPanel crearPanelInferior() {

		JPanel panel =
				new JPanel();

		JButton botonVolver =
				new JButton(
						"Volver"
				);

		panel.add(botonVolver);

		botonVolver.addActionListener(
				e -> volverDashboard()
		);

		return panel;
	}

	private void cargarTurnos() {

		modeloTabla.setRowCount(0);

		LinkedList<Turno> turnos =
				controllerTurno.obtenerTodos();

		for (Turno turno : turnos) {

			modeloTabla.addRow(
					new Object[] {
							turno.getIdTurno(),

							turno.getPaciente()
									.getNombre()
									+ " "
									+ turno.getPaciente()
											.getApellido(),

							turno.getPaciente()
									.getDni(),

							turno.getFecha()
									.format(
											formatoFecha
									),

							turno.getHora()
									.format(
											formatoHora
									),

							turno.getEstado(),

							turno.getDisponibilidad()
									.getIdDisponibilidad()
					}
			);
		}

		turnoSeleccionado = null;

		tablaTurnos.clearSelection();

		actualizarEstadoBotones();
	}

	private void cargarTurnoSeleccionado() {

		int fila =
				tablaTurnos.getSelectedRow();

		if (fila < 0) {

			turnoSeleccionado = null;

			actualizarEstadoBotones();

			return;
		}

		int idTurno =
				(int) modeloTabla.getValueAt(
						fila,
						0
				);

		turnoSeleccionado =
				controllerTurno.buscarPorId(
						idTurno
				);

		actualizarEstadoBotones();
	}

	private void actualizarEstadoBotones() {

		boolean seleccionado =
				turnoSeleccionado != null;

		botonModificar.setEnabled(
				seleccionado
				&& !turnoSeleccionado
						.estaCancelado()
				&& !turnoSeleccionado
						.estaPresente()
		);

		botonCancelar.setEnabled(
				seleccionado
				&& !turnoSeleccionado
						.estaCancelado()
				&& !turnoSeleccionado
						.estaPresente()
		);

		botonConfirmar.setEnabled(
				seleccionado
				&& turnoSeleccionado
						.estaPendiente()
		);

		botonLlegada.setEnabled(
				seleccionado
				&& turnoSeleccionado
						.estaConfirmado()
		);
	}

	private void mostrarDetalle() {

		if (turnoSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un turno de la tabla.",
					"Turno no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		JOptionPane.showMessageDialog(
				this,
				"ID turno: "
						+ turnoSeleccionado
								.getIdTurno()

						+ "\nPaciente: "
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

						+ "\nFecha: "
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

						+ "\nEstado: "
						+ turnoSeleccionado
								.getEstado()

						+ "\nDisponibilidad ID: "
						+ turnoSeleccionado
								.getDisponibilidad()
								.getIdDisponibilidad()

						+ "\nHorario disponible: "
						+ turnoSeleccionado
								.getDisponibilidad()
								.getHoraInicio()
								.format(
										formatoHora
								)
						+ " - "
						+ turnoSeleccionado
								.getDisponibilidad()
								.getHoraFin()
								.format(
										formatoHora
								),

				"Telemedicina - Detalle del turno",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	private void mostrarPendiente(
			String operacion) {

		JOptionPane.showMessageDialog(
				this,
				"La operacion "
						+ operacion
						+ " se conectara en el siguiente paso.",
				"Telemedicina",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	private void volverDashboard() {

		dispose();

		dashboard.setVisible(true);
	}
	
	
	
	private void asignarTurno() {

		ControllerPaciente controllerPaciente =
				new ControllerPaciente();

		LinkedList<Paciente> pacientes =
				controllerPaciente.buscar("");

		if (pacientes.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"No hay pacientes registrados.",
					"Sin pacientes",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		StringBuilder listadoPacientes =
				new StringBuilder();

		for (Paciente paciente : pacientes) {

			listadoPacientes
					.append("ID: ")
					.append(paciente.getIdPaciente())
					.append(" | ")
					.append(paciente.getNombre())
					.append(" ")
					.append(paciente.getApellido())
					.append(" | DNI: ")
					.append(paciente.getDni())
					.append("\n");
		}

		String entradaPaciente =
				JOptionPane.showInputDialog(
						this,
						listadoPacientes.toString()
								+ "\nIngrese el ID del paciente:",
						"Telemedicina - Asignar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaPaciente == null) {
			return;
		}

		int idPaciente;

		try {

			idPaciente =
					Integer.parseInt(
							entradaPaciente.trim()
					);

			if (idPaciente <= 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					this,
					"El ID del paciente debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Paciente paciente =
				controllerPaciente.buscarPorId(
						idPaciente
				);

		if (paciente == null) {

			JOptionPane.showMessageDialog(
					this,
					"No se encontro el paciente seleccionado.",
					"Paciente no encontrado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerProfesional controllerProfesional =
				new ControllerProfesional();

		LinkedList<Profesional> profesionales =
				controllerProfesional.buscar("");

		if (profesionales.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"No hay profesionales registrados.",
					"Sin profesionales",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		StringBuilder listadoProfesionales =
				new StringBuilder();

		for (Profesional profesional : profesionales) {

			Usuario usuarioProfesional =
					controllerProfesional
							.obtenerUsuarioPorProfesional(
									profesional
											.getIdProfesional()
							);

			if (usuarioProfesional != null) {

				listadoProfesionales
						.append("ID: ")
						.append(
								profesional
										.getIdProfesional()
						)
						.append(" | ")
						.append(
								usuarioProfesional
										.getNombre()
						)
						.append(" ")
						.append(
								usuarioProfesional
										.getApellido()
						)
						.append(" | ")
						.append(
								profesional
										.getEspecialidad()
						)
						.append("\n");
			}
		}

		String entradaProfesional =
				JOptionPane.showInputDialog(
						this,
						listadoProfesionales.toString()
								+ "\nIngrese el ID del profesional:",
						"Telemedicina - Asignar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaProfesional == null) {
			return;
		}

		int idProfesional;

		try {

			idProfesional =
					Integer.parseInt(
							entradaProfesional.trim()
					);

			if (idProfesional <= 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					this,
					"El ID del profesional debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Profesional profesional =
				controllerProfesional.buscarPorId(
						idProfesional
				);

		if (profesional == null) {

			JOptionPane.showMessageDialog(
					this,
					"No se encontro el profesional seleccionado.",
					"Profesional no encontrado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerDisponibilidad controllerDisponibilidad =
				new ControllerDisponibilidad();

		LinkedList<Disponibilidad> disponibilidades =
				controllerDisponibilidad
						.obtenerPorProfesional(
								idProfesional
						);

		StringBuilder listadoDisponibilidades =
				new StringBuilder();

		for (Disponibilidad disponibilidad : disponibilidades) {

			if (!disponibilidad
					.getFecha()
					.isBefore(LocalDate.now())) {

				listadoDisponibilidades
						.append("ID: ")
						.append(
								disponibilidad
										.getIdDisponibilidad()
						)
						.append(" | ")
						.append(
								disponibilidad
										.getFecha()
										.format(
												formatoFecha
										)
						)
						.append(" | ")
						.append(
								disponibilidad
										.getHoraInicio()
										.format(
												formatoHora
										)
						)
						.append(" - ")
						.append(
								disponibilidad
										.getHoraFin()
										.format(
												formatoHora
										)
						)
						.append("\n");
			}
		}

		if (listadoDisponibilidades.length() == 0) {

			JOptionPane.showMessageDialog(
					this,
					"El profesional no tiene disponibilidades actuales o futuras.",
					"Sin disponibilidad",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		String entradaDisponibilidad =
				JOptionPane.showInputDialog(
						this,
						listadoDisponibilidades.toString()
								+ "\nIngrese el ID de la disponibilidad:",
						"Telemedicina - Asignar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaDisponibilidad == null) {
			return;
		}

		int idDisponibilidad;

		try {

			idDisponibilidad =
					Integer.parseInt(
							entradaDisponibilidad.trim()
					);

			if (idDisponibilidad <= 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					this,
					"El ID de disponibilidad debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Disponibilidad disponibilidad =
				controllerDisponibilidad.buscarPorId(
						idDisponibilidad,
						idProfesional
				);

		if (disponibilidad == null
				|| disponibilidad
						.getFecha()
						.isBefore(LocalDate.now())) {

			JOptionPane.showMessageDialog(
					this,
					"La disponibilidad seleccionada no es valida.",
					"Disponibilidad invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		String horaTexto =
				JOptionPane.showInputDialog(
						this,
						"Disponibilidad seleccionada:\n"
								+ disponibilidad
										.getFecha()
										.format(formatoFecha)
								+ "\n"
								+ disponibilidad
										.getHoraInicio()
										.format(formatoHora)
								+ " - "
								+ disponibilidad
										.getHoraFin()
										.format(formatoHora)
								+ "\n\nIngrese la hora del turno (HH:mm):",
						"Telemedicina - Horario del turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (horaTexto == null) {
			return;
		}

		LocalTime hora;

		try {

			hora =
					LocalTime.parse(
							horaTexto.trim(),
							formatoHora
					);

		} catch (DateTimeParseException e) {

			JOptionPane.showMessageDialog(
					this,
					"La hora debe tener formato HH:mm.",
					"Hora invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!disponibilidad.esHorarioValido(hora)) {

			JOptionPane.showMessageDialog(
					this,
					"El horario ingresado se encuentra fuera de la disponibilidad del profesional.",
					"Horario invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (controllerTurno.horarioOcupado(
				idDisponibilidad,
				hora)) {

			JOptionPane.showMessageDialog(
					this,
					"El horario seleccionado ya se encuentra ocupado.",
					"Horario ocupado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Turno turnoNuevo =
				new Turno(
						0,
						disponibilidad.getFecha(),
						hora,
						EstadoTurno.PENDIENTE,
						paciente,
						disponibilidad
				);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea asignar este turno?\n\n"
								+ "Paciente: "
								+ paciente.getNombre()
								+ " "
								+ paciente.getApellido()
								+ "\nProfesional: "
								+ profesional.getEspecialidad()
								+ "\nFecha: "
								+ disponibilidad
										.getFecha()
										.format(formatoFecha)
								+ "\nHora: "
								+ hora.format(formatoHora),
						"Confirmar turno",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean registrado =
				controllerTurno.registrar(
						turnoNuevo
				);

		if (registrado) {

			JOptionPane.showMessageDialog(
					this,
					"Turno asignado correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarTurnos();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo asignar el turno.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	
	
	private void modificarTurno() {

		if (turnoSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un turno de la tabla.",
					"Turno no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (turnoSeleccionado.estaCancelado()
				|| turnoSeleccionado.estaPresente()) {

			JOptionPane.showMessageDialog(
					this,
					"No se puede modificar un turno cancelado o con llegada registrada.",
					"Turno no modificable",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Turno turnoActual =
				controllerTurno.buscarPorId(
						turnoSeleccionado
								.getIdTurno()
				);

		if (turnoActual == null) {

			JOptionPane.showMessageDialog(
					this,
					"No se encontro el turno seleccionado.",
					"Turno no encontrado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerProfesional controllerProfesional =
				new ControllerProfesional();

		LinkedList<Profesional> profesionales =
				controllerProfesional.buscar("");

		StringBuilder listadoProfesionales =
				new StringBuilder();

		for (Profesional profesional : profesionales) {

			Usuario usuarioProfesional =
					controllerProfesional
							.obtenerUsuarioPorProfesional(
									profesional
											.getIdProfesional()
							);

			if (usuarioProfesional != null) {

				listadoProfesionales
						.append("ID: ")
						.append(
								profesional
										.getIdProfesional()
						)
						.append(" | ")
						.append(
								usuarioProfesional
										.getNombre()
						)
						.append(" ")
						.append(
								usuarioProfesional
										.getApellido()
						)
						.append(" | ")
						.append(
								profesional
										.getEspecialidad()
						)
						.append("\n");
			}
		}

		String entradaProfesional =
				JOptionPane.showInputDialog(
						this,
						"Turno seleccionado:\n"
								+ turnoActual
										.getPaciente()
										.getNombre()
								+ " "
								+ turnoActual
										.getPaciente()
										.getApellido()
								+ "\n"
								+ turnoActual
										.getFecha()
										.format(formatoFecha)
								+ " "
								+ turnoActual
										.getHora()
										.format(formatoHora)
								+ "\n\n"
								+ listadoProfesionales
										.toString()
								+ "\nIngrese el ID del profesional:",
						"Telemedicina - Modificar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaProfesional == null) {
			return;
		}

		int idProfesional;

		try {

			idProfesional =
					Integer.parseInt(
							entradaProfesional.trim()
					);

			if (idProfesional <= 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					this,
					"El ID del profesional debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Profesional profesional =
				controllerProfesional.buscarPorId(
						idProfesional
				);

		if (profesional == null) {

			JOptionPane.showMessageDialog(
					this,
					"No se encontro el profesional seleccionado.",
					"Profesional no encontrado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerDisponibilidad controllerDisponibilidad =
				new ControllerDisponibilidad();

		LinkedList<Disponibilidad> disponibilidades =
				controllerDisponibilidad
						.obtenerPorProfesional(
								idProfesional
						);

		StringBuilder listadoDisponibilidades =
				new StringBuilder();

		for (Disponibilidad disponibilidad : disponibilidades) {

			if (!disponibilidad
					.getFecha()
					.isBefore(LocalDate.now())) {

				listadoDisponibilidades
						.append("ID: ")
						.append(
								disponibilidad
										.getIdDisponibilidad()
						)
						.append(" | ")
						.append(
								disponibilidad
										.getFecha()
										.format(formatoFecha)
						)
						.append(" | ")
						.append(
								disponibilidad
										.getHoraInicio()
										.format(formatoHora)
						)
						.append(" - ")
						.append(
								disponibilidad
										.getHoraFin()
										.format(formatoHora)
						)
						.append("\n");
			}
		}

		if (listadoDisponibilidades.length() == 0) {

			JOptionPane.showMessageDialog(
					this,
					"El profesional no tiene disponibilidades actuales o futuras.",
					"Sin disponibilidad",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		String entradaDisponibilidad =
				JOptionPane.showInputDialog(
						this,
						listadoDisponibilidades.toString()
								+ "\nIngrese el ID de la nueva disponibilidad:",
						"Telemedicina - Modificar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaDisponibilidad == null) {
			return;
		}

		int idDisponibilidad;

		try {

			idDisponibilidad =
					Integer.parseInt(
							entradaDisponibilidad.trim()
					);

			if (idDisponibilidad <= 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					this,
					"El ID de disponibilidad debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Disponibilidad disponibilidad =
				controllerDisponibilidad.buscarPorId(
						idDisponibilidad,
						idProfesional
				);

		if (disponibilidad == null) {

			JOptionPane.showMessageDialog(
					this,
					"La disponibilidad seleccionada no es valida.",
					"Disponibilidad invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		String horaTexto =
				JOptionPane.showInputDialog(
						this,
						"Disponibilidad seleccionada:\n"
								+ disponibilidad
										.getFecha()
										.format(formatoFecha)
								+ "\n"
								+ disponibilidad
										.getHoraInicio()
										.format(formatoHora)
								+ " - "
								+ disponibilidad
										.getHoraFin()
										.format(formatoHora)
								+ "\n\nIngrese la nueva hora (HH:mm):",
						"Telemedicina - Modificar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (horaTexto == null) {
			return;
		}

		LocalTime nuevaHora;

		try {

			nuevaHora =
					LocalTime.parse(
							horaTexto.trim(),
							formatoHora
					);

		} catch (DateTimeParseException e) {

			JOptionPane.showMessageDialog(
					this,
					"La hora debe tener formato HH:mm.",
					"Hora invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!disponibilidad
				.esHorarioValido(nuevaHora)) {

			JOptionPane.showMessageDialog(
					this,
					"El horario ingresado se encuentra fuera de la disponibilidad del profesional.",
					"Horario invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (controllerTurno
				.horarioOcupadoExceptoTurno(
						idDisponibilidad,
						nuevaHora,
						turnoActual.getIdTurno()
				)) {

			JOptionPane.showMessageDialog(
					this,
					"El horario seleccionado ya se encuentra ocupado por otro turno.",
					"Horario ocupado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Turno turnoModificado =
				new Turno(
						turnoActual.getIdTurno(),
						disponibilidad.getFecha(),
						nuevaHora,
						turnoActual.getEstado(),
						turnoActual.getPaciente(),
						disponibilidad
				);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea guardar los cambios?\n\n"
								+ "Paciente: "
								+ turnoActual
										.getPaciente()
										.getNombre()
								+ " "
								+ turnoActual
										.getPaciente()
										.getApellido()
								+ "\nFecha: "
								+ disponibilidad
										.getFecha()
										.format(formatoFecha)
								+ "\nHora: "
								+ nuevaHora
										.format(formatoHora)
								+ "\nEstado: "
								+ turnoActual.getEstado(),
						"Confirmar modificacion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean modificado =
				controllerTurno.modificar(
						turnoModificado
				);

		if (modificado) {

			JOptionPane.showMessageDialog(
					this,
					"Turno modificado correctamente.",
					"Modificacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarTurnos();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo modificar el turno.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	
	
	
	private void cancelarTurno() {

		if (turnoSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un turno de la tabla.",
					"Turno no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (turnoSeleccionado.estaCancelado()
				|| turnoSeleccionado.estaPresente()) {

			JOptionPane.showMessageDialog(
					this,
					"El turno seleccionado no puede cancelarse.",
					"Operacion no permitida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea cancelar este turno?\n\n"
								+ "Paciente: "
								+ turnoSeleccionado
										.getPaciente()
										.getNombre()
								+ " "
								+ turnoSeleccionado
										.getPaciente()
										.getApellido()
								+ "\nFecha: "
								+ turnoSeleccionado
										.getFecha()
										.format(formatoFecha)
								+ "\nHora: "
								+ turnoSeleccionado
										.getHora()
										.format(formatoHora)
								+ "\nEstado actual: "
								+ turnoSeleccionado
										.getEstado(),
						"Confirmar cancelacion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE
				);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean actualizado =
				controllerTurno.actualizarEstado(
						turnoSeleccionado.getIdTurno(),
						EstadoTurno.CANCELADO
				);

		if (actualizado) {

			JOptionPane.showMessageDialog(
					this,
					"Turno cancelado correctamente.",
					"Cancelacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarTurnos();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo cancelar el turno.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	
	
	private void confirmarTurno() {

		if (turnoSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un turno de la tabla.",
					"Turno no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!turnoSeleccionado.estaPendiente()) {

			JOptionPane.showMessageDialog(
					this,
					"Solo se pueden confirmar turnos pendientes.",
					"Operacion no permitida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea confirmar este turno?\n\n"
								+ "Paciente: "
								+ turnoSeleccionado
										.getPaciente()
										.getNombre()
								+ " "
								+ turnoSeleccionado
										.getPaciente()
										.getApellido()
								+ "\nFecha: "
								+ turnoSeleccionado
										.getFecha()
										.format(formatoFecha)
								+ "\nHora: "
								+ turnoSeleccionado
										.getHora()
										.format(formatoHora),
						"Confirmar turno",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean actualizado =
				controllerTurno.actualizarEstado(
						turnoSeleccionado.getIdTurno(),
						EstadoTurno.CONFIRMADO
				);

		if (actualizado) {

			JOptionPane.showMessageDialog(
					this,
					"Turno confirmado correctamente.",
					"Confirmacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarTurnos();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo confirmar el turno.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	
	
	
	private void registrarLlegada() {

		if (turnoSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un turno de la tabla.",
					"Turno no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!turnoSeleccionado.estaConfirmado()) {

			JOptionPane.showMessageDialog(
					this,
					"Solo se puede registrar la llegada de un turno confirmado.",
					"Operacion no permitida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea registrar la llegada del paciente?\n\n"
								+ "Paciente: "
								+ turnoSeleccionado
										.getPaciente()
										.getNombre()
								+ " "
								+ turnoSeleccionado
										.getPaciente()
										.getApellido()
								+ "\nFecha: "
								+ turnoSeleccionado
										.getFecha()
										.format(formatoFecha)
								+ "\nHora: "
								+ turnoSeleccionado
										.getHora()
										.format(formatoHora),
						"Registrar llegada",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean actualizado =
				controllerTurno.actualizarEstado(
						turnoSeleccionado.getIdTurno(),
						EstadoTurno.PRESENTE
				);

		if (actualizado) {

			JOptionPane.showMessageDialog(
					this,
					"Llegada del paciente registrada correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarTurnos();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo registrar la llegada del paciente.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	
	
	
	
	
	
	
	
}