package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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

		/*
		 * Tamaño pensado para mostrar el GIF de 190 x 190
		 * junto al formulario sin comprimir los componentes.
		 */
		setSize(670, 380);

		setLocationRelativeTo(null);

		setResizable(false);
		
		

		/*
		 * Icono de la aplicación en la barra de título.
		 */
		configurarIconoAplicacion();
	}

	private void inicializarComponentes() {

		setLayout(
				new BorderLayout()
		);

		/*
		 * TITULO
		 */
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

		titulo.setBorder(
				BorderFactory.createEmptyBorder(
						25,
						10,
						15,
						10
				)
		);

		add(
				titulo,
				BorderLayout.NORTH
		);

		/*
		 * PANEL CENTRAL
		 *
		 * GIF a la izquierda.
		 * Formulario a la derecha.
		 */
		JPanel panelCentral =
				new JPanel(
						new BorderLayout(
								30,
								0
						)
				);

		panelCentral.setBorder(
				BorderFactory.createEmptyBorder(
						10,
						45,
						30,
						45
				)
		);

		/*
		 * GIF PRINCIPAL DEL LOGIN
		 */
		JLabel imagenLogin =
				new JLabel(
						"",
						SwingConstants.CENTER
				);

		ImageIcon gifLogin =
				cargarGifEscalado(
						"/resources/imagenes/login.gif",
						190,
						190
				);

		if (gifLogin != null) {

			imagenLogin.setIcon(
					gifLogin
			);
		}

		imagenLogin.setHorizontalAlignment(
				SwingConstants.CENTER
		);

		imagenLogin.setVerticalAlignment(
				SwingConstants.CENTER
		);

		imagenLogin.setBorder(
				BorderFactory.createEmptyBorder(
						0,
						0,
						0,
						10
				)
		);

		panelCentral.add(
				imagenLogin,
				BorderLayout.WEST
		);

		/*
		 * FORMULARIO
		 */
		JPanel panelFormulario =
				new JPanel(
						new GridBagLayout()
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

		/*
		 * USUARIO
		 */
		JLabel etiquetaUsuario =
				new JLabel("Usuario:");

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;

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

		/*
		 * CONTRASEÑA
		 */
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

		/*
		 * BOTON INGRESAR
		 */
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

		panelCentral.add(
				panelFormulario,
				BorderLayout.CENTER
		);

		add(
				panelCentral,
				BorderLayout.CENTER
		);

		/*
		 * Permite iniciar sesión presionando Enter.
		 */
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

		/*
		 * Validación de campos vacíos.
		 */
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

		/*
		 * Autenticación mediante ControllerUsuario.
		 */
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

		/*
		 * Mensaje de bienvenida personalizado según el rol.
		 */
		mostrarBienvenidaConImagen(
				logueado
		);

		dispose();

		/*
		 * Navegación según el tipo de usuario.
		 */
		switch (logueado.getTipoUsuario()) {

		case EMPLEADO:

			DashboardEmpleadoFrame dashboardEmpleado =
					new DashboardEmpleadoFrame(
							logueado
					);

			dashboardEmpleado.setVisible(true);

			break;

		case PROFESIONAL:

			DashboardProfesionalFrame dashboardProfesional =
					new DashboardProfesionalFrame(
							logueado
					);

			dashboardProfesional.setVisible(true);

			break;
		}
	}

	private void mostrarBienvenidaConImagen(
			Usuario usuario) {

		String rutaIcono;

		switch (usuario.getTipoUsuario()) {

		case EMPLEADO:

			rutaIcono =
					"/resources/imagenes/ok_empleado.png";

			break;

		case PROFESIONAL:

			rutaIcono =
					"/resources/imagenes/ok_profesional.png";

			break;

		default:

			rutaIcono =
					"/resources/imagenes/icono_app.png";

			break;
		}

		ImageIcon icono =
				cargarIconoEscalado(
						rutaIcono,
						210,
						210
				);

		JOptionPane.showMessageDialog(
				this,
				"Bienvenido: "
						+ usuario.getNombre()
						+ " "
						+ usuario.getApellido()
						+ "\nPerfil: "
						+ usuario.getTipoUsuario(),
				"Inicio de sesion correcto",
				JOptionPane.INFORMATION_MESSAGE,
				icono
		);
	}

	/*
	 * Carga el GIF principal y lo muestra escalado.
	 * Se mantiene como ImageIcon para conservar la animación.
	 */
	private ImageIcon cargarGifEscalado(
			String ruta,
			int ancho,
			int alto) {

		URL url =
				getClass().getResource(
						ruta
				);

		if (url == null) {

			System.out.println(
					"No se encontro el GIF: "
							+ ruta
			);

			return null;
		}

		ImageIcon gifOriginal =
				new ImageIcon(
						url
				);

		Image gifEscalado =
				gifOriginal
						.getImage()
						.getScaledInstance(
								ancho,
								alto,
								Image.SCALE_DEFAULT
						);

		return new ImageIcon(
				gifEscalado
		);
	}

	/*
	 * Carga y escala imágenes PNG utilizadas como iconos.
	 */
	private ImageIcon cargarIconoEscalado(
			String ruta,
			int ancho,
			int alto) {

		URL url =
				getClass().getResource(
						ruta
				);

		if (url == null) {

			System.out.println(
					"No se encontro la imagen: "
							+ ruta
			);

			return null;
		}

		Image imagen =
				new ImageIcon(
						url
				)
						.getImage()
						.getScaledInstance(
								ancho,
								alto,
								Image.SCALE_SMOOTH
						);

		return new ImageIcon(
				imagen
		);
	}
	
	
	
	
	private void configurarIconoAplicacion() {

		URL url =
				getClass().getResource(
						"/resources/imagenes/icono_app.png"
				);

		if (url == null) {

			System.out.println(
					"No se encontro el icono de la aplicacion."
			);

			return;
		}

		ImageIcon icono =
				new ImageIcon(
						url
				);

		setIconImage(
				icono.getImage()
		);
	}
}