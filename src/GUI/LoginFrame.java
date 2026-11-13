package GUI;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;

import BLL.Usuario;
import DLL.ControllerUsuario;

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	/*
	 * =========================================================
	 * AJUSTES VISUALES RAPIDOS
	 * =========================================================
	 *
	 * Si despues queres modificar proporciones,
	 * hacelo desde estos valores.
	 */
	private static final int ANCHO_VENTANA = 920;
	private static final int ALTO_VENTANA = 560;

	private static final int ANCHO_PANEL_MARCA = 355;

	private static final int TAMANIO_GIF = 215;

	private static final int ANCHO_TARJETA = 420;
	private static final int ALTO_TARJETA = 400;

	private JTextField campoUsuario;
	private JPasswordField campoClave;

	private JButton botonIngresar;
	private JButton botonMostrarClave;

	private char echoOriginal;

	private ControllerUsuario controllerUsuario;

	public LoginFrame() {

		controllerUsuario =
				new ControllerUsuario();

		configurarVentana();
		inicializarComponentes();
	}

	/*
	 * =========================================================
	 * CONFIGURACION GENERAL
	 * =========================================================
	 */
	private void configurarVentana() {

		setTitle(
				"Telemedicina - Inicio de sesion"
		);

		setDefaultCloseOperation(
				JFrame.EXIT_ON_CLOSE
		);

		setSize(
				ANCHO_VENTANA,
				ALTO_VENTANA
		);

		setMinimumSize(
				new Dimension(
						ANCHO_VENTANA,
						ALTO_VENTANA
				)
		);

		setLocationRelativeTo(
				null
		);

		setResizable(
				false
		);

		configurarIconoAplicacion();
	}

	/*
	 * =========================================================
	 * INTERFAZ
	 * =========================================================
	 */
	private void inicializarComponentes() {

		JPanel panelPrincipal =
				new JPanel(
						new BorderLayout()
				);

		panelPrincipal.setBackground(
				UITheme.COLOR_FONDO
		);

		setContentPane(
				panelPrincipal
		);

		/*
		 * =====================================================
		 * PANEL IZQUIERDO
		 * =====================================================
		 */
		JPanel panelMarca =
				crearPanelMarca();

		panelPrincipal.add(
				panelMarca,
				BorderLayout.WEST
		);

		/*
		 * =====================================================
		 * PANEL DERECHO
		 * =====================================================
		 */
		JPanel panelDerecho =
				new JPanel(
						new GridBagLayout()
				);

		panelDerecho.setBackground(
				UITheme.COLOR_FONDO
		);

		panelDerecho.setBorder(
				BorderFactory.createEmptyBorder(
						30,
						45,
						30,
						45
				)
		);

		/*
		 * Tarjeta premium.
		 */
		ShadowCard tarjetaFormulario =
				new ShadowCard();

		tarjetaFormulario.setLayout(
				new GridBagLayout()
		);

		tarjetaFormulario.setPreferredSize(
				new Dimension(
						ANCHO_TARJETA,
						ALTO_TARJETA
				)
		);

		GridBagConstraints contenedor =
				new GridBagConstraints();

		contenedor.gridx = 0;
		contenedor.gridy = 0;

		panelDerecho.add(
				tarjetaFormulario,
				contenedor
		);

		construirFormulario(
				tarjetaFormulario
		);

		panelPrincipal.add(
				panelDerecho,
				BorderLayout.CENTER
		);

		/*
		 * Enter = iniciar sesion.
		 */
		getRootPane()
				.setDefaultButton(
						botonIngresar
				);

		botonIngresar.addActionListener(
				e -> iniciarSesion()
		);
	}

	/*
	 * =========================================================
	 * PANEL DE IDENTIDAD
	 * =========================================================
	 */
	private JPanel crearPanelMarca() {

		GradientPanel panelMarca =
				new GradientPanel();

		panelMarca.setLayout(
				new BoxLayout(
						panelMarca,
						BoxLayout.Y_AXIS
				)
		);

		panelMarca.setPreferredSize(
				new Dimension(
						ANCHO_PANEL_MARCA,
						ALTO_VENTANA
				)
		);

		panelMarca.setBorder(
				BorderFactory.createEmptyBorder(
						40,
						30,
						35,
						30
				)
		);

		/*
		 * Marca.
		 */
		JLabel tituloMarca =
				new JLabel(
						"TELEMEDICINA"
				);

		tituloMarca.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						27
				)
		);

		tituloMarca.setForeground(
				Color.WHITE
		);

		tituloMarca.setAlignmentX(
				CENTER_ALIGNMENT
		);

		JLabel subtituloMarca =
				new JLabel(
						"Sistema de gestion medica"
				);

		subtituloMarca.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						13
				)
		);

		subtituloMarca.setForeground(
				new Color(
						194,
						219,
						228
				)
		);

		subtituloMarca.setAlignmentX(
				CENTER_ALIGNMENT
		);

		/*
		 * Pequeño indicador visual.
		 */
		JLabel badge =
				new JLabel(
						"  GESTION CLINICA  "
				);

		badge.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						10
				)
		);

		badge.setForeground(
				new Color(
						178,
						235,
						242
				)
		);

		badge.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(
								new Color(
										70,
										130,
										148
								),
								1,
								true
						),
						BorderFactory.createEmptyBorder(
								5,
								8,
								5,
								8
						)
				)
		);

		badge.setAlignmentX(
				CENTER_ALIGNMENT
		);

		/*
		 * GIF.
		 */
		JLabel imagenLogin =
				new JLabel(
						"",
						SwingConstants.CENTER
				);

		ImageIcon gifLogin =
				cargarGifEscalado(
						"/resources/imagenes/login.gif",
						TAMANIO_GIF,
						TAMANIO_GIF
				);

		if (gifLogin != null) {

			imagenLogin.setIcon(
					gifLogin
			);
		}

		imagenLogin.setAlignmentX(
				CENTER_ALIGNMENT
		);

		/*
		 * Texto inferior.
		 */
		JLabel textoInstitucional =
				new JLabel(
						"Gestion clinica segura y organizada"
				);

		textoInstitucional.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						12
				)
		);

		textoInstitucional.setForeground(
				new Color(
						190,
						215,
						225
				)
		);

		textoInstitucional.setAlignmentX(
				CENTER_ALIGNMENT
		);

		JLabel textoSecundario =
				new JLabel(
						"Pacientes • Turnos • Consultas"
				);

		textoSecundario.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						11
				)
		);

		textoSecundario.setForeground(
				new Color(
						132,
						174,
						190
				)
		);

		textoSecundario.setAlignmentX(
				CENTER_ALIGNMENT
		);

		panelMarca.add(
				tituloMarca
		);

		panelMarca.add(
				Box.createVerticalStrut(
						5
				)
		);

		panelMarca.add(
				subtituloMarca
		);

		panelMarca.add(
				Box.createVerticalStrut(
						18
				)
		);

		panelMarca.add(
				badge
		);

		panelMarca.add(
				Box.createVerticalStrut(
						18
				)
		);

		panelMarca.add(
				imagenLogin
		);

		panelMarca.add(
				Box.createVerticalGlue()
		);

		panelMarca.add(
				textoInstitucional
		);

		panelMarca.add(
				Box.createVerticalStrut(
						5
				)
		);

		panelMarca.add(
				textoSecundario
		);

		return panelMarca;
	}

	/*
	 * =========================================================
	 * FORMULARIO
	 * =========================================================
	 */
	private void construirFormulario(
			JPanel tarjetaFormulario) {

		GridBagConstraints gbc =
				new GridBagConstraints();

		gbc.gridx = 0;
		gbc.weightx = 1.0;

		gbc.fill =
				GridBagConstraints.HORIZONTAL;

		/*
		 * =====================================================
		 * CABECERA
		 * =====================================================
		 */
		JLabel tituloFormulario =
				new JLabel(
						"Bienvenido"
				);

		tituloFormulario.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						29
				)
		);

		tituloFormulario.setForeground(
				UITheme.COLOR_TEXTO
		);

		gbc.gridy = 0;

		gbc.insets =
				new Insets(
						20,
						28,
						2,
						28
				);

		tarjetaFormulario.add(
				tituloFormulario,
				gbc
		);

		JLabel subtituloFormulario =
				new JLabel(
						"Inicia sesion para continuar"
				);

		subtituloFormulario.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						13
				)
		);

		subtituloFormulario.setForeground(
				UITheme.COLOR_TEXTO_SECUNDARIO
		);

		gbc.gridy = 1;

		gbc.insets =
				new Insets(
						0,
						28,
						6,
						28
				);

		tarjetaFormulario.add(
				subtituloFormulario,
				gbc
		);

		/*
		 * Linea de acento.
		 */
		JPanel lineaAcento =
				new JPanel();

		lineaAcento.setBackground(
				UITheme.COLOR_ACENTO
		);

		lineaAcento.setPreferredSize(
				new Dimension(
						45,
						3
				)
		);

		gbc.gridy = 2;

		gbc.fill =
				GridBagConstraints.NONE;

		gbc.anchor =
				GridBagConstraints.WEST;

		gbc.insets =
				new Insets(
						5,
						28,
						20,
						28
				);

		tarjetaFormulario.add(
				lineaAcento,
				gbc
		);

		gbc.fill =
				GridBagConstraints.HORIZONTAL;

		/*
		 * =====================================================
		 * USUARIO
		 * =====================================================
		 */
		JLabel etiquetaUsuario =
				crearEtiquetaFormulario(
						"Usuario"
				);

		gbc.gridy = 3;

		gbc.insets =
				new Insets(
						0,
						28,
						6,
						28
				);

		tarjetaFormulario.add(
				etiquetaUsuario,
				gbc
		);

		RoundedTextField campoUsuarioPremium =
				new RoundedTextField();

		campoUsuario =
				campoUsuarioPremium;

		campoUsuario.setPreferredSize(
				new Dimension(
						300,
						42
				)
		);

		gbc.gridy = 4;

		gbc.insets =
				new Insets(
						0,
						28,
						15,
						28
				);

		tarjetaFormulario.add(
				campoUsuario,
				gbc
		);

		/*
		 * =====================================================
		 * CONTRASEÑA
		 * =====================================================
		 */
		JLabel etiquetaClave =
				crearEtiquetaFormulario(
						"Contraseña"
				);

		gbc.gridy = 5;

		gbc.insets =
				new Insets(
						0,
						28,
						6,
						28
				);

		tarjetaFormulario.add(
				etiquetaClave,
				gbc
		);

		JPanel panelClave =
				new JPanel(
						new BorderLayout(
								8,
								0
						)
				);

		panelClave.setOpaque(
				false
		);

		RoundedPasswordField campoClavePremium =
				new RoundedPasswordField();

		campoClave =
				campoClavePremium;

		echoOriginal =
				campoClave.getEchoChar();

		campoClave.setPreferredSize(
				new Dimension(
						230,
						42
				)
		);

		botonMostrarClave =
				new JButton(
						"Mostrar"
				);

		configurarBotonMostrarClave();

		panelClave.add(
				campoClave,
				BorderLayout.CENTER
		);

		panelClave.add(
				botonMostrarClave,
				BorderLayout.EAST
		);

		gbc.gridy = 6;

		gbc.insets =
				new Insets(
						0,
						28,
						22,
						28
				);

		tarjetaFormulario.add(
				panelClave,
				gbc
		);

		/*
		 * =====================================================
		 * BOTON PRINCIPAL
		 * =====================================================
		 */
		botonIngresar =
				new PremiumButton(
						"Iniciar sesion"
				);

		botonIngresar.setPreferredSize(
				new Dimension(
						300,
						44
				)
		);

		gbc.gridy = 7;

		gbc.insets =
				new Insets(
						0,
						28,
						17,
						28
				);

		tarjetaFormulario.add(
				botonIngresar,
				gbc
		);

		/*
		 * =====================================================
		 * MENSAJE INFERIOR
		 * =====================================================
		 */
		JLabel textoInferior =
				new JLabel(
						"Acceso seguro al sistema de gestion clinica",
						SwingConstants.CENTER
				);

		textoInferior.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						11
				)
		);

		textoInferior.setForeground(
				UITheme.COLOR_TEXTO_SECUNDARIO
		);

		gbc.gridy = 8;

		gbc.insets =
				new Insets(
						0,
						28,
						15,
						28
				);

		tarjetaFormulario.add(
				textoInferior,
				gbc
		);

		/*
		 * Mostrar / ocultar contraseña.
		 */
		botonMostrarClave.addActionListener(
				e -> alternarVisibilidadClave()
		);
	}

	private JLabel crearEtiquetaFormulario(
			String texto) {

		JLabel label =
				new JLabel(
						texto
				);

		label.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						12
				)
		);

		label.setForeground(
				UITheme.COLOR_TEXTO
		);

		return label;
	}

	/*
	 * =========================================================
	 * MOSTRAR / OCULTAR CONTRASEÑA
	 * =========================================================
	 */
	private void configurarBotonMostrarClave() {

		botonMostrarClave.setUI(
				new BasicButtonUI()
		);

		botonMostrarClave.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						11
				)
		);

		botonMostrarClave.setForeground(
				UITheme.COLOR_PRIMARIO
		);

		botonMostrarClave.setBackground(
				UITheme.COLOR_SUPERFICIE
		);

		botonMostrarClave.setFocusPainted(
				false
		);

		botonMostrarClave.setBorderPainted(
				false
		);

		botonMostrarClave.setContentAreaFilled(
				false
		);

		botonMostrarClave.setCursor(
				Cursor.getPredefinedCursor(
						Cursor.HAND_CURSOR
				)
		);

		botonMostrarClave.setPreferredSize(
				new Dimension(
						70,
						42
				)
		);
	}

	private void alternarVisibilidadClave() {

		if (campoClave.getEchoChar() == 0) {

			campoClave.setEchoChar(
					echoOriginal
			);

			botonMostrarClave.setText(
					"Mostrar"
			);

		} else {

			campoClave.setEchoChar(
					(char) 0
			);

			botonMostrarClave.setText(
					"Ocultar"
			);
		}
	}

	/*
	 * =========================================================
	 * LOGIN
	 * =========================================================
	 */
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

			campoClave.requestFocus();

			return;
		}

		mostrarBienvenidaConImagen(
				logueado
		);

		dispose();

		switch (logueado.getTipoUsuario()) {

		case EMPLEADO:

			DashboardEmpleadoFrame dashboardEmpleado =
					new DashboardEmpleadoFrame(
							logueado
					);

			dashboardEmpleado.setVisible(
					true
			);

			break;

		case PROFESIONAL:

			DashboardProfesionalFrame dashboardProfesional =
					new DashboardProfesionalFrame(
							logueado
					);

			dashboardProfesional.setVisible(
					true
			);

			break;
		}
	}

	/*
	 * =========================================================
	 * BIENVENIDA
	 * =========================================================
	 */
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
						150,
						150
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
	 * =========================================================
	 * GIF
	 * =========================================================
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
	 * =========================================================
	 * IMAGENES
	 * =========================================================
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

	/*
	 * =========================================================
	 * ICONO APP
	 * =========================================================
	 */
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

	/*
	 * =========================================================
	 * COMPONENTE: PANEL CON DEGRADADO
	 * =========================================================
	 */
	private static class GradientPanel
			extends JPanel {

		private static final long serialVersionUID = 1L;

		public GradientPanel() {

			setOpaque(
					false
			);
		}

		@Override
		protected void paintComponent(
				Graphics g) {

			Graphics2D g2 =
					(Graphics2D) g.create();

			g2.setRenderingHint(
					RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY
			);

			GradientPaint gradient =
					new GradientPaint(
							0,
							0,
							new Color(
									12,
									49,
									68
							),
							getWidth(),
							getHeight(),
							new Color(
									7,
									64,
									83
							)
					);

			g2.setPaint(
					gradient
			);

			g2.fillRect(
					0,
					0,
					getWidth(),
					getHeight()
			);

			/*
			 * Detalle decorativo muy sutil.
			 */
			g2.setColor(
					new Color(
							255,
							255,
							255,
							8
					)
			);

			g2.fillOval(
					-80,
					280,
					260,
					260
			);

			g2.fillOval(
					220,
					-90,
					210,
					210
			);

			g2.dispose();

			super.paintComponent(
					g
			);
		}
	}

	/*
	 * =========================================================
	 * COMPONENTE: TARJETA CON SOMBRA
	 * =========================================================
	 */
	private static class ShadowCard
			extends JPanel {

		private static final long serialVersionUID = 1L;

		public ShadowCard() {

			setOpaque(
					false
			);

			setBorder(
					BorderFactory.createEmptyBorder(
							8,
							8,
							12,
							8
					)
			);
		}

		@Override
		protected void paintComponent(
				Graphics g) {

			Graphics2D g2 =
					(Graphics2D) g.create();

			g2.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON
			);

			/*
			 * Sombra.
			 */
			g2.setColor(
					new Color(
							27,
							52,
							67,
							25
					)
			);

			g2.fillRoundRect(
					8,
					12,
					getWidth() - 16,
					getHeight() - 20,
					22,
					22
			);

			/*
			 * Tarjeta.
			 */
			g2.setColor(
					Color.WHITE
			);

			g2.fillRoundRect(
					8,
					6,
					getWidth() - 16,
					getHeight() - 20,
					22,
					22
			);

			/*
			 * Borde.
			 */
			g2.setColor(
					new Color(
							225,
							233,
							238
					)
			);

			g2.setStroke(
					new BasicStroke(
							1f
					)
			);

			g2.drawRoundRect(
					8,
					6,
					getWidth() - 17,
					getHeight() - 21,
					22,
					22
			);

			g2.dispose();

			super.paintComponent(
					g
			);
		}
	}

	/*
	 * =========================================================
	 * COMPONENTE: CAMPO REDONDEADO
	 * =========================================================
	 */
	private static class RoundedTextField
			extends JTextField {

		private static final long serialVersionUID = 1L;

		public RoundedTextField() {

			setOpaque(
					false
			);

			setFont(
					UITheme.FUENTE_NORMAL
			);

			setForeground(
					UITheme.COLOR_TEXTO
			);

			setCaretColor(
					UITheme.COLOR_PRIMARIO
			);

			setBorder(
					BorderFactory.createEmptyBorder(
							0,
							13,
							0,
							13
					)
			);

			addFocusListener(
					new FocusAdapter() {

						@Override
						public void focusGained(
								FocusEvent e) {

							repaint();
						}

						@Override
						public void focusLost(
								FocusEvent e) {

							repaint();
						}
					}
			);
		}

		@Override
		protected void paintComponent(
				Graphics g) {

			Graphics2D g2 =
					(Graphics2D) g.create();

			g2.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON
			);

			g2.setColor(
					Color.WHITE
			);

			g2.fillRoundRect(
					0,
					0,
					getWidth() - 1,
					getHeight() - 1,
					12,
					12
			);

			if (hasFocus()) {

				g2.setColor(
						UITheme.COLOR_ACENTO
				);

				g2.setStroke(
						new BasicStroke(
								1.6f
						)
				);

			} else {

				g2.setColor(
						UITheme.COLOR_BORDE
				);

				g2.setStroke(
						new BasicStroke(
								1f
						)
				);
			}

			g2.drawRoundRect(
					0,
					0,
					getWidth() - 2,
					getHeight() - 2,
					12,
					12
			);

			g2.dispose();

			super.paintComponent(
					g
			);
		}
	}

	/*
	 * =========================================================
	 * COMPONENTE: PASSWORD REDONDEADO
	 * =========================================================
	 */
	private static class RoundedPasswordField
			extends JPasswordField {

		private static final long serialVersionUID = 1L;

		public RoundedPasswordField() {

			setOpaque(
					false
			);

			setFont(
					UITheme.FUENTE_NORMAL
			);

			setForeground(
					UITheme.COLOR_TEXTO
			);

			setCaretColor(
					UITheme.COLOR_PRIMARIO
			);

			setBorder(
					BorderFactory.createEmptyBorder(
							0,
							13,
							0,
							13
					)
			);

			addFocusListener(
					new FocusAdapter() {

						@Override
						public void focusGained(
								FocusEvent e) {

							repaint();
						}

						@Override
						public void focusLost(
								FocusEvent e) {

							repaint();
						}
					}
			);
		}

		@Override
		protected void paintComponent(
				Graphics g) {

			Graphics2D g2 =
					(Graphics2D) g.create();

			g2.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON
			);

			g2.setColor(
					Color.WHITE
			);

			g2.fillRoundRect(
					0,
					0,
					getWidth() - 1,
					getHeight() - 1,
					12,
					12
			);

			if (hasFocus()) {

				g2.setColor(
						UITheme.COLOR_ACENTO
				);

				g2.setStroke(
						new BasicStroke(
								1.6f
						)
				);

			} else {

				g2.setColor(
						UITheme.COLOR_BORDE
				);

				g2.setStroke(
						new BasicStroke(
								1f
						)
				);
			}

			g2.drawRoundRect(
					0,
					0,
					getWidth() - 2,
					getHeight() - 2,
					12,
					12
			);

			g2.dispose();

			super.paintComponent(
					g
			);
		}
	}

	/*
	 * =========================================================
	 * COMPONENTE: BOTON PREMIUM
	 * =========================================================
	 */
	private static class PremiumButton
			extends JButton {

		private static final long serialVersionUID = 1L;

		private boolean hover;

		public PremiumButton(
				String texto) {

			super(
					texto
			);

			setUI(
					new BasicButtonUI()
			);

			setFont(
					UITheme.FUENTE_BOTON
			);

			setForeground(
					Color.WHITE
			);

			setFocusPainted(
					false
			);

			setBorderPainted(
					false
			);

			setContentAreaFilled(
					false
			);

			setOpaque(
					false
			);

			setCursor(
					Cursor.getPredefinedCursor(
							Cursor.HAND_CURSOR
					)
			);

			addMouseListener(
					new MouseAdapter() {

						@Override
						public void mouseEntered(
								MouseEvent e) {

							hover = true;
							repaint();
						}

						@Override
						public void mouseExited(
								MouseEvent e) {

							hover = false;
							repaint();
						}
					}
			);
		}

		@Override
		protected void paintComponent(
				Graphics g) {

			Graphics2D g2 =
					(Graphics2D) g.create();

			g2.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON
			);

			Color fondo;

			if (getModel().isPressed()) {

				fondo =
						new Color(
								6,
								91,
								111
						);

			} else if (hover) {

				fondo =
						UITheme.COLOR_ACENTO;

			} else {

				fondo =
						UITheme.COLOR_PRIMARIO;
			}

			g2.setColor(
					fondo
			);

			g2.fillRoundRect(
					0,
					0,
					getWidth(),
					getHeight(),
					12,
					12
			);

			g2.dispose();

			super.paintComponent(
					g
			);
		}
	}
}