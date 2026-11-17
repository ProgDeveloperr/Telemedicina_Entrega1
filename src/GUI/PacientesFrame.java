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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;

import BLL.Paciente;
import DLL.ControllerPaciente;

public class PacientesFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO_VENTANA = 1180;
	private static final int ALTO_VENTANA = 650;
	private static final int ANCHO_FORMULARIO = 380;

	private DashboardEmpleadoFrame dashboard;
	private ControllerPaciente controllerPaciente;

	private JTable tablaPacientes;
	private DefaultTableModel modeloTabla;

	private JTextField campoBuscar;

	private JTextField campoDni;
	private JTextField campoNombre;
	private JTextField campoApellido;
	private JTextField campoTelefono;

	private JButton botonGuardar;
	private JButton botonEliminar;

	private JLabel labelCantidad;
	private JLabel labelSeleccion;
	private JLabel labelModoFormulario;

	private Paciente pacienteSeleccionado;

	public PacientesFrame(
			DashboardEmpleadoFrame dashboard) {

		this.dashboard = dashboard;

		controllerPaciente =
				new ControllerPaciente();

		configurarVentana();
		inicializarComponentes();
		cargarPacientes();
	}

	/*
	 * =========================================================
	 * VENTANA
	 * =========================================================
	 */
	private void configurarVentana() {

		setTitle(
				"Telemedicina - Gestion de pacientes"
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
						"RECEPCION  /  PACIENTES"
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
						"Gestion de pacientes"
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
						"Administra datos personales y registros de pacientes del sistema."
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
						"Directorio de pacientes"
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
						"0 pacientes"
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
				e -> cargarPacientes()
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
						"Seleccione un paciente para editar sus datos."
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

								cargarPacientes();
							}

							@Override
							public void removeUpdate(
									DocumentEvent e) {

								cargarPacientes();
							}

							@Override
							public void changedUpdate(
									DocumentEvent e) {

								cargarPacientes();
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
				"DNI",
				"Nombre",
				"Apellido",
				"Telefono"
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

		tablaPacientes =
				new JTable(
						modeloTabla
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

		tablaPacientes.setIntercellSpacing(
				new Dimension(
						0,
						1
				)
		);

		tablaPacientes
				.getColumnModel()
				.getColumn(0)
				.setPreferredWidth(
						45
				);

		tablaPacientes
				.getColumnModel()
				.getColumn(1)
				.setPreferredWidth(
						120
				);

		tablaPacientes
				.getColumnModel()
				.getColumn(2)
				.setPreferredWidth(
						140
				);

		tablaPacientes
				.getColumnModel()
				.getColumn(3)
				.setPreferredWidth(
						140
				);

		tablaPacientes
				.getColumnModel()
				.getColumn(4)
				.setPreferredWidth(
						130
				);

		tablaPacientes
				.getSelectionModel()
				.addListSelectionListener(
						e -> {

							if (!e.getValueIsAdjusting()) {

								cargarPacienteSeleccionado();
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

		labelModoFormulario =
				new JLabel(
						"ALTA DE PACIENTE"
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
						"Datos del paciente"
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
								+ "Registre un nuevo paciente o seleccione "
								+ "uno existente para modificar sus datos."
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

		cuerpo.setOpaque(false);

		/*
		 * =====================================================
		 * CAMPOS
		 * =====================================================
		 */
		campoDni =
				new JTextField();

		UITheme.configurarCampo(
				campoDni
		);

		campoTelefono =
				new JTextField();

		UITheme.configurarCampo(
				campoTelefono
		);

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

		/*
		 * =====================================================
		 * GRILLA 2 x 2
		 * =====================================================
		 */
		JPanel grillaCampos =
				new JPanel(
						new java.awt.GridLayout(
								2,
								2,
								12,
								16
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
						"DNI",
						campoDni
				)
		);

		grillaCampos.add(
				crearBloqueCampo(
						"Telefono",
						campoTelefono
				)
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

		cuerpo.add(
				grillaCampos
		);

		cuerpo.add(
				Box.createVerticalStrut(
						24
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
						"Registrar paciente",
						TipoBoton.PRIMARIO
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

		cuerpo.add(
				botonGuardar
		);

		cuerpo.add(
				Box.createVerticalStrut(
						10
				)
		);

		/*
		 * =====================================================
		 * ELIMINAR
		 * =====================================================
		 */
		botonEliminar =
				new PremiumButton(
						"Eliminar seleccionado",
						TipoBoton.PELIGRO
				);

		botonEliminar.setAlignmentX(
				LEFT_ALIGNMENT
		);

		botonEliminar.setMaximumSize(
				new Dimension(
						Integer.MAX_VALUE,
						42
				)
		);

		botonEliminar.setEnabled(
				false
		);

		cuerpo.add(
				botonEliminar
		);

		cuerpo.add(
				Box.createVerticalStrut(
						10
				)
		);

		/*
		 * =====================================================
		 * LIMPIAR
		 * =====================================================
		 */
		JButton botonLimpiar =
				new JButton(
						"Limpiar / Nuevo"
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
		botonGuardar.addActionListener(
				e -> guardarPaciente()
		);

		botonEliminar.addActionListener(
				e -> eliminarPaciente()
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
	 * CARGAR PACIENTES
	 * =========================================================
	 */
	private void cargarPacientes() {

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

		LinkedList<Paciente> pacientes =
				controllerPaciente.buscar(
						filtro
				);

		for (Paciente paciente
				: pacientes) {

			modeloTabla.addRow(
					new Object[] {

							paciente
									.getIdPaciente(),

							paciente
									.getDni(),

							paciente
									.getNombre(),

							paciente
									.getApellido(),

							paciente
									.getTelefono()
					}
			);
		}

		if (labelCantidad != null) {

			labelCantidad.setText(
					pacientes.size()
							+ (
									pacientes.size() == 1
											? " paciente"
											: " pacientes"
							)
			);
		}

		pacienteSeleccionado =
				null;

		if (tablaPacientes != null) {

			tablaPacientes.clearSelection();
		}

		if (labelSeleccion != null) {

			labelSeleccion.setText(
					"Seleccione un paciente para editar sus datos."
			);
		}

		actualizarModoFormulario();
	}

	/*
	 * =========================================================
	 * PACIENTE SELECCIONADO
	 * =========================================================
	 */
	private void cargarPacienteSeleccionado() {

		int fila =
				tablaPacientes
						.getSelectedRow();

		if (fila < 0) {

			pacienteSeleccionado =
					null;

			actualizarModoFormulario();

			return;
		}

		int idPaciente =
				(int) modeloTabla.getValueAt(
						fila,
						0
				);

		pacienteSeleccionado =
				controllerPaciente.buscarPorId(
						idPaciente
				);

		if (pacienteSeleccionado == null) {

			actualizarModoFormulario();

			return;
		}

		campoDni.setText(
				pacienteSeleccionado
						.getDni()
		);

		campoNombre.setText(
				pacienteSeleccionado
						.getNombre()
		);

		campoApellido.setText(
				pacienteSeleccionado
						.getApellido()
		);

		campoTelefono.setText(
				pacienteSeleccionado
						.getTelefono()
		);

		labelSeleccion.setText(
				"Seleccionado: "
						+ pacienteSeleccionado.getNombre()
						+ " "
						+ pacienteSeleccionado.getApellido()
						+ "  •  DNI "
						+ pacienteSeleccionado.getDni()
		);

		actualizarModoFormulario();
	}

	/*
	 * =========================================================
	 * ACTUALIZAR MODO DEL FORMULARIO
	 * =========================================================
	 */
	private void actualizarModoFormulario() {

		if (botonGuardar == null
				|| botonEliminar == null
				|| labelModoFormulario == null) {

			return;
		}

		if (pacienteSeleccionado == null) {

			botonGuardar.setText(
					"Registrar paciente"
			);

			botonEliminar.setEnabled(
					false
			);

			labelModoFormulario.setText(
					"ALTA DE PACIENTE"
			);

		} else {

			botonGuardar.setText(
					"Guardar cambios"
			);

			botonEliminar.setEnabled(
					true
			);

			labelModoFormulario.setText(
					"EDICION DE PACIENTE"
			);
		}
	}

	/*
	 * =========================================================
	 * GUARDAR PACIENTE
	 * =========================================================
	 */
	private void guardarPaciente() {

		String dni =
				campoDni
						.getText()
						.trim();

		String nombre =
				campoNombre
						.getText()
						.trim();

		String apellido =
				campoApellido
						.getText()
						.trim();

		String telefono =
				campoTelefono
						.getText()
						.trim();

		/*
		 * =====================================================
		 * CAMPOS OBLIGATORIOS
		 * =====================================================
		 */
		if (dni.isEmpty()
				|| nombre.isEmpty()
				|| apellido.isEmpty()
				|| telefono.isEmpty()) {

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
		 * DNI SOLO NUMEROS
		 * =====================================================
		 */
		if (!dni.matches("\\d+")) {

			JOptionPane.showMessageDialog(
					this,
					"El DNI debe contener solamente numeros.",
					"DNI invalido",
					JOptionPane.WARNING_MESSAGE
			);

			campoDni.requestFocus();

			return;
		}

		Paciente pacienteConMismoDni =
				controllerPaciente
						.buscarPorDni(
								dni
						);

		/*
		 * =====================================================
		 * ALTA
		 * =====================================================
		 */
		if (pacienteSeleccionado == null) {

			/*
			 * VALIDACION DE DUPLICADO.
			 */
			if (pacienteConMismoDni != null) {

				JOptionPane.showMessageDialog(
						this,
						"Ya existe un paciente registrado con ese DNI.",
						"Paciente existente",
						JOptionPane.WARNING_MESSAGE
				);

				campoDni.requestFocus();

				return;
			}

			Paciente nuevoPaciente =
					new Paciente(
							0,
							dni,
							nombre,
							apellido,
							telefono
					);

			int confirmacion =
					JOptionPane.showConfirmDialog(
							this,
							"¿Desea registrar al paciente?\n\n"
									+ nombre
									+ " "
									+ apellido
									+ "\nDNI: "
									+ dni
									+ "\nTelefono: "
									+ telefono,
							"Confirmar registro",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE
					);

			if (confirmacion
					!= JOptionPane.YES_OPTION) {

				return;
			}

			boolean registrado =
					controllerPaciente.registrar(
							nuevoPaciente
					);

			if (registrado) {

				JOptionPane.showMessageDialog(
						this,
						"Paciente registrado correctamente.",
						"Registro exitoso",
						JOptionPane.INFORMATION_MESSAGE
				);

				limpiarFormulario();

				campoBuscar.setText("");

				cargarPacientes();

			} else {

				mostrarError();
			}

			return;
		}

		/*
		 * =====================================================
		 * EDICION:
		 * EVITAR DNI DE OTRO PACIENTE
		 * =====================================================
		 */
		if (pacienteConMismoDni != null
				&& pacienteConMismoDni
						.getIdPaciente()
						!= pacienteSeleccionado
								.getIdPaciente()) {

			JOptionPane.showMessageDialog(
					this,
					"Ya existe otro paciente registrado con ese DNI.",
					"DNI existente",
					JOptionPane.WARNING_MESSAGE
			);

			campoDni.requestFocus();

			return;
		}

		Paciente pacienteModificado =
				new Paciente(
						pacienteSeleccionado
								.getIdPaciente(),
						dni,
						nombre,
						apellido,
						telefono
				);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea guardar los cambios del paciente?\n\n"
								+ nombre
								+ " "
								+ apellido
								+ "\nDNI: "
								+ dni,
						"Confirmar modificacion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean modificado =
				controllerPaciente.modificar(
						pacienteModificado
				);

		if (modificado) {

			JOptionPane.showMessageDialog(
					this,
					"Datos del paciente actualizados correctamente.",
					"Modificacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

			limpiarFormulario();
			cargarPacientes();

		} else {

			mostrarError();
		}
	}

	/*
	 * =========================================================
	 * ELIMINAR PACIENTE
	 * =========================================================
	 */
	private void eliminarPaciente() {

		if (pacienteSeleccionado == null) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un paciente de la tabla.",
					"Paciente no seleccionado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		/*
		 * Conservamos la integridad referencial:
		 * un paciente con turnos no se elimina.
		 */
		if (controllerPaciente.tieneTurnos(
				pacienteSeleccionado
						.getIdPaciente())) {

			JOptionPane.showMessageDialog(
					this,
					"No se puede eliminar este paciente porque posee turnos asociados.\n\n"
							+ "Sus datos deben conservarse para mantener el historial del sistema.",
					"Paciente no eliminable",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea eliminar al paciente "
								+ pacienteSeleccionado
										.getNombre()
								+ " "
								+ pacienteSeleccionado
										.getApellido()
								+ "?\n\n"
								+ "Esta accion no se puede deshacer.",
						"Confirmar eliminacion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE
				);

		if (confirmacion
				!= JOptionPane.YES_OPTION) {

			return;
		}

		boolean eliminado =
				controllerPaciente.eliminar(
						pacienteSeleccionado
								.getIdPaciente()
				);

		if (eliminado) {

			JOptionPane.showMessageDialog(
					this,
					"Paciente eliminado correctamente.",
					"Eliminacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

			limpiarFormulario();
			cargarPacientes();

		} else {

			mostrarError();
		}
	}

	/*
	 * =========================================================
	 * LIMPIAR
	 * =========================================================
	 */
	private void limpiarFormulario() {

		pacienteSeleccionado =
				null;

		tablaPacientes.clearSelection();

		campoDni.setText("");
		campoNombre.setText("");
		campoApellido.setText("");
		campoTelefono.setText("");

		labelSeleccion.setText(
				"Seleccione un paciente para editar sus datos."
		);

		actualizarModoFormulario();

		campoDni.requestFocus();
	}

	/*
	 * =========================================================
	 * ERROR GENERICO
	 * =========================================================
	 */
	private void mostrarError() {

		JOptionPane.showMessageDialog(
				this,
				"No se pudo completar la operacion.",
				"Error",
				JOptionPane.ERROR_MESSAGE
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
	 * TIPO BOTON
	 * =========================================================
	 */
	private enum TipoBoton {

		PRIMARIO,
		SECUNDARIO,
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

			actualizarColorTexto();

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

			setPreferredSize(
					new Dimension(
							280,
							42
					)
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

			/*
			 * =================================================
			 * PELIGRO
			 * =================================================
			 */
			} else if (tipo == TipoBoton.PELIGRO) {

				if (hover) {

					g2.setColor(
							new Color(
									254,
									242,
									242
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