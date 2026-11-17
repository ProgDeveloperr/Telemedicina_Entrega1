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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;

import BLL.Consulta;
import BLL.Disponibilidad;
import BLL.Paciente;
import BLL.Profesional;
import BLL.Usuario;

import DLL.ControllerConsulta;
import DLL.ControllerDisponibilidad;
import DLL.ControllerPaciente;
import DLL.ControllerProfesional;

public class ConsultasInformacionFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO_VENTANA = 1240;
	private static final int ALTO_VENTANA = 700;

	private DashboardEmpleadoFrame dashboard;

	private ControllerPaciente controllerPaciente;
	private ControllerConsulta controllerConsulta;
	private ControllerProfesional controllerProfesional;
	private ControllerDisponibilidad controllerDisponibilidad;

	private JTable tablaPacientes;
	private JTable tablaConsultas;
	private JTable tablaRetrasos;

	private DefaultTableModel modeloPacientes;
	private DefaultTableModel modeloConsultas;
	private DefaultTableModel modeloRetrasos;

	private JLabel labelCantidadPacientes;
	private JLabel labelPacienteSeleccionado;
	private JLabel labelCantidadConsultas;

	private JLabel labelDetalleEstado;
	private JLabel labelDetallePaciente;
	private JLabel labelDetalleDni;
	private JLabel labelDetalleAtencion;
	private JLabel labelDetalleTurno;

	private JTextArea areaObservaciones;
	private JTextArea areaSeguimiento;

	private JLabel valorRetrasos;
	private JLabel valorRetrasosHoy;
	private JLabel valorRetrasosFuturos;
	private JLabel valorProfesionalesRetraso;

	private LinkedList<Consulta> consultasPacienteActual;
	private Consulta consultaSeleccionada;

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern(
					"dd/MM/yyyy"
			);

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern(
					"HH:mm"
			);

	private final DateTimeFormatter formatoFechaHora =
			DateTimeFormatter.ofPattern(
					"dd/MM/yyyy HH:mm"
			);

	public ConsultasInformacionFrame(
			DashboardEmpleadoFrame dashboard) {

		this.dashboard =
				dashboard;

		controllerPaciente =
				new ControllerPaciente();

		controllerConsulta =
				new ControllerConsulta();

		controllerProfesional =
				new ControllerProfesional();

		controllerDisponibilidad =
				new ControllerDisponibilidad();

		consultasPacienteActual =
				new LinkedList<Consulta>();

		configurarVentana();
		inicializarComponentes();

		cargarPacientes();
		cargarRetrasos();
	}

	/*
	 * =========================================================
	 * VENTANA
	 * =========================================================
	 */
	private void configurarVentana() {

		setTitle(
				"Telemedicina - Consultas e informacion"
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

		JTabbedPane pestanias =
				new JTabbedPane();

		pestanias.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						12
				)
		);

		pestanias.setForeground(
				new Color(
						51,
						65,
						85
				)
		);

		pestanias.setBackground(
				new Color(
						243,
						247,
						250
				)
		);

		pestanias.setBorder(
				BorderFactory.createEmptyBorder(
						0,
						30,
						28,
						30
				)
		);

		pestanias.addTab(
				"Historial por paciente",
				crearPanelHistorial()
		);

		pestanias.addTab(
				"Retrasos registrados",
				crearPanelRetrasos()
		);

		principal.add(
				pestanias,
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
						"RECEPCION  /  CONSULTAS E INFORMACION"
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
						"Consultas e informacion"
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
						"Consulta historiales clinicos y retrasos informados por los profesionales."
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
	 * HISTORIAL POR PACIENTE
	 * =========================================================
	 */
	private JPanel crearPanelHistorial() {

		JPanel panel =
				new JPanel(
						new BorderLayout(
								16,
								0
						)
				);

		panel.setOpaque(
				false
		);

		panel.setBorder(
				BorderFactory.createEmptyBorder(
						14,
						0,
						0,
						0
				)
		);

		panel.add(
				crearTarjetaPacientes(),
				BorderLayout.WEST
		);

		panel.add(
				crearTarjetaConsultas(),
				BorderLayout.CENTER
		);

		panel.add(
				crearTarjetaDetalleConsulta(),
				BorderLayout.EAST
		);

		return panel;
	}

	/*
	 * =========================================================
	 * TARJETA PACIENTES
	 * =========================================================
	 */
	private JPanel crearTarjetaPacientes() {

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
						300,
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
						18,
						18,
						18
				)
		);

		JPanel cabecera =
				new JPanel(
						new BorderLayout()
				);

		cabecera.setOpaque(
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
						"Pacientes"
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

		labelCantidadPacientes =
				new JLabel(
						"0 pacientes"
				);

		labelCantidadPacientes.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						10
				)
		);

		labelCantidadPacientes.setForeground(
				new Color(
						100,
						116,
						139
				)
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
				labelCantidadPacientes
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
						95,
						34
				)
		);

		botonActualizar.addActionListener(
				e -> cargarPacientes()
		);

		cabecera.add(
				textos,
				BorderLayout.WEST
		);

		cabecera.add(
				botonActualizar,
				BorderLayout.EAST
		);

		tarjeta.add(
				cabecera,
				BorderLayout.NORTH
		);

		crearTablaPacientes();

		JScrollPane scroll =
				new JScrollPane(
						tablaPacientes
				);

		UITheme.configurarScroll(
				scroll
		);

		scroll.setBorder(
				BorderFactory.createLineBorder(
						new Color(
								220,
								228,
								236
						)
				)
		);

		tarjeta.add(
				scroll,
				BorderLayout.CENTER
		);

		JLabel ayuda =
				new JLabel(
						"Seleccione un paciente."
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

		tarjeta.add(
				ayuda,
				BorderLayout.SOUTH
		);

		return tarjeta;
	}

	/*
	 * =========================================================
	 * TARJETA CONSULTAS
	 * =========================================================
	 */
	private JPanel crearTarjetaConsultas() {

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
						12
				)
		);

		tarjeta.setBorder(
				BorderFactory.createEmptyBorder(
						18,
						18,
						18,
						18
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

		JLabel titulo =
				new JLabel(
						"Historial clinico"
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

		labelPacienteSeleccionado =
				new JLabel(
						"Seleccione un paciente para consultar su historial."
				);

		labelPacienteSeleccionado.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						10
				)
		);

		labelPacienteSeleccionado.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelPacienteSeleccionado.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelCantidadConsultas =
				new JLabel(
						"0 consultas"
				);

		labelCantidadConsultas.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						10
				)
		);

		labelCantidadConsultas.setForeground(
				UITheme.COLOR_ACENTO
		);

		labelCantidadConsultas.setAlignmentX(
				LEFT_ALIGNMENT
		);

		superior.add(
				titulo
		);

		superior.add(
				Box.createVerticalStrut(
						4
				)
		);

		superior.add(
				labelPacienteSeleccionado
		);

		superior.add(
				Box.createVerticalStrut(
						4
				)
		);

		superior.add(
				labelCantidadConsultas
		);

		tarjeta.add(
				superior,
				BorderLayout.NORTH
		);

		crearTablaConsultas();

		JScrollPane scroll =
				new JScrollPane(
						tablaConsultas
				);

		UITheme.configurarScroll(
				scroll
		);

		scroll.setBorder(
				BorderFactory.createLineBorder(
						new Color(
								220,
								228,
								236
						)
				)
		);

		tarjeta.add(
				scroll,
				BorderLayout.CENTER
		);

		JLabel ayuda =
				new JLabel(
						"Seleccione una consulta para ver su detalle."
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

		tarjeta.add(
				ayuda,
				BorderLayout.SOUTH
		);

		return tarjeta;
	}

	/*
	 * =========================================================
	 * DETALLE CONSULTA
	 * =========================================================
	 */
	private JPanel crearTarjetaDetalleConsulta() {

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
						350,
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
						18,
						18,
						18
				)
		);

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
						18
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

		tarjeta.add(
				cabecera,
				BorderLayout.NORTH
		);

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
						12,
						11,
						12
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

		labelDetalleEstado =
				new JLabel(
						"SIN SELECCION"
				);

		labelDetalleEstado.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						9
				)
		);

		labelDetalleEstado.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelDetalleEstado.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelDetallePaciente =
				new JLabel(
						"Seleccione una consulta"
				);

		labelDetallePaciente.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						13
				)
		);

		labelDetallePaciente.setForeground(
				new Color(
						15,
						23,
						42
				)
		);

		labelDetallePaciente.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelDetalleDni =
				new JLabel(
						"DNI del paciente"
				);

		labelDetalleDni.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						10
				)
		);

		labelDetalleDni.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelDetalleDni.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelDetalleAtencion =
				new JLabel(
						"Atencion: —"
				);

		labelDetalleAtencion.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						9
				)
		);

		labelDetalleAtencion.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelDetalleAtencion.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelDetalleTurno =
				new JLabel(
						"Turno: —"
				);

		labelDetalleTurno.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						9
				)
		);

		labelDetalleTurno.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelDetalleTurno.setAlignmentX(
				LEFT_ALIGNMENT
		);

		ficha.add(
				labelDetalleEstado
		);

		ficha.add(
				Box.createVerticalStrut(
						5
				)
		);

		ficha.add(
				labelDetallePaciente
		);

		ficha.add(
				Box.createVerticalStrut(
						3
				)
		);

		ficha.add(
				labelDetalleDni
		);

		ficha.add(
				Box.createVerticalStrut(
						5
				)
		);

		ficha.add(
				labelDetalleAtencion
		);

		ficha.add(
				Box.createVerticalStrut(
						3
				)
		);

		ficha.add(
				labelDetalleTurno
		);

		cuerpo.add(
				ficha
		);

		cuerpo.add(
				Box.createVerticalStrut(
						12
				)
		);

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

		scrollObservaciones.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						100
				)
		);

		scrollObservaciones.setPreferredSize(
				new Dimension(
						310,
						100
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

		scrollSeguimiento.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						75
				)
		);

		scrollSeguimiento.setPreferredSize(
				new Dimension(
						310,
						75
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

		tarjeta.add(
				cuerpo,
				BorderLayout.CENTER
		);

		return tarjeta;
	}

	/*
	 * =========================================================
	 * TABLAS
	 * =========================================================
	 */
	private void crearTablaPacientes() {

		String[] columnas = {
				"ID",
				"DNI",
				"Paciente"
		};

		modeloPacientes =
				crearModeloNoEditable(
						columnas
				);

		tablaPacientes =
				new JTable(
						modeloPacientes
				);

		UITheme.configurarTabla(
				tablaPacientes
		);

		tablaPacientes.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION
		);

		tablaPacientes.setRowHeight(
				38
		);

		tablaPacientes
				.getColumnModel()
				.getColumn(0)
				.setPreferredWidth(
						35
				);

		tablaPacientes
				.getColumnModel()
				.getColumn(1)
				.setPreferredWidth(
						85
				);

		tablaPacientes
				.getColumnModel()
				.getColumn(2)
				.setPreferredWidth(
						150
				);

		tablaPacientes
				.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								cargarHistorialPaciente();
							}
						}
				);
	}

	private void crearTablaConsultas() {

		String[] columnas = {
				"ID",
				"Fecha atencion",
				"Fecha turno",
				"Hora"
		};

		modeloConsultas =
				crearModeloNoEditable(
						columnas
				);

		tablaConsultas =
				new JTable(
						modeloConsultas
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

		tablaConsultas
				.getColumnModel()
				.getColumn(0)
				.setPreferredWidth(
						40
				);

		tablaConsultas
				.getColumnModel()
				.getColumn(1)
				.setPreferredWidth(
						140
				);

		tablaConsultas
				.getColumnModel()
				.getColumn(2)
				.setPreferredWidth(
						105
				);

		tablaConsultas
				.getColumnModel()
				.getColumn(3)
				.setPreferredWidth(
						70
				);

		tablaConsultas
				.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								mostrarDetalleConsulta();
							}
						}
				);
	}

	private DefaultTableModel crearModeloNoEditable(
			String[] columnas) {

		return new DefaultTableModel(
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
	}

	/*
	 * =========================================================
	 * CARGAR PACIENTES
	 * =========================================================
	 */
	private void cargarPacientes() {

		modeloPacientes.setRowCount(
				0
		);

		LinkedList<Paciente> pacientes =
				controllerPaciente.buscar(
						""
				);

		for (Paciente paciente : pacientes) {

			modeloPacientes.addRow(
					new Object[] {

							paciente
									.getIdPaciente(),

							paciente
									.getDni(),

							paciente
									.getNombre()
									+ " "
									+ paciente
											.getApellido()
					}
			);
		}

		labelCantidadPacientes.setText(
				pacientes.size()
						+ (
								pacientes.size() == 1
										? " paciente"
										: " pacientes"
						)
		);

		tablaPacientes.clearSelection();

		modeloConsultas.setRowCount(
				0
		);

		consultasPacienteActual.clear();

		consultaSeleccionada =
				null;

		labelPacienteSeleccionado.setText(
				"Seleccione un paciente para consultar su historial."
		);

		labelCantidadConsultas.setText(
				"0 consultas"
		);

		actualizarDetalleConsulta();
	}

	/*
	 * =========================================================
	 * HISTORIAL DEL PACIENTE
	 * =========================================================
	 */
	private void cargarHistorialPaciente() {

		int fila =
				tablaPacientes
						.getSelectedRow();

		modeloConsultas.setRowCount(
				0
		);

		consultasPacienteActual.clear();

		consultaSeleccionada =
				null;

		if (fila < 0) {

			labelPacienteSeleccionado.setText(
					"Seleccione un paciente para consultar su historial."
			);

			labelCantidadConsultas.setText(
					"0 consultas"
			);

			actualizarDetalleConsulta();

			return;
		}

		int idPaciente =
				(int) modeloPacientes
						.getValueAt(
								fila,
								0
						);

		Paciente paciente =
				controllerPaciente.buscarPorId(
						idPaciente
				);

		if (paciente == null) {

			labelPacienteSeleccionado.setText(
					"No se pudo obtener el paciente seleccionado."
			);

			labelCantidadConsultas.setText(
					"0 consultas"
			);

			actualizarDetalleConsulta();

			return;
		}

		labelPacienteSeleccionado.setText(
				paciente.getNombre()
						+ " "
						+ paciente.getApellido()
						+ "  •  DNI "
						+ paciente.getDni()
		);

		consultasPacienteActual =
				controllerConsulta.obtenerPorPaciente(
						idPaciente
				);

		for (Consulta consulta
				: consultasPacienteActual) {

			modeloConsultas.addRow(
					new Object[] {

							consulta
									.getIdConsulta(),

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
		}

		labelCantidadConsultas.setText(
				consultasPacienteActual.size()
						+ (
								consultasPacienteActual.size() == 1
										? " consulta"
										: " consultas"
						)
		);

		if (consultasPacienteActual.isEmpty()) {

			labelPacienteSeleccionado.setText(
					paciente.getNombre()
							+ " "
							+ paciente.getApellido()
							+ "  •  DNI "
							+ paciente.getDni()
							+ "  •  Sin consultas registradas"
			);
		}

		tablaConsultas.clearSelection();

		actualizarDetalleConsulta();
	}

	/*
	 * =========================================================
	 * SELECCION DE CONSULTA
	 * =========================================================
	 */
	private void mostrarDetalleConsulta() {

		int fila =
				tablaConsultas
						.getSelectedRow();

		if (fila < 0) {

			consultaSeleccionada =
					null;

			actualizarDetalleConsulta();

			return;
		}

		int idConsulta =
				(int) modeloConsultas
						.getValueAt(
								fila,
								0
						);

		consultaSeleccionada =
				null;

		for (Consulta consulta
				: consultasPacienteActual) {

			if (consulta.getIdConsulta()
					== idConsulta) {

				consultaSeleccionada =
						consulta;

				break;
			}
		}

		actualizarDetalleConsulta();
	}

	/*
	 * =========================================================
	 * DETALLE DE CONSULTA
	 * =========================================================
	 */
	private void actualizarDetalleConsulta() {

		if (labelDetalleEstado == null
				|| labelDetallePaciente == null
				|| labelDetalleDni == null
				|| labelDetalleAtencion == null
				|| labelDetalleTurno == null
				|| areaObservaciones == null
				|| areaSeguimiento == null) {

			return;
		}

		if (consultaSeleccionada == null) {

			labelDetalleEstado.setText(
					"SIN SELECCION"
			);

			labelDetalleEstado.setForeground(
					new Color(
							100,
							116,
							139
					)
			);

			labelDetallePaciente.setText(
					"Seleccione una consulta"
			);

			labelDetalleDni.setText(
					"DNI del paciente"
			);

			labelDetalleAtencion.setText(
					"Atencion: —"
			);

			labelDetalleTurno.setText(
					"Turno: —"
			);

			areaObservaciones.setText(
					""
			);

			areaSeguimiento.setText(
					""
			);

			return;
		}

		labelDetalleEstado.setText(
				"CONSULTA REGISTRADA"
		);

		labelDetalleEstado.setForeground(
				new Color(
						22,
						163,
						74
				)
		);

		labelDetallePaciente.setText(
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

		labelDetalleDni.setText(
				"DNI "
						+ consultaSeleccionada
								.getTurno()
								.getPaciente()
								.getDni()
		);

		labelDetalleAtencion.setText(
				"Atencion: "
						+ consultaSeleccionada
								.getFechaHoraAtencion()
								.format(
										formatoFechaHora
								)
		);

		labelDetalleTurno.setText(
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
				|| seguimiento
						.trim()
						.isEmpty()) {

			seguimiento =
					"Sin seguimiento";
		}

		areaSeguimiento.setText(
				seguimiento
		);

		areaSeguimiento.setCaretPosition(
				0
		);
	}

	/*
	 * =========================================================
	 * RETRASOS
	 * =========================================================
	 */
	private JPanel crearPanelRetrasos() {

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
						"Retrasos informados"
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

		JLabel subtitulo =
				new JLabel(
						"Demoras actuales y futuras comunicadas por los profesionales."
				);

		subtitulo.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						10
				)
		);

		subtitulo.setForeground(
				new Color(
						100,
						116,
						139
				)
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
				subtitulo
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
				e -> cargarRetrasos()
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

		JPanel resumen =
				new JPanel(
						new GridLayout(
								1,
								4,
								12,
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

		valorRetrasos =
				new JLabel(
						"0"
				);

		valorRetrasosHoy =
				new JLabel(
						"0"
				);

		valorRetrasosFuturos =
				new JLabel(
						"0"
				);

		valorProfesionalesRetraso =
				new JLabel(
						"0"
				);

		resumen.add(
				crearResumen(
						"Retrasos",
						valorRetrasos,
						new Color(
								234,
								142,
								25
						)
				)
		);

		resumen.add(
				crearResumen(
						"Hoy",
						valorRetrasosHoy,
						new Color(
								22,
								163,
								74
						)
				)
		);

		resumen.add(
				crearResumen(
						"Proximos",
						valorRetrasosFuturos,
						new Color(
								34,
								125,
								210
						)
				)
		);

		resumen.add(
				crearResumen(
						"Profesionales",
						valorProfesionalesRetraso,
						new Color(
								59,
								91,
								219
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

		String[] columnas = {
				"Profesional",
				"Especialidad",
				"Fecha",
				"Horario",
				"Retraso estimado"
		};

		modeloRetrasos =
				crearModeloNoEditable(
						columnas
				);

		tablaRetrasos =
				new JTable(
						modeloRetrasos
				);

		UITheme.configurarTabla(
				tablaRetrasos
		);

		tablaRetrasos.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION
		);

		tablaRetrasos.setRowHeight(
				38
		);

		tablaRetrasos
				.getColumnModel()
				.getColumn(0)
				.setPreferredWidth(
						200
				);

		tablaRetrasos
				.getColumnModel()
				.getColumn(1)
				.setPreferredWidth(
						180
				);

		tablaRetrasos
				.getColumnModel()
				.getColumn(2)
				.setPreferredWidth(
						110
				);

		tablaRetrasos
				.getColumnModel()
				.getColumn(3)
				.setPreferredWidth(
						150
				);

		tablaRetrasos
				.getColumnModel()
				.getColumn(4)
				.setPreferredWidth(
						140
				);

		JScrollPane scroll =
				new JScrollPane(
						tablaRetrasos
				);

		UITheme.configurarScroll(
				scroll
		);

		scroll.setBorder(
				BorderFactory.createLineBorder(
						new Color(
								220,
								228,
								236
						)
				)
		);

		tarjeta.add(
				scroll,
				BorderLayout.CENTER
		);

		JLabel ayuda =
				new JLabel(
						"Solo se muestran retrasos vigentes de hoy en adelante."
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

		tarjeta.add(
				ayuda,
				BorderLayout.SOUTH
		);

		JPanel contenedor =
				new JPanel(
						new BorderLayout()
				);

		contenedor.setOpaque(
				false
		);

		contenedor.setBorder(
				BorderFactory.createEmptyBorder(
						14,
						0,
						0,
						0
				)
		);

		contenedor.add(
				tarjeta,
				BorderLayout.CENTER
		);

		return contenedor;
	}

	/*
	 * =========================================================
	 * CARGAR RETRASOS
	 * =========================================================
	 */
	private void cargarRetrasos() {

		modeloRetrasos.setRowCount(
				0
		);

		LinkedList<Profesional> profesionales =
				controllerProfesional.buscar(
						""
				);

		int totalRetrasos = 0;
		int retrasosHoy = 0;
		int retrasosFuturos = 0;
		int profesionalesConRetraso = 0;

		LocalDate fechaActual =
				LocalDate.now();

		for (Profesional profesional
				: profesionales) {

			Usuario usuarioProfesional =
					controllerProfesional
							.obtenerUsuarioPorProfesional(
									profesional
											.getIdProfesional()
							);

			if (usuarioProfesional == null) {

				continue;
			}

			LinkedList<Disponibilidad> disponibilidades =
					controllerDisponibilidad
							.obtenerPorProfesional(
									profesional
											.getIdProfesional()
							);

			boolean profesionalContabilizado =
					false;

			for (Disponibilidad disponibilidad
					: disponibilidades) {

				if (disponibilidad.tieneRetraso()
						&& !disponibilidad
								.getFecha()
								.isBefore(
										fechaActual
								)) {

					modeloRetrasos.addRow(
							new Object[] {

									usuarioProfesional
											.getNombre()
											+ " "
											+ usuarioProfesional
													.getApellido(),

									profesional
											.getEspecialidad(),

									disponibilidad
											.getFecha()
											.format(
													formatoFecha
											),

									disponibilidad
											.getHoraInicio()
											.format(
													formatoHora
											)
											+ " - "
											+ disponibilidad
													.getHoraFin()
													.format(
															formatoHora
													),

									disponibilidad
											.getRetrasoEstimado()
											+ " minutos"
							}
					);

					totalRetrasos++;

					if (disponibilidad
							.getFecha()
							.isEqual(
									fechaActual
							)) {

						retrasosHoy++;

					} else if (disponibilidad
							.getFecha()
							.isAfter(
									fechaActual
							)) {

						retrasosFuturos++;
					}

					if (!profesionalContabilizado) {

						profesionalesConRetraso++;

						profesionalContabilizado =
								true;
					}
				}
			}
		}

		valorRetrasos.setText(
				String.valueOf(
						totalRetrasos
				)
		);

		valorRetrasosHoy.setText(
				String.valueOf(
						retrasosHoy
				)
		);

		valorRetrasosFuturos.setText(
				String.valueOf(
						retrasosFuturos
				)
		);

		valorProfesionalesRetraso.setText(
				String.valueOf(
						profesionalesConRetraso
				)
		);
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
	 * AREAS DE SOLO LECTURA
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
	 * ACTUALIZAR TODO
	 * =========================================================
	 */
	private void actualizarTodo() {

		cargarPacientes();
		cargarRetrasos();
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
						10
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
								7,
								11,
								7,
								11
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