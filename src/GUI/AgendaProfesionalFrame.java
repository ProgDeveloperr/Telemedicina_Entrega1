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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import BLL.Profesional;
import BLL.Turno;
import BLL.Usuario;
import DLL.ControllerProfesional;
import DLL.ControllerTurno;

public class AgendaProfesionalFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private DashboardProfesionalFrame dashboard;
	private Usuario usuario;

	private ControllerProfesional controllerProfesional;
	private ControllerTurno controllerTurno;

	private Profesional profesional;

	private JTable tablaTurnos;
	private DefaultTableModel modeloTabla;

	private JLabel labelResumen;

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern("HH:mm");

	public AgendaProfesionalFrame(
			DashboardProfesionalFrame dashboard,
			Usuario usuario) {

		this.dashboard = dashboard;
		this.usuario = usuario;

		controllerProfesional =
				new ControllerProfesional();

		controllerTurno =
				new ControllerTurno();

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
		cargarAgenda();
	}

	private void configurarVentana() {

		setTitle(
				"Telemedicina - Agenda - "
						+ usuario.getNombre()
						+ " "
						+ usuario.getApellido()
		);

		setDefaultCloseOperation(
				JFrame.DO_NOTHING_ON_CLOSE
		);

		setSize(950, 570);

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
						"MI AGENDA DE TURNOS",
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

		add(
				new JScrollPane(
						tablaTurnos
				),
				BorderLayout.CENTER
		);

		JPanel panelInferior =
				new JPanel();

		JButton botonActualizar =
				new JButton(
						"Actualizar agenda"
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
				e -> cargarAgenda()
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
				"ID turno",
				"Paciente",
				"DNI",
				"Fecha",
				"Hora",
				"Estado",
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

		tablaTurnos =
				new JTable(
						modeloTabla
				);
	}

	private void cargarAgenda() {

		modeloTabla.setRowCount(0);

		LinkedList<Turno> turnos =
				controllerTurno
						.obtenerPorProfesional(
								profesional
										.getIdProfesional()
						);

		int cantidad = 0;

		for (Turno turno : turnos) {

			if (!turno.estaCancelado()
					&& !turno.getFecha()
							.isBefore(
									LocalDate.now()
							)) {

				modeloTabla.addRow(
						new Object[] {
								turno
										.getIdTurno(),

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
										),

								turno
										.getEstado(),

								turno
										.getDisponibilidad()
										.getRetrasoEstimado()
										+ " minutos"
						}
				);

				cantidad++;
			}
		}

		if (cantidad == 0) {

			labelResumen.setText(
					"No tiene turnos proximos registrados."
			);

		} else {

			labelResumen.setText(
					"Turnos proximos registrados: "
							+ cantidad
			);
		}
	}

	private void volverDashboard() {

		dispose();

		dashboard.setVisible(true);
	}
}