package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import BLL.Disponibilidad;
import BLL.Profesional;
import BLL.Usuario;
import DLL.ControllerDisponibilidad;
import DLL.ControllerProfesional;

public class DisponibilidadFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private DashboardProfesionalFrame dashboard;
	private Usuario usuario;

	private ControllerProfesional controllerProfesional;
	private ControllerDisponibilidad controllerDisponibilidad;

	private Profesional profesional;

	private JTable tablaDisponibilidades;
	private DefaultTableModel modeloTabla;

	private JTextField campoFecha;
	private JTextField campoHoraInicio;
	private JTextField campoHoraFin;

	private JButton botonGuardar;
	private JButton botonLimpiar;

	private Disponibilidad disponibilidadSeleccionada;

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern("HH:mm");

	public DisponibilidadFrame(
			DashboardProfesionalFrame dashboard,
			Usuario usuario) {

		this.dashboard = dashboard;
		this.usuario = usuario;

		controllerProfesional =
				new ControllerProfesional();

		controllerDisponibilidad =
				new ControllerDisponibilidad();

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
		cargarDisponibilidades();
	}

	private void configurarVentana() {

		setTitle(
				"Telemedicina - Disponibilidad - "
						+ usuario.getNombre()
						+ " "
						+ usuario.getApellido()
		);

		setDefaultCloseOperation(
				JFrame.DO_NOTHING_ON_CLOSE
		);

		setSize(900, 560);

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
						"GESTION DE DISPONIBILIDAD",
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
						tablaDisponibilidades
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
				"ID",
				"Fecha",
				"Hora inicio",
				"Hora fin",
				"Retraso estimado"
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

		tablaDisponibilidades =
				new JTable(
						modeloTabla
				);

		tablaDisponibilidades.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION
		);

		tablaDisponibilidades
				.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								cargarDisponibilidadSeleccionada();
							}
						}
				);
	}

	private JPanel crearPanelFormulario() {

		JPanel panel =
				new JPanel(
						new GridLayout(
								10,
								1,
								5,
								5
						)
				);

		panel.setBorder(
				BorderFactory.createTitledBorder(
						"Datos de disponibilidad"
				)
		);

		panel.add(
				new JLabel(
						"Fecha (dd/MM/yyyy):"
				)
		);

		campoFecha =
				new JTextField();

		panel.add(campoFecha);

		panel.add(
				new JLabel(
						"Hora inicio (HH:mm):"
				)
		);

		campoHoraInicio =
				new JTextField();

		panel.add(campoHoraInicio);

		panel.add(
				new JLabel(
						"Hora fin (HH:mm):"
				)
		);

		campoHoraFin =
				new JTextField();

		panel.add(campoHoraFin);

		botonGuardar =
				new JButton(
						"Registrar disponibilidad"
				);

		botonLimpiar =
				new JButton(
						"Limpiar / Nueva"
				);

		panel.add(botonGuardar);
		panel.add(botonLimpiar);

		botonGuardar.addActionListener(
				e -> guardarDisponibilidad()
		);

		botonLimpiar.addActionListener(
				e -> limpiarFormulario()
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
				e -> cargarDisponibilidades()
		);

		botonVolver.addActionListener(
				e -> volverDashboard()
		);

		return panel;
	}

	private void cargarDisponibilidades() {

		modeloTabla.setRowCount(0);

		LinkedList<Disponibilidad> disponibilidades =
				controllerDisponibilidad
						.obtenerPorProfesional(
								profesional
										.getIdProfesional()
						);

		for (Disponibilidad disponibilidad
				: disponibilidades) {

			modeloTabla.addRow(
					new Object[] {
							disponibilidad
									.getIdDisponibilidad(),

							disponibilidad
									.getFecha()
									.format(
											formatoFecha
									),

							disponibilidad
									.getHoraInicio()
									.format(
											formatoHora
									),

							disponibilidad
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

		disponibilidadSeleccionada = null;

		tablaDisponibilidades
				.clearSelection();

		botonGuardar.setText(
				"Registrar disponibilidad"
		);
	}

	private void cargarDisponibilidadSeleccionada() {

		int fila =
				tablaDisponibilidades
						.getSelectedRow();

		if (fila < 0) {

			disponibilidadSeleccionada = null;

			botonGuardar.setText(
					"Registrar disponibilidad"
			);

			return;
		}

		int idDisponibilidad =
				(int) modeloTabla
						.getValueAt(
								fila,
								0
						);

		disponibilidadSeleccionada =
				controllerDisponibilidad
						.buscarPorId(
								idDisponibilidad,
								profesional
										.getIdProfesional()
						);

		if (disponibilidadSeleccionada == null) {
			return;
		}

		campoFecha.setText(
				disponibilidadSeleccionada
						.getFecha()
						.format(
								formatoFecha
						)
		);

		campoHoraInicio.setText(
				disponibilidadSeleccionada
						.getHoraInicio()
						.format(
								formatoHora
						)
		);

		campoHoraFin.setText(
				disponibilidadSeleccionada
						.getHoraFin()
						.format(
								formatoHora
						)
		);

		botonGuardar.setText(
				"Guardar cambios"
		);
	}

	private void guardarDisponibilidad() {

		String fechaTexto =
				campoFecha
						.getText()
						.trim();

		String horaInicioTexto =
				campoHoraInicio
						.getText()
						.trim();

		String horaFinTexto =
				campoHoraFin
						.getText()
						.trim();

		if (fechaTexto.isEmpty()
				|| horaInicioTexto.isEmpty()
				|| horaFinTexto.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"Todos los campos son obligatorios.",
					"Datos incompletos",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		LocalDate fecha;
		LocalTime horaInicio;
		LocalTime horaFin;

		try {

			fecha =
					LocalDate.parse(
							fechaTexto,
							formatoFecha
					);

			horaInicio =
					LocalTime.parse(
							horaInicioTexto,
							formatoHora
					);

			horaFin =
					LocalTime.parse(
							horaFinTexto,
							formatoHora
					);

		} catch (DateTimeParseException e) {

			JOptionPane.showMessageDialog(
					this,
					"Verifique los formatos ingresados.\n\n"
							+ "Fecha: dd/MM/yyyy\n"
							+ "Hora: HH:mm",
					"Fecha u hora invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (fecha.isBefore(
				LocalDate.now())) {

			JOptionPane.showMessageDialog(
					this,
					"La fecha de disponibilidad no puede ser anterior a hoy.",
					"Fecha invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!horaFin.isAfter(
				horaInicio)) {

			JOptionPane.showMessageDialog(
					this,
					"La hora de finalizacion debe ser posterior a la hora de inicio.",
					"Horario invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (disponibilidadSeleccionada == null) {

			registrarDisponibilidad(
					fecha,
					horaInicio,
					horaFin
			);

		} else {

			modificarDisponibilidad(
					fecha,
					horaInicio,
					horaFin
			);
		}
	}

	private void registrarDisponibilidad(
			LocalDate fecha,
			LocalTime horaInicio,
			LocalTime horaFin) {

		Disponibilidad nuevaDisponibilidad =
				new Disponibilidad(
						0,
						fecha,
						horaInicio,
						horaFin,
						0
				);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea registrar esta disponibilidad?\n\n"
								+ "Fecha: "
								+ fecha.format(
										formatoFecha
								)
								+ "\nHorario: "
								+ horaInicio.format(
										formatoHora
								)
								+ " - "
								+ horaFin.format(
										formatoHora
								),
						"Confirmar disponibilidad",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean registrada =
				controllerDisponibilidad.registrar(
						nuevaDisponibilidad,
						profesional
								.getIdProfesional()
				);

		if (registrada) {

			JOptionPane.showMessageDialog(
					this,
					"Disponibilidad registrada correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

			limpiarFormulario();
			cargarDisponibilidades();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo registrar la disponibilidad.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	private void modificarDisponibilidad(
			LocalDate fecha,
			LocalTime horaInicio,
			LocalTime horaFin) {

		Disponibilidad disponibilidadModificada =
				new Disponibilidad(
						disponibilidadSeleccionada
								.getIdDisponibilidad(),

						fecha,
						horaInicio,
						horaFin,

						disponibilidadSeleccionada
								.getRetrasoEstimado()
				);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea guardar los cambios?\n\n"
								+ "Fecha: "
								+ fecha.format(
										formatoFecha
								)
								+ "\nHorario: "
								+ horaInicio.format(
										formatoHora
								)
								+ " - "
								+ horaFin.format(
										formatoHora
								),
						"Confirmar modificacion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean modificada =
				controllerDisponibilidad.modificar(
						disponibilidadModificada,
						profesional
								.getIdProfesional()
				);

		
		if (modificada) {

			JOptionPane.showMessageDialog(
					this,
					"Disponibilidad actualizada correctamente.",
					"Modificacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

			limpiarFormulario();
			cargarDisponibilidades();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo actualizar la disponibilidad.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	private void limpiarFormulario() {

		disponibilidadSeleccionada = null;

		tablaDisponibilidades
				.clearSelection();

		campoFecha.setText("");
		campoHoraInicio.setText("");
		campoHoraFin.setText("");

		botonGuardar.setText(
				"Registrar disponibilidad"
		);

		campoFecha.requestFocus();
	}

	private void volverDashboard() {

		dispose();

		dashboard.setVisible(true);
	}
}