package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import BLL.Usuario;
import DLL.ControllerUsuario;

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField campoUsuario;
	private JPasswordField campoClave;
	private JButton botonIngresar;

	private ControllerUsuario controllerUsuario;

	public LoginFrame() {

		controllerUsuario = new ControllerUsuario();

		configurarVentana();
		inicializarComponentes();
	}

	private void configurarVentana() {

		setTitle("Telemedicina - Inicio de sesion");

		setDefaultCloseOperation(
				JFrame.EXIT_ON_CLOSE
		);

		setSize(450, 320);

		setLocationRelativeTo(null);

		setResizable(false);
	}

	private void inicializarComponentes() {

		setLayout(new BorderLayout());

		JLabel titulo = new JLabel(
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

		titulo.setBorder(
				BorderFactory.createEmptyBorder(
						25,
						10,
						20,
						10
				)
		);

		add(
				titulo,
				BorderLayout.NORTH
		);

		JPanel panelFormulario =
				new JPanel(
						new GridBagLayout()
				);

		panelFormulario.setBorder(
				BorderFactory.createEmptyBorder(
						10,
						40,
						25,
						40
				)
		);

		GridBagConstraints gbc =
				new GridBagConstraints();

		gbc.insets =
				new Insets(
						8,
						8,
						8,
						8
				);

		gbc.fill =
				GridBagConstraints.HORIZONTAL;

		JLabel etiquetaUsuario =
				new JLabel("Usuario:");

		gbc.gridx = 0;
		gbc.gridy = 0;

		panelFormulario.add(
				etiquetaUsuario,
				gbc
		);

		campoUsuario =
				new JTextField(18);

		gbc.gridx = 1;

		panelFormulario.add(
				campoUsuario,
				gbc
		);

		JLabel etiquetaClave =
				new JLabel("Contraseña:");

		gbc.gridx = 0;
		gbc.gridy = 1;

		panelFormulario.add(
				etiquetaClave,
				gbc
		);

		campoClave =
				new JPasswordField(18);

		gbc.gridx = 1;

		panelFormulario.add(
				campoClave,
				gbc
		);

		botonIngresar =
				new JButton("Ingresar");

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;

		gbc.insets =
				new Insets(
						20,
						8,
						8,
						8
				);

		panelFormulario.add(
				botonIngresar,
				gbc
		);

		add(
				panelFormulario,
				BorderLayout.CENTER
		);

		getRootPane()
				.setDefaultButton(
						botonIngresar
				);

		botonIngresar.addActionListener(
				e -> iniciarSesion()
		);
	}

	private void iniciarSesion() {

		String nombreUsuario =
				campoUsuario
						.getText()
						.trim();

		String clave =
				new String(
						campoClave
								.getPassword()
				);

		if (nombreUsuario.isEmpty()
				|| clave.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"Debe ingresar usuario y contraseña.",
					"Datos incompletos",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Usuario logueado =
				controllerUsuario.login(
						nombreUsuario,
						clave
				);

		if (logueado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Usuario o contraseña incorrectos.",
					"Error de autenticacion",
					JOptionPane.ERROR_MESSAGE
			);

			campoClave.setText("");

			return;
		}

		JOptionPane.showMessageDialog(
				this,
				"Bienvenido: "
						+ logueado.getNombre()
						+ " "
						+ logueado.getApellido()
						+ "\nPerfil: "
						+ logueado.getTipoUsuario(),
				"Inicio de sesion correcto",
				JOptionPane.INFORMATION_MESSAGE
		);

		dispose();

		switch (logueado.getTipoUsuario()) {

		case EMPLEADO:

			DashboardEmpleadoFrame dashboard =
					new DashboardEmpleadoFrame(
							logueado
					);

			dashboard.setVisible(true);

			break;

		case PROFESIONAL:

			/*
			 * El dashboard profesional se migrara
			 * en el siguiente bloque.
			 */
			logueado.MenuPrincipal();

			break;
		}
	}
}