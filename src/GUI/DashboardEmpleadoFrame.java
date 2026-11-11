package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import BLL.Usuario;

public class DashboardEmpleadoFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	public DashboardEmpleadoFrame(Usuario usuario) {

		this.usuario = usuario;

		configurarVentana();
		inicializarComponentes();
	}

	private void configurarVentana() {

		setTitle(
				"Telemedicina - Recepcion - "
						+ usuario.getNombre()
						+ " "
						+ usuario.getApellido()
		);

		setDefaultCloseOperation(
				JFrame.EXIT_ON_CLOSE
		);

		setSize(820, 650);

		setLocationRelativeTo(null);

		setResizable(false);
		
		configurarIconoAplicacion();
	}

	private void inicializarComponentes() {

		setLayout(
				new BorderLayout()
		);

		/*
		 * =====================================================
		 * CABECERA
		 * =====================================================
		 */
		JPanel panelSuperior =
				new JPanel();

		panelSuperior.setLayout(
				new BoxLayout(
						panelSuperior,
						BoxLayout.Y_AXIS
				)
		);

		panelSuperior.setBorder(
				BorderFactory.createEmptyBorder(
						25,
						20,
						10,
						20
				)
		);

		JLabel titulo =
				new JLabel(
						"TELEMEDICINA"
				);

		titulo.setFont(
				new Font(
						"SansSerif",
						Font.BOLD,
						24
				)
		);

		titulo.setAlignmentX(
				CENTER_ALIGNMENT
		);

		JLabel usuarioLabel =
				new JLabel(
						"Recepcionista: "
								+ usuario.getNombre()
								+ " "
								+ usuario.getApellido()
				);

		usuarioLabel.setFont(
				new Font(
						"SansSerif",
						Font.PLAIN,
						14
				)
		);

		usuarioLabel.setAlignmentX(
				CENTER_ALIGNMENT
		);

		panelSuperior.add(
				titulo
		);

		panelSuperior.add(
				Box.createVerticalStrut(5)
		);

		panelSuperior.add(
				usuarioLabel
		);

		add(
				panelSuperior,
				BorderLayout.NORTH
		);

		
		
		
		
		
		
		JPanel panelContenido =
				new JPanel();

		panelContenido.setLayout(
				new BoxLayout(
						panelContenido,
						BoxLayout.Y_AXIS
				)
		);

		panelContenido.setBorder(
				BorderFactory.createEmptyBorder(
						15,
						45,
						10,
						45
				)
		);

		/*
		 * =====================================================
		 * BANNER
		 * =====================================================
		 */
		JLabel imagenEmpleado =
				new JLabel(
						"",
						SwingConstants.CENTER
				);

		ImageIcon iconoEmpleado =
				cargarIconoEscalado(
						"/resources/imagenes/menu_empleado.png",

						/*
						
						 * PRIMER NUMERO  = ANCHO
						 * SEGUNDO NUMERO = ALTO
						
						 */
						620,
						190
				);

		if (iconoEmpleado != null) {

			imagenEmpleado.setIcon(
					iconoEmpleado
			);
		}

		imagenEmpleado.setAlignmentX(
				CENTER_ALIGNMENT
		);

		imagenEmpleado.setHorizontalAlignment(
				SwingConstants.CENTER
		);

		
		imagenEmpleado.setPreferredSize(
				new Dimension(
						650,
						190
				)
		);

		imagenEmpleado.setMaximumSize(
				new Dimension(
						650,
						190
				)
		);

		panelContenido.add(
				imagenEmpleado
		);

		
		
		
		
		
		panelContenido.add(
				Box.createVerticalStrut(25)
		);

	
		
		
		
		
		JPanel panelOpciones =
				new JPanel(
						new GridLayout(
								2,
								2,
								15,
								15
						)
				);

		panelOpciones.setAlignmentX(
				CENTER_ALIGNMENT
		);

		panelOpciones.setPreferredSize(
				new Dimension(
						620,
						170
				)
		);

		panelOpciones.setMaximumSize(
				new Dimension(
						620,
						170
				)
		);

		JButton botonPacientes =
				new JButton(
						"Pacientes"
				);

		JButton botonProfesionales =
				new JButton(
						"Profesionales"
				);

		JButton botonTurnos =
				new JButton(
						"Turnos"
				);

		JButton botonConsultas =
				new JButton(
						"Consultas e informacion"
				);

		panelOpciones.add(
				botonPacientes
		);

		panelOpciones.add(
				botonProfesionales
		);

		panelOpciones.add(
				botonTurnos
		);

		panelOpciones.add(
				botonConsultas
		);

		panelContenido.add(
				panelOpciones
		);

		add(
				panelContenido,
				BorderLayout.CENTER
		);

		/*
		 * =====================================================
		 * CERRAR SESION
		 * =====================================================
		 */
		JButton botonCerrarSesion =
				new JButton(
						"Cerrar sesion"
				);

		JPanel panelInferior =
				new JPanel();

		panelInferior.setBorder(
				BorderFactory.createEmptyBorder(
						10,
						10,
						20,
						10
				)
		);

		panelInferior.add(
				botonCerrarSesion
		);

		add(
				panelInferior,
				BorderLayout.SOUTH
		);

		/*
		 * =====================================================
		 * EVENTOS
		 * =====================================================
		 */

		botonPacientes.addActionListener(
				e -> abrirPacientes()
		);

		botonProfesionales.addActionListener(
				e -> abrirProfesionales()
		);

		botonTurnos.addActionListener(
				e -> abrirTurnos()
		);

		botonConsultas.addActionListener(
				e -> abrirConsultasInformacion()
		);

		botonCerrarSesion.addActionListener(
				e -> cerrarSesion()
		);
	}

	private void cerrarSesion() {

		int confirmacion =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea cerrar la sesion?",
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

	private void abrirPacientes() {

		setVisible(false);

		PacientesFrame pacientesFrame =
				new PacientesFrame(
						this
				);

		pacientesFrame.setVisible(
				true
		);
	}

	private void abrirProfesionales() {

		setVisible(false);

		ProfesionalesFrame profesionalesFrame =
				new ProfesionalesFrame(
						this
				);

		profesionalesFrame.setVisible(
				true
		);
	}

	private void abrirTurnos() {

		setVisible(false);

		TurnosFrame turnosFrame =
				new TurnosFrame(
						this
				);

		turnosFrame.setVisible(
				true
		);
	}

	private void abrirConsultasInformacion() {

		setVisible(false);

		ConsultasInformacionFrame consultasFrame =
				new ConsultasInformacionFrame(
						this
				);

		consultasFrame.setVisible(
				true
		);
	}

	/*
	 * =========================================================
	 * CARGA Y ESCALADO DE IMAGEN
	 * =========================================================
	 */
	private ImageIcon cargarIconoEscalado(
			String ruta,
			int ancho,
			int alto) {

		URL url =
				getClass().getResource(
						ruta
				);

		if (url == null) {

			System.out.println(
					"No se encontro la imagen: "
							+ ruta
			);

			return null;
		}

		Image imagen =
				new ImageIcon(
						url
				)
						.getImage()
						.getScaledInstance(
								ancho,
								alto,
								Image.SCALE_SMOOTH
						);

		return new ImageIcon(
				imagen
		);
	}
	
	
	
	
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
}