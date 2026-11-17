package GUI;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.format.DateTimeFormatter;
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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;

import BLL.Disponibilidad;
import BLL.Profesional;
import BLL.TipoUsuario;
import BLL.Usuario;
import DLL.ControllerDisponibilidad;
import DLL.ControllerProfesional;
import DLL.ControllerUsuario;

public class ProfesionalesFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO_VENTANA = 1220;
	private static final int ALTO_VENTANA = 700;

	/*
	 * Un poco mas ancho que antes para permitir
	 * el formulario premium en dos columnas.
	 */
	private static final int ANCHO_FORMULARIO = 390;

	private DashboardEmpleadoFrame dashboard;

	private ControllerProfesional controllerProfesional;
	private ControllerUsuario controllerUsuario;

	private JTable tablaProfesionales;
	private DefaultTableModel modeloTabla;

	private JTextField campoBuscar;

	private JTextField campoNombre;
	private JTextField campoApellido;
	private JTextField campoUsuario;
	private JPasswordField campoClave;
	private JTextField campoMatricula;
	private JTextField campoEspecialidad;

	private JLabel labelCantidad;
	private JLabel labelSeleccion;

	private Profesional profesionalSeleccionado;

	public ProfesionalesFrame(
			DashboardEmpleadoFrame dashboard) {

		this.dashboard = dashboard;

		controllerProfesional =
				new ControllerProfesional();

		controllerUsuario =
				new ControllerUsuario();

		configurarVentana();
		inicializarComponentes();
		cargarProfesionales();
	}

	/*
	 * =========================================================
	 * VENTANA
	 * =========================================================
	 */
	private void configurarVentana() {

		setTitle(
				"Telemedicina - Gestion de profesionales"
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

		setLocationRelativeTo(null);
		setResizable(false);

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

		contenido.setOpaque(false);

		contenido.setBorder(
				BorderFactory.createEmptyBorder(
						0,
						30,
						28,
						30
				)
		);

		contenido.add(
				crearPanelDirectorio(),
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

		cabecera.setOpaque(false);

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

		textos.setOpaque(false);

		JLabel contexto =
				new JLabel(
						"RECEPCION  /  PROFESIONALES"
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
						"Gestion de profesionales"
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
						"Administra profesionales, credenciales y disponibilidad de atencion."
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
	 * DIRECTORIO
	 * =========================================================
	 */
	private JPanel crearPanelDirectorio() {

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
		 * CABECERA DEL DIRECTORIO
		 * =====================================================
		 */
		JPanel superior =
				new JPanel(
						new BorderLayout()
				);

		superior.setOpaque(false);

		JPanel bloqueTitulo =
				new JPanel();

		bloqueTitulo.setLayout(
				new BoxLayout(
						bloqueTitulo,
						BoxLayout.Y_AXIS
				)
		);

		bloqueTitulo.setOpaque(false);

		JLabel titulo =
				new JLabel(
						"Directorio de profesionales"
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
						"0 profesionales"
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

		bloqueTitulo.add(
				titulo
		);

		bloqueTitulo.add(
				Box.createVerticalStrut(
						4
				)
		);

		bloqueTitulo.add(
				labelCantidad
		);

		superior.add(
				bloqueTitulo,
				BorderLayout.WEST
		);

		/*
		 * =====================================================
		 * BUSCADOR
		 * =====================================================
		 */
		JPanel buscador =
				new JPanel(
						new BorderLayout(
								8,
								0
						)
				);

		buscador.setOpaque(false);

		JLabel labelBuscar =
				new JLabel(
						"Buscar"
				);

		labelBuscar.setFont(
				new Font(
						"Segoe UI",
						Font.BOLD,
						11
				)
		);

		labelBuscar.setForeground(
				new Color(
						71,
						85,
						105
				)
		);

		campoBuscar =
				new JTextField();

		UITheme.configurarCampo(
				campoBuscar
		);

		campoBuscar.setPreferredSize(
				new Dimension(
						200,
						38
				)
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
						105,
						38
				)
		);

		botonActualizar.addActionListener(
				e -> cargarProfesionales()
		);

		buscador.add(
				labelBuscar,
				BorderLayout.WEST
		);

		buscador.add(
				campoBuscar,
				BorderLayout.CENTER
		);

		buscador.add(
				botonActualizar,
				BorderLayout.EAST
		);

		superior.add(
				buscador,
				BorderLayout.EAST
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
						tablaProfesionales
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

		pie.setOpaque(false);

		labelSeleccion =
				new JLabel(
						"Seleccione un profesional para consultar su disponibilidad."
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
						"Datos sincronizados con la base de datos."
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

		/*
		 * =====================================================
		 * BUSQUEDA AUTOMATICA
		 * =====================================================
		 */
		campoBuscar
				.getDocument()
				.addDocumentListener(
						new DocumentListener() {

							@Override
							public void insertUpdate(
									DocumentEvent e) {

								cargarProfesionales();
							}

							@Override
							public void removeUpdate(
									DocumentEvent e) {

								cargarProfesionales();
							}

							@Override
							public void changedUpdate(
									DocumentEvent e) {

								cargarProfesionales();
							}
						}
				);

		return tarjeta;
	}

	/*
	 * =========================================================
	 * TABLA
	 * =========================================================
	 */
	private void crearTabla() {

		String[] columnas = {
				"ID",
				"Nombre",
				"Apellido",
				"Usuario",
				"Matricula",
				"Especialidad"
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

		tablaProfesionales =
				new JTable(
						modeloTabla
				);

		UITheme.configurarTabla(
				tablaProfesionales
		);

		tablaProfesionales.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION
		);

		tablaProfesionales.setRowHeight(
				38
		);

		tablaProfesionales.setIntercellSpacing(
				new Dimension(
						0,
						1
				)
		);

		tablaProfesionales
				.getColumnModel()
				.getColumn(0)
				.setPreferredWidth(40);

		tablaProfesionales
				.getColumnModel()
				.getColumn(1)
				.setPreferredWidth(115);

		tablaProfesionales
				.getColumnModel()
				.getColumn(2)
				.setPreferredWidth(115);

		tablaProfesionales
				.getColumnModel()
				.getColumn(3)
				.setPreferredWidth(110);

		tablaProfesionales
				.getColumnModel()
				.getColumn(4)
				.setPreferredWidth(105);

		tablaProfesionales
				.getColumnModel()
				.getColumn(5)
				.setPreferredWidth(150);

		tablaProfesionales
				.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								cargarProfesionalSeleccionado();
							}
						}
				);
	}

	/*
	 * =========================================================
	 * FORMULARIO PREMIUM
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

		cabecera.setOpaque(false);

		JLabel badge =
				new JLabel(
						"ALTA PROFESIONAL"
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
						"Registrar profesional"
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
								+ "Complete los datos requeridos. "
								+ "El usuario y la matricula deben ser unicos."
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
		 * CUERPO DEL FORMULARIO
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

		cuerpo.setOpaque(false);

		/*
		 * =====================================================
		 * CAMPOS
		 * =====================================================
		 */
		campoNombre =
				new JTextField();

		UITheme.configurarCampo(
				campoNombre
		);

		campoApellido =
				new JTextField();

		UITheme.configurarCampo(
				campoApellido
		);

		campoUsuario =
				new JTextField();

		UITheme.configurarCampo(
				campoUsuario
		);

		campoClave =
				new JPasswordField();

		UITheme.configurarCampoClave(
				campoClave
		);

		campoMatricula =
				new JTextField();

		UITheme.configurarCampo(
				campoMatricula
		);

		campoEspecialidad =
				new JTextField();

		UITheme.configurarCampo(
				campoEspecialidad
		);

		/*
		 * =====================================================
		 * GRILLA 3 x 2
		 * =====================================================
		 */
		JPanel grillaCampos =
				new JPanel(
						new java.awt.GridLayout(
								3,
								2,
								12,
								14
						)
				);

		grillaCampos.setOpaque(
				false
		);

		grillaCampos.setAlignmentX(
				LEFT_ALIGNMENT
		);

		grillaCampos.add(
				crearBloqueCampo(
						"Nombre",
						campoNombre
				)
		);

		grillaCampos.add(
				crearBloqueCampo(
						"Apellido",
						campoApellido
				)
		);

		grillaCampos.add(
				crearBloqueCampo(
						"Usuario",
						campoUsuario
				)
		);

		grillaCampos.add(
				crearBloqueCampo(
						"Contraseña",
						campoClave
				)
		);

		grillaCampos.add(
				crearBloqueCampo(
						"Matricula",
						campoMatricula
				)
		);

		grillaCampos.add(
				crearBloqueCampo(
						"Especialidad",
						campoEspecialidad
				)
		);

		cuerpo.add(
				grillaCampos
		);

		cuerpo.add(
				Box.createVerticalStrut(
						18
				)
		);

		/*
		 * =====================================================
		 * SEPARADOR
		 * =====================================================
		 */
		JPanel separador =
				new JPanel();

		separador.setBackground(
				new Color(
						226,
						232,
						240
				)
		);

		separador.setPreferredSize(
				new Dimension(
						100,
						1
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

		cuerpo.add(
				separador
		);

		cuerpo.add(
				Box.createVerticalStrut(
						16
				)
		);

		/*
		 * =====================================================
		 * REGISTRAR
		 * =====================================================
		 */
		JButton botonRegistrar =
				new PremiumButton(
						"Registrar profesional",
						true
				);

		botonRegistrar.setAlignmentX(
				LEFT_ALIGNMENT
		);

		botonRegistrar.setPreferredSize(
				new Dimension(
						300,
						42
				)
		);

		botonRegistrar.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						42
				)
		);

		cuerpo.add(
				botonRegistrar
		);

		cuerpo.add(
				Box.createVerticalStrut(
						9
				)
		);

		/*
		 * =====================================================
		 * DISPONIBILIDAD
		 * =====================================================
		 */
		JButton botonDisponibilidad =
				new PremiumButton(
						"Ver disponibilidad",
						false
				);

		botonDisponibilidad.setAlignmentX(
				LEFT_ALIGNMENT
		);

		botonDisponibilidad.setPreferredSize(
				new Dimension(
						300,
						42
				)
		);

		botonDisponibilidad.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						42
				)
		);

		cuerpo.add(
				botonDisponibilidad
		);

		cuerpo.add(
				Box.createVerticalStrut(
						9
				)
		);

		/*
		 * =====================================================
		 * LIMPIAR
		 * =====================================================
		 */
		JButton botonLimpiar =
				new JButton(
						"Limpiar formulario"
				);

		configurarBotonTexto(
				botonLimpiar
		);

		botonLimpiar.setAlignmentX(
				CENTER_ALIGNMENT
		);

		botonLimpiar.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						30
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
		botonRegistrar.addActionListener(
				e -> registrarProfesional()
		);

		botonDisponibilidad.addActionListener(
				e -> consultarDisponibilidad()
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
	 * BLOQUE DE CAMPO
	 * =========================================================
	 */
	private JPanel crearBloqueCampo(
			String texto,
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
						texto
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

		campo.setMinimumSize(
				new Dimension(
						100,
						36
				)
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

		return bloque;
	}

	/*
	 * =========================================================
	 * CARGAR PROFESIONALES
	 * =========================================================
	 */
	private void cargarProfesionales() {

		modeloTabla.setRowCount(
				0
		);

		String filtro = "";

		if (campoBuscar != null) {

			filtro =
					campoBuscar
							.getText()
							.trim();
		}

		LinkedList<Profesional> profesionales =
				controllerProfesional.buscar(
						filtro
				);

		for (Profesional profesional
				: profesionales) {

			Usuario usuarioProfesional =
					controllerProfesional
							.obtenerUsuarioPorProfesional(
									profesional
											.getIdProfesional()
							);

			if (usuarioProfesional != null) {

				modeloTabla.addRow(
						new Object[] {

								profesional
										.getIdProfesional(),

								usuarioProfesional
										.getNombre(),

								usuarioProfesional
										.getApellido(),

								usuarioProfesional
										.getNombreUsuario(),

								profesional
										.getMatricula(),

								profesional
										.getEspecialidad()
						}
				);
			}
		}

		if (labelCantidad != null) {

			labelCantidad.setText(
					profesionales.size()
							+ (
									profesionales.size() == 1
											? " profesional"
											: " profesionales"
							)
			);
		}

		profesionalSeleccionado =
				null;

		if (tablaProfesionales != null) {

			tablaProfesionales.clearSelection();
		}

		if (labelSeleccion != null) {

			labelSeleccion.setText(
					"Seleccione un profesional para consultar su disponibilidad."
			);
		}
	}

	/*
	 * =========================================================
	 * PROFESIONAL SELECCIONADO
	 * =========================================================
	 */
	private void cargarProfesionalSeleccionado() {

		int fila =
				tablaProfesionales
						.getSelectedRow();

		if (fila < 0) {

			profesionalSeleccionado =
					null;

			labelSeleccion.setText(
					"Seleccione un profesional para consultar su disponibilidad."
			);

			return;
		}

		int idProfesional =
				(int) modeloTabla.getValueAt(
						fila,
						0
				);

		profesionalSeleccionado =
				controllerProfesional
						.buscarPorId(
								idProfesional
						);

		if (profesionalSeleccionado == null) {

			labelSeleccion.setText(
					"No se pudieron recuperar los datos del profesional."
			);

			return;
		}

		Usuario usuarioProfesional =
				controllerProfesional
						.obtenerUsuarioPorProfesional(
								idProfesional
						);

		if (usuarioProfesional != null) {

			labelSeleccion.setText(
					"Seleccionado: "
							+ usuarioProfesional.getNombre()
							+ " "
							+ usuarioProfesional.getApellido()
							+ "  •  "
							+ profesionalSeleccionado.getMatricula()
			);
		}
	}

	/*
	 * =========================================================
	 * REGISTRAR PROFESIONAL
	 * =========================================================
	 */
	private void registrarProfesional() {

		String nombre =
				campoNombre
						.getText()
						.trim();

		String apellido =
				campoApellido
						.getText()
						.trim();

		String nombreUsuario =
				campoUsuario
						.getText()
						.trim();

		String clave =
				new String(
						campoClave
								.getPassword()
				);

		String matricula =
				campoMatricula
						.getText()
						.trim();

		String especialidad =
				campoEspecialidad
						.getText()
						.trim();

		/*
		 * =====================================================
		 * CAMPOS OBLIGATORIOS
		 * =====================================================
		 */
		if (nombre.isEmpty()
				|| apellido.isEmpty()
				|| nombreUsuario.isEmpty()
				|| clave.isEmpty()
				|| matricula.isEmpty()
				|| especialidad.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"Todos los campos son obligatorios.",
					"Datos incompletos",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		/*
		 * =====================================================
		 * VALIDACION:
		 * USUARIO DUPLICADO
		 * =====================================================
		 */
		if (controllerUsuario
				.existeNombreUsuario(
						nombreUsuario
				)) {

			JOptionPane.showMessageDialog(
					this,
					"Ya existe un usuario con ese nombre de usuario.",
					"Usuario existente",
					JOptionPane.WARNING_MESSAGE
			);

			campoUsuario.requestFocus();

			return;
		}

		/*
		 * =====================================================
		 * VALIDACION:
		 * MATRICULA DUPLICADA
		 * =====================================================
		 */
		if (controllerProfesional
				.existeMatricula(
						matricula
				)) {

			JOptionPane.showMessageDialog(
					this,
					"Ya existe un profesional con esa matricula.",
					"Matricula existente",
					JOptionPane.WARNING_MESSAGE
			);

			campoMatricula.requestFocus();

			return;
		}

		Usuario usuarioNuevo =
				new Usuario(
						0,
						nombre,
						apellido,
						nombreUsuario,
						clave,
						TipoUsuario.PROFESIONAL
				);

		Profesional profesionalNuevo =
				new Profesional(
						0,
						matricula,
						especialidad
				);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea registrar al profesional?\n\n"
								+ nombre
								+ " "
								+ apellido
								+ "\nUsuario: "
								+ nombreUsuario
								+ "\nMatricula: "
								+ matricula
								+ "\nEspecialidad: "
								+ especialidad,
						"Confirmar registro",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean registrado =
				controllerProfesional.registrar(
						usuarioNuevo,
						profesionalNuevo
				);

		if (registrado) {

			JOptionPane.showMessageDialog(
					this,
					"Profesional registrado correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

			limpiarFormulario();

			/*
			 * Si habia una busqueda activa,
			 * se limpia para mostrar el nuevo registro.
			 */
			campoBuscar.setText("");

			cargarProfesionales();

		} else {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo registrar el profesional.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	/*
	 * =========================================================
	 * CONSULTAR DISPONIBILIDAD
	 * =========================================================
	 */
	private void consultarDisponibilidad() {

		if (profesionalSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un profesional de la tabla.",
					"Profesional no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerDisponibilidad controllerDisponibilidad =
				new ControllerDisponibilidad();

		LinkedList<Disponibilidad> disponibilidades =
				controllerDisponibilidad
						.obtenerPorProfesional(
								profesionalSeleccionado
										.getIdProfesional()
						);

		if (disponibilidades.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"El profesional seleccionado no tiene disponibilidades registradas.",
					"Sin disponibilidades",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		Usuario usuarioProfesional =
				controllerProfesional
						.obtenerUsuarioPorProfesional(
								profesionalSeleccionado
										.getIdProfesional()
						);

		DateTimeFormatter formatoFecha =
				DateTimeFormatter.ofPattern(
						"dd/MM/yyyy"
				);

		DateTimeFormatter formatoHora =
				DateTimeFormatter.ofPattern(
						"HH:mm"
				);

		StringBuilder resultado =
				new StringBuilder();

		if (usuarioProfesional != null) {

			resultado
					.append(
							"Profesional: "
					)

					.append(
							usuarioProfesional
									.getNombre()
					)

					.append(
							" "
					)

					.append(
							usuarioProfesional
									.getApellido()
					)

					.append(
							"\nEspecialidad: "
					)

					.append(
							profesionalSeleccionado
									.getEspecialidad()
					)

					.append(
							"\n\n"
					);
		}

		for (Disponibilidad disponibilidad
				: disponibilidades) {

			resultado
					.append(
							"ID disponibilidad: "
					)

					.append(
							disponibilidad
									.getIdDisponibilidad()
					)

					.append(
							"\nFecha: "
					)

					.append(
							disponibilidad
									.getFecha()
									.format(
											formatoFecha
									)
					)

					.append(
							"\nHorario: "
					)

					.append(
							disponibilidad
									.getHoraInicio()
									.format(
											formatoHora
									)
					)

					.append(
							" - "
					)

					.append(
							disponibilidad
									.getHoraFin()
									.format(
											formatoHora
									)
					)

					.append(
							"\nRetraso estimado: "
					)

					.append(
							disponibilidad
									.getRetrasoEstimado()
					)

					.append(
							" minutos"
					)

					.append(
							"\n------------------------------\n"
					);
		}

		JOptionPane.showMessageDialog(
				this,
				resultado.toString(),
				"Telemedicina - Disponibilidad",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	/*
	 * =========================================================
	 * LIMPIAR
	 * =========================================================
	 */
	private void limpiarFormulario() {

		campoNombre.setText("");
		campoApellido.setText("");
		campoUsuario.setText("");
		campoClave.setText("");
		campoMatricula.setText("");
		campoEspecialidad.setText("");

		tablaProfesionales.clearSelection();

		profesionalSeleccionado =
				null;

		labelSeleccion.setText(
				"Seleccione un profesional para consultar su disponibilidad."
		);

		campoNombre.requestFocus();
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

		boton.setFocusPainted(false);
		boton.setContentAreaFilled(true);
		boton.setOpaque(true);

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

		boton.setBackground(
				Color.WHITE
		);

		boton.setFocusPainted(false);
		boton.setBorderPainted(false);
		boton.setContentAreaFilled(false);

		boton.setCursor(
				Cursor.getPredefinedCursor(
						Cursor.HAND_CURSOR
				)
		);
	}

	/*
	 * =========================================================
	 * TARJETA REDONDEADA
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

				if (getModel().isPressed()) {

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

			} else {

				if (hover) {

					g2.setColor(
							new Color(
									238,
									249,
									252
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