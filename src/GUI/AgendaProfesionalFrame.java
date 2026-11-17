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
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;

import BLL.Profesional;
import BLL.Turno;
import BLL.Usuario;
import DLL.ControllerProfesional;
import DLL.ControllerTurno;

public class AgendaProfesionalFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO_VENTANA = 1180;
	private static final int ALTO_VENTANA = 650;
	private static final int ANCHO_DETALLE = 330;

	private DashboardProfesionalFrame dashboard;
	private Usuario usuario;

	private ControllerProfesional controllerProfesional;
	private ControllerTurno controllerTurno;

	private Profesional profesional;

	private JTable tablaTurnos;
	private DefaultTableModel modeloTabla;

	private JLabel labelCantidad;
	private JLabel labelSeleccion;

	private JLabel valorProximos;
	private JLabel valorHoy;
	private JLabel valorPendientes;
	private JLabel valorConfirmados;

	private JLabel labelEstadoDetalle;
	private JLabel labelPacienteDetalle;
	private JLabel labelDniDetalle;
	private JLabel labelFechaDetalle;
	private JLabel labelHoraDetalle;
	private JLabel labelRetrasoDetalle;

	private Turno turnoSeleccionado;

	private final DateTimeFormatter formatoFecha =
			DateTimeFormatter.ofPattern(
					"dd/MM/yyyy"
			);

	private final DateTimeFormatter formatoHora =
			DateTimeFormatter.ofPattern(
					"HH:mm"
			);

	public AgendaProfesionalFrame(
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

		profesional =
				controllerProfesional.obtenerPorUsuario(
						usuario.getIdUsuario()
				);

		if (profesional == null) {

			javax.swing.JOptionPane.showMessageDialog(
					null,
					"No se pudieron obtener los datos del profesional.",
					"Error",
					javax.swing.JOptionPane.ERROR_MESSAGE
			);

			dispose();

			dashboard.setVisible(
					true
			);

			return;
		}

		configurarVentana();
		inicializarComponentes();
		cargarAgenda();
	}

	/*
	 * =========================================================
	 * VENTANA
	 * =========================================================
	 */
	private void configurarVentana() {

		setTitle(
				"Telemedicina - Agenda - "
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
				crearPanelAgenda(),
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
						"PROFESIONAL  /  AGENDA"
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
						"Mi agenda de turnos"
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
						"Consulta los proximos turnos asignados a tu agenda profesional."
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
	 * PANEL AGENDA
	 * =========================================================
	 */
	private JPanel crearPanelAgenda() {

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
		 * TITULO Y ACTUALIZAR
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
						"Turnos proximos"
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
						"0 turnos proximos"
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
				e -> cargarAgenda()
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

		valorProximos =
				new JLabel(
						"0"
				);

		valorHoy =
				new JLabel(
						"0"
				);

		valorPendientes =
				new JLabel(
						"0"
				);

		valorConfirmados =
				new JLabel(
						"0"
				);

		resumen.add(
				crearResumen(
						"Proximos",
						valorProximos,
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
						"Confirmados",
						valorConfirmados,
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
						"Seleccione un turno para consultar sus datos."
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
						"Los turnos cancelados no se muestran en la agenda."
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
	 * TARJETA RESUMEN
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
				"Hora",
				"Estado",
				"Retraso"
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
						60
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(1)
				.setPreferredWidth(
						165
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(2)
				.setPreferredWidth(
						100
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
						75
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(5)
				.setPreferredWidth(
						105
				);

		tablaTurnos
				.getColumnModel()
				.getColumn(6)
				.setPreferredWidth(
						90
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
						18
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
						"DETALLE DEL TURNO"
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
						"Informacion de atencion"
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
								+ "Seleccione un turno de la agenda para "
								+ "consultar su informacion."
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
		 * ESTADO
		 * =====================================================
		 */
		RoundedPanel fichaEstado =
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

		fichaEstado.setLayout(
				new BoxLayout(
						fichaEstado,
						BoxLayout.Y_AXIS
				)
		);

		fichaEstado.setBorder(
				BorderFactory.createEmptyBorder(
						14,
						14,
						14,
						14
				)
		);

		fichaEstado.setAlignmentX(
				LEFT_ALIGNMENT
		);

		fichaEstado.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						95
				)
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
						"Seleccione un turno"
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

		fichaEstado.add(
				labelEstadoDetalle
		);

		fichaEstado.add(
				Box.createVerticalStrut(
						7
				)
		);

		fichaEstado.add(
				labelPacienteDetalle
		);

		fichaEstado.add(
				Box.createVerticalStrut(
						4
				)
		);

		fichaEstado.add(
				labelDniDetalle
		);

		cuerpo.add(
				fichaEstado
		);

		cuerpo.add(
				Box.createVerticalStrut(
						18
				)
		);

		/*
		 * =====================================================
		 * INFORMACION
		 * =====================================================
		 */
		labelFechaDetalle =
				crearValorDetalle(
						"Fecha",
						"—"
				);

		labelHoraDetalle =
				crearValorDetalle(
						"Horario",
						"—"
				);

		labelRetrasoDetalle =
				crearValorDetalle(
						"Retraso estimado",
						"—"
				);

		cuerpo.add(
				labelFechaDetalle
		);

		cuerpo.add(
				Box.createVerticalStrut(
						12
				)
		);

		cuerpo.add(
				labelHoraDetalle
		);

		cuerpo.add(
				Box.createVerticalStrut(
						12
				)
		);

		cuerpo.add(
				labelRetrasoDetalle
		);

		cuerpo.add(
				Box.createVerticalStrut(
						22
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
						11,
						12,
						11,
						12
				)
		);

		aviso.setAlignmentX(
				LEFT_ALIGNMENT
		);

		aviso.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						64
				)
		);

		JLabel textoAviso =
				new JLabel(
						"<html>"
								+ "<b>Agenda profesional</b><br>"
								+ "Esta vista muestra solamente turnos actuales "
								+ "y futuros que no hayan sido cancelados."
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
	 * VALOR DE DETALLE
	 * =========================================================
	 */
	private JLabel crearValorDetalle(
			String titulo,
			String valor) {

		JLabel label =
				new JLabel(
						"<html>"
								+ "<span style='color:#64748B;'>"
								+ titulo
								+ "</span><br>"
								+ "<b>"
								+ valor
								+ "</b>"
								+ "</html>"
				);

		label.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						11
				)
		);

		label.setForeground(
				new Color(
						15,
						23,
						42
				)
		);

		label.setAlignmentX(
				LEFT_ALIGNMENT
		);

		return label;
	}

	/*
	 * =========================================================
	 * CARGAR AGENDA
	 * =========================================================
	 */
	private void cargarAgenda() {

		modeloTabla.setRowCount(
				0
		);

		LinkedList<Turno> turnos =
				controllerTurno.obtenerPorProfesional(
						profesional.getIdProfesional()
				);

		int cantidad = 0;
		int hoy = 0;
		int pendientes = 0;
		int confirmados = 0;

		LocalDate fechaActual =
				LocalDate.now();

		for (Turno turno : turnos) {

			if (!turno.estaCancelado()
					&& !turno.getFecha()
							.isBefore(
									fechaActual
							)) {

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
										),

								turno
										.getEstado(),

								turno
										.getDisponibilidad()
										.getRetrasoEstimado()
										+ " minutos"
						}
				);

				cantidad++;

				if (turno
						.getFecha()
						.isEqual(
								fechaActual
						)) {

					hoy++;
				}

				if (turno.estaPendiente()) {

					pendientes++;
				}

				if (turno.estaConfirmado()) {

					confirmados++;
				}
			}
		}

		labelCantidad.setText(
				cantidad
						+ (
								cantidad == 1
										? " turno proximo"
										: " turnos proximos"
						)
		);

		valorProximos.setText(
				String.valueOf(
						cantidad
				)
		);

		valorHoy.setText(
				String.valueOf(
						hoy
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

		turnoSeleccionado =
				null;

		tablaTurnos.clearSelection();

		actualizarDetalle();
	}

	/*
	 * =========================================================
	 * TURNO SELECCIONADO
	 * =========================================================
	 */
	private void cargarTurnoSeleccionado() {

		int fila =
				tablaTurnos
						.getSelectedRow();

		if (fila < 0) {

			turnoSeleccionado =
					null;

			actualizarDetalle();

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
				|| labelFechaDetalle == null
				|| labelHoraDetalle == null
				|| labelRetrasoDetalle == null) {

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
					"Seleccione un turno"
			);

			labelDniDetalle.setText(
					"DNI del paciente"
			);

			labelFechaDetalle.setText(
					"<html>"
							+ "<span style='color:#64748B;'>Fecha</span><br>"
							+ "<b>—</b>"
							+ "</html>"
			);

			labelHoraDetalle.setText(
					"<html>"
							+ "<span style='color:#64748B;'>Horario</span><br>"
							+ "<b>—</b>"
							+ "</html>"
			);

			labelRetrasoDetalle.setText(
					"<html>"
							+ "<span style='color:#64748B;'>Retraso estimado</span><br>"
							+ "<b>—</b>"
							+ "</html>"
			);

			labelSeleccion.setText(
					"Seleccione un turno para consultar sus datos."
			);

			return;
		}

		labelEstadoDetalle.setText(
				turnoSeleccionado
						.getEstado()
						.toString()
		);

		if (turnoSeleccionado.estaPendiente()) {

			labelEstadoDetalle.setForeground(
					new Color(
							202,
							113,
							16
					)
			);

		} else if (turnoSeleccionado.estaConfirmado()) {

			labelEstadoDetalle.setForeground(
					new Color(
							59,
							91,
							219
					)
			);

		} else if (turnoSeleccionado.estaPresente()) {

			labelEstadoDetalle.setForeground(
					new Color(
							22,
							163,
							74
					)
			);

		} else {

			labelEstadoDetalle.setForeground(
					new Color(
							100,
							116,
							139
					)
			);
		}

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
				"<html>"
						+ "<span style='color:#64748B;'>Fecha</span><br>"
						+ "<b>"
						+ turnoSeleccionado
								.getFecha()
								.format(
										formatoFecha
								)
						+ "</b>"
						+ "</html>"
		);

		labelHoraDetalle.setText(
				"<html>"
						+ "<span style='color:#64748B;'>Horario</span><br>"
						+ "<b>"
						+ turnoSeleccionado
								.getHora()
								.format(
										formatoHora
								)
						+ "</b>"
						+ "</html>"
		);

		labelRetrasoDetalle.setText(
				"<html>"
						+ "<span style='color:#64748B;'>Retraso estimado</span><br>"
						+ "<b>"
						+ turnoSeleccionado
								.getDisponibilidad()
								.getRetrasoEstimado()
						+ " minutos</b>"
						+ "</html>"
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