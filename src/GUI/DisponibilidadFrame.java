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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
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

public class DisponibilidadFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO_VENTANA = 1180;
	private static final int ALTO_VENTANA = 650;
	private static final int ANCHO_FORMULARIO = 350;

	private DashboardProfesionalFrame dashboard;
	private Usuario usuario;

	private ControllerProfesional controllerProfesional;
	private ControllerDisponibilidad controllerDisponibilidad;

	private Profesional profesional;

	private JTable tablaDisponibilidades;
	private DefaultTableModel modeloTabla;

	private JTextField campoFecha;
	private JTextField campoHoraInicio;
	private JTextField campoHoraFin;

	private JButton botonGuardar;

	private JLabel labelCantidad;
	private JLabel labelSeleccion;
	private JLabel labelModoFormulario;

	private JLabel valorTotales;
	private JLabel valorFuturas;
	private JLabel valorHoy;
	private JLabel valorRetrasos;

	private Disponibilidad disponibilidadSeleccionada;

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern(
					"dd/MM/yyyy"
			);

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern(
					"HH:mm"
			);

	public DisponibilidadFrame(
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
				"Telemedicina - Disponibilidad - "
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
				crearPanelFormulario(),
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
						"PROFESIONAL  /  DISPONIBILIDAD"
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
						"Gestion de disponibilidad"
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
						"Administra fechas y horarios disponibles para la atencion de pacientes."
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
						"Agenda de disponibilidad"
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
						"0 disponibilidades registradas"
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

		valorTotales =
				new JLabel(
						"0"
				);

		valorFuturas =
				new JLabel(
						"0"
				);

		valorHoy =
				new JLabel(
						"0"
				);

		valorRetrasos =
				new JLabel(
						"0"
				);

		resumen.add(
				crearResumen(
						"Totales",
						valorTotales,
						new Color(
								34,
								125,
								210
						)
				)
		);

		resumen.add(
				crearResumen(
						"Futuras",
						valorFuturas,
						new Color(
								59,
								91,
								219
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
						valorRetrasos,
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
						"Seleccione una disponibilidad para modificarla."
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
						"Las fechas anteriores a hoy no pueden registrarse."
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
				"Retraso estimado"
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
						130
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
						150
				);

		tablaDisponibilidades
				.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								cargarDisponibilidadSeleccionada();
							}
						}
				);
	}

	/*
	 * =========================================================
	 * FORMULARIO
	 * =========================================================
	 */
	private JPanel crearPanelFormulario() {

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
						ANCHO_FORMULARIO,
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
						16,
						20
				)
		);

		/*
		 * =====================================================
		 * CABECERA DEL FORMULARIO
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

		labelModoFormulario =
				new JLabel(
						"NUEVA DISPONIBILIDAD"
				);

		labelModoFormulario.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						9
				)
		);

		labelModoFormulario.setForeground(
				UITheme.COLOR_ACENTO
		);

		labelModoFormulario.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel titulo =
				new JLabel(
						"Horario de atencion"
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
								+ "Defina la fecha y el rango horario "
								+ "en el que estara disponible para atender."
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
				labelModoFormulario
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

		campoFecha =
				new JTextField();

		UITheme.configurarCampo(
				campoFecha
		);

		campoHoraInicio =
				new JTextField();

		UITheme.configurarCampo(
				campoHoraInicio
		);

		campoHoraFin =
				new JTextField();

		UITheme.configurarCampo(
				campoHoraFin
		);

		/*
		 * =====================================================
		 * FECHA
		 * =====================================================
		 */
		cuerpo.add(
				crearBloqueCampo(
						"Fecha",
						"Formato dd/MM/yyyy",
						campoFecha
				)
		);

		cuerpo.add(
				Box.createVerticalStrut(
						10
				)
		);

		/*
		 * =====================================================
		 * HORARIOS
		 * =====================================================
		 */
		JPanel filaHoras =
				new JPanel(
						new GridLayout(
								1,
								2,
								12,
								0
						)
				);

		filaHoras.setOpaque(
				false
		);

		filaHoras.setAlignmentX(
				LEFT_ALIGNMENT
		);

		filaHoras.add(
				crearBloqueCampo(
						"Hora inicio",
						"Formato HH:mm",
						campoHoraInicio
				)
		);

		filaHoras.add(
				crearBloqueCampo(
						"Hora fin",
						"Formato HH:mm",
						campoHoraFin
				)
		);

		cuerpo.add(
				filaHoras
		);

		cuerpo.add(
				Box.createVerticalStrut(
						14
				)
		);

		/*
		 * =====================================================
		 * VALIDACION AUTOMATICA
		 * =====================================================
		 */
		RoundedPanel ayuda =
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

		ayuda.setLayout(
				new BorderLayout()
		);

		ayuda.setBorder(
				BorderFactory.createEmptyBorder(
						9,
						11,
						9,
						11
				)
		);

		ayuda.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						54
				)
		);

		ayuda.setAlignmentX(
				LEFT_ALIGNMENT
		);

		JLabel textoAyuda =
				new JLabel(
						"<html>"
								+ "<b>Validacion automatica</b><br>"
								+ "La fecha no puede ser anterior a hoy y la hora final "
								+ "debe ser posterior a la inicial."
								+ "</html>"
				);

		textoAyuda.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						9
				)
		);

		textoAyuda.setForeground(
				new Color(
						71,
						85,
						105
				)
		);

		ayuda.add(
				textoAyuda,
				BorderLayout.CENTER
		);

		cuerpo.add(
				ayuda
		);

		/*
		 * Aire limpio.
		 * Sin separador gris.
		 */
		cuerpo.add(
				Box.createVerticalStrut(
						14
				)
		);

		/*
		 * =====================================================
		 * REGISTRAR / GUARDAR
		 * =====================================================
		 */
		botonGuardar =
				new PremiumButton(
						"Registrar disponibilidad",
						true
				);

		botonGuardar.setAlignmentX(
				LEFT_ALIGNMENT
		);

		botonGuardar.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						40
				)
		);

		botonGuardar.setPreferredSize(
				new Dimension(
						300,
						40
				)
		);

		cuerpo.add(
				botonGuardar
		);

		cuerpo.add(
				Box.createVerticalStrut(
						6
				)
		);

		/*
		 * =====================================================
		 * LIMPIAR / NUEVA
		 * =====================================================
		 */
		JButton botonLimpiar =
				new JButton(
						"Limpiar / Nueva"
				);

		configurarBotonTexto(
				botonLimpiar
		);

		botonLimpiar.setAlignmentX(
				CENTER_ALIGNMENT
		);

		botonLimpiar.setPreferredSize(
				new Dimension(
						160,
						24
				)
		);

		botonLimpiar.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						24
				)
		);

		cuerpo.add(
				botonLimpiar
		);

		/*
		 * =====================================================
		 * EVENTOS
		 * =====================================================
		 */
		botonGuardar.addActionListener(
				e -> guardarDisponibilidad()
		);

		botonLimpiar.addActionListener(
				e -> limpiarFormulario()
		);

		tarjeta.add(
				cuerpo,
				BorderLayout.CENTER
		);

		return tarjeta;
	}

	/*
	 * =========================================================
	 * BLOQUE CAMPO
	 * =========================================================
	 */
	private JPanel crearBloqueCampo(
			String titulo,
			String ayuda,
			JComponent campo) {

		JPanel bloque =
				new JPanel();

		bloque.setLayout(
				new BoxLayout(
						bloque,
						BoxLayout.Y_AXIS
				)
		);

		bloque.setOpaque(
				false
		);

		JLabel label =
				new JLabel(
						titulo
				);

		label.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						10
				)
		);

		label.setForeground(
				new Color(
						51,
						65,
						85
				)
		);

		label.setAlignmentX(
				LEFT_ALIGNMENT
		);

		campo.setAlignmentX(
				LEFT_ALIGNMENT
		);

		campo.setPreferredSize(
				new Dimension(
						150,
						36
				)
		);

		campo.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						36
				)
		);

		JLabel textoAyuda =
				new JLabel(
						ayuda
				);

		textoAyuda.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						9
				)
		);

		textoAyuda.setForeground(
				new Color(
						148,
						163,
						184
				)
		);

		textoAyuda.setAlignmentX(
				LEFT_ALIGNMENT
		);

		bloque.add(
				label
		);

		bloque.add(
				Box.createVerticalStrut(
						5
				)
		);

		bloque.add(
				campo
		);

		bloque.add(
				Box.createVerticalStrut(
						4
				)
		);

		bloque.add(
				textoAyuda
		);

		return bloque;
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
				controllerDisponibilidad
						.obtenerPorProfesional(
								profesional
										.getIdProfesional()
						);

		int futuras = 0;
		int hoy = 0;
		int conRetraso = 0;

		LocalDate fechaActual =
				LocalDate.now();

		for (Disponibilidad disponibilidad
				: disponibilidades) {

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

			if (disponibilidad
					.getFecha()
					.isAfter(
							fechaActual
					)) {

				futuras++;
			}

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
			}
		}

		labelCantidad.setText(
				disponibilidades.size()
						+ (
								disponibilidades.size() == 1
										? " disponibilidad registrada"
										: " disponibilidades registradas"
						)
		);

		valorTotales.setText(
				String.valueOf(
						disponibilidades.size()
				)
		);

		valorFuturas.setText(
				String.valueOf(
						futuras
				)
		);

		valorHoy.setText(
				String.valueOf(
						hoy
				)
		);

		valorRetrasos.setText(
				String.valueOf(
						conRetraso
				)
		);

		disponibilidadSeleccionada =
				null;

		tablaDisponibilidades.clearSelection();

		actualizarModoFormulario();
	}

	/*
	 * =========================================================
	 * DISPONIBILIDAD SELECCIONADA
	 * =========================================================
	 */
	private void cargarDisponibilidadSeleccionada() {

		int fila =
				tablaDisponibilidades
						.getSelectedRow();

		if (fila < 0) {

			disponibilidadSeleccionada =
					null;

			actualizarModoFormulario();

			return;
		}

		int idDisponibilidad =
				(int) modeloTabla.getValueAt(
						fila,
						0
				);

		disponibilidadSeleccionada =
				controllerDisponibilidad
						.buscarPorId(
								idDisponibilidad,
								profesional
										.getIdProfesional()
						);

		if (disponibilidadSeleccionada == null) {

			actualizarModoFormulario();

			return;
		}

		campoFecha.setText(
				disponibilidadSeleccionada
						.getFecha()
						.format(
								formatoFecha
						)
		);

		campoHoraInicio.setText(
				disponibilidadSeleccionada
						.getHoraInicio()
						.format(
								formatoHora
						)
		);

		campoHoraFin.setText(
				disponibilidadSeleccionada
						.getHoraFin()
						.format(
								formatoHora
						)
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
						+ "  •  "
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
		);

		actualizarModoFormulario();
	}

	/*
	 * =========================================================
	 * MODO FORMULARIO
	 * =========================================================
	 */
	private void actualizarModoFormulario() {

		if (botonGuardar == null
				|| labelModoFormulario == null
				|| labelSeleccion == null) {

			return;
		}

		if (disponibilidadSeleccionada == null) {

			botonGuardar.setText(
					"Registrar disponibilidad"
			);

			labelModoFormulario.setText(
					"NUEVA DISPONIBILIDAD"
			);

			labelSeleccion.setText(
					"Seleccione una disponibilidad para modificarla."
			);

		} else {

			botonGuardar.setText(
					"Guardar cambios"
			);

			labelModoFormulario.setText(
					"EDICION DE DISPONIBILIDAD"
			);
		}
	}

	/*
	 * =========================================================
	 * GUARDAR
	 * =========================================================
	 */
	private void guardarDisponibilidad() {

		String fechaTexto =
				campoFecha
						.getText()
						.trim();

		String horaInicioTexto =
				campoHoraInicio
						.getText()
						.trim();

		String horaFinTexto =
				campoHoraFin
						.getText()
						.trim();

		if (fechaTexto.isEmpty()
				|| horaInicioTexto.isEmpty()
				|| horaFinTexto.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"Todos los campos son obligatorios.",
					"Datos incompletos",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		LocalDate fecha;
		LocalTime horaInicio;
		LocalTime horaFin;

		try {

			fecha =
					LocalDate.parse(
							fechaTexto,
							formatoFecha
					);

			horaInicio =
					LocalTime.parse(
							horaInicioTexto,
							formatoHora
					);

			horaFin =
					LocalTime.parse(
							horaFinTexto,
							formatoHora
					);

		} catch (DateTimeParseException e) {

			JOptionPane.showMessageDialog(
					this,
					"Verifique los formatos ingresados.\n\n"
							+ "Fecha: dd/MM/yyyy\n"
							+ "Hora: HH:mm",
					"Fecha u hora invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (fecha.isBefore(
				LocalDate.now()
		)) {

			JOptionPane.showMessageDialog(
					this,
					"La fecha de disponibilidad no puede ser anterior a hoy.",
					"Fecha invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!horaFin.isAfter(
				horaInicio
		)) {

			JOptionPane.showMessageDialog(
					this,
					"La hora de finalizacion debe ser posterior a la hora de inicio.",
					"Horario invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (disponibilidadSeleccionada == null) {

			registrarDisponibilidad(
					fecha,
					horaInicio,
					horaFin
			);

		} else {

			modificarDisponibilidad(
					fecha,
					horaInicio,
					horaFin
			);
		}
	}

	/*
	 * =========================================================
	 * REGISTRAR
	 * =========================================================
	 */
	private void registrarDisponibilidad(
			LocalDate fecha,
			LocalTime horaInicio,
			LocalTime horaFin) {

		Disponibilidad nuevaDisponibilidad =
				new Disponibilidad(
						0,
						fecha,
						horaInicio,
						horaFin,
						0
				);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea registrar esta disponibilidad?\n\n"
								+ "Fecha: "
								+ fecha.format(
										formatoFecha
								)
								+ "\nHorario: "
								+ horaInicio.format(
										formatoHora
								)
								+ " - "
								+ horaFin.format(
										formatoHora
								),
						"Confirmar disponibilidad",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean registrada =
				controllerDisponibilidad.registrar(
						nuevaDisponibilidad,
						profesional
								.getIdProfesional()
				);

		if (registrada) {

			JOptionPane.showMessageDialog(
					this,
					"Disponibilidad registrada correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

			limpiarFormulario();
			cargarDisponibilidades();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo registrar la disponibilidad.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	/*
	 * =========================================================
	 * MODIFICAR
	 * =========================================================
	 */
	private void modificarDisponibilidad(
			LocalDate fecha,
			LocalTime horaInicio,
			LocalTime horaFin) {

		Disponibilidad disponibilidadModificada =
				new Disponibilidad(
						disponibilidadSeleccionada
								.getIdDisponibilidad(),

						fecha,
						horaInicio,
						horaFin,

						disponibilidadSeleccionada
								.getRetrasoEstimado()
				);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea guardar los cambios?\n\n"
								+ "Fecha: "
								+ fecha.format(
										formatoFecha
								)
								+ "\nHorario: "
								+ horaInicio.format(
										formatoHora
								)
								+ " - "
								+ horaFin.format(
										formatoHora
								),
						"Confirmar modificacion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean modificada =
				controllerDisponibilidad.modificar(
						disponibilidadModificada,
						profesional
								.getIdProfesional()
				);

		if (modificada) {

			JOptionPane.showMessageDialog(
					this,
					"Disponibilidad actualizada correctamente.",
					"Modificacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

			limpiarFormulario();
			cargarDisponibilidades();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo actualizar la disponibilidad.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	/*
	 * =========================================================
	 * LIMPIAR
	 * =========================================================
	 */
	private void limpiarFormulario() {

		disponibilidadSeleccionada =
				null;

		tablaDisponibilidades.clearSelection();

		campoFecha.setText("");
		campoHoraInicio.setText("");
		campoHoraFin.setText("");

		actualizarModoFormulario();

		campoFecha.requestFocus();
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
	 * BOTON TEXTO
	 * =========================================================
	 */
	private void configurarBotonTexto(
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
						100,
						116,
						139
				)
		);

		boton.setFocusPainted(
				false
		);

		boton.setBorderPainted(
				false
		);

		boton.setContentAreaFilled(
				false
		);

		boton.setCursor(
				Cursor.getPredefinedCursor(
						Cursor.HAND_CURSOR
				)
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

		private final boolean primario;
		private boolean hover;

		public PremiumButton(
				String texto,
				boolean primario) {

			super(
					texto
			);

			this.primario =
					primario;

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

			setForeground(
					primario
							? Color.WHITE
							: new Color(
									15,
									76,
									105
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

			if (primario) {

				g2.setColor(
						hover
								? UITheme.COLOR_ACENTO
								: UITheme.COLOR_PRIMARIO
				);

				g2.fillRoundRect(
						0,
						0,
						getWidth(),
						getHeight(),
						12,
						12
				);

			} else {

				g2.setColor(
						hover
								? new Color(
										238,
										249,
										252
								)
								: Color.WHITE
				);

				g2.fillRoundRect(
						0,
						0,
						getWidth() - 1,
						getHeight() - 1,
						12,
						12
				);

				g2.setColor(
						hover
								? UITheme.COLOR_ACENTO
								: new Color(
										211,
										222,
										230
								)
				);

				g2.drawRoundRect(
						0,
						0,
						getWidth() - 2,
						getHeight() - 2,
						12,
						12
				);
			}

			g2.dispose();

			super.paintComponent(
					g
			);
		}
	}
}