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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import BLL.Disponibilidad;
import BLL.Profesional;
import BLL.TipoUsuario;
import BLL.Usuario;
import DLL.ControllerDisponibilidad;
import DLL.ControllerProfesional;
import DLL.ControllerUsuario;

public class ProfesionalesFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private DashboardEmpleadoFrame dashboard;

	private ControllerProfesional controllerProfesional;
	private ControllerUsuario controllerUsuario;

	private JTable tablaProfesionales;
	private DefaultTableModel modeloTabla;

	private JTextField campoNombre;
	private JTextField campoApellido;
	private JTextField campoUsuario;
	private JPasswordField campoClave;
	private JTextField campoMatricula;
	private JTextField campoEspecialidad;

	private Profesional profesionalSeleccionado;

	public ProfesionalesFrame(
			DashboardEmpleadoFrame dashboard) {

		this.dashboard = dashboard;

		controllerProfesional =
				new ControllerProfesional();

		controllerUsuario =
				new ControllerUsuario();

		configurarVentana();
		inicializarComponentes();
		cargarProfesionales();
	}

	private void configurarVentana() {

		setTitle(
				"Telemedicina - Gestion de profesionales"
		);

		setDefaultCloseOperation(
				JFrame.DO_NOTHING_ON_CLOSE
		);

		setSize(1050, 620);

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
						"GESTION DE PROFESIONALES",
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
						tablaProfesionales
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
				"Nombre",
				"Apellido",
				"Usuario",
				"Matricula",
				"Especialidad"
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

		tablaProfesionales =
				new JTable(modeloTabla);

		tablaProfesionales.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION
		);

		tablaProfesionales
				.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								cargarProfesionalSeleccionado();
							}
						}
				);
	}

	private JPanel crearPanelFormulario() {

		JPanel panel =
				new JPanel(
						new GridLayout(
								16,
								1,
								5,
								5
						)
				);

		panel.setBorder(
				BorderFactory.createTitledBorder(
						"Registrar profesional"
				)
		);

		panel.add(
				new JLabel("Nombre:")
		);

		campoNombre =
				new JTextField();

		panel.add(campoNombre);

		panel.add(
				new JLabel("Apellido:")
		);

		campoApellido =
				new JTextField();

		panel.add(campoApellido);

		panel.add(
				new JLabel("Usuario:")
		);

		campoUsuario =
				new JTextField();

		panel.add(campoUsuario);

		panel.add(
				new JLabel("Contraseña:")
		);

		campoClave =
				new JPasswordField();

		panel.add(campoClave);

		panel.add(
				new JLabel("Matricula:")
		);

		campoMatricula =
				new JTextField();

		panel.add(campoMatricula);

		panel.add(
				new JLabel("Especialidad:")
		);

		campoEspecialidad =
				new JTextField();

		panel.add(campoEspecialidad);

		JButton botonRegistrar =
				new JButton(
						"Registrar profesional"
				);

		JButton botonLimpiar =
				new JButton(
						"Limpiar"
				);

		JButton botonDisponibilidad =
				new JButton(
						"Ver disponibilidad"
				);

		panel.add(botonRegistrar);
		panel.add(botonLimpiar);
		panel.add(botonDisponibilidad);

		botonRegistrar.addActionListener(
				e -> registrarProfesional()
		);

		botonLimpiar.addActionListener(
				e -> limpiarFormulario()
		);

		botonDisponibilidad.addActionListener(
				e -> consultarDisponibilidad()
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
				e -> cargarProfesionales()
		);

		botonVolver.addActionListener(
				e -> volverDashboard()
		);

		return panel;
	}

	private void cargarProfesionales() {

		modeloTabla.setRowCount(0);

		LinkedList<Profesional> profesionales =
				controllerProfesional.buscar("");

		for (Profesional profesional : profesionales) {

			Usuario usuarioProfesional =
					controllerProfesional
							.obtenerUsuarioPorProfesional(
									profesional
											.getIdProfesional()
							);

			if (usuarioProfesional != null) {

				modeloTabla.addRow(
						new Object[] {
								profesional
										.getIdProfesional(),

								usuarioProfesional
										.getNombre(),

								usuarioProfesional
										.getApellido(),

								usuarioProfesional
										.getNombreUsuario(),

								profesional
										.getMatricula(),

								profesional
										.getEspecialidad()
						}
				);
			}
		}
	}

	private void cargarProfesionalSeleccionado() {

		int fila =
				tablaProfesionales
						.getSelectedRow();

		if (fila < 0) {

			profesionalSeleccionado = null;
			return;
		}

		int idProfesional =
				(int) modeloTabla.getValueAt(
						fila,
						0
				);

		profesionalSeleccionado =
				controllerProfesional
						.buscarPorId(
								idProfesional
						);
	}

	private void registrarProfesional() {

		String nombre =
				campoNombre
						.getText()
						.trim();

		String apellido =
				campoApellido
						.getText()
						.trim();

		String nombreUsuario =
				campoUsuario
						.getText()
						.trim();

		String clave =
				new String(
						campoClave
								.getPassword()
				);

		String matricula =
				campoMatricula
						.getText()
						.trim();

		String especialidad =
				campoEspecialidad
						.getText()
						.trim();

		if (nombre.isEmpty()
				|| apellido.isEmpty()
				|| nombreUsuario.isEmpty()
				|| clave.isEmpty()
				|| matricula.isEmpty()
				|| especialidad.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"Todos los campos son obligatorios.",
					"Datos incompletos",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (controllerUsuario
				.existeNombreUsuario(
						nombreUsuario
				)) {

			JOptionPane.showMessageDialog(
					this,
					"Ya existe un usuario con ese nombre de usuario.",
					"Usuario existente",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (controllerProfesional
				.existeMatricula(
						matricula
				)) {

			JOptionPane.showMessageDialog(
					this,
					"Ya existe un profesional con esa matricula.",
					"Matricula existente",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Usuario usuarioNuevo =
				new Usuario(
						0,
						nombre,
						apellido,
						nombreUsuario,
						clave,
						TipoUsuario.PROFESIONAL
				);

		Profesional profesionalNuevo =
				new Profesional(
						0,
						matricula,
						especialidad
				);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea registrar al profesional?\n\n"
								+ nombre
								+ " "
								+ apellido
								+ "\nUsuario: "
								+ nombreUsuario
								+ "\nMatricula: "
								+ matricula
								+ "\nEspecialidad: "
								+ especialidad,
						"Confirmar registro",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean registrado =
				controllerProfesional.registrar(
						usuarioNuevo,
						profesionalNuevo
				);

		if (registrado) {

			JOptionPane.showMessageDialog(
					this,
					"Profesional registrado correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

			limpiarFormulario();
			cargarProfesionales();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo registrar el profesional.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	private void consultarDisponibilidad() {

		if (profesionalSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un profesional de la tabla.",
					"Profesional no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerDisponibilidad controllerDisponibilidad =
				new ControllerDisponibilidad();

		LinkedList<Disponibilidad> disponibilidades =
				controllerDisponibilidad
						.obtenerPorProfesional(
								profesionalSeleccionado
										.getIdProfesional()
						);

		if (disponibilidades.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"El profesional seleccionado no tiene disponibilidades registradas.",
					"Sin disponibilidades",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		Usuario usuarioProfesional =
				controllerProfesional
						.obtenerUsuarioPorProfesional(
								profesionalSeleccionado
										.getIdProfesional()
						);

		DateTimeFormatter formatoFecha =
				DateTimeFormatter.ofPattern(
						"dd/MM/yyyy"
				);

		DateTimeFormatter formatoHora =
				DateTimeFormatter.ofPattern(
						"HH:mm"
				);

		StringBuilder resultado =
				new StringBuilder();

		if (usuarioProfesional != null) {

			resultado.append("Profesional: ")
					.append(
							usuarioProfesional
									.getNombre()
					)
					.append(" ")
					.append(
							usuarioProfesional
									.getApellido()
					)

					.append("\nEspecialidad: ")
					.append(
							profesionalSeleccionado
									.getEspecialidad()
					)

					.append("\n\n");
		}

		for (Disponibilidad disponibilidad
				: disponibilidades) {

			resultado.append("ID disponibilidad: ")
					.append(
							disponibilidad
									.getIdDisponibilidad()
					)

					.append("\nFecha: ")
					.append(
							disponibilidad
									.getFecha()
									.format(
											formatoFecha
									)
					)

					.append("\nHorario: ")
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

					.append("\nRetraso estimado: ")
					.append(
							disponibilidad
									.getRetrasoEstimado()
					)
					.append(" minutos")

					.append(
							"\n------------------------------\n"
					);
		}

		JOptionPane.showMessageDialog(
				this,
				resultado.toString(),
				"Telemedicina - Disponibilidad",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	private void limpiarFormulario() {

		campoNombre.setText("");
		campoApellido.setText("");
		campoUsuario.setText("");
		campoClave.setText("");
		campoMatricula.setText("");
		campoEspecialidad.setText("");

		tablaProfesionales
				.clearSelection();

		profesionalSeleccionado = null;

		campoNombre.requestFocus();
	}

	private void volverDashboard() {

		dispose();

		dashboard.setVisible(true);
	}
}