package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
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

		setSize(750, 500);

		setLocationRelativeTo(null);

		setResizable(false);
	}

	private void inicializarComponentes() {

		setLayout(
				new BorderLayout()
		);

		JPanel panelSuperior =
				new JPanel(
						new BorderLayout()
				);

		panelSuperior.setBorder(
				BorderFactory.createEmptyBorder(
						25,
						30,
						20,
						30
				)
		);

		JLabel titulo =
				new JLabel(
						"TELEMEDICINA",
						SwingConstants.CENTER
				);

		titulo.setFont(
				new Font(
						"SansSerif",
						Font.BOLD,
						24
				)
		);

		JLabel profesionalLabel =
				new JLabel(
						"Profesional: "
								+ usuario.getNombre()
								+ " "
								+ usuario.getApellido(),
						SwingConstants.CENTER
				);

		profesionalLabel.setFont(
				new Font(
						"SansSerif",
						Font.PLAIN,
						14
				)
		);

		panelSuperior.add(
				titulo,
				BorderLayout.NORTH
		);

		panelSuperior.add(
				profesionalLabel,
				BorderLayout.SOUTH
		);

		add(
				panelSuperior,
				BorderLayout.NORTH
		);

		JPanel panelOpciones =
				new JPanel(
						new GridLayout(
								3,
								2,
								15,
								15
						)
				);

		panelOpciones.setBorder(
				BorderFactory.createEmptyBorder(
						30,
						60,
						30,
						60
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

		add(
				panelOpciones,
				BorderLayout.CENTER
		);

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

		disponibilidadFrame.setVisible(true);
	}
	
	
	
	private void abrirAgenda() {

		setVisible(false);

		AgendaProfesionalFrame agendaFrame =
				new AgendaProfesionalFrame(
						this,
						usuario
				);

		agendaFrame.setVisible(true);
	}
	
	
	
	private void abrirRetraso() {

		setVisible(false);

		RetrasoProfesionalFrame retrasoFrame =
				new RetrasoProfesionalFrame(
						this,
						usuario
				);

		retrasoFrame.setVisible(true);
	}
	
	
	private void abrirRegistrarConsulta() {

		setVisible(false);

		RegistrarConsultaFrame consultaFrame =
				new RegistrarConsultaFrame(
						this,
						usuario
				);

		consultaFrame.setVisible(true);
	}
	
	
	
	private void abrirHistorial() {

		setVisible(false);

		HistorialProfesionalFrame historialFrame =
				new HistorialProfesionalFrame(
						this,
						usuario
				);

		historialFrame.setVisible(true);
	}
	
	
	
	
}