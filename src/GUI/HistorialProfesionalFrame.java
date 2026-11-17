package GUI;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;

import BLL.Consulta;
import BLL.Profesional;
import BLL.Usuario;
import DLL.ControllerConsulta;
import DLL.ControllerProfesional;

public class HistorialProfesionalFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO_VENTANA = 1240;
	private static final int ALTO_VENTANA = 700;
	private static final int ANCHO_DETALLE = 390;

	private DashboardProfesionalFrame dashboard;
	private Usuario usuario;

	private ControllerProfesional controllerProfesional;
	private ControllerConsulta controllerConsulta;

	private Profesional profesional;

	private JTable tablaConsultas;
	private DefaultTableModel modeloTabla;

	private JTextArea areaObservaciones;
	private JTextArea areaSeguimiento;

	private JLabel labelCantidad;
	private JLabel labelSeleccion;

	private JLabel valorTotal;
	private JLabel valorHoy;
	private JLabel valorConSeguimiento;
	private JLabel valorSinSeguimiento;

	private JLabel labelEstadoDetalle;
	private JLabel labelPacienteDetalle;
	private JLabel labelDniDetalle;
	private JLabel labelFechaAtencionDetalle;
	private JLabel labelTurnoDetalle;

	private LinkedList<Consulta> consultas;
	private Consulta consultaSeleccionada;

	private final DateTimeFormatter formatoFechaHora =
			DateTimeFormatter.ofPattern(
					"dd/MM/yyyy HH:mm"
			);

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern(
					"dd/MM/yyyy"
			);

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern(
					"HH:mm"
			);

	public HistorialProfesionalFrame(
			DashboardProfesionalFrame dashboard,
			Usuario usuario) {

		this.dashboard =
				dashboard;

		this.usuario =
				usuario;

		controllerProfesional =
				new ControllerProfesional();

		controllerConsulta =
				new ControllerConsulta();

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

			dashboard.setVisible(
					true
			);

			return;
		}

		configurarVentana();
		inicializarComponentes();
		cargarHistorial();
	}

	/*
	 * =========================================================
	 * VENTANA
	 * =========================================================
	 */
	private void configurarVentana() {

		setTitle(
				"Telemedicina - Historial de consultas - "
						+ usuario.getNombre()
						+ " "
						+ usuario.getApellido()
		);

		setDefaultCloseOperation(
				JFrame.DO_NOTHING_ON_CLOSE
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

	/*
	 * =========================================================
	 * INTERFAZ PRINCIPAL
	 * =========================================================
	 */
	private void inicializarComponentes() {

		JPanel principal =
				new JPanel(
						new BorderLayout()
				);

		principal.setBackground(
				new Color(
						243,
						247,
						250
				)
		);

		setContentPane(
				principal
		);

		principal.add(
				crearCabecera(),
				BorderLayout.NORTH
		);

		JPanel contenido =
				new JPanel(
						new BorderLayout(
								18,
								0
						)
				);

		contenido.setOpaque(
				false
		);

		contenido.setBorder(
				BorderFactory.createEmptyBorder(
						0,
						30,
						28,
						30
				)
		);

		contenido.add(
				crearPanelHistorial(),
				BorderLayout.CENTER
		);

		contenido.add(
				crearPanelDetalle(),
				BorderLayout.EAST
		);

		principal.add(
				contenido,
				BorderLayout.CENTER
		);
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

		cabecera.setOpaque(
				false
		);

		cabecera.setBorder(
				BorderFactory.createEmptyBorder(
						26,
						30,
						22,
						30
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

		textos.setOpaque(
				false
		);

		JLabel contexto =
				new JLabel(
						"PROFESIONAL  /  HISTORIAL"
				);

		contexto.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						10
				)
		);

		contexto.setForeground(
				UITheme.COLOR_ACENTO
		);

		contexto.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel titulo =
				new JLabel(
						"Historial de consultas"
				);

		titulo.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						28
				)
		);

		titulo.setForeground(
				new Color(
						15,
						23,
						42
				)
		);

		titulo.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel subtitulo =
				new JLabel(
						"Consulta las atenciones realizadas, observaciones y seguimiento de pacientes."
				);

		subtitulo.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						13
				)
		);

		subtitulo.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		subtitulo.setAlignmentX(
				LEFT_ALIGNMENT
		);

		textos.add(
				contexto
		);

		textos.add(
				Box.createVerticalStrut(
						6
				)
		);

		textos.add(
				titulo
		);

		textos.add(
				Box.createVerticalStrut(
						5
				)
		);

		textos.add(
				subtitulo
		);

		JButton botonVolver =
				new JButton(
						"Volver al dashboard"
				);

		configurarBotonSecundario(
				botonVolver
		);

		botonVolver.setPreferredSize(
				new Dimension(
						180,
						42
				)
		);

		botonVolver.addActionListener(
				e -> volverDashboard()
		);

		cabecera.add(
				textos,
				BorderLayout.WEST
		);

		cabecera.add(
				botonVolver,
				BorderLayout.EAST
		);

		return cabecera;
	}

	/*
	 * =========================================================
	 * PANEL HISTORIAL
	 * =========================================================
	 */
	private JPanel crearPanelHistorial() {

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
				new BorderLayout(
						0,
						16
				)
		);

		tarjeta.setBorder(
				BorderFactory.createEmptyBorder(
						20,
						20,
						18,
						20
				)
		);

		JPanel superior =
				new JPanel();

		superior.setLayout(
				new BoxLayout(
						superior,
						BoxLayout.Y_AXIS
				)
		);

		superior.setOpaque(
				false
		);

		/*
		 * =====================================================
		 * TITULO + ACTUALIZAR
		 * =====================================================
		 */
		JPanel filaTitulo =
				new JPanel(
						new BorderLayout()
				);

		filaTitulo.setOpaque(
				false
		);

		JPanel textos =
				new JPanel();

		textos.setLayout(
				new BoxLayout(
						textos,
						BoxLayout.Y_AXIS
				)
		);

		textos.setOpaque(
				false
		);

		JLabel titulo =
				new JLabel(
						"Registro de atenciones"
				);

		titulo.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						17
				)
		);

		titulo.setForeground(
				new Color(
						15,
						23,
						42
				)
		);

		titulo.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelCantidad =
				new JLabel(
						"0 consultas registradas"
				);

		labelCantidad.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						11
				)
		);

		labelCantidad.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelCantidad.setAlignmentX(
				LEFT_ALIGNMENT
		);

		textos.add(
				titulo
		);

		textos.add(
				Box.createVerticalStrut(
						4
				)
		);

		textos.add(
				labelCantidad
		);

		JButton botonActualizar =
				new JButton(
						"Actualizar"
				);

		configurarBotonSecundario(
				botonActualizar
		);

		botonActualizar.setPreferredSize(
				new Dimension(
						110,
						38
				)
		);

		botonActualizar.addActionListener(
				e -> cargarHistorial()
		);

		filaTitulo.add(
				textos,
				BorderLayout.WEST
		);

		filaTitulo.add(
				botonActualizar,
				BorderLayout.EAST
		);

		superior.add(
				filaTitulo
		);

		superior.add(
				Box.createVerticalStrut(
						16
				)
		);

		/*
		 * =====================================================
		 * RESUMEN
		 * =====================================================
		 */
		JPanel resumen =
				new JPanel(
						new GridLayout(
								1,
								4,
								10,
								0
						)
				);

		resumen.setOpaque(
				false
		);

		resumen.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						76
				)
		);

		resumen.setAlignmentX(
				LEFT_ALIGNMENT
		);

		valorTotal =
				new JLabel(
						"0"
				);

		valorHoy =
				new JLabel(
						"0"
				);

		valorConSeguimiento =
				new JLabel(
						"0"
				);

		valorSinSeguimiento =
				new JLabel(
						"0"
				);

		resumen.add(
				crearResumen(
						"Consultas",
						valorTotal,
						new Color(
								34,
								125,
								210
						)
				)
		);

		resumen.add(
				crearResumen(
						"Hoy",
						valorHoy,
						new Color(
								22,
								163,
								74
						)
				)
		);

		resumen.add(
				crearResumen(
						"Seguimiento",
						valorConSeguimiento,
						new Color(
								59,
								91,
								219
						)
				)
		);

		resumen.add(
				crearResumen(
						"Sin seguimiento",
						valorSinSeguimiento,
						new Color(
								234,
								142,
								25
						)
				)
		);

		superior.add(
				resumen
		);

		tarjeta.add(
				superior,
				BorderLayout.NORTH
		);

		/*
		 * =====================================================
		 * TABLA
		 * =====================================================
		 */
		crearTabla();

		JScrollPane scrollTabla =
				new JScrollPane(
						tablaConsultas
				);

		UITheme.configurarScroll(
				scrollTabla
		);

		scrollTabla.setBorder(
				BorderFactory.createLineBorder(
						new Color(
								220,
								228,
								236
						)
				)
		);

		tarjeta.add(
				scrollTabla,
				BorderLayout.CENTER
		);

		/*
		 * =====================================================
		 * PIE
		 * =====================================================
		 */
		JPanel pie =
				new JPanel(
						new BorderLayout()
				);

		pie.setOpaque(
				false
		);

		labelSeleccion =
				new JLabel(
						"Seleccione una consulta para visualizar su detalle."
				);

		labelSeleccion.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						11
				)
		);

		labelSeleccion.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		JLabel ayuda =
				new JLabel(
						"Historial ordenado desde la atencion mas reciente."
				);

		ayuda.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						10
				)
		);

		ayuda.setForeground(
				new Color(
						148,
						163,
						184
				)
		);

		pie.add(
				labelSeleccion,
				BorderLayout.WEST
		);

		pie.add(
				ayuda,
				BorderLayout.EAST
		);

		tarjeta.add(
				pie,
				BorderLayout.SOUTH
		);

		return tarjeta;
	}

	/*
	 * =========================================================
	 * RESUMEN
	 * =========================================================
	 */
	private JPanel crearResumen(
			String titulo,
			JLabel valor,
			Color acento) {

		RoundedPanel panel =
				new RoundedPanel(
						new Color(
								249,
								251,
								253
						),
						14,
						new Color(
								226,
								232,
								240
						)
				);

		panel.setLayout(
				new BorderLayout(
						8,
						0
				)
		);

		panel.setBorder(
				BorderFactory.createEmptyBorder(
						10,
						12,
						10,
						12
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
						10
				)
		);

		etiqueta.setForeground(
				acento
		);

		valor.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						20
				)
		);

		valor.setForeground(
				new Color(
						15,
						23,
						42
				)
		);

		panel.add(
				etiqueta,
				BorderLayout.CENTER
		);

		panel.add(
				valor,
				BorderLayout.EAST
		);

		return panel;
	}

	/*
	 * =========================================================
	 * TABLA
	 * =========================================================
	 */
	private void crearTabla() {

		String[] columnas = {
				"ID",
				"Paciente",
				"DNI",
				"Fecha atencion",
				"Fecha turno",
				"Hora"
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

		tablaConsultas =
				new JTable(
						modeloTabla
				);

		UITheme.configurarTabla(
				tablaConsultas
		);

		tablaConsultas.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION
		);

		tablaConsultas.setRowHeight(
				38
		);

		tablaConsultas.setIntercellSpacing(
				new Dimension(
						0,
						1
				)
		);

		tablaConsultas
				.getColumnModel()
				.getColumn(0)
				.setPreferredWidth(
						45
				);

		tablaConsultas
				.getColumnModel()
				.getColumn(1)
				.setPreferredWidth(
						165
				);

		tablaConsultas
				.getColumnModel()
				.getColumn(2)
				.setPreferredWidth(
						100
				);

		tablaConsultas
				.getColumnModel()
				.getColumn(3)
				.setPreferredWidth(
						140
				);

		tablaConsultas
				.getColumnModel()
				.getColumn(4)
				.setPreferredWidth(
						105
				);

		tablaConsultas
				.getColumnModel()
				.getColumn(5)
				.setPreferredWidth(
						75
				);

		tablaConsultas
				.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								mostrarDetalleSeleccionado();
							}
						}
				);
	}

	/*
	 * =========================================================
	 * PANEL DETALLE
	 * =========================================================
	 */
	private JPanel crearPanelDetalle() {

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

		tarjeta.setPreferredSize(
				new Dimension(
						ANCHO_DETALLE,
						0
				)
		);

		tarjeta.setLayout(
				new BorderLayout(
						0,
						12
				)
		);

		tarjeta.setBorder(
				BorderFactory.createEmptyBorder(
						18,
						20,
						18,
						20
				)
		);

		/*
		 * =====================================================
		 * CABECERA
		 * =====================================================
		 */
		JPanel cabecera =
				new JPanel();

		cabecera.setLayout(
				new BoxLayout(
						cabecera,
						BoxLayout.Y_AXIS
				)
		);

		cabecera.setOpaque(
				false
		);

		JLabel badge =
				new JLabel(
						"DETALLE CLINICO"
				);

		badge.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						9
				)
		);

		badge.setForeground(
				UITheme.COLOR_ACENTO
		);

		badge.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel titulo =
				new JLabel(
						"Consulta seleccionada"
				);

		titulo.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						20
				)
		);

		titulo.setForeground(
				new Color(
						15,
						23,
						42
				)
		);

		titulo.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel descripcion =
				new JLabel(
						"<html>"
								+ "Revise la informacion registrada "
								+ "durante la atencion del paciente."
								+ "</html>"
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

		cabecera.add(
				badge
		);

		cabecera.add(
				Box.createVerticalStrut(
						4
				)
		);

		cabecera.add(
				titulo
		);

		cabecera.add(
				Box.createVerticalStrut(
						4
				)
		);

		cabecera.add(
				descripcion
		);

		tarjeta.add(
				cabecera,
				BorderLayout.NORTH
		);

		/*
		 * =====================================================
		 * CUERPO
		 * =====================================================
		 */
		JPanel cuerpo =
				new JPanel();

		cuerpo.setLayout(
				new BoxLayout(
						cuerpo,
						BoxLayout.Y_AXIS
				)
		);

		cuerpo.setOpaque(
				false
		);

		/*
		 * =====================================================
		 * FICHA
		 * =====================================================
		 */
		RoundedPanel ficha =
				new RoundedPanel(
						new Color(
								247,
								250,
								252
						),
						14,
						new Color(
								226,
								232,
								240
						)
				);

		ficha.setLayout(
				new BoxLayout(
						ficha,
						BoxLayout.Y_AXIS
				)
		);

		ficha.setBorder(
				BorderFactory.createEmptyBorder(
						11,
						13,
						11,
						13
				)
		);

		ficha.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						115
				)
		);

		ficha.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelEstadoDetalle =
				new JLabel(
						"SIN SELECCION"
				);

		labelEstadoDetalle.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						9
				)
		);

		labelEstadoDetalle.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelEstadoDetalle.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelPacienteDetalle =
				new JLabel(
						"Seleccione una consulta"
				);

		labelPacienteDetalle.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						13
				)
		);

		labelPacienteDetalle.setForeground(
				new Color(
						15,
						23,
						42
				)
		);

		labelPacienteDetalle.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelDniDetalle =
				new JLabel(
						"DNI del paciente"
				);

		labelDniDetalle.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						10
				)
		);

		labelDniDetalle.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelDniDetalle.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelFechaAtencionDetalle =
				new JLabel(
						"Atencion: —"
				);

		labelFechaAtencionDetalle.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						9
				)
		);

		labelFechaAtencionDetalle.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelFechaAtencionDetalle.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelTurnoDetalle =
				new JLabel(
						"Turno: —"
				);

		labelTurnoDetalle.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						9
				)
		);

		labelTurnoDetalle.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelTurnoDetalle.setAlignmentX(
				LEFT_ALIGNMENT
		);

		ficha.add(
				labelEstadoDetalle
		);

		ficha.add(
				Box.createVerticalStrut(
						5
				)
		);

		ficha.add(
				labelPacienteDetalle
		);

		ficha.add(
				Box.createVerticalStrut(
						3
				)
		);

		ficha.add(
				labelDniDetalle
		);

		ficha.add(
				Box.createVerticalStrut(
						5
				)
		);

		ficha.add(
				labelFechaAtencionDetalle
		);

		ficha.add(
				Box.createVerticalStrut(
						3
				)
		);

		ficha.add(
				labelTurnoDetalle
		);

		cuerpo.add(
				ficha
		);

		cuerpo.add(
				Box.createVerticalStrut(
						12
				)
		);

		/*
		 * =====================================================
		 * OBSERVACIONES
		 * =====================================================
		 */
		JLabel labelObservaciones =
				new JLabel(
						"Observaciones"
				);

		labelObservaciones.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						10
				)
		);

		labelObservaciones.setForeground(
				new Color(
						51,
						65,
						85
				)
		);

		labelObservaciones.setAlignmentX(
				LEFT_ALIGNMENT
		);

		areaObservaciones =
				new JTextArea();

		configurarAreaLectura(
				areaObservaciones
		);

		JScrollPane scrollObservaciones =
				new JScrollPane(
						areaObservaciones
				);

		UITheme.configurarScroll(
				scrollObservaciones
		);

		scrollObservaciones.setBorder(
				BorderFactory.createLineBorder(
						new Color(
								203,
								213,
								225
						)
				)
		);

		scrollObservaciones.setPreferredSize(
				new Dimension(
						330,
						105
				)
		);

		scrollObservaciones.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						105
				)
		);

		scrollObservaciones.setAlignmentX(
				LEFT_ALIGNMENT
		);

		cuerpo.add(
				labelObservaciones
		);

		cuerpo.add(
				Box.createVerticalStrut(
						5
				)
		);

		cuerpo.add(
				scrollObservaciones
		);

		cuerpo.add(
				Box.createVerticalStrut(
						12
				)
		);

		/*
		 * =====================================================
		 * SEGUIMIENTO
		 * =====================================================
		 */
		JLabel labelSeguimiento =
				new JLabel(
						"Seguimiento"
				);

		labelSeguimiento.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						10
				)
		);

		labelSeguimiento.setForeground(
				new Color(
						51,
						65,
						85
				)
		);

		labelSeguimiento.setAlignmentX(
				LEFT_ALIGNMENT
		);

		areaSeguimiento =
				new JTextArea();

		configurarAreaLectura(
				areaSeguimiento
		);

		JScrollPane scrollSeguimiento =
				new JScrollPane(
						areaSeguimiento
				);

		UITheme.configurarScroll(
				scrollSeguimiento
		);

		scrollSeguimiento.setBorder(
				BorderFactory.createLineBorder(
						new Color(
								203,
								213,
								225
						)
				)
		);

		scrollSeguimiento.setPreferredSize(
				new Dimension(
						330,
						80
				)
		);

		scrollSeguimiento.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						80
				)
		);

		scrollSeguimiento.setAlignmentX(
				LEFT_ALIGNMENT
		);

		cuerpo.add(
				labelSeguimiento
		);

		cuerpo.add(
				Box.createVerticalStrut(
						5
				)
		);

		cuerpo.add(
				scrollSeguimiento
		);

		cuerpo.add(
				Box.createVerticalStrut(
						12
				)
		);

		/*
		 * =====================================================
		 * AVISO
		 * =====================================================
		 */
		RoundedPanel aviso =
				new RoundedPanel(
						new Color(
								242,
								249,
								252
						),
						12,
						new Color(
								210,
								232,
								240
						)
				);

		aviso.setLayout(
				new BorderLayout()
		);

		aviso.setBorder(
				BorderFactory.createEmptyBorder(
						9,
						11,
						9,
						11
				)
		);

		aviso.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						52
				)
		);

		aviso.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel textoAviso =
				new JLabel(
						"<html>"
								+ "<b>Historial clinico</b><br>"
								+ "La informacion de esta vista es de solo lectura."
								+ "</html>"
				);

		textoAviso.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						9
				)
		);

		textoAviso.setForeground(
				new Color(
						71,
						85,
						105
				)
		);

		aviso.add(
				textoAviso,
				BorderLayout.CENTER
		);

		cuerpo.add(
				aviso
		);

		tarjeta.add(
				cuerpo,
				BorderLayout.CENTER
		);

		return tarjeta;
	}

	/*
	 * =========================================================
	 * AREA SOLO LECTURA
	 * =========================================================
	 */
	private void configurarAreaLectura(
			JTextArea area) {

		area.setEditable(
				false
		);

		area.setLineWrap(
				true
		);

		area.setWrapStyleWord(
				true
		);

		area.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						11
				)
		);

		area.setForeground(
				new Color(
						51,
						65,
						85
				)
		);

		area.setBackground(
				new Color(
						249,
						251,
						253
				)
		);

		area.setBorder(
				BorderFactory.createEmptyBorder(
						8,
						9,
						8,
						9
				)
		);
	}

	/*
	 * =========================================================
	 * CARGAR HISTORIAL
	 * =========================================================
	 */
	private void cargarHistorial() {

		modeloTabla.setRowCount(
				0
		);

		areaObservaciones.setText(
				""
		);

		areaSeguimiento.setText(
				""
		);

		consultas =
				controllerConsulta.obtenerPorProfesional(
						profesional.getIdProfesional()
				);

		int total = 0;
		int hoy = 0;
		int conSeguimiento = 0;
		int sinSeguimiento = 0;

		LocalDate fechaActual =
				LocalDate.now();

		for (Consulta consulta : consultas) {

			modeloTabla.addRow(
					new Object[] {

							consulta
									.getIdConsulta(),

							consulta
									.getTurno()
									.getPaciente()
									.getNombre()
									+ " "
									+ consulta
											.getTurno()
											.getPaciente()
											.getApellido(),

							consulta
									.getTurno()
									.getPaciente()
									.getDni(),

							consulta
									.getFechaHoraAtencion()
									.format(
											formatoFechaHora
									),

							consulta
									.getTurno()
									.getFecha()
									.format(
											formatoFecha
									),

							consulta
									.getTurno()
									.getHora()
									.format(
											formatoHora
									)
					}
			);

			total++;

			if (consulta
					.getFechaHoraAtencion()
					.toLocalDate()
					.isEqual(
							fechaActual
					)) {

				hoy++;
			}

			String seguimiento =
					consulta.getSeguimiento();

			if (seguimiento != null
					&& !seguimiento.trim().isEmpty()) {

				conSeguimiento++;

			} else {

				sinSeguimiento++;
			}
		}

		labelCantidad.setText(
				total
						+ (
								total == 1
										? " consulta registrada"
										: " consultas registradas"
						)
		);

		valorTotal.setText(
				String.valueOf(
						total
				)
		);

		valorHoy.setText(
				String.valueOf(
						hoy
				)
		);

		valorConSeguimiento.setText(
				String.valueOf(
						conSeguimiento
				)
		);

		valorSinSeguimiento.setText(
				String.valueOf(
						sinSeguimiento
				)
		);

		consultaSeleccionada =
				null;

		tablaConsultas.clearSelection();

		actualizarDetalle();
	}

	/*
	 * =========================================================
	 * MOSTRAR DETALLE
	 * =========================================================
	 */
	private void mostrarDetalleSeleccionado() {

		int fila =
				tablaConsultas.getSelectedRow();

		if (fila < 0) {

			consultaSeleccionada =
					null;

			actualizarDetalle();

			return;
		}

		int idConsulta =
				(int) modeloTabla.getValueAt(
						fila,
						0
				);

		consultaSeleccionada =
				null;

		for (Consulta consulta : consultas) {

			if (consulta.getIdConsulta()
					== idConsulta) {

				consultaSeleccionada =
						consulta;

				break;
			}
		}

		actualizarDetalle();
	}

	/*
	 * =========================================================
	 * ACTUALIZAR DETALLE
	 * =========================================================
	 */
	private void actualizarDetalle() {

		if (labelEstadoDetalle == null
				|| labelPacienteDetalle == null
				|| labelDniDetalle == null
				|| labelFechaAtencionDetalle == null
				|| labelTurnoDetalle == null
				|| areaObservaciones == null
				|| areaSeguimiento == null) {

			return;
		}

		if (consultaSeleccionada == null) {

			labelEstadoDetalle.setText(
					"SIN SELECCION"
			);

			labelEstadoDetalle.setForeground(
					new Color(
							100,
							116,
							139
					)
			);

			labelPacienteDetalle.setText(
					"Seleccione una consulta"
			);

			labelDniDetalle.setText(
					"DNI del paciente"
			);

			labelFechaAtencionDetalle.setText(
					"Atencion: —"
			);

			labelTurnoDetalle.setText(
					"Turno: —"
			);

			areaObservaciones.setText(
					""
			);

			areaSeguimiento.setText(
					""
			);

			labelSeleccion.setText(
					"Seleccione una consulta para visualizar su detalle."
			);

			return;
		}

		labelEstadoDetalle.setText(
				"CONSULTA REGISTRADA"
		);

		labelEstadoDetalle.setForeground(
				new Color(
						22,
						163,
						74
				)
		);

		labelPacienteDetalle.setText(
				consultaSeleccionada
						.getTurno()
						.getPaciente()
						.getNombre()
						+ " "
						+ consultaSeleccionada
								.getTurno()
								.getPaciente()
								.getApellido()
		);

		labelDniDetalle.setText(
				"DNI "
						+ consultaSeleccionada
								.getTurno()
								.getPaciente()
								.getDni()
		);

		labelFechaAtencionDetalle.setText(
				"Atencion: "
						+ consultaSeleccionada
								.getFechaHoraAtencion()
								.format(
										formatoFechaHora
								)
		);

		labelTurnoDetalle.setText(
				"Turno: "
						+ consultaSeleccionada
								.getTurno()
								.getFecha()
								.format(
										formatoFecha
								)
						+ "  •  "
						+ consultaSeleccionada
								.getTurno()
								.getHora()
								.format(
										formatoHora
								)
		);

		areaObservaciones.setText(
				consultaSeleccionada
						.getObservaciones()
		);

		areaObservaciones.setCaretPosition(
				0
		);

		String seguimiento =
				consultaSeleccionada
						.getSeguimiento();

		if (seguimiento == null
				|| seguimiento.trim().isEmpty()) {

			seguimiento =
					"Sin seguimiento";
		}

		areaSeguimiento.setText(
				seguimiento
		);

		areaSeguimiento.setCaretPosition(
				0
		);

		labelSeleccion.setText(
				"Seleccionada: consulta #"
						+ consultaSeleccionada
								.getIdConsulta()
						+ "  •  "
						+ consultaSeleccionada
								.getTurno()
								.getPaciente()
								.getNombre()
						+ " "
						+ consultaSeleccionada
								.getTurno()
								.getPaciente()
								.getApellido()
		);
	}

	/*
	 * =========================================================
	 * VOLVER
	 * =========================================================
	 */
	private void volverDashboard() {

		dispose();

		dashboard.setVisible(
				true
		);
	}

	/*
	 * =========================================================
	 * ICONO
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
	 * BOTON SECUNDARIO
	 * =========================================================
	 */
	private void configurarBotonSecundario(
			JButton boton) {

		boton.setUI(
				new BasicButtonUI()
		);

		boton.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						11
				)
		);

		boton.setForeground(
				new Color(
						15,
						76,
						105
				)
		);

		boton.setBackground(
				Color.WHITE
		);

		boton.setFocusPainted(
				false
		);

		boton.setContentAreaFilled(
				true
		);

		boton.setOpaque(
				true
		);

		boton.setCursor(
				Cursor.getPredefinedCursor(
						Cursor.HAND_CURSOR
				)
		);

		boton.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(
								new Color(
										211,
										222,
										230
								)
						),
						BorderFactory.createEmptyBorder(
								8,
								14,
								8,
								14
						)
				)
		);

		Color normal =
				Color.WHITE;

		Color hover =
				new Color(
						239,
						249,
						252
				);

		boton.addMouseListener(
				new MouseAdapter() {

					@Override
					public void mouseEntered(
							MouseEvent e) {

						if (boton.isEnabled()) {

							boton.setBackground(
									hover
							);
						}
					}

					@Override
					public void mouseExited(
							MouseEvent e) {

						if (boton.isEnabled()) {

							boton.setBackground(
									normal
							);
						}
					}
				}
		);
	}

	/*
	 * =========================================================
	 * PANEL REDONDEADO
	 * =========================================================
	 */
	private static class RoundedPanel
			extends JPanel {

		private static final long serialVersionUID = 1L;

		private final Color fondo;
		private final int radio;
		private final Color borde;

		public RoundedPanel(
				Color fondo,
				int radio,
				Color borde) {

			this.fondo =
					fondo;

			this.radio =
					radio;

			this.borde =
					borde;

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
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON
			);

			g2.setColor(
					fondo
			);

			g2.fillRoundRect(
					0,
					0,
					getWidth() - 1,
					getHeight() - 1,
					radio,
					radio
			);

			if (borde != null) {

				g2.setColor(
						borde
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

			super.paintComponent(
					g
			);
		}
	}
}