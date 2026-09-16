package BLL;

import javax.swing.JOptionPane;

public class Usuario implements Menu {


	private int idUsuario;
	private String nombre;
	private String apellido;
	private String nombreUsuario;
	private String clave;
	private TipoUsuario tipoUsuario;

	public Usuario(int idUsuario, String nombre, String apellido, String nombreUsuario, String clave,
			TipoUsuario tipoUsuario) {
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.apellido = apellido;
		this.nombreUsuario = nombreUsuario;
		this.clave = clave;
		this.tipoUsuario = tipoUsuario;
	}

	

	// Queda preparado para desarrollar permisos mas adelante.
	public boolean tienePermiso(String permiso) {
		return false;
	}

	

	public int getIdUsuario() {
		return idUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	@Override
	public void MenuPrincipal() {
		if (tipoUsuario == TipoUsuario.EMPLEADO) {
			menuEmpleado();
		} else {
			menuProfesional();
		}
	}

	private void menuEmpleado() {
		String[] opciones = {
				"Pacientes",
				"Profesionales",
				"Turnos",
				"Consultas e informacion",
				"Cerrar sesion"
		};

		int opcion;

		do {
			opcion = JOptionPane.showOptionDialog(
					null,
					"Seleccione una opcion",
					"Telemedicina - Empleado / Recepcionista - " + nombre + " " + apellido,
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					opciones,
					opciones[0]);

			switch (opcion) {
			case 0:
				menuPacientes();
				break;
			case 1:
				menuProfesionales();
				break;
			case 2:
				menuTurnos();
				break;
			case 3:
				menuConsultasEmpleado();
				break;
			}

		} while (opcion != 4 && opcion != JOptionPane.CLOSED_OPTION);
	}

	private void menuProfesional() {
		String[] opciones = {
				"Disponibilidad",
				"Consultar agenda de turnos",
				"Registrar retraso estimado",
				"Registrar consulta realizada",
				"Consultar historial de consultas",
				"Cerrar sesion"
		};

		int opcion;

		do {
			opcion = JOptionPane.showOptionDialog(
					null,
					"Seleccione una opcion",
					"Telemedicina - Medico / Profesional - " + nombre + " " + apellido,
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					opciones,
					opciones[0]);

			if (opcion == 0) {
				menuDisponibilidad();
			} else if (opcion >= 1 && opcion <= 4) {
				mostrarPendiente(opciones[opcion]);
			}

		} while (opcion != 5 && opcion != JOptionPane.CLOSED_OPTION);
	}

	private void menuPacientes() {
		String[] opciones = {
				"Registrar paciente",
				"Consultar pacientes",
				"Modificar datos de paciente",
				"Volver"
		};

		menuSinFunciones("Pacientes", opciones);
	}

	private void menuProfesionales() {
		String[] opciones = {
				"Registrar profesional",
				"Consultar profesionales",
				"Consultar disponibilidad de profesionales",
				"Volver"
		};

		menuSinFunciones("Profesionales", opciones);
	}

	private void menuTurnos() {
		String[] opciones = {
				"Asignar turno",
				"Consultar turnos",
				"Modificar turno",
				"Cancelar turno",
				"Registrar confirmacion de turno",
				"Registrar llegada del paciente",
				"Volver"
		};

		menuSinFunciones("Turnos", opciones);
	}

	private void menuConsultasEmpleado() {
		String[] opciones = {
				"Consultar retraso registrado",
				"Consultar historial de consultas",
				"Volver"
		};

		menuSinFunciones("Consultas e informacion", opciones);
	}

	private void menuDisponibilidad() {
		String[] opciones = {
				"Establecer disponibilidad",
				"Modificar disponibilidad",
				"Volver"
		};

		menuSinFunciones("Disponibilidad", opciones);
	}

	private void menuSinFunciones(String titulo, String[] opciones) {
		int opcion;

		do {
			opcion = JOptionPane.showOptionDialog(
					null,
					"Seleccione una opcion",
					"Telemedicina - " + titulo,
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					opciones,
					opciones[0]);

			if (opcion >= 0 && opcion < opciones.length - 1) {
				mostrarPendiente(opciones[opcion]);
			}

		} while (opcion != opciones.length - 1 && opcion != JOptionPane.CLOSED_OPTION);
	}

	private void mostrarPendiente(String opcion) {
		JOptionPane.showMessageDialog(
				null,
				opcion + "\n\nFuncionalidad pendiente de implementacion.",
				"Maqueta - Entrega 1",
				JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public String toString() {
		return nombre + " " + apellido + " - " + tipoUsuario;
	}
}
