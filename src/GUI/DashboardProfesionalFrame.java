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

import BLL.Consulta;
import BLL.Disponibilidad;
import BLL.Profesional;
import BLL.Turno;
import BLL.Usuario;
import DLL.ControllerConsulta;
import DLL.ControllerDisponibilidad;
import DLL.ControllerProfesional;
import DLL.ControllerTurno;

public class DashboardProfesionalFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO_VENTANA = 1240;
	private static final int ALTO_VENTANA = 760;
	private static final int ANCHO_SIDEBAR = 300;

	private Usuario usuario;
	private Profesional profesional;

	private ControllerProfesional controllerProfesional;
	private ControllerDisponibilidad controllerDisponibilidad;
	private ControllerTurno controllerTurno;
	private ControllerConsulta controllerConsulta;

	private JLabel valorDisponibilidades;
	private JLabel valorTurnosActivos;
	private JLabel valorPresentes;
	private JLabel valorConsultas;
	private JLabel valorRetrasos;

	public DashboardProfesionalFrame(Usuario usuario) {

		this.usuario = usuario;

		controllerProfesional =
				new ControllerProfesional();

		controllerDisponibilidad =
				new ControllerDisponibilidad();

		controllerTurno =
				new ControllerTurno();

		controllerConsulta =
				new ControllerConsulta();

		profesional =
				controllerProfesional.obtenerPorUsuario(
						usuario.getIdUsuario()
				);

		configurarVentana();
		inicializarComponentes();

		addWindowFocusListener(
				new WindowAdapter() {

					@Override
					public void windowGainedFocus(
							WindowEvent e) {

						actualizarResumen();
					}
				}
		);

		actualizarResumen();
	}

	/*
	 * =========================================================
	 * VENTANA
	 * =========================================================
	 */
	private void configurarVentana() {

		setTitle(
				"Telemedicina - Panel Profesional - "
						+ usuario.getNombre()
						+ " "
						+ usuario.getApellido()
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

		setLocationRelativeTo(null);
		setResizable(false);

		configurarIconoAplicacion();
	}

	/*
	 * =========================================================
	 * INTERFAZ
	 * =========================================================
	 */
	private void inicializarComponentes() {

		JPanel principal =
				new JPanel(
						new BorderLayout()
				);

		principal.setBackground(
				UITheme.COLOR_FONDO
		);

		setContentPane(
				principal
		);

		principal.add(
				crearSidebar(),
				BorderLayout.WEST
		);

		principal.add(
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

		JPanel sidebar =
				new JPanel();

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
		 * MARCA
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
						"Panel Profesional"
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
		panelMarca.add(Box.createVerticalStrut(6));
		panelMarca.add(modulo);

		sidebar.add(
				panelMarca
		);

		sidebar.add(
				Box.createVerticalStrut(
						34
				)
		);

		/*
		 * =====================================================
		 * NAVEGACION
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
						"Disponibilidad",
						TipoIcono.DISPONIBILIDAD,
						false,
						() -> abrirDisponibilidad()
				)
		);

		sidebar.add(
				Box.createVerticalStrut(
						12
				)
		);

		sidebar.add(
				crearBotonSidebar(
						"Agenda de turnos",
						TipoIcono.AGENDA,
						false,
						() -> abrirAgenda()
				)
		);

		sidebar.add(
				Box.createVerticalStrut(
						12
				)
		);

		sidebar.add(
				crearBotonSidebar(
						"Registrar retraso",
						TipoIcono.RETRASO,
						false,
						() -> abrirRetraso()
				)
		);

		sidebar.add(
				Box.createVerticalStrut(
						12
				)
		);

		sidebar.add(
				crearBotonSidebar(
						"Registrar consulta",
						TipoIcono.CONSULTA,
						false,
						() -> abrirRegistrarConsulta()
				)
		);

		sidebar.add(
				Box.createVerticalStrut(
						12
				)
		);

		sidebar.add(
				crearBotonSidebar(
						"Historial de consultas",
						TipoIcono.HISTORIAL,
						false,
						() -> abrirHistorial()
				)
		);

		sidebar.add(
				Box.createVerticalGlue()
		);

		/*
		 * SEPARADOR
		 */
		JPanel separador =
				new JPanel();

		separador.setBackground(
				new Color(
						42,
						82,
						103
				)
		);

		separador.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						1
				)
		);

		separador.setAlignmentX(
				LEFT_ALIGNMENT
		);

		sidebar.add(separador);
		sidebar.add(Box.createVerticalStrut(12));

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

		sidebar.add(etiquetaCuenta);
		sidebar.add(Box.createVerticalStrut(7));

		/*
		 * SESION
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
						105
				)
		);

		tarjetaUsuario.setAlignmentX(
				LEFT_ALIGNMENT
		);

		tarjetaUsuario.setBorder(
				BorderFactory.createEmptyBorder(
						13,
						15,
						13,
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

		JLabel nombre =
				new JLabel(
						usuario.getNombre()
								+ " "
								+ usuario.getApellido()
				);

		nombre.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						13
				)
		);

		nombre.setForeground(
				Color.WHITE
		);

		nombre.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel rol =
				new JLabel(
						"Profesional"
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
		tarjetaUsuario.add(Box.createVerticalStrut(6));
		tarjetaUsuario.add(nombre);
		tarjetaUsuario.add(Box.createVerticalStrut(4));
		tarjetaUsuario.add(rol);

		sidebar.add(tarjetaUsuario);
		sidebar.add(Box.createVerticalStrut(12));

		sidebar.add(
				crearBotonCerrarSesion()
		);

		return sidebar;
	}

	/*
	 * =========================================================
	 * CONTENIDO PRINCIPAL
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
				crearCabecera()
		);

		contenido.add(
				Box.createVerticalStrut(
						18
				)
		);

		contenido.add(
				crearHero()
		);

		contenido.add(
				Box.createVerticalStrut(
						20
				)
		);

		JLabel tituloResumen =
				crearTituloSeccion(
						"Resumen clinico"
				);

		tituloResumen.setAlignmentX(
				LEFT_ALIGNMENT
		);

		contenido.add(tituloResumen);
		contenido.add(Box.createVerticalStrut(12));

		/*
		 * METRICAS
		 */
		JPanel metricas =
				new JPanel(
						new GridLayout(
								1,
								5,
								14,
								0
						)
				);

		metricas.setOpaque(false);

		metricas.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						132
				)
		);

		metricas.setAlignmentX(
				LEFT_ALIGNMENT
		);

		valorDisponibilidades =
				crearValorMetrica();

		valorTurnosActivos =
				crearValorMetrica();

		valorPresentes =
				crearValorMetrica();

		valorConsultas =
				crearValorMetrica();

		valorRetrasos =
				crearValorMetrica();

		metricas.add(
				crearTarjetaMetrica(
						"Disponibilidades",
						valorDisponibilidades,
						"Horarios registrados",
						new Color(
								34,
								125,
								210
						)
				)
		);

		metricas.add(
				crearTarjetaMetrica(
						"Turnos activos",
						valorTurnosActivos,
						"Turnos no cancelados",
						new Color(
								84,
								114,
								255
						)
				)
		);

		metricas.add(
				crearTarjetaMetrica(
						"Presentes",
						valorPresentes,
						"Pacientes en atencion",
						new Color(
								22,
								163,
								74
						)
				)
		);

		metricas.add(
				crearTarjetaMetrica(
						"Consultas",
						valorConsultas,
						"Atenciones registradas",
						new Color(
								0,
								170,
								210
						)
				)
		);

		metricas.add(
				crearTarjetaMetrica(
						"Retrasos",
						valorRetrasos,
						"Disponibilidades con demora",
						new Color(
								230,
								146,
								35
						)
				)
		);

		contenido.add(metricas);
		contenido.add(Box.createVerticalStrut(24));

		/*
		 * ACCESOS RAPIDOS
		 */
		JLabel tituloAccesos =
				crearTituloSeccion(
						"Gestion profesional"
				);

		tituloAccesos.setAlignmentX(
				LEFT_ALIGNMENT
		);

		contenido.add(tituloAccesos);
		contenido.add(Box.createVerticalStrut(12));

		JPanel accesos =
				new JPanel(
						new GridLayout(
								2,
								3,
								14,
								14
						)
				);

		accesos.setOpaque(false);

		accesos.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						250
				)
		);

		accesos.setAlignmentX(
				LEFT_ALIGNMENT
		);

		accesos.add(
				new ActionButton(
						"Disponibilidad",
						"Gestionar dias y horarios disponibles para atencion.",
						() -> abrirDisponibilidad()
				)
		);

		accesos.add(
				new ActionButton(
						"Agenda",
						"Consultar los turnos asociados a su disponibilidad.",
						() -> abrirAgenda()
				)
		);

		accesos.add(
				new ActionButton(
						"Retraso estimado",
						"Informar demoras para mantener actualizado al sistema.",
						() -> abrirRetraso()
				)
		);

		accesos.add(
				new ActionButton(
						"Registrar consulta",
						"Documentar observaciones y seguimiento del paciente.",
						() -> abrirRegistrarConsulta()
				)
		);

		accesos.add(
				new ActionButton(
						"Historial",
						"Consultar las atenciones realizadas anteriormente.",
						() -> abrirHistorial()
				)
		);

		accesos.add(
				new InfoCard(
						"Perfil profesional",
						obtenerTextoEspecialidad()
				)
		);

		contenido.add(accesos);
		contenido.add(Box.createVerticalStrut(22));

		/*
		 * ESTADO
		 */
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

		JLabel textoEstado =
				new JLabel(
						"●  Sesion profesional activa y sistema listo para operar"
				);

		textoEstado.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						12
				)
		);

		textoEstado.setForeground(
				new Color(
						29,
						122,
						84
				)
		);

		JLabel perfil =
				new JLabel(
						"Perfil: PROFESIONAL"
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
				textoEstado,
				BorderLayout.WEST
		);

		estado.add(
				perfil,
				BorderLayout.EAST
		);

		contenido.add(estado);

		return contenido;
	}

	/*
	 * =========================================================
	 * CABECERA
	 * =========================================================
	 */
	private JPanel crearCabecera() {

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
						"Panel clinico y gestion de atenciones profesionales"
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

		textos.add(bienvenida);
		textos.add(Box.createVerticalStrut(5));
		textos.add(descripcion);

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
						105,
						52
				)
		);

		JLabel textoBadge =
				new JLabel(
						"PROFESIONAL",
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

	/*
	 * =========================================================
	 * HERO
	 * =========================================================
	 */
	private JPanel crearHero() {

		GradientPanel hero =
				new GradientPanel(
						new Color(
								13,
								47,
								78
						),
						new Color(
								16,
								139,
								168
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
						"Panel clinico profesional"
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

		JLabel especialidad =
				new JLabel(
						obtenerTextoEspecialidad()
				);

		especialidad.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						12
				)
		);

		especialidad.setForeground(
				new Color(
						221,
						240,
						247
				)
		);

		izquierda.add(titulo);
		izquierda.add(Box.createVerticalStrut(8));
		izquierda.add(especialidad);

		JPanel derecha =
				new JPanel();

		derecha.setLayout(
				new BoxLayout(
						derecha,
						BoxLayout.Y_AXIS
				)
		);

		derecha.setOpaque(false);

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

		JLabel hoy =
				new JLabel(
						"HOY"
				);

		hoy.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						10
				)
		);

		hoy.setForeground(
				new Color(
						201,
						244,
						255
				)
		);

		hoy.setAlignmentX(
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

		JLabel estado =
				new JLabel(
						"Gestion clinica activa"
				);

		estado.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						11
				)
		);

		estado.setForeground(
				new Color(
						220,
						244,
						250
				)
		);

		estado.setAlignmentX(
				RIGHT_ALIGNMENT
		);

		derecha.add(hoy);
		derecha.add(Box.createVerticalStrut(6));
		derecha.add(fecha);
		derecha.add(Box.createVerticalStrut(4));
		derecha.add(estado);

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

	private String obtenerTextoEspecialidad() {

		if (profesional == null) {

			return "Perfil profesional";
		}

		return profesional.getEspecialidad()
				+ "  •  Matricula "
				+ profesional.getMatricula();
	}

	/*
	 * =========================================================
	 * METRICAS
	 * =========================================================
	 */
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

		barra.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						14
				)
		);

		barra.setPreferredSize(
				new Dimension(
						100,
						14
				)
		);

		barra.setAlignmentX(
				LEFT_ALIGNMENT
		);

		tarjeta.add(etiqueta);
		tarjeta.add(Box.createVerticalStrut(7));
		tarjeta.add(valor);
		tarjeta.add(Box.createVerticalStrut(4));
		tarjeta.add(descripcion);
		tarjeta.add(Box.createVerticalStrut(10));
		tarjeta.add(barra);

		return tarjeta;
	}

	private void actualizarResumen() {

		if (profesional == null) {

			mostrarMetricasNoDisponibles();
			return;
		}

		try {

			int idProfesional =
					profesional.getIdProfesional();

			LinkedList<Disponibilidad> disponibilidades =
					controllerDisponibilidad
							.obtenerPorProfesional(
									idProfesional
							);

			LinkedList<Turno> turnos =
					controllerTurno
							.obtenerPorProfesional(
									idProfesional
							);

			LinkedList<Consulta> consultas =
					controllerConsulta
							.obtenerPorProfesional(
									idProfesional
							);

			int activos = 0;
			int presentes = 0;
			int retrasos = 0;

			for (Turno turno : turnos) {

				if (!turno.estaCancelado()) {

					activos++;
				}

				if (turno.estaPresente()) {

					presentes++;
				}
			}

			for (Disponibilidad disponibilidad
					: disponibilidades) {

				if (disponibilidad.tieneRetraso()) {

					retrasos++;
				}
			}

			valorDisponibilidades.setText(
					String.valueOf(
							disponibilidades.size()
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

			valorConsultas.setText(
					String.valueOf(
							consultas.size()
					)
			);

			valorRetrasos.setText(
					String.valueOf(
							retrasos
					)
			);

		} catch (Exception e) {

			mostrarMetricasNoDisponibles();
			e.printStackTrace();
		}
	}

	private void mostrarMetricasNoDisponibles() {

		valorDisponibilidades.setText("-");
		valorTurnosActivos.setText("-");
		valorPresentes.setText("-");
		valorConsultas.setText("-");
		valorRetrasos.setText("-");
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

	/*
	 * =========================================================
	 * BOTONES SIDEBAR
	 * =========================================================
	 */
	private JButton crearBotonSidebar(
			String texto,
			TipoIcono icono,
			boolean activo,
			Runnable accion) {

		SidebarButton boton =
				new SidebarButton(
						texto,
						icono,
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

		/*
		 * Altura premium del boton.
		 */
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

		boton.addActionListener(
				e -> cerrarSesion()
		);

		return boton;
	}

	/*
	 * =========================================================
	 * NAVEGACION ORIGINAL
	 * =========================================================
	 */
	private void abrirDisponibilidad() {

		setVisible(false);

		DisponibilidadFrame frame =
				new DisponibilidadFrame(
						this,
						usuario
				);

		frame.setVisible(true);
	}

	private void abrirAgenda() {

		setVisible(false);

		AgendaProfesionalFrame frame =
				new AgendaProfesionalFrame(
						this,
						usuario
				);

		frame.setVisible(true);
	}

	private void abrirRetraso() {

		setVisible(false);

		RetrasoProfesionalFrame frame =
				new RetrasoProfesionalFrame(
						this,
						usuario
				);

		frame.setVisible(true);
	}

	private void abrirRegistrarConsulta() {

		setVisible(false);

		RegistrarConsultaFrame frame =
				new RegistrarConsultaFrame(
						this,
						usuario
				);

		frame.setVisible(true);
	}

	private void abrirHistorial() {

		setVisible(false);

		HistorialProfesionalFrame frame =
				new HistorialProfesionalFrame(
						this,
						usuario
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
				+ texto.substring(1);
	}

	/*
	 * =========================================================
	 * ICONOS
	 * =========================================================
	 */
	private enum TipoIcono {

		DASHBOARD,
		DISPONIBILIDAD,
		AGENDA,
		RETRASO,
		CONSULTA,
		HISTORIAL
	}

	private static class NavIcon
			implements Icon {

		private TipoIcono tipo;

		public NavIcon(
				TipoIcono tipo) {

			this.tipo =
					tipo;
		}

		@Override
		public int getIconWidth() {

			return 20;
		}

		@Override
		public int getIconHeight() {

			return 20;
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

			case DISPONIBILIDAD:

				dibujarCalendario(
						g2,
						x,
						y
				);

				break;

			case AGENDA:

				dibujarAgenda(
						g2,
						x,
						y
				);

				break;

			case RETRASO:

				dibujarReloj(
						g2,
						x,
						y
				);

				break;

			case CONSULTA:

				dibujarConsulta(
						g2,
						x,
						y
				);

				break;

			case HISTORIAL:

				dibujarHistorial(
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

			g2.drawRoundRect(x + 1, y + 1, 7, 7, 2, 2);
			g2.drawRoundRect(x + 12, y + 1, 7, 7, 2, 2);
			g2.drawRoundRect(x + 1, y + 12, 7, 7, 2, 2);
			g2.drawRoundRect(x + 12, y + 12, 7, 7, 2, 2);
		}

		private void dibujarCalendario(
				Graphics2D g2,
				int x,
				int y) {

			g2.drawRoundRect(x + 2, y + 4, 16, 14, 3, 3);
			g2.drawLine(x + 2, y + 8, x + 18, y + 8);
			g2.drawLine(x + 6, y + 1, x + 6, y + 6);
			g2.drawLine(x + 14, y + 1, x + 14, y + 6);
		}

		private void dibujarAgenda(
				Graphics2D g2,
				int x,
				int y) {

			g2.drawRoundRect(x + 2, y + 3, 16, 15, 3, 3);
			g2.drawLine(x + 5, y + 8, x + 15, y + 8);
			g2.drawLine(x + 5, y + 12, x + 13, y + 12);
			g2.drawLine(x + 5, y + 16, x + 11, y + 16);
		}

		private void dibujarReloj(
				Graphics2D g2,
				int x,
				int y) {

			g2.drawOval(
					x + 2,
					y + 2,
					16,
					16
			);

			g2.drawLine(
					x + 10,
					y + 5,
					x + 10,
					y + 11
			);

			g2.drawLine(
					x + 10,
					y + 11,
					x + 14,
					y + 13
			);
		}

		private void dibujarConsulta(
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
					x + 13,
					y + 7
			);

			g2.drawLine(
					x + 10,
					y + 11,
					x + 10,
					y + 17
			);

			g2.drawLine(
					x + 7,
					y + 14,
					x + 13,
					y + 14
			);
		}

		private void dibujarHistorial(
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

			g2.drawLine(x + 7, y + 6, x + 14, y + 6);
			g2.drawLine(x + 7, y + 10, x + 14, y + 10);
			g2.drawLine(x + 7, y + 14, x + 12, y + 14);
		}
	}

	/*
	 * =========================================================
	 * SIDEBAR BUTTON
	 * =========================================================
	 */
	private static class SidebarButton
	extends JButton {

private static final long serialVersionUID = 1L;

private boolean activo;
private boolean hover;

public SidebarButton(
		String texto,
		TipoIcono icono,
		boolean activo) {

	super(
			texto
	);

	this.activo =
			activo;

	setUI(
			new BasicButtonUI()
	);

	/*
	 * Texto ligeramente mas grande.
	 */
	setFont(
			new Font(
					"Segoe UI",
					Font.BOLD,
					15
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
					icono
			)
	);

	/*
	 * Separacion entre icono y texto.
	 */
	setIconTextGap(
			15
	);

	/*
	 * Padding interno.
	 */
	setBorder(
			BorderFactory.createEmptyBorder(
					0,
					18,
					0,
					14
			)
	);

	setFocusPainted(
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

	/*
	 * Fondo activo / hover.
	 */
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

	/*
	 * Indicador cyan del modulo activo.
	 */
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

	super.paintComponent(
			g
	);
}
}
	/*
	 * =========================================================
	 * PANEL REDONDEADO
	 * =========================================================
	 */
	private static class RoundedPanel
			extends JPanel {

		private static final long serialVersionUID = 1L;

		private Color fondo;
		private int radio;
		private Color borde;

		public RoundedPanel(
				Color fondo,
				int radio,
				Color borde) {

			this.fondo = fondo;
			this.radio = radio;
			this.borde = borde;

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

			g2.setColor(fondo);

			g2.fillRoundRect(
					0,
					0,
					getWidth() - 1,
					getHeight() - 1,
					radio,
					radio
			);

			if (borde != null) {

				g2.setColor(borde);
				g2.setStroke(new BasicStroke(1f));

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
	 * HERO
	 * =========================================================
	 */
	private static class GradientPanel
			extends RoundedPanel {

		private static final long serialVersionUID = 1L;

		private Color inicio;
		private Color fin;

		public GradientPanel(
				Color inicio,
				Color fin) {

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

			this.inicio = inicio;
			this.fin = fin;
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

			GradientPaint gradient =
					new GradientPaint(
							0,
							0,
							inicio,
							getWidth(),
							getHeight(),
							fin
					);

			g2.setPaint(gradient);

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
	 * ACTION BUTTON
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
							+ "<div style='width:220px;'>"
							+ "<b style='font-size:13px; color:#0F172A;'>"
							+ titulo
							+ "</b>"
							+ "<br><br>"
							+ "<span style='font-size:9px; color:#64748B;'>"
							+ descripcion
							+ "</span>"
							+ "</div>"
							+ "</html>"
			);

			setUI(new BasicButtonUI());

			setHorizontalAlignment(
					SwingConstants.LEFT
			);

			setVerticalAlignment(
					SwingConstants.CENTER
			);

			setBorder(
					BorderFactory.createEmptyBorder(
							15,
							17,
							15,
							17
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

	/*
	 * =========================================================
	 * INFO CARD
	 * =========================================================
	 */
	private static class InfoCard
			extends JPanel {

		private static final long serialVersionUID = 1L;

		public InfoCard(
				String titulo,
				String detalle) {

			setOpaque(false);

			setLayout(
					new BoxLayout(
							this,
							BoxLayout.Y_AXIS
					)
			);

			setBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createLineBorder(
									new Color(
											220,
											228,
											236
									)
							),
							BorderFactory.createEmptyBorder(
									15,
									17,
									15,
									17
							)
					)
			);

			JLabel labelTitulo =
					new JLabel(
							titulo
					);

			labelTitulo.setFont(
					new Font(
							"Segoe UI",
							Font.BOLD,
							13
					)
			);

			labelTitulo.setForeground(
					new Color(
							15,
							23,
							42
					)
			);

			JLabel labelDetalle =
					new JLabel(
							"<html>"
									+ detalle
									+ "</html>"
					);

			labelDetalle.setFont(
					new Font(
							"Segoe UI",
							Font.PLAIN,
							11
					)
			);

			labelDetalle.setForeground(
					new Color(
							100,
							116,
							139
					)
			);

			add(labelTitulo);
			add(Box.createVerticalStrut(12));
			add(labelDetalle);
		}
	}
}