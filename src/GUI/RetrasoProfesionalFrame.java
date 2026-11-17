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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;

import BLL.Disponibilidad;
import BLL.Profesional;
import BLL.Usuario;
import DLL.ControllerDisponibilidad;
import DLL.ControllerProfesional;

public class RetrasoProfesionalFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO_VENTANA = 1160;

	/*
	 * Altura ampliada para que todas las acciones
	 * entren completas sin comprimir la interfaz.
	 */
	private static final int ALTO_VENTANA = 650;

	private static final int ANCHO_PANEL = 340;

	private DashboardProfesionalFrame dashboard;
	private Usuario usuario;

	private ControllerProfesional controllerProfesional;
	private ControllerDisponibilidad controllerDisponibilidad;

	private Profesional profesional;

	private JTable tablaDisponibilidades;
	private DefaultTableModel modeloTabla;

	private JTextField campoRetraso;

	private JButton botonGuardar;

	private JLabel labelCantidad;
	private JLabel labelSeleccion;

	private JLabel valorDisponibles;
	private JLabel valorHoy;
	private JLabel valorConRetraso;
	private JLabel valorSinRetraso;

	private JLabel labelEstadoDetalle;
	private JLabel labelFechaDetalle;
	private JLabel labelHorarioDetalle;
	private JLabel labelRetrasoActual;

	private Disponibilidad disponibilidadSeleccionada;

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern(
					"dd/MM/yyyy"
			);

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern(
					"HH:mm"
			);

	public RetrasoProfesionalFrame(
			DashboardProfesionalFrame dashboard,
			Usuario usuario) {

		this.dashboard =
				dashboard;

		this.usuario =
				usuario;

		controllerProfesional =
				new ControllerProfesional();

		controllerDisponibilidad =
				new ControllerDisponibilidad();

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
		cargarDisponibilidades();
	}

	/*
	 * =========================================================
	 * VENTANA
	 * =========================================================
	 */
	private void configurarVentana() {

		setTitle(
				"Telemedicina - Retraso estimado - "
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
				crearPanelDisponibilidades(),
				BorderLayout.CENTER
		);

		contenido.add(
				crearPanelRetraso(),
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
						"PROFESIONAL  /  RETRASO ESTIMADO"
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
						"Gestion de retrasos"
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
						"Informa demoras estimadas para mantener actualizada la atencion de pacientes."
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
	 * PANEL DE DISPONIBILIDADES
	 * =========================================================
	 */
	private JPanel crearPanelDisponibilidades() {

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
		 * TITULO
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
						"Disponibilidades activas"
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
						"0 disponibilidades"
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
				e -> cargarDisponibilidades()
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

		valorDisponibles =
				new JLabel(
						"0"
				);

		valorHoy =
				new JLabel(
						"0"
				);

		valorConRetraso =
				new JLabel(
						"0"
				);

		valorSinRetraso =
				new JLabel(
						"0"
				);

		resumen.add(
				crearResumen(
						"Disponibles",
						valorDisponibles,
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
						"Con retraso",
						valorConRetraso,
						new Color(
								234,
								142,
								25
						)
				)
		);

		resumen.add(
				crearResumen(
						"Sin retraso",
						valorSinRetraso,
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

		JScrollPane scroll =
				new JScrollPane(
						tablaDisponibilidades
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
						"Seleccione una disponibilidad para informar un retraso."
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
						"Solo se muestran disponibilidades de hoy en adelante."
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
				"Fecha",
				"Hora inicio",
				"Hora fin",
				"Retraso actual"
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

		tablaDisponibilidades =
				new JTable(
						modeloTabla
				);

		UITheme.configurarTabla(
				tablaDisponibilidades
		);

		tablaDisponibilidades.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION
		);

		tablaDisponibilidades.setRowHeight(
				38
		);

		tablaDisponibilidades.setIntercellSpacing(
				new Dimension(
						0,
						1
				)
		);

		tablaDisponibilidades
				.getColumnModel()
				.getColumn(0)
				.setPreferredWidth(
						50
				);

		tablaDisponibilidades
				.getColumnModel()
				.getColumn(1)
				.setPreferredWidth(
						125
				);

		tablaDisponibilidades
				.getColumnModel()
				.getColumn(2)
				.setPreferredWidth(
						120
				);

		tablaDisponibilidades
				.getColumnModel()
				.getColumn(3)
				.setPreferredWidth(
						120
				);

		tablaDisponibilidades
				.getColumnModel()
				.getColumn(4)
				.setPreferredWidth(
						140
				);

		tablaDisponibilidades
				.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								cargarSeleccion();
							}
						}
				);
	}

	/*
	 * =========================================================
	 * PANEL DE RETRASO
	 * =========================================================
	 */
	private JPanel crearPanelRetraso() {

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
						ANCHO_PANEL,
						0
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
						20,
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
						"ACTUALIZACION CLINICA"
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
						"Retraso estimado"
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
								+ "Seleccione una disponibilidad e informe "
								+ "la demora estimada en minutos."
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
						5
				)
		);

		cabecera.add(
				titulo
		);

		cabecera.add(
				Box.createVerticalStrut(
						5
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
		 * DETALLE DE SELECCION
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
						14,
						14,
						14,
						14
				)
		);

		ficha.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						125
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

		labelFechaDetalle =
				new JLabel(
						"Seleccione una disponibilidad"
				);

		labelFechaDetalle.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						13
				)
		);

		labelFechaDetalle.setForeground(
				new Color(
						15,
						23,
						42
				)
		);

		labelFechaDetalle.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelHorarioDetalle =
				new JLabel(
						"Horario de atencion"
				);

		labelHorarioDetalle.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						10
				)
		);

		labelHorarioDetalle.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelHorarioDetalle.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelRetrasoActual =
				new JLabel(
						"Retraso actual: —"
				);

		labelRetrasoActual.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						10
				)
		);

		labelRetrasoActual.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelRetrasoActual.setAlignmentX(
				LEFT_ALIGNMENT
		);

		ficha.add(
				labelEstadoDetalle
		);

		ficha.add(
				Box.createVerticalStrut(
						7
				)
		);

		ficha.add(
				labelFechaDetalle
		);

		ficha.add(
				Box.createVerticalStrut(
						4
				)
		);

		ficha.add(
				labelHorarioDetalle
		);

		ficha.add(
				Box.createVerticalStrut(
						4
				)
		);

		ficha.add(
				labelRetrasoActual
		);

		cuerpo.add(
				ficha
		);

		cuerpo.add(
				Box.createVerticalStrut(
						18
				)
		);

		/*
		 * =====================================================
		 * CAMPO RETRASO
		 * =====================================================
		 */
		JLabel labelMinutos =
				new JLabel(
						"Retraso estimado en minutos"
				);

		labelMinutos.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						10
				)
		);

		labelMinutos.setForeground(
				new Color(
						51,
						65,
						85
				)
		);

		labelMinutos.setAlignmentX(
				LEFT_ALIGNMENT
		);

		campoRetraso =
				new JTextField();

		UITheme.configurarCampo(
				campoRetraso
		);

		campoRetraso.setAlignmentX(
				LEFT_ALIGNMENT
		);

		campoRetraso.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						38
				)
		);

		campoRetraso.setPreferredSize(
				new Dimension(
						280,
						38
				)
		);

		JLabel ayudaCampo =
				new JLabel(
						"Ingrese 0 si no existe demora."
				);

		ayudaCampo.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						9
				)
		);

		ayudaCampo.setForeground(
				new Color(
						148,
						163,
						184
				)
		);

		ayudaCampo.setAlignmentX(
				LEFT_ALIGNMENT
		);

		cuerpo.add(
				labelMinutos
		);

		cuerpo.add(
				Box.createVerticalStrut(
						6
				)
		);

		cuerpo.add(
				campoRetraso
		);

		cuerpo.add(
				Box.createVerticalStrut(
						5
				)
		);

		cuerpo.add(
				ayudaCampo
		);

		cuerpo.add(
				Box.createVerticalStrut(
						18
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
								255,
								250,
								235
						),
						12,
						new Color(
								245,
								222,
								160
						)
				);

		aviso.setLayout(
				new BorderLayout()
		);

		aviso.setBorder(
				BorderFactory.createEmptyBorder(
						10,
						11,
						10,
						11
				)
		);

		aviso.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						58
				)
		);

		aviso.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel textoAviso =
				new JLabel(
						"<html>"
								+ "<b>Informacion para recepcion</b><br>"
								+ "El retraso registrado queda asociado a esta disponibilidad."
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
						120,
						89,
						30
				)
		);

		aviso.add(
				textoAviso,
				BorderLayout.CENTER
		);

		cuerpo.add(
				aviso
		);

		cuerpo.add(
				Box.createVerticalStrut(
						18
				)
		);

		/*
		 * =====================================================
		 * GUARDAR
		 * =====================================================
		 */
		botonGuardar =
				new PremiumButton(
						"Guardar retraso"
				);

		botonGuardar.setAlignmentX(
				LEFT_ALIGNMENT
		);

		botonGuardar.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						42
				)
		);

		botonGuardar.setPreferredSize(
				new Dimension(
						280,
						42
				)
		);

		botonGuardar.setEnabled(
				false
		);

		botonGuardar.addActionListener(
				e -> guardarRetraso()
		);

		cuerpo.add(
				botonGuardar
		);

		tarjeta.add(
				cuerpo,
				BorderLayout.CENTER
		);

		return tarjeta;
	}

	/*
	 * =========================================================
	 * CARGAR DISPONIBILIDADES
	 * =========================================================
	 */
	private void cargarDisponibilidades() {

		modeloTabla.setRowCount(
				0
		);

		LinkedList<Disponibilidad> disponibilidades =
				controllerDisponibilidad.obtenerPorProfesional(
						profesional.getIdProfesional()
				);

		int disponibles = 0;
		int hoy = 0;
		int conRetraso = 0;
		int sinRetraso = 0;

		LocalDate fechaActual =
				LocalDate.now();

		for (Disponibilidad disponibilidad
				: disponibilidades) {

			if (!disponibilidad
					.getFecha()
					.isBefore(
							fechaActual
					)) {

				modeloTabla.addRow(
						new Object[] {

								disponibilidad
										.getIdDisponibilidad(),

								disponibilidad
										.getFecha()
										.format(
												formatoFecha
										),

								disponibilidad
										.getHoraInicio()
										.format(
												formatoHora
										),

								disponibilidad
										.getHoraFin()
										.format(
												formatoHora
										),

								disponibilidad
										.getRetrasoEstimado()
										+ " minutos"
						}
				);

				disponibles++;

				if (disponibilidad
						.getFecha()
						.isEqual(
								fechaActual
						)) {

					hoy++;
				}

				if (disponibilidad
						.getRetrasoEstimado() > 0) {

					conRetraso++;

				} else {

					sinRetraso++;
				}
			}
		}

		labelCantidad.setText(
				disponibles
						+ (
								disponibles == 1
										? " disponibilidad activa"
										: " disponibilidades activas"
						)
		);

		valorDisponibles.setText(
				String.valueOf(
						disponibles
				)
		);

		valorHoy.setText(
				String.valueOf(
						hoy
				)
		);

		valorConRetraso.setText(
				String.valueOf(
						conRetraso
				)
		);

		valorSinRetraso.setText(
				String.valueOf(
						sinRetraso
				)
		);

		disponibilidadSeleccionada =
				null;

		tablaDisponibilidades.clearSelection();

		campoRetraso.setText(
				""
		);

		botonGuardar.setEnabled(
				false
		);

		actualizarDetalle();
	}

	/*
	 * =========================================================
	 * SELECCION
	 * =========================================================
	 */
	private void cargarSeleccion() {

		int fila =
				tablaDisponibilidades
						.getSelectedRow();

		if (fila < 0) {

			disponibilidadSeleccionada =
					null;

			campoRetraso.setText(
					""
			);

			botonGuardar.setEnabled(
					false
			);

			actualizarDetalle();

			return;
		}

		int idDisponibilidad =
				(int) modeloTabla.getValueAt(
						fila,
						0
				);

		disponibilidadSeleccionada =
				controllerDisponibilidad.buscarPorId(
						idDisponibilidad,
						profesional.getIdProfesional()
				);

		if (disponibilidadSeleccionada == null) {

			campoRetraso.setText(
					""
			);

			botonGuardar.setEnabled(
					false
			);

			actualizarDetalle();

			return;
		}

		campoRetraso.setText(
				String.valueOf(
						disponibilidadSeleccionada
								.getRetrasoEstimado()
				)
		);

		botonGuardar.setEnabled(
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
				|| labelFechaDetalle == null
				|| labelHorarioDetalle == null
				|| labelRetrasoActual == null) {

			return;
		}

		if (disponibilidadSeleccionada == null) {

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

			labelFechaDetalle.setText(
					"Seleccione una disponibilidad"
			);

			labelHorarioDetalle.setText(
					"Horario de atencion"
			);

			labelRetrasoActual.setText(
					"Retraso actual: —"
			);

			labelSeleccion.setText(
					"Seleccione una disponibilidad para informar un retraso."
			);

			return;
		}

		int retraso =
				disponibilidadSeleccionada
						.getRetrasoEstimado();

		if (retraso > 0) {

			labelEstadoDetalle.setText(
					"CON RETRASO"
			);

			labelEstadoDetalle.setForeground(
					new Color(
							202,
							113,
							16
					)
			);

		} else {

			labelEstadoDetalle.setText(
					"SIN RETRASO"
			);

			labelEstadoDetalle.setForeground(
					new Color(
							22,
							163,
							74
					)
			);
		}

		labelFechaDetalle.setText(
				disponibilidadSeleccionada
						.getFecha()
						.format(
								formatoFecha
						)
		);

		labelHorarioDetalle.setText(
				disponibilidadSeleccionada
						.getHoraInicio()
						.format(
								formatoHora
						)
						+ " - "
						+ disponibilidadSeleccionada
								.getHoraFin()
								.format(
										formatoHora
								)
		);

		labelRetrasoActual.setText(
				"Retraso actual: "
						+ retraso
						+ " minutos"
		);

		labelSeleccion.setText(
				"Seleccionada: disponibilidad #"
						+ disponibilidadSeleccionada
								.getIdDisponibilidad()
						+ "  •  "
						+ disponibilidadSeleccionada
								.getFecha()
								.format(
										formatoFecha
								)
		);
	}

	/*
	 * =========================================================
	 * GUARDAR RETRASO
	 * =========================================================
	 */
	private void guardarRetraso() {

		if (disponibilidadSeleccionada == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione una disponibilidad de la tabla.",
					"Disponibilidad no seleccionada",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		String retrasoTexto =
				campoRetraso
						.getText()
						.trim();

		if (retrasoTexto.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"Debe ingresar el retraso estimado.",
					"Dato incompleto",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int minutos;

		try {

			minutos =
					Integer.parseInt(
							retrasoTexto
					);

			if (minutos < 0) {

				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					this,
					"El retraso debe ser un numero entero mayor o igual a cero.",
					"Retraso invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea registrar este retraso?\n\n"
								+ "Fecha: "
								+ disponibilidadSeleccionada
										.getFecha()
										.format(
												formatoFecha
										)
								+ "\nHorario: "
								+ disponibilidadSeleccionada
										.getHoraInicio()
										.format(
												formatoHora
										)
								+ " - "
								+ disponibilidadSeleccionada
										.getHoraFin()
										.format(
												formatoHora
										)
								+ "\nRetraso estimado: "
								+ minutos
								+ " minutos",
						"Confirmar retraso",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean actualizado =
				controllerDisponibilidad.actualizarRetraso(
						disponibilidadSeleccionada
								.getIdDisponibilidad(),

						profesional
								.getIdProfesional(),

						minutos
				);

		if (actualizado) {

			JOptionPane.showMessageDialog(
					this,
					"Retraso estimado registrado correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarDisponibilidades();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo registrar el retraso estimado.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
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