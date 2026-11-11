package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
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

import BLL.Paciente;
import DLL.ControllerPaciente;

public class PacientesFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private DashboardEmpleadoFrame dashboard;
	private ControllerPaciente controllerPaciente;

	private JTable tablaPacientes;
	private DefaultTableModel modeloTabla;

	private JTextField campoDni;
	private JTextField campoNombre;
	private JTextField campoApellido;
	private JTextField campoTelefono;

	private JButton botonGuardar;
	private JButton botonLimpiar;

	private Paciente pacienteSeleccionado;

	public PacientesFrame(
			DashboardEmpleadoFrame dashboard) {

		this.dashboard = dashboard;

		controllerPaciente =
				new ControllerPaciente();

		configurarVentana();
		inicializarComponentes();
		cargarPacientes();
	}

	private void configurarVentana() {

		setTitle(
				"Telemedicina - Gestion de pacientes"
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
						BorderFactory
								.createEmptyBorder(
										20,
										20,
										20,
										20
								)
				);

		JLabel titulo =
				new JLabel(
						"GESTION DE PACIENTES",
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
				new JScrollPane(tablaPacientes),
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
				"DNI",
				"Nombre",
				"Apellido",
				"Telefono"
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

		tablaPacientes =
				new JTable(modeloTabla);

		tablaPacientes.setSelectionMode(
				ListSelectionModel
						.SINGLE_SELECTION
		);

		tablaPacientes.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								cargarPacienteSeleccionado();
							}
						}
				);
	}

	private JPanel crearPanelFormulario() {

		JPanel panel =
				new JPanel(
						new GridLayout(
								12,
								1,
								5,
								5
						)
				);

		panel.setBorder(
				BorderFactory.createTitledBorder(
						"Datos del paciente"
				)
		);

		panel.add(
				new JLabel("DNI:")
		);

		campoDni =
				new JTextField();

		panel.add(campoDni);

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
				new JLabel("Telefono:")
		);

		campoTelefono =
				new JTextField();

		panel.add(campoTelefono);

		botonGuardar =
				new JButton(
						"Registrar paciente"
				);

		botonLimpiar =
				new JButton(
						"Limpiar / Nuevo"
				);

		JButton botonEliminar =
				new JButton(
						"Eliminar seleccionado"
				);

		panel.add(botonGuardar);
		panel.add(botonLimpiar);
		panel.add(botonEliminar);

		botonGuardar.addActionListener(
				e -> guardarPaciente()
		);

		botonLimpiar.addActionListener(
				e -> limpiarFormulario()
		);

		botonEliminar.addActionListener(
				e -> eliminarPaciente()
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
				e -> cargarPacientes()
		);

		botonVolver.addActionListener(
				e -> volverDashboard()
		);

		return panel;
	}

	private void cargarPacientes() {

		modeloTabla.setRowCount(0);

		LinkedList<Paciente> pacientes =
				controllerPaciente.buscar("");

		for (Paciente paciente : pacientes) {

			modeloTabla.addRow(
					new Object[] {
							paciente.getIdPaciente(),
							paciente.getDni(),
							paciente.getNombre(),
							paciente.getApellido(),
							paciente.getTelefono()
					}
			);
		}
	}

	private void cargarPacienteSeleccionado() {

		int fila =
				tablaPacientes.getSelectedRow();

		if (fila < 0) {
			return;
		}

		int idPaciente =
				(int) modeloTabla.getValueAt(
						fila,
						0
				);

		pacienteSeleccionado =
				controllerPaciente.buscarPorId(
						idPaciente
				);

		if (pacienteSeleccionado == null) {
			return;
		}

		campoDni.setText(
				pacienteSeleccionado.getDni()
		);

		campoNombre.setText(
				pacienteSeleccionado.getNombre()
		);

		campoApellido.setText(
				pacienteSeleccionado.getApellido()
		);

		campoTelefono.setText(
				pacienteSeleccionado.getTelefono()
		);

		botonGuardar.setText(
				"Guardar cambios"
		);
	}

	private void guardarPaciente() {

		String dni =
				campoDni
						.getText()
						.trim();

		String nombre =
				campoNombre
						.getText()
						.trim();

		String apellido =
				campoApellido
						.getText()
						.trim();

		String telefono =
				campoTelefono
						.getText()
						.trim();

		if (dni.isEmpty()
				|| nombre.isEmpty()
				|| apellido.isEmpty()
				|| telefono.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"Todos los campos son obligatorios.",
					"Datos incompletos",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!dni.matches("\\d+")) {

			JOptionPane.showMessageDialog(
					this,
					"El DNI debe contener solamente numeros.",
					"DNI invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Paciente pacienteConMismoDni =
				controllerPaciente
						.buscarPorDni(dni);

		if (pacienteSeleccionado == null) {

			if (pacienteConMismoDni != null) {

				JOptionPane.showMessageDialog(
						this,
						"Ya existe un paciente registrado con ese DNI.",
						"Paciente existente",
						JOptionPane.WARNING_MESSAGE
				);

				return;
			}

			Paciente nuevoPaciente =
					new Paciente(
							0,
							dni,
							nombre,
							apellido,
							telefono
					);

			boolean registrado =
					controllerPaciente.registrar(
							nuevoPaciente
					);

			if (registrado) {

				JOptionPane.showMessageDialog(
						this,
						"Paciente registrado correctamente.",
						"Registro exitoso",
						JOptionPane.INFORMATION_MESSAGE
				);

				limpiarFormulario();
				cargarPacientes();

			} else {

				mostrarError();
			}

			return;
		}

		if (pacienteConMismoDni != null
				&& pacienteConMismoDni
						.getIdPaciente()
						!= pacienteSeleccionado
								.getIdPaciente()) {

			JOptionPane.showMessageDialog(
					this,
					"Ya existe otro paciente registrado con ese DNI.",
					"DNI existente",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Paciente pacienteModificado =
				new Paciente(
						pacienteSeleccionado
								.getIdPaciente(),
						dni,
						nombre,
						apellido,
						telefono
				);

		boolean modificado =
				controllerPaciente.modificar(
						pacienteModificado
				);

		if (modificado) {

			JOptionPane.showMessageDialog(
					this,
					"Datos del paciente actualizados correctamente.",
					"Modificacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

			limpiarFormulario();
			cargarPacientes();

		} else {

			mostrarError();
		}
	}

	private void eliminarPaciente() {

		if (pacienteSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un paciente de la tabla.",
					"Paciente no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (controllerPaciente.tieneTurnos(
				pacienteSeleccionado
						.getIdPaciente())) {

			JOptionPane.showMessageDialog(
					this,
					"No se puede eliminar este paciente porque posee turnos asociados.\n\n"
							+ "Sus datos deben conservarse para mantener el historial del sistema.",
					"Paciente no eliminable",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea eliminar al paciente "
								+ pacienteSeleccionado
										.getNombre()
								+ " "
								+ pacienteSeleccionado
										.getApellido()
								+ "?",
						"Confirmar eliminacion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE
				);

		if (confirmacion !=
				JOptionPane.YES_OPTION) {

			return;
		}

		boolean eliminado =
				controllerPaciente.eliminar(
						pacienteSeleccionado
								.getIdPaciente()
				);

		if (eliminado) {

			JOptionPane.showMessageDialog(
					this,
					"Paciente eliminado correctamente.",
					"Eliminacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

			limpiarFormulario();
			cargarPacientes();

		} else {

			mostrarError();
		}
	}

	private void limpiarFormulario() {

		pacienteSeleccionado = null;

		tablaPacientes.clearSelection();

		campoDni.setText("");
		campoNombre.setText("");
		campoApellido.setText("");
		campoTelefono.setText("");

		botonGuardar.setText(
				"Registrar paciente"
		);

		campoDni.requestFocus();
	}

	private void mostrarError() {

		JOptionPane.showMessageDialog(
				this,
				"No se pudo completar la operacion.",
				"Error",
				JOptionPane.ERROR_MESSAGE
		);
	}

	private void volverDashboard() {

		dispose();

		dashboard.setVisible(true);
	}
}