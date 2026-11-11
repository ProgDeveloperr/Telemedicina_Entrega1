package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import BLL.Usuario;

public class DashboardEmpleadoFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	public DashboardEmpleadoFrame(Usuario usuario) {

		this.usuario = usuario;

		configurarVentana();
		inicializarComponentes();
	}

	private void configurarVentana() {

		setTitle(
				"Telemedicina - Recepcion - "
				+ usuario.getNombre()
				+ " "
				+ usuario.getApellido()
		);

		setDefaultCloseOperation(
				JFrame.EXIT_ON_CLOSE
		);

		setSize(650, 430);

		setLocationRelativeTo(null);

		setResizable(false);
	}

	private void inicializarComponentes() {

		setLayout(new BorderLayout());

		JPanel panelSuperior =
				new JPanel(
						new BorderLayout()
				);

		panelSuperior.setBorder(
				BorderFactory.createEmptyBorder(
						25,
						30,
						20,
						30
				)
		);

		JLabel titulo =
				new JLabel(
						"TELEMEDICINA",
						SwingConstants.CENTER
				);

		titulo.setFont(
				new Font(
						"SansSerif",
						Font.BOLD,
						24
				)
		);

		JLabel usuarioLabel =
				new JLabel(
						"Recepcionista: "
						+ usuario.getNombre()
						+ " "
						+ usuario.getApellido(),
						SwingConstants.CENTER
				);

		usuarioLabel.setFont(
				new Font(
						"SansSerif",
						Font.PLAIN,
						14
				)
		);

		panelSuperior.add(
				titulo,
				BorderLayout.NORTH
		);

		panelSuperior.add(
				usuarioLabel,
				BorderLayout.SOUTH
		);

		add(
				panelSuperior,
				BorderLayout.NORTH
		);

		JPanel panelOpciones =
				new JPanel(
						new GridLayout(
								2,
								2,
								15,
								15
						)
				);

		panelOpciones.setBorder(
				BorderFactory.createEmptyBorder(
						30,
						60,
						30,
						60
				)
		);

		JButton botonPacientes =
				new JButton("Pacientes");

		JButton botonProfesionales =
				new JButton("Profesionales");

		JButton botonTurnos =
				new JButton("Turnos");

		JButton botonConsultas =
				new JButton(
						"Consultas e informacion"
				);

		panelOpciones.add(
				botonPacientes
		);

		panelOpciones.add(
				botonProfesionales
		);

		panelOpciones.add(
				botonTurnos
		);

		panelOpciones.add(
				botonConsultas
		);

		add(
				panelOpciones,
				BorderLayout.CENTER
		);

		JButton botonCerrarSesion =
				new JButton("Cerrar sesion");

		JPanel panelInferior =
				new JPanel();

		panelInferior.setBorder(
				BorderFactory.createEmptyBorder(
						10,
						10,
						20,
						10
				)
		);

		panelInferior.add(
				botonCerrarSesion
		);

		add(
				panelInferior,
				BorderLayout.SOUTH
		);

		/*
		 * Por ahora los modulos se migraran
		 * uno por uno a JFrame.
		 */
		botonPacientes.addActionListener(
				e -> mostrarModuloPendiente(
						"Pacientes"
				)
		);

		botonProfesionales.addActionListener(
				e -> mostrarModuloPendiente(
						"Profesionales"
				)
		);

		botonTurnos.addActionListener(
				e -> mostrarModuloPendiente(
						"Turnos"
				)
		);

		botonConsultas.addActionListener(
				e -> mostrarModuloPendiente(
						"Consultas e informacion"
				)
		);

		botonCerrarSesion.addActionListener(
				e -> cerrarSesion()
		);
	}

	private void mostrarModuloPendiente(
			String modulo) {

		JOptionPane.showMessageDialog(
				this,
				"El modulo "
						+ modulo
						+ " sera migrado a JFrame en el siguiente paso.",
				"Telemedicina",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	private void cerrarSesion() {

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea cerrar la sesion?",
						"Cerrar sesion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		dispose();

		LoginFrame login =
				new LoginFrame();

		login.setVisible(true);
	}
}