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
import java.time.LocalDateTime;
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
import BLL.Turno;
import BLL.Usuario;
import DLL.ControllerConsulta;
import DLL.ControllerProfesional;
import DLL.ControllerTurno;

public class RegistrarConsultaFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO_VENTANA = 1240;
	private static final int ALTO_VENTANA = 700;
	private static final int ANCHO_PANEL_CLINICO = 390;

	private DashboardProfesionalFrame dashboard;
	private Usuario usuario;

	private ControllerProfesional controllerProfesional;
	private ControllerTurno controllerTurno;
	private ControllerConsulta controllerConsulta;

	private Profesional profesional;

	private JTable tablaTurnos;
	private DefaultTableModel modeloTabla;

	private JTextArea campoObservaciones;
	private JTextArea campoSeguimiento;

	private JButton botonRegistrar;

	private JLabel labelCantidad;
	private JLabel labelSeleccion;

	private JLabel valorPendientes;
	private JLabel valorHoy;
	private JLabel valorPresentes;
	private JLabel valorRegistradas;

	private JLabel labelEstadoDetalle;
	private JLabel labelPacienteDetalle;
	private JLabel labelDniDetalle;
	private JLabel labelFechaDetalle;
	private JLabel labelHoraDetalle;

	private Turno turnoSeleccionado;

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern(
					"dd/MM/yyyy"
			);

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern(
					"HH:mm"
			);

	public RegistrarConsultaFrame(
			DashboardProfesionalFrame dashboard,
			Usuario usuario) {

		this.dashboard =
				dashboard;

		this.usuario =
				usuario;

		controllerProfesional =
				new ControllerProfesional();

		controllerTurno =
				new ControllerTurno();

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
		cargarTurnosPendientes();
	}

	/*
	 * =========================================================
	 * VENTANA
	 * =========================================================
	 */
	private void configurarVentana() {

		setTitle(
				"Telemedicina - Registrar consulta - "
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
				crearPanelTurnos(),
				BorderLayout.CENTER
		);

		contenido.add(
				crearPanelConsulta(),
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
						"PROFESIONAL  /  REGISTRAR CONSULTA"
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
						"Registrar consulta realizada"
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
						"Documenta la atencion, observaciones clinicas y seguimiento del paciente."
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
	 * PANEL TURNOS
	 * =========================================================
	 */
	private JPanel crearPanelTurnos() {

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
						"Atenciones pendientes de registro"
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
						"0 consultas pendientes"
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
				e -> cargarTurnosPendientes()
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
		 * RESUMEN CLINICO
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

		valorPendientes =
				new JLabel(
						"0"
				);

		valorHoy =
				new JLabel(
						"0"
				);

		valorPresentes =
				new JLabel(
						"0"
				);

		valorRegistradas =
				new JLabel(
						"0"
				);

		resumen.add(
				crearResumen(
						"Pendientes",
						valorPendientes,
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
						"Presentes",
						valorPresentes,
						new Color(
								34,
								125,
								210
						)
				)
		);

		resumen.add(
				crearResumen(
						"Registradas",
						valorRegistradas,
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

		/*
		 * =====================================================
		 * TABLA
		 * =====================================================
		 */
		crearTabla();

		JScrollPane scrollTabla =
				new JScrollPane(
						tablaTurnos
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
						"Seleccione una atencion para registrar la consulta."
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
						"Solo se muestran pacientes presentes sin consulta registrada."
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
				"ID turno",
				"Paciente",
				"DNI",
				"Fecha",
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

		tablaTurnos =
				new JTable(
						modeloTabla
				);

		UITheme.configurarTabla(
				tablaTurnos
		);

		tablaTurnos.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION
		);

		tablaTurnos.setRowHeight(
				38
		);

		tablaTurnos.setIntercellSpacing(
				new Dimension(
						0,
						1
				)
		);

		tablaTurnos
				.getColumnModel()
				.getColumn(0)
				.setPreferredWidth(
						65
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(1)
				.setPreferredWidth(
						190
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(2)
				.setPreferredWidth(
						120
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(3)
				.setPreferredWidth(
						110
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(4)
				.setPreferredWidth(
						85
				);

		tablaTurnos
				.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								cargarTurnoSeleccionado();
							}
						}
				);
	}

	/*
	 * =========================================================
	 * PANEL CLINICO
	 * =========================================================
	 */
	private JPanel crearPanelConsulta() {

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
						ANCHO_PANEL_CLINICO,
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
						"REGISTRO CLINICO"
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
						"Datos de la consulta"
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
								+ "Documente la atencion realizada "
								+ "al paciente seleccionado."
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
		 * TURNO SELECCIONADO
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
						105
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
						"Seleccione una atencion"
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

		JPanel filaFechaHora =
				new JPanel(
						new GridLayout(
								1,
								2,
								8,
								0
						)
				);

		filaFechaHora.setOpaque(
				false
		);

		filaFechaHora.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelFechaDetalle =
				new JLabel(
						"Fecha: —"
				);

		labelHoraDetalle =
				new JLabel(
						"Hora: —"
				);

		labelFechaDetalle.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						9
				)
		);

		labelHoraDetalle.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						9
				)
		);

		labelFechaDetalle.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelHoraDetalle.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		filaFechaHora.add(
				labelFechaDetalle
		);

		filaFechaHora.add(
				labelHoraDetalle
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
				filaFechaHora
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
						"Observaciones *"
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

		campoObservaciones =
				new JTextArea();

		configurarAreaTexto(
				campoObservaciones
		);

		JScrollPane scrollObservaciones =
				new JScrollPane(
						campoObservaciones
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
						320,
						95
				)
		);

		scrollObservaciones.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						95
				)
		);

		scrollObservaciones.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel ayudaObservaciones =
				new JLabel(
						"Campo obligatorio."
				);

		ayudaObservaciones.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						9
				)
		);

		ayudaObservaciones.setForeground(
				new Color(
						148,
						163,
						184
				)
		);

		ayudaObservaciones.setAlignmentX(
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
						3
				)
		);

		cuerpo.add(
				ayudaObservaciones
		);

		cuerpo.add(
				Box.createVerticalStrut(
						10
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

		campoSeguimiento =
				new JTextArea();

		configurarAreaTexto(
				campoSeguimiento
		);

		JScrollPane scrollSeguimiento =
				new JScrollPane(
						campoSeguimiento
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
						320,
						70
				)
		);

		scrollSeguimiento.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						70
				)
		);

		scrollSeguimiento.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel ayudaSeguimiento =
				new JLabel(
						"Opcional. Puede dejarse vacio."
				);

		ayudaSeguimiento.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						9
				)
		);

		ayudaSeguimiento.setForeground(
				new Color(
						148,
						163,
						184
				)
		);

		ayudaSeguimiento.setAlignmentX(
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
						3
				)
		);

		cuerpo.add(
				ayudaSeguimiento
		);

		cuerpo.add(
				Box.createVerticalStrut(
						12
				)
		);

		/*
		 * =====================================================
		 * REGISTRAR
		 * =====================================================
		 */
		botonRegistrar =
				new PremiumButton(
						"Registrar consulta"
				);

		botonRegistrar.setAlignmentX(
				LEFT_ALIGNMENT
		);

		botonRegistrar.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						40
				)
		);

		botonRegistrar.setPreferredSize(
				new Dimension(
						320,
						40
				)
		);

		botonRegistrar.setEnabled(
				false
		);

		botonRegistrar.addActionListener(
				e -> registrarConsulta()
		);

		cuerpo.add(
				botonRegistrar
		);

		tarjeta.add(
				cuerpo,
				BorderLayout.CENTER
		);

		return tarjeta;
	}

	/*
	 * =========================================================
	 * TEXT AREA
	 * =========================================================
	 */
	private void configurarAreaTexto(
			JTextArea area) {

		area.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						12
				)
		);

		area.setForeground(
				new Color(
						15,
						23,
						42
				)
		);

		area.setBackground(
				Color.WHITE
		);

		area.setLineWrap(
				true
		);

		area.setWrapStyleWord(
				true
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
	 * CARGAR TURNOS PENDIENTES
	 * =========================================================
	 */
	private void cargarTurnosPendientes() {

		modeloTabla.setRowCount(
				0
		);

		LinkedList<Turno> turnos =
				controllerTurno.obtenerPorProfesional(
						profesional.getIdProfesional()
				);

		int pendientes = 0;
		int hoy = 0;
		int presentes = 0;
		int registradas = 0;

		LocalDate fechaActual =
				LocalDate.now();

		for (Turno turno : turnos) {

			if (turno.estaPresente()) {

				presentes++;

				boolean existeConsulta =
						controllerConsulta.existePorTurno(
								turno.getIdTurno()
						);

				if (existeConsulta) {

					registradas++;

				} else {

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
											)
							}
					);

					pendientes++;

					if (turno
							.getFecha()
							.isEqual(
									fechaActual
							)) {

						hoy++;
					}
				}
			}
		}

		labelCantidad.setText(
				pendientes
						+ (
								pendientes == 1
										? " consulta pendiente"
										: " consultas pendientes"
						)
		);

		valorPendientes.setText(
				String.valueOf(
						pendientes
				)
		);

		valorHoy.setText(
				String.valueOf(
						hoy
				)
		);

		valorPresentes.setText(
				String.valueOf(
						presentes
				)
		);

		valorRegistradas.setText(
				String.valueOf(
						registradas
				)
		);

		turnoSeleccionado =
				null;

		tablaTurnos.clearSelection();

		campoObservaciones.setText(
				""
		);

		campoSeguimiento.setText(
				""
		);

		botonRegistrar.setEnabled(
				false
		);

		actualizarDetalle();
	}

	/*
	 * =========================================================
	 * TURNO SELECCIONADO
	 * =========================================================
	 */
	private void cargarTurnoSeleccionado() {

		int fila =
				tablaTurnos.getSelectedRow();

		if (fila < 0) {

			turnoSeleccionado =
					null;

			botonRegistrar.setEnabled(
					false
			);

			actualizarDetalle();

			return;
		}

		int idTurno =
				(int) modeloTabla.getValueAt(
						fila,
						0
				);

		Turno turno =
				controllerTurno.buscarPorId(
						idTurno
				);

		if (turno == null
				|| !turno.estaPresente()
				|| controllerConsulta.existePorTurno(
						idTurno
				)) {

			turnoSeleccionado =
					null;

			botonRegistrar.setEnabled(
					false
			);

			actualizarDetalle();

			return;
		}

		turnoSeleccionado =
				turno;

		botonRegistrar.setEnabled(
				true
		);

		actualizarDetalle();
	}

	/*
	 * =========================================================
	 * DETALLE
	 * =========================================================
	 */
	private void actualizarDetalle() {

		if (labelEstadoDetalle == null
				|| labelPacienteDetalle == null
				|| labelDniDetalle == null
				|| labelFechaDetalle == null
				|| labelHoraDetalle == null) {

			return;
		}

		if (turnoSeleccionado == null) {

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
					"Seleccione una atencion"
			);

			labelDniDetalle.setText(
					"DNI del paciente"
			);

			labelFechaDetalle.setText(
					"Fecha: —"
			);

			labelHoraDetalle.setText(
					"Hora: —"
			);

			labelSeleccion.setText(
					"Seleccione una atencion para registrar la consulta."
			);

			return;
		}

		labelEstadoDetalle.setText(
				"PACIENTE PRESENTE"
		);

		labelEstadoDetalle.setForeground(
				new Color(
						22,
						163,
						74
				)
		);

		labelPacienteDetalle.setText(
				turnoSeleccionado
						.getPaciente()
						.getNombre()
						+ " "
						+ turnoSeleccionado
								.getPaciente()
								.getApellido()
		);

		labelDniDetalle.setText(
				"DNI "
						+ turnoSeleccionado
								.getPaciente()
								.getDni()
		);

		labelFechaDetalle.setText(
				"Fecha: "
						+ turnoSeleccionado
								.getFecha()
								.format(
										formatoFecha
								)
		);

		labelHoraDetalle.setText(
				"Hora: "
						+ turnoSeleccionado
								.getHora()
								.format(
										formatoHora
								)
		);

		labelSeleccion.setText(
				"Seleccionado: turno #"
						+ turnoSeleccionado
								.getIdTurno()
						+ "  •  "
						+ turnoSeleccionado
								.getPaciente()
								.getNombre()
						+ " "
						+ turnoSeleccionado
								.getPaciente()
								.getApellido()
		);
	}

	/*
	 * =========================================================
	 * REGISTRAR CONSULTA
	 * =========================================================
	 */
	private void registrarConsulta() {

		if (turnoSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un turno de la tabla.",
					"Turno no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		/*
		 * Verificacion nuevamente antes de registrar.
		 */
		if (!turnoSeleccionado.estaPresente()) {

			JOptionPane.showMessageDialog(
					this,
					"Solo se puede registrar una consulta para un paciente presente.",
					"Operacion no permitida",
					JOptionPane.WARNING_MESSAGE
			);

			cargarTurnosPendientes();

			return;
		}

		/*
		 * Evita duplicar una consulta si la base cambio
		 * desde que se cargo la tabla.
		 */
		if (controllerConsulta.existePorTurno(
				turnoSeleccionado.getIdTurno())) {

			JOptionPane.showMessageDialog(
					this,
					"Este turno ya posee una consulta registrada.",
					"Consulta existente",
					JOptionPane.WARNING_MESSAGE
			);

			cargarTurnosPendientes();

			return;
		}

		String observaciones =
				campoObservaciones
						.getText()
						.trim();

		String seguimiento =
				campoSeguimiento
						.getText()
						.trim();

		if (observaciones.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"Las observaciones son obligatorias.",
					"Datos incompletos",
					JOptionPane.WARNING_MESSAGE
			);

			campoObservaciones.requestFocus();

			return;
		}

		if (seguimiento.isEmpty()) {

			seguimiento =
					null;
		}

		Consulta consultaNueva =
				new Consulta(
						0,
						LocalDateTime.now(),
						observaciones,
						seguimiento,
						turnoSeleccionado
				);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea registrar esta consulta?\n\n"
								+ "Paciente: "
								+ turnoSeleccionado
										.getPaciente()
										.getNombre()
								+ " "
								+ turnoSeleccionado
										.getPaciente()
										.getApellido()

								+ "\nDNI: "
								+ turnoSeleccionado
										.getPaciente()
										.getDni()

								+ "\nFecha del turno: "
								+ turnoSeleccionado
										.getFecha()
										.format(
												formatoFecha
										)

								+ "\nHora: "
								+ turnoSeleccionado
										.getHora()
										.format(
												formatoHora
										)

								+ "\n\nObservaciones: "
								+ observaciones

								+ "\nSeguimiento: "
								+ (
										seguimiento == null
												? "Sin seguimiento"
												: seguimiento
								),
						"Confirmar consulta",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean registrada =
				controllerConsulta.registrar(
						consultaNueva
				);

		if (registrada) {

			JOptionPane.showMessageDialog(
					this,
					"Consulta registrada correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarTurnosPendientes();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo registrar la consulta.\n"
							+ "Verifique que el paciente continue presente "
							+ "y que el turno no tenga otra consulta asociada.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);

			cargarTurnosPendientes();
		}
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

	/*
	 * =========================================================
	 * BOTON PREMIUM
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
					new Font(
							"Segoe UI",
							Font.BOLD,
							12
					)
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

			actualizarColorTexto();

			addMouseListener(
					new MouseAdapter() {

						@Override
						public void mouseEntered(
								MouseEvent e) {

							if (isEnabled()) {

								hover = true;
								repaint();
							}
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

		private void actualizarColorTexto() {

			if (isEnabled()) {

				setForeground(
						Color.WHITE
				);

			} else {

				setForeground(
						new Color(
								148,
								163,
								184
						)
				);
			}
		}

		@Override
		public void setEnabled(
				boolean enabled) {

			super.setEnabled(
					enabled
			);

			actualizarColorTexto();
			repaint();
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

			if (!isEnabled()) {

				g2.setColor(
						new Color(
								241,
								245,
								249
						)
				);

			} else if (getModel().isPressed()) {

				g2.setColor(
						new Color(
								7,
								92,
								116
						)
				);

			} else if (hover) {

				g2.setColor(
						UITheme.COLOR_ACENTO
				);

			} else {

				g2.setColor(
						UITheme.COLOR_PRIMARIO
				);
			}

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