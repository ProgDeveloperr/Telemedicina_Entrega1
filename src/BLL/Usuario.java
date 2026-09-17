package BLL;

import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import DLL.ControllerPaciente;

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

		int opcion;

		do {

			opcion = JOptionPane.showOptionDialog(
					null,
					"Seleccione una opcion",
					"Telemedicina - Pacientes",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					opciones,
					opciones[0]
			);

			switch (opcion) {

			case 0:
				registrarPaciente();
				break;

			case 1:
				menuConsultarPacientes();
				break;

			case 2:
				mostrarPendiente("Modificar datos de paciente");
				break;
			}

		} while (opcion != 3 && opcion != JOptionPane.CLOSED_OPTION);
	}

	private void registrarPaciente() {

		JTextField campoDni = new JTextField();
		JTextField campoNombre = new JTextField();
		JTextField campoApellido = new JTextField();
		JTextField campoTelefono = new JTextField();

		Object[] campos = {
				"DNI:", campoDni,
				"Nombre:", campoNombre,
				"Apellido:", campoApellido,
				"Telefono:", campoTelefono
		};

		int opcion = JOptionPane.showConfirmDialog(
				null,
				campos,
				"Telemedicina - Registrar paciente",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);

		if (opcion != JOptionPane.OK_OPTION) {
			return;
		}

		String dni = campoDni.getText().trim();
		String nombrePaciente = campoNombre.getText().trim();
		String apellidoPaciente = campoApellido.getText().trim();
		String telefono = campoTelefono.getText().trim();

		if (dni.isEmpty()
				|| nombrePaciente.isEmpty()
				|| apellidoPaciente.isEmpty()
				|| telefono.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"Todos los campos son obligatorios.",
					"Datos incompletos",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!dni.matches("\\d+")) {

			JOptionPane.showMessageDialog(
					null,
					"El DNI debe contener solamente numeros.",
					"DNI invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerPaciente controllerPaciente = new ControllerPaciente();

		Paciente existente = controllerPaciente.buscarPorDni(dni);

		if (existente != null) {

			JOptionPane.showMessageDialog(
					null,
					"Ya existe un paciente registrado con ese DNI.",
					"Paciente existente",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Paciente pacienteNuevo = new Paciente(
				0,
				dni,
				nombrePaciente,
				apellidoPaciente,
				telefono
		);

		boolean registrado = controllerPaciente.registrar(pacienteNuevo);

		if (registrado) {

			JOptionPane.showMessageDialog(
					null,
					"Paciente registrado correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

		} else {

			JOptionPane.showMessageDialog(
					null,
					"No se pudo registrar el paciente.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	
	
	private void menuConsultarPacientes() {

		String[] opciones = {
				"Buscar por ID",
				"Buscar por DNI / nombre / apellido",
				"Volver"
		};

		int opcion;

		do {

			opcion = JOptionPane.showOptionDialog(
					null,
					"Seleccione el tipo de busqueda",
					"Telemedicina - Consultar pacientes",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					opciones,
					opciones[0]
			);

			switch (opcion) {

			case 0:
				consultarPacientePorId();
				break;

			case 1:
				buscarPacientesPorFiltro();
				break;
			}

		} while (opcion != 2 && opcion != JOptionPane.CLOSED_OPTION);
	}
	
	
	private void buscarPacientesPorFiltro() {

		String filtro = JOptionPane.showInputDialog(
				null,
				"Ingrese DNI, nombre o apellido:",
				"Telemedicina - Buscar pacientes",
				JOptionPane.QUESTION_MESSAGE
		);

		if (filtro == null) {
			return;
		}

		filtro = filtro.trim();

		if (filtro.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"Debe ingresar un criterio de busqueda.",
					"Dato incompleto",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerPaciente controllerPaciente = new ControllerPaciente();

		LinkedList<Paciente> pacientes = controllerPaciente.buscar(filtro);

		if (pacientes.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"No se encontraron pacientes para el criterio ingresado.",
					"Sin resultados",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		StringBuilder resultado = new StringBuilder();

		for (Paciente paciente : pacientes) {

			resultado.append("ID: ")
					.append(paciente.getIdPaciente())
					.append("\nDNI: ")
					.append(paciente.getDni())
					.append("\nNombre: ")
					.append(paciente.getNombre())
					.append(" ")
					.append(paciente.getApellido())
					.append("\nTelefono: ")
					.append(paciente.getTelefono())
					.append("\n------------------------------\n");
		}

		JOptionPane.showMessageDialog(
				null,
				resultado.toString(),
				"Pacientes encontrados",
				JOptionPane.INFORMATION_MESSAGE
		);
	}
	
	
	
	
	private void consultarPacientePorId() {

		String entrada = JOptionPane.showInputDialog(
				null,
				"Ingrese el ID del paciente:",
				"Telemedicina - Buscar paciente por ID",
				JOptionPane.QUESTION_MESSAGE
		);

		if (entrada == null) {
			return;
		}

		entrada = entrada.trim();

		if (entrada.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"Debe ingresar un ID.",
					"Dato incompleto",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int idPaciente;

		try {

			idPaciente = Integer.parseInt(entrada);

			if (idPaciente <= 0) {

				JOptionPane.showMessageDialog(
						null,
						"El ID debe ser mayor que cero.",
						"ID invalido",
						JOptionPane.WARNING_MESSAGE
				);

				return;
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					null,
					"El ID debe ser un numero entero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerPaciente controllerPaciente = new ControllerPaciente();

		Paciente paciente = controllerPaciente.buscarPorId(idPaciente);

		if (paciente == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se encontro un paciente con el ID ingresado.",
					"Paciente no encontrado",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		JOptionPane.showMessageDialog(
				null,
				"ID: " + paciente.getIdPaciente()
						+ "\nDNI: " + paciente.getDni()
						+ "\nNombre: " + paciente.getNombre()
						+ "\nApellido: " + paciente.getApellido()
						+ "\nTelefono: " + paciente.getTelefono(),
				"Datos del paciente",
				JOptionPane.INFORMATION_MESSAGE
		);
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
