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
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;

import BLL.Paciente;
import BLL.Profesional;
import BLL.Turno;
import BLL.Usuario;
import DLL.ControllerPaciente;
import DLL.ControllerProfesional;
import DLL.ControllerTurno;

public class DashboardEmpleadoFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO_VENTANA = 1240;
	private static final int ALTO_VENTANA = 760;
	private static final int ANCHO_SIDEBAR = 285;

	private Usuario usuario;

	private ControllerPaciente controllerPaciente;
	private ControllerProfesional controllerProfesional;
	private ControllerTurno controllerTurno;

	private JLabel valorPacientes;
	private JLabel valorProfesionales;
	private JLabel valorTurnosActivos;
	private JLabel valorPresentes;
	private JLabel valorCancelados;

	public DashboardEmpleadoFrame(Usuario usuario) {

		this.usuario = usuario;

		controllerPaciente = new ControllerPaciente();
		controllerProfesional = new ControllerProfesional();
		controllerTurno = new ControllerTurno();

		configurarVentana();
		inicializarComponentes();

		addWindowFocusListener(
				new WindowAdapter() {

					@Override
					public void windowGainedFocus(WindowEvent e) {

						actualizarResumen();
					}
				}
		);

		actualizarResumen();
	}

	private void configurarVentana() {

		setTitle("Telemedicina - Panel de Recepcion");

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

		setLocationRelativeTo(null);
		setResizable(false);

		configurarIconoAplicacion();
	}

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

		panelPrincipal.add(
				crearSidebar(),
				BorderLayout.WEST
		);

		panelPrincipal.add(
				crearContenidoPrincipal(),
				BorderLayout.CENTER
		);
	}

	/*
	 * =========================================================
	 * SIDEBAR
	 * =========================================================
	 */
	private JPanel crearSidebar() {

		JPanel sidebar = new JPanel();

		sidebar.setLayout(
				new BoxLayout(
						sidebar,
						BoxLayout.Y_AXIS
				)
		);

		sidebar.setBackground(
				new Color(
						9,
						41,
						62
				)
		);

		sidebar.setPreferredSize(
				new Dimension(
						ANCHO_SIDEBAR,
						ALTO_VENTANA
				)
		);

		sidebar.setBorder(
				BorderFactory.createEmptyBorder(
						30,
						18,
						24,
						18
				)
		);

		/*
		 * =====================================================
		 * MARCA
		 * =====================================================
		 */
		JPanel panelMarca =
				new JPanel();

		panelMarca.setLayout(
				new BoxLayout(
						panelMarca,
						BoxLayout.Y_AXIS
				)
		);

		panelMarca.setOpaque(false);
		panelMarca.setAlignmentX(LEFT_ALIGNMENT);

		JLabel marca =
				new JLabel(
						"TELEMEDICINA"
				);

		marca.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						22
				)
		);

		marca.setForeground(
				Color.WHITE
		);

		marca.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel modulo =
				new JLabel(
						"Panel de Recepcion"
				);

		modulo.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						14
				)
		);

		modulo.setForeground(
				new Color(
						174,
						207,
						219
				)
		);

		modulo.setAlignmentX(
				LEFT_ALIGNMENT
		);

		panelMarca.add(marca);

		panelMarca.add(
				Box.createVerticalStrut(
						6
				)
		);

		panelMarca.add(modulo);

		sidebar.add(panelMarca);

		sidebar.add(
				Box.createVerticalStrut(
						34
				)
		);

		/*
		 * =====================================================
		 * NAVEGACION CON ICONOS
		 * =====================================================
		 */
		sidebar.add(
				crearBotonSidebar(
						"Dashboard",
						TipoIcono.DASHBOARD,
						true,
						null
				)
		);

		sidebar.add(
				Box.createVerticalStrut(
						12
				)
		);

		sidebar.add(
				crearBotonSidebar(
						"Pacientes",
						TipoIcono.PACIENTES,
						false,
						() -> abrirPacientes()
				)
		);

		sidebar.add(
				Box.createVerticalStrut(
						12
				)
		);

		sidebar.add(
				crearBotonSidebar(
						"Profesionales",
						TipoIcono.PROFESIONALES,
						false,
						() -> abrirProfesionales()
				)
		);

		sidebar.add(
				Box.createVerticalStrut(
						12
				)
		);

		sidebar.add(
				crearBotonSidebar(
						"Turnos",
						TipoIcono.TURNOS,
						false,
						() -> abrirTurnos()
				)
		);

		sidebar.add(
				Box.createVerticalStrut(
						12
				)
		);

		sidebar.add(
				crearBotonSidebar(
						"Consultas e informacion",
						TipoIcono.CONSULTAS,
						false,
						() -> abrirConsultasInformacion()
				)
		);

		sidebar.add(
				Box.createVerticalGlue()
		);

		/*
		 * =====================================================
		 * SEPARADOR DE CUENTA
		 * =====================================================
		 */
		JPanel separadorCuenta =
				new JPanel();

		separadorCuenta.setBackground(
				new Color(
						42,
						82,
						103
				)
		);

		separadorCuenta.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						1
				)
		);

		separadorCuenta.setPreferredSize(
				new Dimension(
						100,
						1
				)
		);

		separadorCuenta.setAlignmentX(
				LEFT_ALIGNMENT
		);

		sidebar.add(
				separadorCuenta
		);

		sidebar.add(
				Box.createVerticalStrut(
						14
				)
		);

		JLabel etiquetaCuenta =
				new JLabel(
						"CUENTA"
				);

		etiquetaCuenta.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						9
				)
		);

		etiquetaCuenta.setForeground(
				new Color(
						112,
						163,
						184
				)
		);

		etiquetaCuenta.setAlignmentX(
				LEFT_ALIGNMENT
		);

		sidebar.add(
				etiquetaCuenta
		);

		sidebar.add(
				Box.createVerticalStrut(
						8
				)
		);

		/*
		 * =====================================================
		 * SESION ACTUAL
		 * =====================================================
		 */
		RoundedPanel tarjetaUsuario =
				new RoundedPanel(
						new Color(
								17,
								56,
								81
						),
						18,
						new Color(
								30,
								82,
								109
						)
				);

		tarjetaUsuario.setLayout(
				new BoxLayout(
						tarjetaUsuario,
						BoxLayout.Y_AXIS
				)
		);

		tarjetaUsuario.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						102
				)
		);

		tarjetaUsuario.setAlignmentX(
				LEFT_ALIGNMENT
		);

		tarjetaUsuario.setBorder(
				BorderFactory.createEmptyBorder(
						14,
						15,
						14,
						15
				)
		);

		JLabel sesion =
				new JLabel(
						"SESION ACTUAL"
				);

		sesion.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						10
				)
		);

		sesion.setForeground(
				new Color(
						24,
						198,
						255
				)
		);

		sesion.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel nombreUsuario =
				new JLabel(
						usuario.getNombre()
								+ " "
								+ usuario.getApellido()
				);

		nombreUsuario.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						13
				)
		);

		nombreUsuario.setForeground(
				Color.WHITE
		);

		nombreUsuario.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel rol =
				new JLabel(
						"Recepcionista"
				);

		rol.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						12
				)
		);

		rol.setForeground(
				new Color(
						194,
						220,
						230
				)
		);

		rol.setAlignmentX(
				LEFT_ALIGNMENT
		);

		tarjetaUsuario.add(sesion);

		tarjetaUsuario.add(
				Box.createVerticalStrut(
						6
				)
		);

		tarjetaUsuario.add(nombreUsuario);

		tarjetaUsuario.add(
				Box.createVerticalStrut(
						4
				)
		);

		tarjetaUsuario.add(rol);

		sidebar.add(
				tarjetaUsuario
		);

		sidebar.add(
				Box.createVerticalStrut(
						14
				)
		);

		sidebar.add(
				crearBotonCerrarSesion()
		);

		return sidebar;
	}

	/*
	 * =========================================================
	 * CONTENIDO
	 * =========================================================
	 */
	private JPanel crearContenidoPrincipal() {

		JPanel contenido =
				new JPanel();

		contenido.setLayout(
				new BoxLayout(
						contenido,
						BoxLayout.Y_AXIS
				)
		);

		contenido.setBackground(
				new Color(
						243,
						247,
						250
				)
		);

		contenido.setBorder(
				BorderFactory.createEmptyBorder(
						28,
						30,
						26,
						30
				)
		);

		contenido.add(
				crearCabeceraSuperior()
		);

		contenido.add(
				Box.createVerticalStrut(
						18
				)
		);

		contenido.add(
				crearHeroPanel()
		);

		contenido.add(
				Box.createVerticalStrut(
						20
				)
		);

		JLabel tituloResumen =
				crearTituloSeccion(
						"Resumen general"
				);

		tituloResumen.setAlignmentX(
				LEFT_ALIGNMENT
		);

		contenido.add(
				tituloResumen
		);

		contenido.add(
				Box.createVerticalStrut(
						12
				)
		);

		JPanel panelMetricas =
				new JPanel(
						new GridLayout(
								1,
								5,
								14,
								0
						)
				);

		panelMetricas.setOpaque(false);

		panelMetricas.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						132
				)
		);

		panelMetricas.setAlignmentX(
				LEFT_ALIGNMENT
		);

		valorPacientes =
				crearValorMetrica();

		valorProfesionales =
				crearValorMetrica();

		valorTurnosActivos =
				crearValorMetrica();

		valorPresentes =
				crearValorMetrica();

		valorCancelados =
				crearValorMetrica();

		panelMetricas.add(
				crearTarjetaMetrica(
						"Pacientes",
						valorPacientes,
						"Pacientes registrados",
						new Color(
								34,
								125,
								210
						)
				)
		);

		panelMetricas.add(
				crearTarjetaMetrica(
						"Profesionales",
						valorProfesionales,
						"Perfiles profesionales",
						new Color(
								0,
								170,
								210
						)
				)
		);

		panelMetricas.add(
				crearTarjetaMetrica(
						"Activos",
						valorTurnosActivos,
						"Turnos no cancelados",
						new Color(
								84,
								114,
								255
						)
				)
		);

		panelMetricas.add(
				crearTarjetaMetrica(
						"Presentes",
						valorPresentes,
						"Pacientes con llegada",
						new Color(
								22,
								163,
								74
						)
				)
		);

		panelMetricas.add(
				crearTarjetaMetrica(
						"Cancelados",
						valorCancelados,
						"Turnos anulados",
						new Color(
								220,
								38,
								38
						)
				)
		);

		contenido.add(
				panelMetricas
		);

		contenido.add(
				Box.createVerticalStrut(
						24
				)
		);

		JLabel tituloAccesos =
				crearTituloSeccion(
						"Accesos rapidos"
				);

		tituloAccesos.setAlignmentX(
				LEFT_ALIGNMENT
		);

		contenido.add(
				tituloAccesos
		);

		contenido.add(
				Box.createVerticalStrut(
						12
				)
		);

		JPanel panelAccesos =
				new JPanel(
						new GridLayout(
								2,
								2,
								14,
								14
						)
				);

		panelAccesos.setOpaque(false);

		panelAccesos.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						260
				)
		);

		panelAccesos.setAlignmentX(
				LEFT_ALIGNMENT
		);

		panelAccesos.add(
				new ActionButton(
						"Pacientes",
						"Administrar altas, modificaciones y datos personales de pacientes.",
						() -> abrirPacientes()
				)
		);

		panelAccesos.add(
				new ActionButton(
						"Profesionales",
						"Registrar profesionales y consultar datos de atencion y disponibilidad.",
						() -> abrirProfesionales()
				)
		);

		panelAccesos.add(
				new ActionButton(
						"Turnos",
						"Asignar, confirmar, cancelar y registrar llegadas de pacientes.",
						() -> abrirTurnos()
				)
		);

		panelAccesos.add(
				new ActionButton(
						"Consultas e informacion",
						"Consultar historiales, atenciones registradas y retrasos informados.",
						() -> abrirConsultasInformacion()
				)
		);

		contenido.add(
				panelAccesos
		);

		contenido.add(
				Box.createVerticalStrut(
						22
				)
		);

		RoundedPanel estado =
				new RoundedPanel(
						new Color(
								236,
								248,
								244
						),
						16,
						new Color(
								191,
								227,
								210
						)
				);

		estado.setLayout(
				new BorderLayout()
		);

		estado.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						56
				)
		);

		estado.setAlignmentX(
				LEFT_ALIGNMENT
		);

		estado.setBorder(
				BorderFactory.createEmptyBorder(
						12,
						16,
						12,
						16
				)
		);

		JLabel sistema =
				new JLabel(
						"●  Sistema conectado y listo para operar"
				);

		sistema.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						12
				)
		);

		sistema.setForeground(
				new Color(
						29,
						122,
						84
				)
		);

		JLabel perfil =
				new JLabel(
						"Perfil: EMPLEADO"
				);

		perfil.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						10
				)
		);

		perfil.setForeground(
				new Color(
						91,
						109,
						127
				)
		);

		estado.add(
				sistema,
				BorderLayout.WEST
		);

		estado.add(
				perfil,
				BorderLayout.EAST
		);

		contenido.add(
				estado
		);

		return contenido;
	}

	private JPanel crearCabeceraSuperior() {

		JPanel cabecera =
				new JPanel(
						new BorderLayout()
				);

		cabecera.setOpaque(false);

		cabecera.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						82
				)
		);

		JPanel textos =
				new JPanel();

		textos.setLayout(
				new BoxLayout(
						textos,
						BoxLayout.Y_AXIS
				)
		);

		textos.setOpaque(false);

		JLabel bienvenida =
				new JLabel(
						"Buenos dias, "
								+ usuario.getNombre()
				);

		bienvenida.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						30
				)
		);

		bienvenida.setForeground(
				new Color(
						15,
						23,
						42
				)
		);

		JLabel descripcion =
				new JLabel(
						"Resumen operativo del sistema de Telemedicina"
				);

		descripcion.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						13
				)
		);

		descripcion.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		textos.add(
				bienvenida
		);

		textos.add(
				Box.createVerticalStrut(
						5
				)
		);

		textos.add(
				descripcion
		);

		RoundedPanel badge =
				new RoundedPanel(
						new Color(
								227,
								244,
								249
						),
						16,
						null
				);

		badge.setLayout(
				new BorderLayout()
		);

		badge.setPreferredSize(
				new Dimension(
						92,
						52
				)
		);

		JLabel textoBadge =
				new JLabel(
						"RECEPCION",
						SwingConstants.CENTER
				);

		textoBadge.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						10
				)
		);

		textoBadge.setForeground(
				new Color(
						8,
						78,
						118
				)
		);

		badge.add(
				textoBadge,
				BorderLayout.CENTER
		);

		cabecera.add(
				textos,
				BorderLayout.WEST
		);

		cabecera.add(
				badge,
				BorderLayout.EAST
		);

		return cabecera;
	}

	private JPanel crearHeroPanel() {

		GradientPanel hero =
				new GradientPanel(
						new Color(
								13,
								47,
								78
						),
						new Color(
								19,
								127,
								166
						)
				);

		hero.setLayout(
				new BorderLayout()
		);

		hero.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						120
				)
		);

		hero.setAlignmentX(
				LEFT_ALIGNMENT
		);

		hero.setBorder(
				BorderFactory.createEmptyBorder(
						18,
						22,
						18,
						22
				)
		);

		JPanel izquierda =
				new JPanel();

		izquierda.setLayout(
				new BoxLayout(
						izquierda,
						BoxLayout.Y_AXIS
				)
		);

		izquierda.setOpaque(false);

		JLabel titulo =
				new JLabel(
						"Panel principal de recepcion"
				);

		titulo.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						22
				)
		);

		titulo.setForeground(
				Color.WHITE
		);

		JLabel subtitulo =
				new JLabel(
						"Gestion centralizada de pacientes, profesionales y turnos."
				);

		subtitulo.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						12
				)
		);

		subtitulo.setForeground(
				new Color(
						221,
						240,
						247
				)
		);

		izquierda.add(
				titulo
		);

		izquierda.add(
				Box.createVerticalStrut(
						8
				)
		);

		izquierda.add(
				subtitulo
		);

		JPanel derecha =
				new JPanel();

		derecha.setLayout(
				new BoxLayout(
						derecha,
						BoxLayout.Y_AXIS
				)
		);

		derecha.setOpaque(false);

		/*
		 * Locale moderno.
		 * Evita el constructor deprecated de Locale.
		 */
		String fechaHoy =
				LocalDate.now()
						.format(
								DateTimeFormatter.ofPattern(
										"EEEE dd 'de' MMMM",
										Locale.forLanguageTag(
												"es-ES"
										)
								)
						);

		JLabel etiqueta =
				new JLabel(
						"HOY"
				);

		etiqueta.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						10
				)
		);

		etiqueta.setForeground(
				new Color(
						201,
						244,
						255
				)
		);

		etiqueta.setAlignmentX(
				RIGHT_ALIGNMENT
		);

		JLabel fecha =
				new JLabel(
						capitalizar(
								fechaHoy
						)
				);

		fecha.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						15
				)
		);

		fecha.setForeground(
				Color.WHITE
		);

		fecha.setAlignmentX(
				RIGHT_ALIGNMENT
		);

		JLabel linea =
				new JLabel(
						"Operacion lista"
				);

		linea.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						11
				)
		);

		linea.setForeground(
				new Color(
						220,
						244,
						250
				)
		);

		linea.setAlignmentX(
				RIGHT_ALIGNMENT
		);

		derecha.add(
				etiqueta
		);

		derecha.add(
				Box.createVerticalStrut(
						6
				)
		);

		derecha.add(
				fecha
		);

		derecha.add(
				Box.createVerticalStrut(
						4
				)
		);

		derecha.add(
				linea
		);

		hero.add(
				izquierda,
				BorderLayout.WEST
		);

		hero.add(
				derecha,
				BorderLayout.EAST
		);

		return hero;
	}

	private JLabel crearTituloSeccion(
			String texto) {

		JLabel label =
				new JLabel(
						texto
				);

		label.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						16
				)
		);

		label.setForeground(
				new Color(
						15,
						23,
						42
				)
		);

		return label;
	}

	private JLabel crearValorMetrica() {

		JLabel label =
				new JLabel(
						"0"
				);

		label.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						30
				)
		);

		label.setForeground(
				new Color(
						15,
						23,
						42
				)
		);

		return label;
	}

	private JPanel crearTarjetaMetrica(
			String titulo,
			JLabel valor,
			String detalle,
			Color colorAcento) {

		RoundedPanel tarjeta =
				new RoundedPanel(
						Color.WHITE,
						18,
						new Color(
								220,
								228,
								236
						)
				);

		tarjeta.setLayout(
				new BoxLayout(
						tarjeta,
						BoxLayout.Y_AXIS
				)
		);

		tarjeta.setBorder(
				BorderFactory.createEmptyBorder(
						15,
						17,
						15,
						17
				)
		);

		JLabel etiqueta =
				new JLabel(
						"●  " + titulo
				);

		etiqueta.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						11
				)
		);

		etiqueta.setForeground(
				colorAcento
		);

		etiqueta.setAlignmentX(
				LEFT_ALIGNMENT
		);

		valor.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel descripcion =
				new JLabel(
						detalle
				);

		descripcion.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						10
				)
		);

		descripcion.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		descripcion.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JPanel barra =
				new JPanel() {

					private static final long serialVersionUID = 1L;

					@Override
					protected void paintComponent(Graphics g) {

						super.paintComponent(g);

						Graphics2D g2 =
								(Graphics2D) g.create();

						g2.setRenderingHint(
								RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON
						);

						g2.setColor(
								new Color(
										233,
										239,
										245
								)
						);

						g2.fillRoundRect(
								0,
								4,
								getWidth(),
								6,
								6,
								6
						);

						g2.setColor(
								colorAcento
						);

						g2.fillRoundRect(
								0,
								4,
								Math.max(
										40,
										getWidth() / 2
								),
								6,
								6,
								6
						);

						g2.dispose();
					}
				};

		barra.setOpaque(false);

		barra.setPreferredSize(
				new Dimension(
						100,
						14
				)
		);

		barra.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						14
				)
		);

		barra.setAlignmentX(
				LEFT_ALIGNMENT
		);

		tarjeta.add(etiqueta);

		tarjeta.add(
				Box.createVerticalStrut(
						7
				)
		);

		tarjeta.add(valor);

		tarjeta.add(
				Box.createVerticalStrut(
						4
				)
		);

		tarjeta.add(descripcion);

		tarjeta.add(
				Box.createVerticalStrut(
						10
				)
		);

		tarjeta.add(barra);

		return tarjeta;
	}

	private void actualizarResumen() {

		try {

			LinkedList<Paciente> pacientes =
					controllerPaciente.buscar("");

			LinkedList<Profesional> profesionales =
					controllerProfesional.buscar("");

			LinkedList<Turno> turnos =
					controllerTurno.obtenerTodos();

			int activos = 0;
			int presentes = 0;
			int cancelados = 0;

			for (Turno turno : turnos) {

				if (turno.estaCancelado()) {

					cancelados++;

				} else {

					activos++;
				}

				if (turno.estaPresente()) {

					presentes++;
				}
			}

			valorPacientes.setText(
					String.valueOf(
							pacientes.size()
					)
			);

			valorProfesionales.setText(
					String.valueOf(
							profesionales.size()
					)
			);

			valorTurnosActivos.setText(
					String.valueOf(
							activos
					)
			);

			valorPresentes.setText(
					String.valueOf(
							presentes
					)
			);

			valorCancelados.setText(
					String.valueOf(
							cancelados
					)
			);

		} catch (Exception e) {

			valorPacientes.setText("-");
			valorProfesionales.setText("-");
			valorTurnosActivos.setText("-");
			valorPresentes.setText("-");
			valorCancelados.setText("-");

			e.printStackTrace();
		}
	}

	/*
	 * =========================================================
	 * BOTONES SIDEBAR
	 * =========================================================
	 */
	private JButton crearBotonSidebar(
			String texto,
			TipoIcono tipoIcono,
			boolean activo,
			Runnable accion) {

		SidebarButton boton =
				new SidebarButton(
						texto,
						tipoIcono,
						activo
				);

		if (accion != null) {

			boton.addActionListener(
					e -> accion.run()
			);
		}

		boton.setAlignmentX(
				LEFT_ALIGNMENT
		);

		boton.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						54
				)
		);

		boton.setPreferredSize(
				new Dimension(
						0,
						54
				)
		);

		return boton;
	}

	private JButton crearBotonCerrarSesion() {

		JButton boton =
				new JButton(
						"Cerrar sesion"
				);

		boton.setUI(
				new BasicButtonUI()
		);

		boton.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						13
				)
		);

		boton.setForeground(
				Color.WHITE
		);

		boton.setBackground(
				new Color(
						18,
						63,
						92
				)
		);

		boton.setFocusPainted(false);
		boton.setBorderPainted(false);

		boton.setCursor(
				Cursor.getPredefinedCursor(
						Cursor.HAND_CURSOR
				)
		);

		boton.setAlignmentX(
				LEFT_ALIGNMENT
		);

		boton.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						46
				)
		);

		boton.setPreferredSize(
				new Dimension(
						0,
						46
				)
		);

		boton.addActionListener(
				e -> cerrarSesion()
		);

		return boton;
	}

	/*
	 * =========================================================
	 * NAVEGACION
	 * =========================================================
	 */
	private void abrirPacientes() {

		setVisible(false);

		PacientesFrame frame =
				new PacientesFrame(
						this
				);

		frame.setVisible(true);
	}

	private void abrirProfesionales() {

		setVisible(false);

		ProfesionalesFrame frame =
				new ProfesionalesFrame(
						this
				);

		frame.setVisible(true);
	}

	private void abrirTurnos() {

		setVisible(false);

		TurnosFrame frame =
				new TurnosFrame(
						this
				);

		frame.setVisible(true);
	}

	private void abrirConsultasInformacion() {

		setVisible(false);

		ConsultasInformacionFrame frame =
				new ConsultasInformacionFrame(
						this
				);

		frame.setVisible(true);
	}

	private void cerrarSesion() {

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea cerrar la sesion actual?",
						"Cerrar sesion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		dispose();

		LoginFrame login =
				new LoginFrame();

		login.setVisible(true);
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

	private String capitalizar(
			String texto) {

		if (texto == null
				|| texto.isEmpty()) {

			return texto;
		}

		return texto.substring(
				0,
				1
		).toUpperCase()
				+ texto.substring(
						1
				);
	}

	/*
	 * =========================================================
	 * TIPOS DE ICONOS
	 * =========================================================
	 */
	private enum TipoIcono {

		DASHBOARD,
		PACIENTES,
		PROFESIONALES,
		TURNOS,
		CONSULTAS
	}

	/*
	 * =========================================================
	 * PANEL REDONDEADO
	 * =========================================================
	 */
	private static class RoundedPanel
			extends JPanel {

		private static final long serialVersionUID = 1L;

		private Color colorFondo;
		private int radio;
		private Color colorBorde;

		public RoundedPanel(
				Color colorFondo,
				int radio,
				Color colorBorde) {

			this.colorFondo =
					colorFondo;

			this.radio =
					radio;

			this.colorBorde =
					colorBorde;

			setOpaque(false);
		}

		@Override
		protected void paintComponent(
				Graphics g) {

			super.paintComponent(g);

			Graphics2D g2 =
					(Graphics2D) g.create();

			g2.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON
			);

			g2.setColor(
					colorFondo
			);

			g2.fillRoundRect(
					0,
					0,
					getWidth() - 1,
					getHeight() - 1,
					radio,
					radio
			);

			if (colorBorde != null) {

				g2.setColor(
						colorBorde
				);

				g2.setStroke(
						new BasicStroke(
								1f
						)
				);

				g2.drawRoundRect(
						0,
						0,
						getWidth() - 2,
						getHeight() - 2,
						radio,
						radio
				);
			}

			g2.dispose();
		}
	}

	/*
	 * =========================================================
	 * HERO DEGRADADO
	 * =========================================================
	 */
	private static class GradientPanel
			extends RoundedPanel {

		private static final long serialVersionUID = 1L;

		private Color colorInicio;
		private Color colorFin;

		public GradientPanel(
				Color colorInicio,
				Color colorFin) {

			super(
					new Color(
							0,
							0,
							0,
							0
					),
					20,
					null
			);

			this.colorInicio =
					colorInicio;

			this.colorFin =
					colorFin;

			setOpaque(false);
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

			GradientPaint gp =
					new GradientPaint(
							0,
							0,
							colorInicio,
							getWidth(),
							getHeight(),
							colorFin
					);

			g2.setPaint(
					gp
			);

			g2.fillRoundRect(
					0,
					0,
					getWidth(),
					getHeight(),
					22,
					22
			);

			g2.setColor(
					new Color(
							255,
							255,
							255,
							35
					)
			);

			g2.drawRoundRect(
					0,
					0,
					getWidth() - 1,
					getHeight() - 1,
					22,
					22
			);

			g2.dispose();

			super.paintComponent(g);
		}
	}

	/*
	 * =========================================================
	 * BOTON DE NAVEGACION
	 * =========================================================
	 */
	private static class SidebarButton
			extends JButton {

		private static final long serialVersionUID = 1L;

		private boolean activo;
		private boolean hover;

		public SidebarButton(
				String texto,
				TipoIcono tipoIcono,
				boolean activo) {

			super(texto);

			this.activo =
					activo;

			setUI(
					new BasicButtonUI()
			);

			setFont(
					new Font(
							"Segoe UI",
							Font.BOLD,
							14
					)
			);

			setForeground(
					Color.WHITE
			);

			setHorizontalAlignment(
					SwingConstants.LEFT
			);

			setIcon(
					new NavIcon(
							tipoIcono
					)
			);

			setIconTextGap(
					14
			);

			setBorder(
					BorderFactory.createEmptyBorder(
							0,
							17,
							0,
							14
					)
			);

			setFocusPainted(false);
			setContentAreaFilled(false);
			setOpaque(false);

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

			if (activo || hover) {

				g2.setColor(
						new Color(
								21,
								67,
								94
						)
				);

				g2.fillRoundRect(
						0,
						0,
						getWidth(),
						getHeight(),
						14,
						14
				);
			}

			if (activo) {

				g2.setColor(
						new Color(
								38,
								198,
								255
						)
				);

				g2.fillRoundRect(
						0,
						10,
						5,
						getHeight() - 20,
						5,
						5
				);
			}

			g2.dispose();

			super.paintComponent(g);
		}
	}

	/*
	 * =========================================================
	 * ICONOS VECTORIALES DEL SIDEBAR
	 * =========================================================
	 */
	private static class NavIcon
			implements Icon {

		private final TipoIcono tipo;

		private static final int SIZE = 20;

		public NavIcon(
				TipoIcono tipo) {

			this.tipo =
					tipo;
		}

		@Override
		public int getIconWidth() {

			return SIZE;
		}

		@Override
		public int getIconHeight() {

			return SIZE;
		}

		@Override
		public void paintIcon(
				java.awt.Component c,
				Graphics g,
				int x,
				int y) {

			Graphics2D g2 =
					(Graphics2D) g.create();

			g2.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON
			);

			g2.setStroke(
					new BasicStroke(
							1.8f,
							BasicStroke.CAP_ROUND,
							BasicStroke.JOIN_ROUND
					)
			);

			boolean destacado =
					false;

			if (c instanceof SidebarButton) {

				SidebarButton boton =
						(SidebarButton) c;

				destacado =
						boton.activo
								|| boton.hover;
			}

			if (destacado) {

				g2.setColor(
						new Color(
								68,
								211,
								255
						)
				);

			} else {

				g2.setColor(
						new Color(
								190,
								215,
								225
						)
				);
			}

			switch (tipo) {

			case DASHBOARD:

				dibujarDashboard(
						g2,
						x,
						y
				);

				break;

			case PACIENTES:

				dibujarPaciente(
						g2,
						x,
						y
				);

				break;

			case PROFESIONALES:

				dibujarProfesional(
						g2,
						x,
						y
				);

				break;

			case TURNOS:

				dibujarCalendario(
						g2,
						x,
						y
				);

				break;

			case CONSULTAS:

				dibujarDocumento(
						g2,
						x,
						y
				);

				break;
			}

			g2.dispose();
		}

		private void dibujarDashboard(
				Graphics2D g2,
				int x,
				int y) {

			g2.drawRoundRect(
					x + 1,
					y + 1,
					7,
					7,
					2,
					2
			);

			g2.drawRoundRect(
					x + 12,
					y + 1,
					7,
					7,
					2,
					2
			);

			g2.drawRoundRect(
					x + 1,
					y + 12,
					7,
					7,
					2,
					2
			);

			g2.drawRoundRect(
					x + 12,
					y + 12,
					7,
					7,
					2,
					2
			);
		}

		private void dibujarPaciente(
				Graphics2D g2,
				int x,
				int y) {

			g2.drawOval(
					x + 6,
					y + 1,
					8,
					8
			);

			g2.drawArc(
					x + 3,
					y + 10,
					14,
					10,
					0,
					180
			);
		}

		private void dibujarProfesional(
				Graphics2D g2,
				int x,
				int y) {

			g2.drawOval(
					x + 6,
					y + 1,
					8,
					8
			);

			g2.drawArc(
					x + 3,
					y + 10,
					14,
					10,
					0,
					180
			);

			g2.drawLine(
					x + 15,
					y + 11,
					x + 15,
					y + 17
			);

			g2.drawLine(
					x + 12,
					y + 14,
					x + 18,
					y + 14
			);
		}

		private void dibujarCalendario(
				Graphics2D g2,
				int x,
				int y) {

			g2.drawRoundRect(
					x + 2,
					y + 4,
					16,
					14,
					3,
					3
			);

			g2.drawLine(
					x + 2,
					y + 8,
					x + 18,
					y + 8
			);

			g2.drawLine(
					x + 6,
					y + 1,
					x + 6,
					y + 6
			);

			g2.drawLine(
					x + 14,
					y + 1,
					x + 14,
					y + 6
			);

			g2.fillOval(
					x + 6,
					y + 11,
					2,
					2
			);

			g2.fillOval(
					x + 12,
					y + 11,
					2,
					2
			);
		}

		private void dibujarDocumento(
				Graphics2D g2,
				int x,
				int y) {

			g2.drawRoundRect(
					x + 3,
					y + 1,
					14,
					18,
					3,
					3
			);

			g2.drawLine(
					x + 7,
					y + 7,
					x + 14,
					y + 7
			);

			g2.drawLine(
					x + 7,
					y + 11,
					x + 14,
					y + 11
			);

			g2.drawLine(
					x + 7,
					y + 15,
					x + 12,
					y + 15
			);
		}
	}

	/*
	 * =========================================================
	 * ACCESOS RAPIDOS
	 * =========================================================
	 */
	private static class ActionButton
			extends JButton {

		private static final long serialVersionUID = 1L;

		private boolean hover;

		public ActionButton(
				String titulo,
				String descripcion,
				Runnable accion) {

			super(
					"<html>"
							+ "<div style='width:300px;'>"
							+ "<b style='font-size:14px; color:#0F172A;'>"
							+ titulo
							+ "</b>"
							+ "<br><br>"
							+ "<span style='font-size:10px; color:#64748B;'>"
							+ descripcion
							+ "</span>"
							+ "</div>"
							+ "</html>"
			);

			setUI(
					new BasicButtonUI()
			);

			setFont(
					new Font(
							"Segoe UI",
							Font.PLAIN,
							12
					)
			);

			setForeground(
					new Color(
							15,
							23,
							42
					)
			);

			setHorizontalAlignment(
					SwingConstants.LEFT
			);

			setVerticalAlignment(
					SwingConstants.CENTER
			);

			setBorder(
					BorderFactory.createEmptyBorder(
							18,
							20,
							18,
							20
					)
			);

			setFocusPainted(false);
			setContentAreaFilled(false);
			setOpaque(false);

			setCursor(
					Cursor.getPredefinedCursor(
							Cursor.HAND_CURSOR
					)
			);

			addActionListener(
					e -> accion.run()
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

			if (hover) {

				g2.setColor(
						new Color(
								247,
								251,
								253
						)
				);

			} else {

				g2.setColor(
						Color.WHITE
				);
			}

			g2.fillRoundRect(
					0,
					0,
					getWidth() - 1,
					getHeight() - 1,
					18,
					18
			);

			if (hover) {

				g2.setColor(
						new Color(
								24,
								198,
								255
						)
				);

			} else {

				g2.setColor(
						new Color(
								220,
								228,
								236
						)
				);
			}

			g2.setStroke(
					new BasicStroke(
							1f
					)
			);

			g2.drawRoundRect(
					0,
					0,
					getWidth() - 2,
					getHeight() - 2,
					18,
					18
			);

			g2.dispose();

			super.paintComponent(g);
		}
	}
}