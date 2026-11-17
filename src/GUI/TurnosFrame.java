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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;

import BLL.Disponibilidad;
import BLL.EstadoTurno;
import BLL.Paciente;
import BLL.Profesional;
import BLL.Turno;
import BLL.Usuario;
import DLL.ControllerDisponibilidad;
import DLL.ControllerPaciente;
import DLL.ControllerProfesional;
import DLL.ControllerTurno;

public class TurnosFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO_VENTANA = 1240;
	private static final int ALTO_VENTANA = 700;
	private static final int ANCHO_ACCIONES = 330;

	private DashboardEmpleadoFrame dashboard;

	private ControllerTurno controllerTurno;

	private JTable tablaTurnos;
	private DefaultTableModel modeloTabla;

	private Turno turnoSeleccionado;

	private JButton botonModificar;
	private JButton botonCancelar;
	private JButton botonConfirmar;
	private JButton botonLlegada;
	private JButton botonDetalle;

	private JLabel labelCantidad;
	private JLabel labelSeleccion;

	private JLabel valorPendientes;
	private JLabel valorConfirmados;
	private JLabel valorPresentes;
	private JLabel valorCancelados;

	private JLabel labelEstadoSeleccion;
	private JLabel labelPacienteSeleccion;
	private JLabel labelFechaSeleccion;

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern(
					"dd/MM/yyyy"
			);

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern(
					"HH:mm"
			);

	public TurnosFrame(
			DashboardEmpleadoFrame dashboard) {

		this.dashboard =
				dashboard;

		controllerTurno =
				new ControllerTurno();

		configurarVentana();
		inicializarComponentes();
		cargarTurnos();
	}

	/*
	 * =========================================================
	 * VENTANA
	 * =========================================================
	 */
	private void configurarVentana() {

		setTitle(
				"Telemedicina - Gestion de turnos"
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
				crearPanelAcciones(),
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
						"RECEPCION  /  TURNOS"
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
						"Gestion de turnos"
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
						"Administra asignaciones, confirmaciones, cancelaciones y llegadas."
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
	 * PANEL PRINCIPAL DE TURNOS
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

		/*
		 * =====================================================
		 * PARTE SUPERIOR
		 * =====================================================
		 */
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

		JPanel tituloFila =
				new JPanel(
						new BorderLayout()
				);

		tituloFila.setOpaque(
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
						"Agenda general de turnos"
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
						"0 turnos registrados"
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
				e -> cargarTurnos()
		);

		tituloFila.add(
				textos,
				BorderLayout.WEST
		);

		tituloFila.add(
				botonActualizar,
				BorderLayout.EAST
		);

		superior.add(
				tituloFila
		);

		superior.add(
				Box.createVerticalStrut(
						16
				)
		);

		/*
		 * =====================================================
		 * RESUMEN DE ESTADOS
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

		valorConfirmados =
				new JLabel(
						"0"
				);

		valorPresentes =
				new JLabel(
						"0"
				);

		valorCancelados =
				new JLabel(
						"0"
				);

		resumen.add(
				crearResumenEstado(
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
				crearResumenEstado(
						"Confirmados",
						valorConfirmados,
						new Color(
								59,
								91,
								219
						)
				)
		);

		resumen.add(
				crearResumenEstado(
						"Presentes",
						valorPresentes,
						new Color(
								22,
								163,
								74
						)
				)
		);

		resumen.add(
				crearResumenEstado(
						"Cancelados",
						valorCancelados,
						new Color(
								220,
								38,
								38
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
						tablaTurnos
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
						"Seleccione un turno para habilitar sus operaciones."
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
						"Los estados controlan las operaciones disponibles."
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
	 * RESUMEN ESTADO
	 * =========================================================
	 */
	private JPanel crearResumenEstado(
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
				"Fecha",
				"Hora",
				"Estado",
				"Disponibilidad"
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
						45
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(1)
				.setPreferredWidth(
						180
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(2)
				.setPreferredWidth(
						110
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(3)
				.setPreferredWidth(
						100
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(4)
				.setPreferredWidth(
						80
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(5)
				.setPreferredWidth(
						110
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(6)
				.setPreferredWidth(
						100
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
	 * PANEL DE ACCIONES
	 * =========================================================
	 */
	private JPanel crearPanelAcciones() {

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
						ANCHO_ACCIONES,
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
						"OPERACIONES"
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
						"Gestion del turno"
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
								+ "Seleccione un turno para visualizar "
								+ "y administrar las operaciones disponibles."
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
		 * FICHA DEL TURNO SELECCIONADO
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
						13,
						14,
						13,
						14
				)
		);

		ficha.setAlignmentX(
				LEFT_ALIGNMENT
		);

		ficha.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						96
				)
		);

		labelEstadoSeleccion =
				new JLabel(
						"SIN SELECCION"
				);

		labelEstadoSeleccion.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						9
				)
		);

		labelEstadoSeleccion.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelEstadoSeleccion.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelPacienteSeleccion =
				new JLabel(
						"Seleccione un turno"
				);

		labelPacienteSeleccion.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						12
				)
		);

		labelPacienteSeleccion.setForeground(
				new Color(
						15,
						23,
						42
				)
		);

		labelPacienteSeleccion.setAlignmentX(
				LEFT_ALIGNMENT
		);

		labelFechaSeleccion =
				new JLabel(
						"Paciente, fecha y horario"
				);

		labelFechaSeleccion.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						10
				)
		);

		labelFechaSeleccion.setForeground(
				new Color(
						100,
						116,
						139
				)
		);

		labelFechaSeleccion.setAlignmentX(
				LEFT_ALIGNMENT
		);

		ficha.add(
				labelEstadoSeleccion
		);

		ficha.add(
				Box.createVerticalStrut(
						6
				)
		);

		ficha.add(
				labelPacienteSeleccion
		);

		ficha.add(
				Box.createVerticalStrut(
						4
				)
		);

		ficha.add(
				labelFechaSeleccion
		);

		cuerpo.add(
				ficha
		);

		cuerpo.add(
				Box.createVerticalStrut(
						16
				)
		);

		/*
		 * =====================================================
		 * ASIGNAR NUEVO TURNO
		 * =====================================================
		 */
		JButton botonAsignar =
				new PremiumButton(
						"Asignar nuevo turno",
						TipoBoton.PRIMARIO
				);

		configurarTamanioAccion(
				botonAsignar
		);

		cuerpo.add(
				botonAsignar
		);

		cuerpo.add(
				Box.createVerticalStrut(
						9
				)
		);

		/*
		 * =====================================================
		 * MODIFICAR
		 * =====================================================
		 */
		botonModificar =
				new PremiumButton(
						"Modificar turno",
						TipoBoton.SECUNDARIO
				);

		configurarTamanioAccion(
				botonModificar
		);

		cuerpo.add(
				botonModificar
		);

		cuerpo.add(
				Box.createVerticalStrut(
						9
				)
		);

		/*
		 * =====================================================
		 * CONFIRMAR
		 * =====================================================
		 */
		botonConfirmar =
				new PremiumButton(
						"Confirmar turno",
						TipoBoton.SUCCESS
				);

		configurarTamanioAccion(
				botonConfirmar
		);

		cuerpo.add(
				botonConfirmar
		);

		cuerpo.add(
				Box.createVerticalStrut(
						9
				)
		);

		/*
		 * =====================================================
		 * REGISTRAR LLEGADA
		 * =====================================================
		 */
		botonLlegada =
				new PremiumButton(
						"Registrar llegada",
						TipoBoton.SUCCESS
				);

		configurarTamanioAccion(
				botonLlegada
		);

		cuerpo.add(
				botonLlegada
		);

		cuerpo.add(
				Box.createVerticalStrut(
						9
				)
		);

		/*
		 * =====================================================
		 * CANCELAR
		 * =====================================================
		 */
		botonCancelar =
				new PremiumButton(
						"Cancelar turno",
						TipoBoton.PELIGRO
				);

		configurarTamanioAccion(
				botonCancelar
		);

		cuerpo.add(
				botonCancelar
		);

		cuerpo.add(
				Box.createVerticalStrut(
						9
				)
		);

		/*
		 * =====================================================
		 * DETALLE
		 * =====================================================
		 */
		botonDetalle =
				new PremiumButton(
						"Ver detalle",
						TipoBoton.SECUNDARIO
				);

		configurarTamanioAccion(
				botonDetalle
		);

		cuerpo.add(
				botonDetalle
		);

		/*
		 * =====================================================
		 * EVENTOS
		 * =====================================================
		 */
		botonAsignar.addActionListener(
				e -> asignarTurno()
		);

		botonModificar.addActionListener(
				e -> modificarTurno()
		);

		botonConfirmar.addActionListener(
				e -> confirmarTurno()
		);

		botonLlegada.addActionListener(
				e -> registrarLlegada()
		);

		botonCancelar.addActionListener(
				e -> cancelarTurno()
		);

		botonDetalle.addActionListener(
				e -> mostrarDetalle()
		);

		tarjeta.add(
				cuerpo,
				BorderLayout.CENTER
		);

		actualizarEstadoBotones();

		return tarjeta;
	}

	private void configurarTamanioAccion(
			JButton boton) {

		boton.setAlignmentX(
				LEFT_ALIGNMENT
		);

		boton.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						40
				)
		);

		boton.setPreferredSize(
				new Dimension(
						260,
						40
				)
		);
	}

	/*
	 * =========================================================
	 * CARGAR TURNOS
	 * =========================================================
	 */
	private void cargarTurnos() {

		modeloTabla.setRowCount(
				0
		);

		LinkedList<Turno> turnos =
				controllerTurno.obtenerTodos();

		int pendientes = 0;
		int confirmados = 0;
		int presentes = 0;
		int cancelados = 0;

		for (Turno turno : turnos) {

			modeloTabla.addRow(
					new Object[] {

							turno.getIdTurno(),

							turno.getPaciente()
									.getNombre()
									+ " "
									+ turno.getPaciente()
											.getApellido(),

							turno.getPaciente()
									.getDni(),

							turno.getFecha()
									.format(
											formatoFecha
									),

							turno.getHora()
									.format(
											formatoHora
									),

							turno.getEstado(),

							turno.getDisponibilidad()
									.getIdDisponibilidad()
					}
			);

			if (turno.estaPendiente()) {

				pendientes++;

			} else if (turno.estaConfirmado()) {

				confirmados++;

			} else if (turno.estaPresente()) {

				presentes++;

			} else if (turno.estaCancelado()) {

				cancelados++;
			}
		}

		labelCantidad.setText(
				turnos.size()
						+ (
								turnos.size() == 1
										? " turno registrado"
										: " turnos registrados"
						)
		);

		valorPendientes.setText(
				String.valueOf(
						pendientes
				)
		);

		valorConfirmados.setText(
				String.valueOf(
						confirmados
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

		turnoSeleccionado =
				null;

		tablaTurnos.clearSelection();

		actualizarEstadoBotones();
		actualizarFichaSeleccion();
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

			actualizarEstadoBotones();
			actualizarFichaSeleccion();

			return;
		}

		int idTurno =
				(int) modeloTabla.getValueAt(
						fila,
						0
				);

		turnoSeleccionado =
				controllerTurno.buscarPorId(
						idTurno
				);

		actualizarEstadoBotones();
		actualizarFichaSeleccion();
	}

	/*
	 * =========================================================
	 * FICHA DEL TURNO
	 * =========================================================
	 */
	private void actualizarFichaSeleccion() {

		if (labelEstadoSeleccion == null
				|| labelPacienteSeleccion == null
				|| labelFechaSeleccion == null) {

			return;
		}

		if (turnoSeleccionado == null) {

			labelEstadoSeleccion.setText(
					"SIN SELECCION"
			);

			labelEstadoSeleccion.setForeground(
					new Color(
							100,
							116,
							139
					)
			);

			labelPacienteSeleccion.setText(
					"Seleccione un turno"
			);

			labelFechaSeleccion.setText(
					"Paciente, fecha y horario"
			);

			if (labelSeleccion != null) {

				labelSeleccion.setText(
						"Seleccione un turno para habilitar sus operaciones."
				);
			}

			return;
		}

		labelEstadoSeleccion.setText(
				turnoSeleccionado
						.getEstado()
						.toString()
		);

		if (turnoSeleccionado.estaPendiente()) {

			labelEstadoSeleccion.setForeground(
					new Color(
							202,
							113,
							16
					)
			);

		} else if (turnoSeleccionado.estaConfirmado()) {

			labelEstadoSeleccion.setForeground(
					new Color(
							59,
							91,
							219
					)
			);

		} else if (turnoSeleccionado.estaPresente()) {

			labelEstadoSeleccion.setForeground(
					new Color(
							22,
							163,
							74
					)
			);

		} else {

			labelEstadoSeleccion.setForeground(
					new Color(
							220,
							38,
							38
					)
			);
		}

		labelPacienteSeleccion.setText(
				turnoSeleccionado
						.getPaciente()
						.getNombre()
						+ " "
						+ turnoSeleccionado
								.getPaciente()
								.getApellido()
		);

		labelFechaSeleccion.setText(
				turnoSeleccionado
						.getFecha()
						.format(
								formatoFecha
						)
						+ "  •  "
						+ turnoSeleccionado
								.getHora()
								.format(
										formatoHora
								)
		);

		if (labelSeleccion != null) {

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
	}

	/*
	 * =========================================================
	 * ESTADO DE BOTONES
	 * =========================================================
	 */
	private void actualizarEstadoBotones() {

		if (botonModificar == null
				|| botonCancelar == null
				|| botonConfirmar == null
				|| botonLlegada == null
				|| botonDetalle == null) {

			return;
		}

		boolean seleccionado =
				turnoSeleccionado != null;

		botonModificar.setEnabled(
				seleccionado
						&& !turnoSeleccionado
								.estaCancelado()
						&& !turnoSeleccionado
								.estaPresente()
		);

		botonCancelar.setEnabled(
				seleccionado
						&& !turnoSeleccionado
								.estaCancelado()
						&& !turnoSeleccionado
								.estaPresente()
		);

		botonConfirmar.setEnabled(
				seleccionado
						&& turnoSeleccionado
								.estaPendiente()
		);

		botonLlegada.setEnabled(
				seleccionado
						&& turnoSeleccionado
								.estaConfirmado()
		);

		botonDetalle.setEnabled(
				seleccionado
		);
	}

	/*
	 * =========================================================
	 * DETALLE
	 * =========================================================
	 */
	private void mostrarDetalle() {

		if (turnoSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un turno de la tabla.",
					"Turno no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		JOptionPane.showMessageDialog(
				this,
				"ID turno: "
						+ turnoSeleccionado
								.getIdTurno()

						+ "\nPaciente: "
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

						+ "\nFecha: "
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

						+ "\nEstado: "
						+ turnoSeleccionado
								.getEstado()

						+ "\nDisponibilidad ID: "
						+ turnoSeleccionado
								.getDisponibilidad()
								.getIdDisponibilidad()

						+ "\nHorario disponible: "
						+ turnoSeleccionado
								.getDisponibilidad()
								.getHoraInicio()
								.format(
										formatoHora
								)
						+ " - "
						+ turnoSeleccionado
								.getDisponibilidad()
								.getHoraFin()
								.format(
										formatoHora
								),

				"Telemedicina - Detalle del turno",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	/*
	 * =========================================================
	 * ASIGNAR TURNO
	 * =========================================================
	 */
	private void asignarTurno() {

		ControllerPaciente controllerPaciente =
				new ControllerPaciente();

		LinkedList<Paciente> pacientes =
				controllerPaciente.buscar("");

		if (pacientes.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"No hay pacientes registrados.",
					"Sin pacientes",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		StringBuilder listadoPacientes =
				new StringBuilder();

		for (Paciente paciente : pacientes) {

			listadoPacientes
					.append("ID: ")
					.append(
							paciente.getIdPaciente()
					)
					.append(" | ")
					.append(
							paciente.getNombre()
					)
					.append(" ")
					.append(
							paciente.getApellido()
					)
					.append(" | DNI: ")
					.append(
							paciente.getDni()
					)
					.append("\n");
		}

		String entradaPaciente =
				JOptionPane.showInputDialog(
						this,
						listadoPacientes.toString()
								+ "\nIngrese el ID del paciente:",
						"Telemedicina - Asignar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaPaciente == null) {

			return;
		}

		int idPaciente;

		try {

			idPaciente =
					Integer.parseInt(
							entradaPaciente.trim()
					);

			if (idPaciente <= 0) {

				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					this,
					"El ID del paciente debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Paciente paciente =
				controllerPaciente.buscarPorId(
						idPaciente
				);

		if (paciente == null) {

			JOptionPane.showMessageDialog(
					this,
					"No se encontro el paciente seleccionado.",
					"Paciente no encontrado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerProfesional controllerProfesional =
				new ControllerProfesional();

		LinkedList<Profesional> profesionales =
				controllerProfesional.buscar("");

		if (profesionales.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"No hay profesionales registrados.",
					"Sin profesionales",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		StringBuilder listadoProfesionales =
				new StringBuilder();

		for (Profesional profesional : profesionales) {

			Usuario usuarioProfesional =
					controllerProfesional
							.obtenerUsuarioPorProfesional(
									profesional
											.getIdProfesional()
							);

			if (usuarioProfesional != null) {

				listadoProfesionales
						.append("ID: ")
						.append(
								profesional
										.getIdProfesional()
						)
						.append(" | ")
						.append(
								usuarioProfesional
										.getNombre()
						)
						.append(" ")
						.append(
								usuarioProfesional
										.getApellido()
						)
						.append(" | ")
						.append(
								profesional
										.getEspecialidad()
						)
						.append("\n");
			}
		}

		String entradaProfesional =
				JOptionPane.showInputDialog(
						this,
						listadoProfesionales.toString()
								+ "\nIngrese el ID del profesional:",
						"Telemedicina - Asignar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaProfesional == null) {

			return;
		}

		int idProfesional;

		try {

			idProfesional =
					Integer.parseInt(
							entradaProfesional.trim()
					);

			if (idProfesional <= 0) {

				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					this,
					"El ID del profesional debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Profesional profesional =
				controllerProfesional.buscarPorId(
						idProfesional
				);

		if (profesional == null) {

			JOptionPane.showMessageDialog(
					this,
					"No se encontro el profesional seleccionado.",
					"Profesional no encontrado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerDisponibilidad controllerDisponibilidad =
				new ControllerDisponibilidad();

		LinkedList<Disponibilidad> disponibilidades =
				controllerDisponibilidad
						.obtenerPorProfesional(
								idProfesional
						);

		StringBuilder listadoDisponibilidades =
				new StringBuilder();

		for (Disponibilidad disponibilidad
				: disponibilidades) {

			if (!disponibilidad
					.getFecha()
					.isBefore(
							LocalDate.now()
					)) {

				listadoDisponibilidades
						.append("ID: ")
						.append(
								disponibilidad
										.getIdDisponibilidad()
						)
						.append(" | ")
						.append(
								disponibilidad
										.getFecha()
										.format(
												formatoFecha
										)
						)
						.append(" | ")
						.append(
								disponibilidad
										.getHoraInicio()
										.format(
												formatoHora
										)
						)
						.append(" - ")
						.append(
								disponibilidad
										.getHoraFin()
										.format(
												formatoHora
										)
						)
						.append("\n");
			}
		}

		if (listadoDisponibilidades.length() == 0) {

			JOptionPane.showMessageDialog(
					this,
					"El profesional no tiene disponibilidades actuales o futuras.",
					"Sin disponibilidad",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		String entradaDisponibilidad =
				JOptionPane.showInputDialog(
						this,
						listadoDisponibilidades.toString()
								+ "\nIngrese el ID de la disponibilidad:",
						"Telemedicina - Asignar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaDisponibilidad == null) {

			return;
		}

		int idDisponibilidad;

		try {

			idDisponibilidad =
					Integer.parseInt(
							entradaDisponibilidad.trim()
					);

			if (idDisponibilidad <= 0) {

				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					this,
					"El ID de disponibilidad debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Disponibilidad disponibilidad =
				controllerDisponibilidad.buscarPorId(
						idDisponibilidad,
						idProfesional
				);

		if (disponibilidad == null
				|| disponibilidad
						.getFecha()
						.isBefore(
								LocalDate.now()
						)) {

			JOptionPane.showMessageDialog(
					this,
					"La disponibilidad seleccionada no es valida.",
					"Disponibilidad invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		String horaTexto =
				JOptionPane.showInputDialog(
						this,
						"Disponibilidad seleccionada:\n"
								+ disponibilidad
										.getFecha()
										.format(
												formatoFecha
										)
								+ "\n"
								+ disponibilidad
										.getHoraInicio()
										.format(
												formatoHora
										)
								+ " - "
								+ disponibilidad
										.getHoraFin()
										.format(
												formatoHora
										)
								+ "\n\nIngrese la hora del turno (HH:mm):",
						"Telemedicina - Horario del turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (horaTexto == null) {

			return;
		}

		LocalTime hora;

		try {

			hora =
					LocalTime.parse(
							horaTexto.trim(),
							formatoHora
					);

		} catch (DateTimeParseException e) {

			JOptionPane.showMessageDialog(
					this,
					"La hora debe tener formato HH:mm.",
					"Hora invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!disponibilidad.esHorarioValido(
				hora
		)) {

			JOptionPane.showMessageDialog(
					this,
					"El horario ingresado se encuentra fuera de la disponibilidad del profesional.",
					"Horario invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (controllerTurno.horarioOcupado(
				idDisponibilidad,
				hora
		)) {

			JOptionPane.showMessageDialog(
					this,
					"El horario seleccionado ya se encuentra ocupado.",
					"Horario ocupado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Turno turnoNuevo =
				new Turno(
						0,
						disponibilidad.getFecha(),
						hora,
						EstadoTurno.PENDIENTE,
						paciente,
						disponibilidad
				);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea asignar este turno?\n\n"
								+ "Paciente: "
								+ paciente.getNombre()
								+ " "
								+ paciente.getApellido()
								+ "\nProfesional: "
								+ profesional.getEspecialidad()
								+ "\nFecha: "
								+ disponibilidad
										.getFecha()
										.format(
												formatoFecha
										)
								+ "\nHora: "
								+ hora.format(
										formatoHora
								),
						"Confirmar turno",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean registrado =
				controllerTurno.registrar(
						turnoNuevo
				);

		if (registrado) {

			JOptionPane.showMessageDialog(
					this,
					"Turno asignado correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarTurnos();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo asignar el turno.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	/*
	 * =========================================================
	 * MODIFICAR TURNO
	 * =========================================================
	 */
	private void modificarTurno() {

		if (turnoSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un turno de la tabla.",
					"Turno no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (turnoSeleccionado.estaCancelado()
				|| turnoSeleccionado
						.estaPresente()) {

			JOptionPane.showMessageDialog(
					this,
					"No se puede modificar un turno cancelado o con llegada registrada.",
					"Turno no modificable",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Turno turnoActual =
				controllerTurno.buscarPorId(
						turnoSeleccionado
								.getIdTurno()
				);

		if (turnoActual == null) {

			JOptionPane.showMessageDialog(
					this,
					"No se encontro el turno seleccionado.",
					"Turno no encontrado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerProfesional controllerProfesional =
				new ControllerProfesional();

		LinkedList<Profesional> profesionales =
				controllerProfesional.buscar("");

		StringBuilder listadoProfesionales =
				new StringBuilder();

		for (Profesional profesional
				: profesionales) {

			Usuario usuarioProfesional =
					controllerProfesional
							.obtenerUsuarioPorProfesional(
									profesional
											.getIdProfesional()
							);

			if (usuarioProfesional != null) {

				listadoProfesionales
						.append("ID: ")
						.append(
								profesional
										.getIdProfesional()
						)
						.append(" | ")
						.append(
								usuarioProfesional
										.getNombre()
						)
						.append(" ")
						.append(
								usuarioProfesional
										.getApellido()
						)
						.append(" | ")
						.append(
								profesional
										.getEspecialidad()
						)
						.append("\n");
			}
		}

		String entradaProfesional =
				JOptionPane.showInputDialog(
						this,
						"Turno seleccionado:\n"
								+ turnoActual
										.getPaciente()
										.getNombre()
								+ " "
								+ turnoActual
										.getPaciente()
										.getApellido()
								+ "\n"
								+ turnoActual
										.getFecha()
										.format(
												formatoFecha
										)
								+ " "
								+ turnoActual
										.getHora()
										.format(
												formatoHora
										)
								+ "\n\n"
								+ listadoProfesionales
										.toString()
								+ "\nIngrese el ID del profesional:",
						"Telemedicina - Modificar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaProfesional == null) {

			return;
		}

		int idProfesional;

		try {

			idProfesional =
					Integer.parseInt(
							entradaProfesional.trim()
					);

			if (idProfesional <= 0) {

				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					this,
					"El ID del profesional debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Profesional profesional =
				controllerProfesional.buscarPorId(
						idProfesional
				);

		if (profesional == null) {

			JOptionPane.showMessageDialog(
					this,
					"No se encontro el profesional seleccionado.",
					"Profesional no encontrado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerDisponibilidad controllerDisponibilidad =
				new ControllerDisponibilidad();

		LinkedList<Disponibilidad> disponibilidades =
				controllerDisponibilidad
						.obtenerPorProfesional(
								idProfesional
						);

		StringBuilder listadoDisponibilidades =
				new StringBuilder();

		for (Disponibilidad disponibilidad
				: disponibilidades) {

			if (!disponibilidad
					.getFecha()
					.isBefore(
							LocalDate.now()
					)) {

				listadoDisponibilidades
						.append("ID: ")
						.append(
								disponibilidad
										.getIdDisponibilidad()
						)
						.append(" | ")
						.append(
								disponibilidad
										.getFecha()
										.format(
												formatoFecha
										)
						)
						.append(" | ")
						.append(
								disponibilidad
										.getHoraInicio()
										.format(
												formatoHora
										)
						)
						.append(" - ")
						.append(
								disponibilidad
										.getHoraFin()
										.format(
												formatoHora
										)
						)
						.append("\n");
			}
		}

		if (listadoDisponibilidades.length() == 0) {

			JOptionPane.showMessageDialog(
					this,
					"El profesional no tiene disponibilidades actuales o futuras.",
					"Sin disponibilidad",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		String entradaDisponibilidad =
				JOptionPane.showInputDialog(
						this,
						listadoDisponibilidades.toString()
								+ "\nIngrese el ID de la nueva disponibilidad:",
						"Telemedicina - Modificar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaDisponibilidad == null) {

			return;
		}

		int idDisponibilidad;

		try {

			idDisponibilidad =
					Integer.parseInt(
							entradaDisponibilidad.trim()
					);

			if (idDisponibilidad <= 0) {

				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					this,
					"El ID de disponibilidad debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Disponibilidad disponibilidad =
				controllerDisponibilidad.buscarPorId(
						idDisponibilidad,
						idProfesional
				);

		if (disponibilidad == null) {

			JOptionPane.showMessageDialog(
					this,
					"La disponibilidad seleccionada no es valida.",
					"Disponibilidad invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		String horaTexto =
				JOptionPane.showInputDialog(
						this,
						"Disponibilidad seleccionada:\n"
								+ disponibilidad
										.getFecha()
										.format(
												formatoFecha
										)
								+ "\n"
								+ disponibilidad
										.getHoraInicio()
										.format(
												formatoHora
										)
								+ " - "
								+ disponibilidad
										.getHoraFin()
										.format(
												formatoHora
										)
								+ "\n\nIngrese la nueva hora (HH:mm):",
						"Telemedicina - Modificar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (horaTexto == null) {

			return;
		}

		LocalTime nuevaHora;

		try {

			nuevaHora =
					LocalTime.parse(
							horaTexto.trim(),
							formatoHora
					);

		} catch (DateTimeParseException e) {

			JOptionPane.showMessageDialog(
					this,
					"La hora debe tener formato HH:mm.",
					"Hora invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!disponibilidad.esHorarioValido(
				nuevaHora
		)) {

			JOptionPane.showMessageDialog(
					this,
					"El horario ingresado se encuentra fuera de la disponibilidad del profesional.",
					"Horario invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (controllerTurno
				.horarioOcupadoExceptoTurno(
						idDisponibilidad,
						nuevaHora,
						turnoActual
								.getIdTurno()
				)) {

			JOptionPane.showMessageDialog(
					this,
					"El horario seleccionado ya se encuentra ocupado por otro turno.",
					"Horario ocupado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Turno turnoModificado =
				new Turno(
						turnoActual.getIdTurno(),
						disponibilidad.getFecha(),
						nuevaHora,
						turnoActual.getEstado(),
						turnoActual.getPaciente(),
						disponibilidad
				);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea guardar los cambios?\n\n"
								+ "Paciente: "
								+ turnoActual
										.getPaciente()
										.getNombre()
								+ " "
								+ turnoActual
										.getPaciente()
										.getApellido()
								+ "\nFecha: "
								+ disponibilidad
										.getFecha()
										.format(
												formatoFecha
										)
								+ "\nHora: "
								+ nuevaHora
										.format(
												formatoHora
										)
								+ "\nEstado: "
								+ turnoActual
										.getEstado(),
						"Confirmar modificacion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean modificado =
				controllerTurno.modificar(
						turnoModificado
				);

		if (modificado) {

			JOptionPane.showMessageDialog(
					this,
					"Turno modificado correctamente.",
					"Modificacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarTurnos();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo modificar el turno.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	/*
	 * =========================================================
	 * CANCELAR TURNO
	 * =========================================================
	 */
	private void cancelarTurno() {

		if (turnoSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un turno de la tabla.",
					"Turno no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (turnoSeleccionado.estaCancelado()
				|| turnoSeleccionado
						.estaPresente()) {

			JOptionPane.showMessageDialog(
					this,
					"El turno seleccionado no puede cancelarse.",
					"Operacion no permitida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea cancelar este turno?\n\n"
								+ "Paciente: "
								+ turnoSeleccionado
										.getPaciente()
										.getNombre()
								+ " "
								+ turnoSeleccionado
										.getPaciente()
										.getApellido()
								+ "\nFecha: "
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
								+ "\nEstado actual: "
								+ turnoSeleccionado
										.getEstado(),
						"Confirmar cancelacion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean actualizado =
				controllerTurno.actualizarEstado(
						turnoSeleccionado
								.getIdTurno(),
						EstadoTurno.CANCELADO
				);

		if (actualizado) {

			JOptionPane.showMessageDialog(
					this,
					"Turno cancelado correctamente.",
					"Cancelacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarTurnos();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo cancelar el turno.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	/*
	 * =========================================================
	 * CONFIRMAR TURNO
	 * =========================================================
	 */
	private void confirmarTurno() {

		if (turnoSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un turno de la tabla.",
					"Turno no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!turnoSeleccionado.estaPendiente()) {

			JOptionPane.showMessageDialog(
					this,
					"Solo se pueden confirmar turnos pendientes.",
					"Operacion no permitida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea confirmar este turno?\n\n"
								+ "Paciente: "
								+ turnoSeleccionado
										.getPaciente()
										.getNombre()
								+ " "
								+ turnoSeleccionado
										.getPaciente()
										.getApellido()
								+ "\nFecha: "
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
										),
						"Confirmar turno",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean actualizado =
				controllerTurno.actualizarEstado(
						turnoSeleccionado
								.getIdTurno(),
						EstadoTurno.CONFIRMADO
				);

		if (actualizado) {

			JOptionPane.showMessageDialog(
					this,
					"Turno confirmado correctamente.",
					"Confirmacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarTurnos();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo confirmar el turno.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	/*
	 * =========================================================
	 * REGISTRAR LLEGADA
	 * =========================================================
	 */
	private void registrarLlegada() {

		if (turnoSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un turno de la tabla.",
					"Turno no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!turnoSeleccionado.estaConfirmado()) {

			JOptionPane.showMessageDialog(
					this,
					"Solo se puede registrar la llegada de un turno confirmado.",
					"Operacion no permitida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea registrar la llegada del paciente?\n\n"
								+ "Paciente: "
								+ turnoSeleccionado
										.getPaciente()
										.getNombre()
								+ " "
								+ turnoSeleccionado
										.getPaciente()
										.getApellido()
								+ "\nFecha: "
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
										),
						"Registrar llegada",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean actualizado =
				controllerTurno.actualizarEstado(
						turnoSeleccionado
								.getIdTurno(),
						EstadoTurno.PRESENTE
				);

		if (actualizado) {

			JOptionPane.showMessageDialog(
					this,
					"Llegada del paciente registrada correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

			cargarTurnos();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo registrar la llegada del paciente.",
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
	 * TIPO BOTON
	 * =========================================================
	 */
	private enum TipoBoton {

		PRIMARIO,
		SECUNDARIO,
		SUCCESS,
		PELIGRO
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

		private final TipoBoton tipo;
		private boolean hover;

		public PremiumButton(
				String texto,
				TipoBoton tipo) {

			super(
					texto
			);

			this.tipo =
					tipo;

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

			if (!isEnabled()) {

				setForeground(
						new Color(
								148,
								163,
								184
						)
				);

				return;
			}

			switch (tipo) {

			case PRIMARIO:

				setForeground(
						Color.WHITE
				);

				break;

			case SUCCESS:

				setForeground(
						new Color(
								22,
								122,
								79
						)
				);

				break;

			case PELIGRO:

				setForeground(
						new Color(
								185,
								28,
								28
						)
				);

				break;

			default:

				setForeground(
						new Color(
								15,
								76,
								105
						)
				);

				break;
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

			/*
			 * =================================================
			 * DESHABILITADO
			 * =================================================
			 */
			if (!isEnabled()) {

				g2.setColor(
						new Color(
								248,
								250,
								252
						)
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
						new Color(
								226,
								232,
								240
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

				g2.dispose();

				super.paintComponent(
						g
				);

				return;
			}

			/*
			 * =================================================
			 * PRIMARIO
			 * =================================================
			 */
			if (tipo == TipoBoton.PRIMARIO) {

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

			/*
			 * =================================================
			 * SUCCESS
			 * =================================================
			 */
			} else if (tipo == TipoBoton.SUCCESS) {

				g2.setColor(
						hover
								? new Color(
										236,
										253,
										245
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
								? new Color(
										22,
										163,
										74
								)
								: new Color(
										187,
										231,
										209
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

			/*
			 * =================================================
			 * PELIGRO
			 * =================================================
			 */
			} else if (tipo == TipoBoton.PELIGRO) {

				g2.setColor(
						hover
								? new Color(
										254,
										242,
										242
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
								? new Color(
										220,
										38,
										38
								)
								: new Color(
										254,
										202,
										202
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

			/*
			 * =================================================
			 * SECUNDARIO
			 * =================================================
			 */
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