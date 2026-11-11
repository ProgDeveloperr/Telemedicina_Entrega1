package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.time.LocalDate;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import BLL.Disponibilidad;
import BLL.Profesional;
import BLL.Usuario;
import DLL.ControllerDisponibilidad;
import DLL.ControllerProfesional;

public class RetrasoProfesionalFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private DashboardProfesionalFrame dashboard;
	private Usuario usuario;

	private ControllerProfesional controllerProfesional;
	private ControllerDisponibilidad controllerDisponibilidad;

	private Profesional profesional;

	private JTable tablaDisponibilidades;
	private DefaultTableModel modeloTabla;

	private JTextField campoRetraso;

	private JButton botonGuardar;

	private Disponibilidad disponibilidadSeleccionada;

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern("HH:mm");

	public RetrasoProfesionalFrame(
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
				"Telemedicina - Retraso estimado - "
						+ usuario.getNombre()
						+ " "
						+ usuario.getApellido()
		);

		setDefaultCloseOperation(
				JFrame.DO_NOTHING_ON_CLOSE
		);

		setSize(850, 520);

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
						"REGISTRAR RETRASO ESTIMADO",
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
				crearPanelRetraso(),
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
				"Retraso actual"
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

								cargarSeleccion();
							}
						}
				);
	}

	private JPanel crearPanelRetraso() {

		JPanel panel =
				new JPanel();

		panel.setLayout(
				new javax.swing.BoxLayout(
						panel,
						javax.swing.BoxLayout.Y_AXIS
				)
		);

		panel.setBorder(
				BorderFactory.createTitledBorder(
						"Retraso estimado"
				)
		);

		JLabel label =
				new JLabel(
						"Minutos:"
				);

		campoRetraso =
				new JTextField();

		campoRetraso.setMaximumSize(
				new java.awt.Dimension(
						150,
						28
				)
		);

		botonGuardar =
				new JButton(
						"Guardar retraso"
				);

		botonGuardar.setEnabled(false);

		panel.add(label);
		panel.add(
				javax.swing.Box.createVerticalStrut(5)
		);

		panel.add(campoRetraso);
		panel.add(
				javax.swing.Box.createVerticalStrut(10)
		);

		panel.add(botonGuardar);

		botonGuardar.addActionListener(
				e -> guardarRetraso()
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

			if (!disponibilidad
					.getFecha()
					.isBefore(
							LocalDate.now()
					)) {

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
		}

		disponibilidadSeleccionada = null;

		tablaDisponibilidades
				.clearSelection();

		campoRetraso.setText("");

		botonGuardar.setEnabled(false);
	}

	private void cargarSeleccion() {

		int fila =
				tablaDisponibilidades
						.getSelectedRow();

		if (fila < 0) {

			disponibilidadSeleccionada = null;

			campoRetraso.setText("");

			botonGuardar.setEnabled(false);

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

			campoRetraso.setText("");

			botonGuardar.setEnabled(false);

			return;
		}

		campoRetraso.setText(
				String.valueOf(
						disponibilidadSeleccionada
								.getRetrasoEstimado()
				)
		);

		botonGuardar.setEnabled(true);
	}

	private void guardarRetraso() {

		if (disponibilidadSeleccionada == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione una disponibilidad de la tabla.",
					"Disponibilidad no seleccionada",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		String retrasoTexto =
				campoRetraso
						.getText()
						.trim();

		if (retrasoTexto.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"Debe ingresar el retraso estimado.",
					"Dato incompleto",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int minutos;

		try {

			minutos =
					Integer.parseInt(
							retrasoTexto
					);

			if (minutos < 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					this,
					"El retraso debe ser un numero entero mayor o igual a cero.",
					"Retraso invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea registrar este retraso?\n\n"
								+ "Fecha: "
								+ disponibilidadSeleccionada
										.getFecha()
										.format(
												formatoFecha
										)
								+ "\nHorario: "
								+ disponibilidadSeleccionada
										.getHoraInicio()
										.format(
												formatoHora
										)
								+ " - "
								+ disponibilidadSeleccionada
										.getHoraFin()
										.format(
												formatoHora
										)
								+ "\nRetraso estimado: "
								+ minutos
								+ " minutos",
						"Confirmar retraso",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean actualizado =
				controllerDisponibilidad
						.actualizarRetraso(
								disponibilidadSeleccionada
										.getIdDisponibilidad(),

								profesional
										.getIdProfesional(),

								minutos
						);

		if (actualizado) {

			JOptionPane.showMessageDialog(
					this,
					"Retraso estimado registrado correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarDisponibilidades();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo registrar el retraso estimado.",
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