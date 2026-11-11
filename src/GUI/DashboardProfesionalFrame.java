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

public class DashboardProfesionalFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	public DashboardProfesionalFrame(
			Usuario usuario) {

		this.usuario = usuario;

		configurarVentana();
		inicializarComponentes();
	}

	private void configurarVentana() {

		setTitle(
				"Telemedicina - Profesional - "
						+ usuario.getNombre()
						+ " "
						+ usuario.getApellido()
		);

		setDefaultCloseOperation(
				JFrame.EXIT_ON_CLOSE
		);

		/*
		 * Tamaño general de la ventana.
		 */
		setSize(900, 680);

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

		JLabel profesionalLabel =
				new JLabel(
						"Profesional: "
								+ usuario.getNombre()
								+ " "
								+ usuario.getApellido()
				);

		profesionalLabel.setFont(
				new Font(
						"SansSerif",
						Font.PLAIN,
						14
				)
		);

		profesionalLabel.setAlignmentX(
				CENTER_ALIGNMENT
		);

		panelSuperior.add(
				titulo
		);

		panelSuperior.add(
				Box.createVerticalStrut(5)
		);

		panelSuperior.add(
				profesionalLabel
		);

		add(
				panelSuperior,
				BorderLayout.NORTH
		);

		/*
		 * =====================================================
		 * CONTENIDO CENTRAL
		 * =====================================================
		 *
		 * BANNER
		 * ↓
		 * BOTONES DEL PROFESIONAL
		 */
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
						60,
						25,
						60
				)
		);

		/*
		 * =====================================================
		 * BANNER DEL PROFESIONAL
		 * =====================================================
		 */
		JLabel imagenProfesional =
				new JLabel(
						"",
						SwingConstants.CENTER
				);

		ImageIcon iconoProfesional =
				cargarIconoEscalado(
						"/resources/imagenes/menu_profesional.png",

						/*
						 * =====================================
						 * TAMAÑO DEL BANNER
						 * =====================================
						 *
						 * PRIMER NUMERO  = ANCHO
						 * SEGUNDO NUMERO = ALTO
						 *
						 * Podes modificar estos dos valores
						 * para ajustarlo a tu gusto.
						 */
						620,
						190
				);

		if (iconoProfesional != null) {

			imagenProfesional.setIcon(
					iconoProfesional
			);
		}

		imagenProfesional.setAlignmentX(
				CENTER_ALIGNMENT
		);

		imagenProfesional.setHorizontalAlignment(
				SwingConstants.CENTER
		);

		imagenProfesional.setPreferredSize(
				new Dimension(
						620,
						190
				)
		);

		imagenProfesional.setMaximumSize(
				new Dimension(
						620,
						190
				)
		);

		panelContenido.add(
				imagenProfesional
		);

		/*
		 * Espacio entre banner y botones.
		 */
		panelContenido.add(
				Box.createVerticalStrut(25)
		);

		/*
		 * =====================================================
		 * BOTONES DEL PROFESIONAL
		 * =====================================================
		 *
		 * Se mantienen exactamente en 3 filas x 2 columnas.
		 */
		JPanel panelOpciones =
				new JPanel(
						new GridLayout(
								3,
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
						240
				)
		);

		panelOpciones.setMaximumSize(
				new Dimension(
						620,
						240
				)
		);

		JButton botonDisponibilidad =
				new JButton(
						"Disponibilidad"
				);

		JButton botonAgenda =
				new JButton(
						"Agenda de turnos"
				);

		JButton botonRetraso =
				new JButton(
						"Registrar retraso estimado"
				);

		JButton botonConsulta =
				new JButton(
						"Registrar consulta realizada"
				);

		JButton botonHistorial =
				new JButton(
						"Historial de consultas"
				);

		JButton botonCerrarSesion =
				new JButton(
						"Cerrar sesion"
				);

		panelOpciones.add(
				botonDisponibilidad
		);

		panelOpciones.add(
				botonAgenda
		);

		panelOpciones.add(
				botonRetraso
		);

		panelOpciones.add(
				botonConsulta
		);

		panelOpciones.add(
				botonHistorial
		);

		panelOpciones.add(
				botonCerrarSesion
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
		 * EVENTOS
		 * =====================================================
		 */
		botonDisponibilidad.addActionListener(
				e -> abrirDisponibilidad()
		);

		botonAgenda.addActionListener(
				e -> abrirAgenda()
		);

		botonRetraso.addActionListener(
				e -> abrirRetraso()
		);

		botonConsulta.addActionListener(
				e -> abrirRegistrarConsulta()
		);

		botonHistorial.addActionListener(
				e -> abrirHistorial()
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

	private void abrirDisponibilidad() {

		setVisible(false);

		DisponibilidadFrame disponibilidadFrame =
				new DisponibilidadFrame(
						this,
						usuario
				);

		disponibilidadFrame.setVisible(
				true
		);
	}

	private void abrirAgenda() {

		setVisible(false);

		AgendaProfesionalFrame agendaFrame =
				new AgendaProfesionalFrame(
						this,
						usuario
				);

		agendaFrame.setVisible(
				true
		);
	}

	private void abrirRetraso() {

		setVisible(false);

		RetrasoProfesionalFrame retrasoFrame =
				new RetrasoProfesionalFrame(
						this,
						usuario
				);

		retrasoFrame.setVisible(
				true
		);
	}

	private void abrirRegistrarConsulta() {

		setVisible(false);

		RegistrarConsultaFrame consultaFrame =
				new RegistrarConsultaFrame(
						this,
						usuario
				);

		consultaFrame.setVisible(
				true
		);
	}

	private void abrirHistorial() {

		setVisible(false);

		HistorialProfesionalFrame historialFrame =
				new HistorialProfesionalFrame(
						this,
						usuario
				);

		historialFrame.setVisible(
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