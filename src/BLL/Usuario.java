package BLL;

import java.util.LinkedList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import DLL.ControllerPaciente;
import DLL.ControllerProfesional;
import DLL.ControllerUsuario;
import DLL.ControllerDisponibilidad;
import DLL.ControllerTurno;
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

	public String getClave() {
		return clave;
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
				modificarPaciente();
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
	
	
	private void modificarPaciente() {

		String entrada = JOptionPane.showInputDialog(
				null,
				"Ingrese el ID del paciente que desea modificar:",
				"Telemedicina - Modificar paciente",
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

		Paciente pacienteActual = controllerPaciente.buscarPorId(idPaciente);

		if (pacienteActual == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se encontro un paciente con el ID ingresado.",
					"Paciente no encontrado",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		JTextField campoDni = new JTextField(pacienteActual.getDni());
		JTextField campoNombre = new JTextField(pacienteActual.getNombre());
		JTextField campoApellido = new JTextField(pacienteActual.getApellido());
		JTextField campoTelefono = new JTextField(pacienteActual.getTelefono());

		Object[] campos = {
				"DNI:", campoDni,
				"Nombre:", campoNombre,
				"Apellido:", campoApellido,
				"Telefono:", campoTelefono
		};

		int opcion = JOptionPane.showConfirmDialog(
				null,
				campos,
				"Telemedicina - Modificar paciente",
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

		Paciente pacienteConMismoDni = controllerPaciente.buscarPorDni(dni);

		if (pacienteConMismoDni != null
				&& pacienteConMismoDni.getIdPaciente() != pacienteActual.getIdPaciente()) {

			JOptionPane.showMessageDialog(
					null,
					"Ya existe otro paciente registrado con ese DNI.",
					"DNI existente",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Paciente pacienteModificado = new Paciente(
				pacienteActual.getIdPaciente(),
				dni,
				nombrePaciente,
				apellidoPaciente,
				telefono
		);

		int confirmacion = JOptionPane.showConfirmDialog(
				null,
				"¿Desea guardar los cambios del paciente?",
				"Confirmar modificacion",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
		);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean modificado = controllerPaciente.modificar(pacienteModificado);

		if (modificado) {

			JOptionPane.showMessageDialog(
					null,
					"Datos del paciente actualizados correctamente.",
					"Modificacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

		} else {

			JOptionPane.showMessageDialog(
					null,
					"No se pudieron actualizar los datos del paciente.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	
	private void menuProfesionales() {

		String[] opciones = {
				"Registrar profesional",
				"Consultar profesionales",
				"Consultar disponibilidad de profesionales",
				"Volver"
		};

		int opcion;

		do {

			opcion = JOptionPane.showOptionDialog(
					null,
					"Seleccione una opcion",
					"Telemedicina - Profesionales",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					opciones,
					opciones[0]
			);

			switch (opcion) {

			case 0:
				registrarProfesional();
				break;

			case 1:
				menuConsultarProfesionales();
				break;

			case 2:
				consultarDisponibilidadProfesional();
				break;
			}

		} while (opcion != 3 && opcion != JOptionPane.CLOSED_OPTION);
	}
	
	
	
	private void registrarProfesional() {

		JTextField campoNombre = new JTextField();
		JTextField campoApellido = new JTextField();
		JTextField campoUsuario = new JTextField();
		JPasswordField campoClave = new JPasswordField();
		JTextField campoMatricula = new JTextField();
		JTextField campoEspecialidad = new JTextField();

		Object[] campos = {
				"Nombre:", campoNombre,
				"Apellido:", campoApellido,
				"Usuario:", campoUsuario,
				"Contraseña:", campoClave,
				"Matricula:", campoMatricula,
				"Especialidad:", campoEspecialidad
		};

		int opcion = JOptionPane.showConfirmDialog(
				null,
				campos,
				"Telemedicina - Registrar profesional",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);

		if (opcion != JOptionPane.OK_OPTION) {
			return;
		}

		String nombreProfesional = campoNombre.getText().trim();
		String apellidoProfesional = campoApellido.getText().trim();
		String nombreUsuario = campoUsuario.getText().trim();
		String clave = new String(campoClave.getPassword());
		String matricula = campoMatricula.getText().trim();
		String especialidad = campoEspecialidad.getText().trim();

		if (nombreProfesional.isEmpty()
				|| apellidoProfesional.isEmpty()
				|| nombreUsuario.isEmpty()
				|| clave.isEmpty()
				|| matricula.isEmpty()
				|| especialidad.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"Todos los campos son obligatorios.",
					"Datos incompletos",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerUsuario controllerUsuario = new ControllerUsuario();

		if (controllerUsuario.existeNombreUsuario(nombreUsuario)) {

			JOptionPane.showMessageDialog(
					null,
					"Ya existe un usuario con ese nombre de usuario.",
					"Usuario existente",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerProfesional controllerProfesional =
				new ControllerProfesional();

		if (controllerProfesional.existeMatricula(matricula)) {

			JOptionPane.showMessageDialog(
					null,
					"Ya existe un profesional con esa matricula.",
					"Matricula existente",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Usuario usuarioNuevo = new Usuario(
				0,
				nombreProfesional,
				apellidoProfesional,
				nombreUsuario,
				clave,
				TipoUsuario.PROFESIONAL
		);

		Profesional profesionalNuevo = new Profesional(
				0,
				matricula,
				especialidad
		);

		int confirmacion = JOptionPane.showConfirmDialog(
				null,
				"¿Desea registrar al profesional?\n\n"
						+ nombreProfesional + " " + apellidoProfesional
						+ "\nUsuario: " + nombreUsuario
						+ "\nMatricula: " + matricula
						+ "\nEspecialidad: " + especialidad,
				"Confirmar registro",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
		);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean registrado = controllerProfesional.registrar(
				usuarioNuevo,
				profesionalNuevo
		);

		if (registrado) {

			JOptionPane.showMessageDialog(
					null,
					"Profesional registrado correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

		} else {

			JOptionPane.showMessageDialog(
					null,
					"No se pudo registrar el profesional.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	private void menuConsultarProfesionales() {

		String[] opciones = {
				"Buscar por ID",
				"Buscar por nombre / apellido / usuario / matricula / especialidad",
				"Volver"
		};

		int opcion;

		do {

			opcion = JOptionPane.showOptionDialog(
					null,
					"Seleccione el tipo de busqueda",
					"Telemedicina - Consultar profesionales",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					opciones,
					opciones[0]
			);

			switch (opcion) {

			case 0:
				consultarProfesionalPorId();
				break;

			case 1:
				buscarProfesionalesPorFiltro();
				break;
			}

		} while (opcion != 2 && opcion != JOptionPane.CLOSED_OPTION);
	}	
	
	
	private void consultarProfesionalPorId() {

		String entrada = JOptionPane.showInputDialog(
				null,
				"Ingrese el ID del profesional:",
				"Telemedicina - Buscar profesional por ID",
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

		int idProfesional;

		try {

			idProfesional = Integer.parseInt(entrada);

			if (idProfesional <= 0) {

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

		ControllerProfesional controllerProfesional =
				new ControllerProfesional();

		Profesional profesional =
				controllerProfesional.buscarPorId(idProfesional);

		if (profesional == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se encontro un profesional con el ID ingresado.",
					"Profesional no encontrado",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		Usuario usuarioProfesional =
				controllerProfesional.obtenerUsuarioPorProfesional(
						profesional.getIdProfesional()
				);

		if (usuarioProfesional == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se pudieron obtener los datos del usuario asociado.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);

			return;
		}

		JOptionPane.showMessageDialog(
				null,
				"ID profesional: " + profesional.getIdProfesional()
						+ "\nNombre: " + usuarioProfesional.getNombre()
						+ "\nApellido: " + usuarioProfesional.getApellido()
						+ "\nUsuario: " + usuarioProfesional.getNombreUsuario()
						+ "\nMatricula: " + profesional.getMatricula()
						+ "\nEspecialidad: " + profesional.getEspecialidad(),
				"Datos del profesional",
				JOptionPane.INFORMATION_MESSAGE
		);
	}
	
	private void buscarProfesionalesPorFiltro() {

		String filtro = JOptionPane.showInputDialog(
				null,
				"Ingrese nombre, apellido, usuario, matricula o especialidad:",
				"Telemedicina - Buscar profesionales",
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

		ControllerProfesional controllerProfesional =
				new ControllerProfesional();

		LinkedList<Profesional> profesionales =
				controllerProfesional.buscar(filtro);

		if (profesionales.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"No se encontraron profesionales para el criterio ingresado.",
					"Sin resultados",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		StringBuilder resultado = new StringBuilder();

		for (Profesional profesional : profesionales) {

			Usuario usuarioProfesional =
					controllerProfesional.obtenerUsuarioPorProfesional(
							profesional.getIdProfesional()
					);

			if (usuarioProfesional != null) {

				resultado.append("ID: ")
						.append(profesional.getIdProfesional())
						.append("\nNombre: ")
						.append(usuarioProfesional.getNombre())
						.append(" ")
						.append(usuarioProfesional.getApellido())
						.append("\nUsuario: ")
						.append(usuarioProfesional.getNombreUsuario())
						.append("\nMatricula: ")
						.append(profesional.getMatricula())
						.append("\nEspecialidad: ")
						.append(profesional.getEspecialidad())
						.append("\n------------------------------\n");
			}
		}

		JOptionPane.showMessageDialog(
				null,
				resultado.toString(),
				"Profesionales encontrados",
				JOptionPane.INFORMATION_MESSAGE
		);
	}
	
	
	
	private void consultarDisponibilidadProfesional() {

		ControllerProfesional controllerProfesional =
				new ControllerProfesional();

		LinkedList<Profesional> profesionales =
				controllerProfesional.buscar("");

		if (profesionales.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"No hay profesionales registrados.",
					"Sin profesionales",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		StringBuilder listadoProfesionales = new StringBuilder();

		for (Profesional profesional : profesionales) {

			Usuario usuarioProfesional =
					controllerProfesional.obtenerUsuarioPorProfesional(
							profesional.getIdProfesional()
					);

			if (usuarioProfesional != null) {

				listadoProfesionales.append("ID: ")
						.append(profesional.getIdProfesional())
						.append(" | ")
						.append(usuarioProfesional.getNombre())
						.append(" ")
						.append(usuarioProfesional.getApellido())
						.append(" | ")
						.append(profesional.getEspecialidad())
						.append("\n");
			}
		}

		String entrada = JOptionPane.showInputDialog(
				null,
				listadoProfesionales.toString()
						+ "\nIngrese el ID del profesional:",
				"Telemedicina - Consultar disponibilidad",
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

		int idProfesional;

		try {

			idProfesional = Integer.parseInt(entrada);

			if (idProfesional <= 0) {

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

		Profesional profesional =
				controllerProfesional.buscarPorId(idProfesional);

		if (profesional == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se encontro un profesional con el ID ingresado.",
					"Profesional no encontrado",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		Usuario usuarioProfesional =
				controllerProfesional.obtenerUsuarioPorProfesional(
						idProfesional
				);

		ControllerDisponibilidad controllerDisponibilidad =
				new ControllerDisponibilidad();

		LinkedList<Disponibilidad> disponibilidades =
				controllerDisponibilidad.obtenerPorProfesional(
						idProfesional
				);

		if (disponibilidades.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"El profesional "
							+ usuarioProfesional.getNombre()
							+ " "
							+ usuarioProfesional.getApellido()
							+ " no tiene disponibilidades registradas.",
					"Sin disponibilidad",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		DateTimeFormatter formatoFecha =
				DateTimeFormatter.ofPattern("dd/MM/yyyy");

		DateTimeFormatter formatoHora =
				DateTimeFormatter.ofPattern("HH:mm");

		StringBuilder resultado = new StringBuilder();

		resultado.append("Profesional: ")
				.append(usuarioProfesional.getNombre())
				.append(" ")
				.append(usuarioProfesional.getApellido())
				.append("\nEspecialidad: ")
				.append(profesional.getEspecialidad())
				.append("\nMatricula: ")
				.append(profesional.getMatricula())
				.append("\n\nDisponibilidad:\n\n");

		for (Disponibilidad disponibilidad : disponibilidades) {

			resultado.append("Fecha: ")
					.append(
							disponibilidad.getFecha()
									.format(formatoFecha)
					)
					.append("\nHorario: ")
					.append(
							disponibilidad.getHoraInicio()
									.format(formatoHora)
					)
					.append(" - ")
					.append(
							disponibilidad.getHoraFin()
									.format(formatoHora)
					)
					.append("\n------------------------------\n");
		}

		JOptionPane.showMessageDialog(
				null,
				resultado.toString(),
				"Disponibilidad del profesional",
				JOptionPane.INFORMATION_MESSAGE
		);
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

		int opcion;

		do {

			opcion = JOptionPane.showOptionDialog(
					null,
					"Seleccione una opcion",
					"Telemedicina - Turnos",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					opciones,
					opciones[0]
			);

			switch (opcion) {

			case 0:
				asignarTurno();
				break;

			case 1:
				consultarTurnos();
				break;

			case 2:
				modificarTurno();
				break;

			case 3:
				cancelarTurno();
				break;

			case 4:
				confirmarTurno();
				break;

			case 5:
				registrarLlegadaPaciente();
				break;
			}

		} while (opcion != 6 && opcion != JOptionPane.CLOSED_OPTION);
	}
	
	
	
	
	private void consultarTurnos() {

		ControllerTurno controllerTurno =
				new ControllerTurno();

		LinkedList<Turno> turnos =
				controllerTurno.obtenerTodos();

		if (turnos.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"No hay turnos registrados.",
					"Sin turnos",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		DateTimeFormatter formatoFecha =
				DateTimeFormatter.ofPattern("dd/MM/yyyy");

		DateTimeFormatter formatoHora =
				DateTimeFormatter.ofPattern("HH:mm");

		StringBuilder resultado =
				new StringBuilder();

		for (Turno turno : turnos) {

			resultado.append("ID turno: ")
					.append(turno.getIdTurno())

					.append("\nPaciente: ")
					.append(turno.getPaciente().getNombre())
					.append(" ")
					.append(turno.getPaciente().getApellido())

					.append("\nDNI: ")
					.append(turno.getPaciente().getDni())

					.append("\nFecha: ")
					.append(
							turno.getFecha()
									.format(formatoFecha)
					)

					.append("\nHora: ")
					.append(
							turno.getHora()
									.format(formatoHora)
					)

					.append("\nEstado: ")
					.append(turno.getEstado())

					.append("\nDisponibilidad ID: ")
					.append(
							turno.getDisponibilidad()
									.getIdDisponibilidad()
					)

					.append("\n------------------------------\n");
		}

		JOptionPane.showMessageDialog(
				null,
				resultado.toString(),
				"Telemedicina - Turnos registrados",
				JOptionPane.INFORMATION_MESSAGE
		);
	}
	
	
	
	private void modificarTurno() {

		ControllerTurno controllerTurno =
				new ControllerTurno();

		LinkedList<Turno> turnos =
				controllerTurno.obtenerTodos();

		if (turnos.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"No hay turnos registrados.",
					"Sin turnos",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		DateTimeFormatter formatoFecha =
				DateTimeFormatter.ofPattern("dd/MM/yyyy");

		DateTimeFormatter formatoHora =
				DateTimeFormatter.ofPattern("HH:mm");

		StringBuilder listadoTurnos =
				new StringBuilder();

		for (Turno turno : turnos) {

			listadoTurnos
					.append("ID: ")
					.append(turno.getIdTurno())
					.append(" | ")
					.append(turno.getPaciente().getNombre())
					.append(" ")
					.append(turno.getPaciente().getApellido())
					.append(" | ")
					.append(turno.getFecha().format(formatoFecha))
					.append(" ")
					.append(turno.getHora().format(formatoHora))
					.append(" | ")
					.append(turno.getEstado())
					.append("\n");
		}

		String entradaTurno =
				JOptionPane.showInputDialog(
						null,
						listadoTurnos.toString()
								+ "\nIngrese el ID del turno que desea modificar:",
						"Telemedicina - Modificar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaTurno == null) {
			return;
		}

		entradaTurno = entradaTurno.trim();

		int idTurno;

		try {

			idTurno = Integer.parseInt(entradaTurno);

			if (idTurno <= 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					null,
					"El ID del turno debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Turno turnoActual =
				controllerTurno.buscarPorId(idTurno);

		if (turnoActual == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se encontro el turno seleccionado.",
					"Turno no encontrado",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		if (turnoActual.estaCancelado()
				|| turnoActual.estaPresente()) {

			JOptionPane.showMessageDialog(
					null,
					"No se puede modificar un turno cancelado o con llegada registrada.",
					"Turno no modificable",
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

		for (Profesional profesional : profesionales) {

			Usuario usuarioProfesional =
					controllerProfesional
							.obtenerUsuarioPorProfesional(
									profesional.getIdProfesional()
							);

			if (usuarioProfesional != null) {

				listadoProfesionales
						.append("ID: ")
						.append(profesional.getIdProfesional())
						.append(" | ")
						.append(usuarioProfesional.getNombre())
						.append(" ")
						.append(usuarioProfesional.getApellido())
						.append(" | ")
						.append(profesional.getEspecialidad())
						.append("\n");
			}
		}

		String entradaProfesional =
				JOptionPane.showInputDialog(
						null,
						"Turno actual:\n"
								+ turnoActual.getPaciente().getNombre()
								+ " "
								+ turnoActual.getPaciente().getApellido()
								+ "\n"
								+ turnoActual.getFecha().format(formatoFecha)
								+ " "
								+ turnoActual.getHora().format(formatoHora)
								+ "\n\n"
								+ listadoProfesionales.toString()
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
					null,
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
					null,
					"No se encontro el profesional seleccionado.",
					"Profesional no encontrado",
					JOptionPane.INFORMATION_MESSAGE
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

		for (Disponibilidad disponibilidad : disponibilidades) {

			if (!disponibilidad.getFecha()
					.isBefore(LocalDate.now())) {

				listadoDisponibilidades
						.append("ID: ")
						.append(
								disponibilidad.getIdDisponibilidad()
						)
						.append(" | ")
						.append(
								disponibilidad.getFecha()
										.format(formatoFecha)
						)
						.append(" | ")
						.append(
								disponibilidad.getHoraInicio()
										.format(formatoHora)
						)
						.append(" - ")
						.append(
								disponibilidad.getHoraFin()
										.format(formatoHora)
						)
						.append("\n");
			}
		}

		if (listadoDisponibilidades.length() == 0) {

			JOptionPane.showMessageDialog(
					null,
					"El profesional no tiene disponibilidades futuras.",
					"Sin disponibilidad",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		String entradaDisponibilidad =
				JOptionPane.showInputDialog(
						null,
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
					null,
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
					null,
					"La disponibilidad seleccionada no es valida.",
					"Disponibilidad invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		String horaTexto =
				JOptionPane.showInputDialog(
						null,
						"Disponibilidad:\n"
								+ disponibilidad.getFecha()
										.format(formatoFecha)
								+ "\n"
								+ disponibilidad.getHoraInicio()
										.format(formatoHora)
								+ " - "
								+ disponibilidad.getHoraFin()
										.format(formatoHora)
								+ "\n\nIngrese la nueva hora (HH:mm):",
						"Telemedicina - Modificar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (horaTexto == null) {
			return;
		}

		LocalTime nuevaHora;

		try {

			nuevaHora = LocalTime.parse(
					horaTexto.trim(),
					formatoHora
			);

		} catch (DateTimeParseException e) {

			JOptionPane.showMessageDialog(
					null,
					"La hora debe tener formato HH:mm.",
					"Hora invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!disponibilidad.esHorarioValido(
				nuevaHora)) {

			JOptionPane.showMessageDialog(
					null,
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
						turnoActual.getIdTurno()
				)) {

			JOptionPane.showMessageDialog(
					null,
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
						null,
						"¿Desea guardar los cambios?\n\n"
								+ "Paciente: "
								+ turnoActual.getPaciente().getNombre()
								+ " "
								+ turnoActual.getPaciente().getApellido()
								+ "\nFecha: "
								+ disponibilidad.getFecha()
										.format(formatoFecha)
								+ "\nHora: "
								+ nuevaHora.format(formatoHora)
								+ "\nEstado: "
								+ turnoActual.getEstado(),
						"Confirmar modificacion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean modificado =
				controllerTurno.modificar(
						turnoModificado
				);

		if (modificado) {

			JOptionPane.showMessageDialog(
					null,
					"Turno modificado correctamente.",
					"Modificacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

		} else {

			JOptionPane.showMessageDialog(
					null,
					"No se pudo modificar el turno.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	
	
	private void cancelarTurno() {

		ControllerTurno controllerTurno =
				new ControllerTurno();

		LinkedList<Turno> turnos =
				controllerTurno.obtenerTodos();

		if (turnos.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"No hay turnos registrados.",
					"Sin turnos",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		DateTimeFormatter formatoFecha =
				DateTimeFormatter.ofPattern("dd/MM/yyyy");

		DateTimeFormatter formatoHora =
				DateTimeFormatter.ofPattern("HH:mm");

		StringBuilder listado =
				new StringBuilder();

		int cantidadCancelables = 0;

		for (Turno turno : turnos) {

			if (!turno.estaCancelado()
					&& !turno.estaPresente()) {

				listado.append("ID: ")
						.append(turno.getIdTurno())
						.append(" | ")
						.append(turno.getPaciente().getNombre())
						.append(" ")
						.append(turno.getPaciente().getApellido())
						.append(" | ")
						.append(turno.getFecha().format(formatoFecha))
						.append(" ")
						.append(turno.getHora().format(formatoHora))
						.append(" | ")
						.append(turno.getEstado())
						.append("\n");

				cantidadCancelables++;
			}
		}

		if (cantidadCancelables == 0) {

			JOptionPane.showMessageDialog(
					null,
					"No hay turnos disponibles para cancelar.",
					"Sin turnos cancelables",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		String entrada =
				JOptionPane.showInputDialog(
						null,
						listado.toString()
								+ "\nIngrese el ID del turno que desea cancelar:",
						"Telemedicina - Cancelar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entrada == null) {
			return;
		}

		int idTurno;

		try {

			idTurno = Integer.parseInt(
					entrada.trim()
			);

			if (idTurno <= 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					null,
					"El ID del turno debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Turno turno =
				controllerTurno.buscarPorId(idTurno);

		if (turno == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se encontro el turno seleccionado.",
					"Turno no encontrado",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		if (turno.estaCancelado()) {

			JOptionPane.showMessageDialog(
					null,
					"El turno ya se encuentra cancelado.",
					"Turno cancelado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (turno.estaPresente()) {

			JOptionPane.showMessageDialog(
					null,
					"No se puede cancelar un turno con llegada registrada.",
					"Turno no cancelable",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int confirmacion =
				JOptionPane.showConfirmDialog(
						null,
						"¿Desea cancelar este turno?\n\n"
								+ "Paciente: "
								+ turno.getPaciente().getNombre()
								+ " "
								+ turno.getPaciente().getApellido()
								+ "\nFecha: "
								+ turno.getFecha().format(formatoFecha)
								+ "\nHora: "
								+ turno.getHora().format(formatoHora)
								+ "\nEstado actual: "
								+ turno.getEstado(),
						"Confirmar cancelacion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE
				);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean cancelado =
				controllerTurno.actualizarEstado(
						turno.getIdTurno(),
						EstadoTurno.CANCELADO
				);

		if (cancelado) {

			JOptionPane.showMessageDialog(
					null,
					"Turno cancelado correctamente.",
					"Cancelacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

		} else {

			JOptionPane.showMessageDialog(
					null,
					"No se pudo cancelar el turno.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	

	private void confirmarTurno() {

		ControllerTurno controllerTurno =
				new ControllerTurno();

		LinkedList<Turno> turnos =
				controllerTurno.obtenerTodos();

		if (turnos.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"No hay turnos registrados.",
					"Sin turnos",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		DateTimeFormatter formatoFecha =
				DateTimeFormatter.ofPattern("dd/MM/yyyy");

		DateTimeFormatter formatoHora =
				DateTimeFormatter.ofPattern("HH:mm");

		StringBuilder listado =
				new StringBuilder();

		int cantidadPendientes = 0;

		for (Turno turno : turnos) {

			if (turno.estaPendiente()) {

				listado.append("ID: ")
						.append(turno.getIdTurno())
						.append(" | ")
						.append(turno.getPaciente().getNombre())
						.append(" ")
						.append(turno.getPaciente().getApellido())
						.append(" | ")
						.append(turno.getFecha().format(formatoFecha))
						.append(" ")
						.append(turno.getHora().format(formatoHora))
						.append(" | ")
						.append(turno.getEstado())
						.append("\n");

				cantidadPendientes++;
			}
		}

		if (cantidadPendientes == 0) {

			JOptionPane.showMessageDialog(
					null,
					"No hay turnos pendientes para confirmar.",
					"Sin turnos pendientes",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		String entrada =
				JOptionPane.showInputDialog(
						null,
						listado.toString()
								+ "\nIngrese el ID del turno que desea confirmar:",
						"Telemedicina - Confirmar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entrada == null) {
			return;
		}

		int idTurno;

		try {

			idTurno = Integer.parseInt(
					entrada.trim()
			);

			if (idTurno <= 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					null,
					"El ID del turno debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Turno turno =
				controllerTurno.buscarPorId(idTurno);

		if (turno == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se encontro el turno seleccionado.",
					"Turno no encontrado",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		if (turno.estaConfirmado()) {

			JOptionPane.showMessageDialog(
					null,
					"El turno ya se encuentra confirmado.",
					"Turno confirmado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (turno.estaCancelado()) {

			JOptionPane.showMessageDialog(
					null,
					"No se puede confirmar un turno cancelado.",
					"Turno no confirmable",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (turno.estaPresente()) {

			JOptionPane.showMessageDialog(
					null,
					"El turno ya tiene registrada la llegada del paciente.",
					"Turno no confirmable",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int confirmacion =
				JOptionPane.showConfirmDialog(
						null,
						"¿Desea confirmar este turno?\n\n"
								+ "Paciente: "
								+ turno.getPaciente().getNombre()
								+ " "
								+ turno.getPaciente().getApellido()
								+ "\nFecha: "
								+ turno.getFecha().format(formatoFecha)
								+ "\nHora: "
								+ turno.getHora().format(formatoHora)
								+ "\nEstado actual: "
								+ turno.getEstado(),
						"Confirmar turno",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean confirmado =
				controllerTurno.actualizarEstado(
						turno.getIdTurno(),
						EstadoTurno.CONFIRMADO
				);

		if (confirmado) {

			JOptionPane.showMessageDialog(
					null,
					"Turno confirmado correctamente.",
					"Confirmacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

		} else {

			JOptionPane.showMessageDialog(
					null,
					"No se pudo confirmar el turno.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	
	
	
	
	private void registrarLlegadaPaciente() {

		ControllerTurno controllerTurno =
				new ControllerTurno();

		LinkedList<Turno> turnos =
				controllerTurno.obtenerTodos();

		if (turnos.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"No hay turnos registrados.",
					"Sin turnos",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		DateTimeFormatter formatoFecha =
				DateTimeFormatter.ofPattern("dd/MM/yyyy");

		DateTimeFormatter formatoHora =
				DateTimeFormatter.ofPattern("HH:mm");

		StringBuilder listado =
				new StringBuilder();

		int cantidadConfirmados = 0;

		for (Turno turno : turnos) {

			if (turno.estaConfirmado()) {

				listado.append("ID: ")
						.append(turno.getIdTurno())
						.append(" | ")
						.append(turno.getPaciente().getNombre())
						.append(" ")
						.append(turno.getPaciente().getApellido())
						.append(" | ")
						.append(turno.getFecha().format(formatoFecha))
						.append(" ")
						.append(turno.getHora().format(formatoHora))
						.append(" | ")
						.append(turno.getEstado())
						.append("\n");

				cantidadConfirmados++;
			}
		}

		if (cantidadConfirmados == 0) {

			JOptionPane.showMessageDialog(
					null,
					"No hay turnos confirmados para registrar llegada.",
					"Sin turnos confirmados",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		String entrada =
				JOptionPane.showInputDialog(
						null,
						listado.toString()
								+ "\nIngrese el ID del turno:",
						"Telemedicina - Registrar llegada",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entrada == null) {
			return;
		}

		int idTurno;

		try {

			idTurno = Integer.parseInt(
					entrada.trim()
			);

			if (idTurno <= 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					null,
					"El ID del turno debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Turno turno =
				controllerTurno.buscarPorId(idTurno);

		if (turno == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se encontro el turno seleccionado.",
					"Turno no encontrado",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		if (turno.estaCancelado()) {

			JOptionPane.showMessageDialog(
					null,
					"No se puede registrar llegada en un turno cancelado.",
					"Turno no valido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (turno.estaPendiente()) {

			JOptionPane.showMessageDialog(
					null,
					"El turno debe estar confirmado antes de registrar la llegada.",
					"Turno pendiente",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (turno.estaPresente()) {

			JOptionPane.showMessageDialog(
					null,
					"La llegada del paciente ya se encuentra registrada.",
					"Llegada ya registrada",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		int confirmacion =
				JOptionPane.showConfirmDialog(
						null,
						"¿Desea registrar la llegada del paciente?\n\n"
								+ "Paciente: "
								+ turno.getPaciente().getNombre()
								+ " "
								+ turno.getPaciente().getApellido()
								+ "\nFecha: "
								+ turno.getFecha().format(formatoFecha)
								+ "\nHora: "
								+ turno.getHora().format(formatoHora),
						"Confirmar llegada",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean llegadaRegistrada =
				controllerTurno.actualizarEstado(
						turno.getIdTurno(),
						EstadoTurno.PRESENTE
				);

		if (llegadaRegistrada) {

			JOptionPane.showMessageDialog(
					null,
					"Llegada del paciente registrada correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

		} else {

			JOptionPane.showMessageDialog(
					null,
					"No se pudo registrar la llegada del paciente.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	
	

	
	
	
	private void asignarTurno() {

		ControllerPaciente controllerPaciente =
				new ControllerPaciente();

		LinkedList<Paciente> pacientes =
				controllerPaciente.buscar("");

		if (pacientes.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"No hay pacientes registrados.",
					"Sin pacientes",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		StringBuilder listadoPacientes =
				new StringBuilder();

		for (Paciente paciente : pacientes) {

			listadoPacientes.append("ID: ")
					.append(paciente.getIdPaciente())
					.append(" | ")
					.append(paciente.getNombre())
					.append(" ")
					.append(paciente.getApellido())
					.append(" | DNI: ")
					.append(paciente.getDni())
					.append("\n");
		}

		String entradaPaciente =
				JOptionPane.showInputDialog(
						null,
						listadoPacientes.toString()
								+ "\nIngrese el ID del paciente:",
						"Telemedicina - Asignar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaPaciente == null) {
			return;
		}

		entradaPaciente = entradaPaciente.trim();

		int idPaciente;

		try {

			idPaciente =
					Integer.parseInt(entradaPaciente);

			if (idPaciente <= 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					null,
					"El ID del paciente debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Paciente paciente =
				controllerPaciente.buscarPorId(idPaciente);

		if (paciente == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se encontro el paciente seleccionado.",
					"Paciente no encontrado",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		ControllerProfesional controllerProfesional =
				new ControllerProfesional();

		LinkedList<Profesional> profesionales =
				controllerProfesional.buscar("");

		if (profesionales.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
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
									profesional.getIdProfesional()
							);

			if (usuarioProfesional != null) {

				listadoProfesionales
						.append("ID: ")
						.append(profesional.getIdProfesional())
						.append(" | ")
						.append(usuarioProfesional.getNombre())
						.append(" ")
						.append(usuarioProfesional.getApellido())
						.append(" | ")
						.append(profesional.getEspecialidad())
						.append("\n");
			}
		}

		String entradaProfesional =
				JOptionPane.showInputDialog(
						null,
						listadoProfesionales.toString()
								+ "\nIngrese el ID del profesional:",
						"Telemedicina - Asignar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaProfesional == null) {
			return;
		}

		entradaProfesional =
				entradaProfesional.trim();

		int idProfesional;

		try {

			idProfesional =
					Integer.parseInt(
							entradaProfesional
					);

			if (idProfesional <= 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					null,
					"El ID del profesional debe ser un numero entero mayor que cero.",
					"ID invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Profesional profesional =
				controllerProfesional
						.buscarPorId(idProfesional);

		if (profesional == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se encontro el profesional seleccionado.",
					"Profesional no encontrado",
					JOptionPane.INFORMATION_MESSAGE
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

		if (disponibilidades.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"El profesional seleccionado no tiene disponibilidades registradas.",
					"Sin disponibilidad",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		DateTimeFormatter formatoFecha =
				DateTimeFormatter.ofPattern(
						"dd/MM/yyyy"
				);

		DateTimeFormatter formatoHora =
				DateTimeFormatter.ofPattern(
						"HH:mm"
				);

		StringBuilder listadoDisponibilidades =
				new StringBuilder();

		for (Disponibilidad disponibilidad : disponibilidades) {

			if (!disponibilidad.getFecha()
					.isBefore(LocalDate.now())) {

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
										.format(formatoFecha)
						)
						.append(" | ")
						.append(
								disponibilidad
										.getHoraInicio()
										.format(formatoHora)
						)
						.append(" - ")
						.append(
								disponibilidad
										.getHoraFin()
										.format(formatoHora)
						)
						.append("\n");
			}
		}

		if (listadoDisponibilidades.length() == 0) {

			JOptionPane.showMessageDialog(
					null,
					"El profesional no tiene disponibilidades futuras.",
					"Sin disponibilidad",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		String entradaDisponibilidad =
				JOptionPane.showInputDialog(
						null,
						listadoDisponibilidades.toString()
								+ "\nIngrese el ID de la disponibilidad:",
						"Telemedicina - Asignar turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (entradaDisponibilidad == null) {
			return;
		}

		entradaDisponibilidad =
				entradaDisponibilidad.trim();

		int idDisponibilidad;

		try {

			idDisponibilidad =
					Integer.parseInt(
							entradaDisponibilidad
					);

			if (idDisponibilidad <= 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(
					null,
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
				|| disponibilidad.getFecha()
						.isBefore(LocalDate.now())) {

			JOptionPane.showMessageDialog(
					null,
					"La disponibilidad seleccionada no es valida.",
					"Disponibilidad invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		String horaTexto =
				JOptionPane.showInputDialog(
						null,
						"Disponibilidad seleccionada:\n"
								+ disponibilidad
										.getFecha()
										.format(formatoFecha)
								+ "\n"
								+ disponibilidad
										.getHoraInicio()
										.format(formatoHora)
								+ " - "
								+ disponibilidad
										.getHoraFin()
										.format(formatoHora)
								+ "\n\nIngrese la hora del turno (HH:mm):",
						"Telemedicina - Horario del turno",
						JOptionPane.QUESTION_MESSAGE
				);

		if (horaTexto == null) {
			return;
		}

		horaTexto = horaTexto.trim();

		LocalTime hora;

		try {

			hora = LocalTime.parse(
					horaTexto,
					formatoHora
			);

		} catch (DateTimeParseException e) {

			JOptionPane.showMessageDialog(
					null,
					"La hora debe tener formato HH:mm.",
					"Hora invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!disponibilidad.esHorarioValido(hora)) {

			JOptionPane.showMessageDialog(
					null,
					"El horario ingresado se encuentra fuera de la disponibilidad del profesional.",
					"Horario invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		ControllerTurno controllerTurno =
				new ControllerTurno();

		if (controllerTurno.horarioOcupado(
				idDisponibilidad,
				hora)) {

			JOptionPane.showMessageDialog(
					null,
					"El horario seleccionado ya se encuentra ocupado.",
					"Horario ocupado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Turno turnoNuevo = new Turno(
				0,
				disponibilidad.getFecha(),
				hora,
				EstadoTurno.PENDIENTE,
				paciente,
				disponibilidad
		);

		int confirmacion =
				JOptionPane.showConfirmDialog(
						null,
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
										.format(formatoFecha)
								+ "\nHora: "
								+ hora.format(formatoHora),
						"Confirmar turno",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean registrado =
				controllerTurno.registrar(
						turnoNuevo
				);

		if (registrado) {

			JOptionPane.showMessageDialog(
					null,
					"Turno asignado correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

		} else {

			JOptionPane.showMessageDialog(
					null,
					"No se pudo asignar el turno.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
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

		int opcion;

		do {

			opcion = JOptionPane.showOptionDialog(
					null,
					"Seleccione una opcion",
					"Telemedicina - Disponibilidad",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					opciones,
					opciones[0]
			);

			switch (opcion) {

			case 0:
				establecerDisponibilidad();
				break;

			case 1:
				modificarDisponibilidad();
				break;
			}

		} while (opcion != 2 && opcion != JOptionPane.CLOSED_OPTION);
	}
	
	
	private void establecerDisponibilidad() {

		ControllerProfesional controllerProfesional =
				new ControllerProfesional();

		Profesional profesional =
				controllerProfesional.obtenerPorUsuario(idUsuario);

		if (profesional == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se pudieron obtener los datos del profesional.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);

			return;
		}

		JTextField campoFecha = new JTextField();
		JTextField campoHoraInicio = new JTextField();
		JTextField campoHoraFin = new JTextField();

		Object[] campos = {
				"Fecha (dd/MM/yyyy):", campoFecha,
				"Hora inicio (HH:mm):", campoHoraInicio,
				"Hora fin (HH:mm):", campoHoraFin
		};

		int opcion = JOptionPane.showConfirmDialog(
				null,
				campos,
				"Telemedicina - Establecer disponibilidad",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);

		if (opcion != JOptionPane.OK_OPTION) {
			return;
		}

		String fechaTexto = campoFecha.getText().trim();
		String horaInicioTexto = campoHoraInicio.getText().trim();
		String horaFinTexto = campoHoraFin.getText().trim();

		if (fechaTexto.isEmpty()
				|| horaInicioTexto.isEmpty()
				|| horaFinTexto.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"Todos los campos son obligatorios.",
					"Datos incompletos",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		DateTimeFormatter formatoFecha =
				DateTimeFormatter.ofPattern("dd/MM/yyyy");

		DateTimeFormatter formatoHora =
				DateTimeFormatter.ofPattern("HH:mm");

		LocalDate fecha;
		LocalTime horaInicio;
		LocalTime horaFin;

		try {

			fecha = LocalDate.parse(fechaTexto, formatoFecha);
			horaInicio = LocalTime.parse(horaInicioTexto, formatoHora);
			horaFin = LocalTime.parse(horaFinTexto, formatoHora);

		} catch (DateTimeParseException e) {

			JOptionPane.showMessageDialog(
					null,
					"Verifique los formatos ingresados.\n\n"
							+ "Fecha: dd/MM/yyyy\n"
							+ "Hora: HH:mm",
					"Fecha u hora invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (fecha.isBefore(LocalDate.now())) {

			JOptionPane.showMessageDialog(
					null,
					"La fecha de disponibilidad no puede ser anterior a hoy.",
					"Fecha invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!horaFin.isAfter(horaInicio)) {

			JOptionPane.showMessageDialog(
					null,
					"La hora de finalizacion debe ser posterior a la hora de inicio.",
					"Horario invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Disponibilidad disponibilidad = new Disponibilidad(
				0,
				fecha,
				horaInicio,
				horaFin,
				0
		);

		int confirmacion = JOptionPane.showConfirmDialog(
				null,
				"¿Desea registrar esta disponibilidad?\n\n"
						+ "Fecha: " + fechaTexto
						+ "\nHorario: " + horaInicioTexto
						+ " - " + horaFinTexto,
				"Confirmar disponibilidad",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
		);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		ControllerDisponibilidad controllerDisponibilidad =
				new ControllerDisponibilidad();

		boolean registrada = controllerDisponibilidad.registrar(
				disponibilidad,
				profesional.getIdProfesional()
		);

		if (registrada) {

			JOptionPane.showMessageDialog(
					null,
					"Disponibilidad registrada correctamente.",
					"Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE
			);

		} else {

			JOptionPane.showMessageDialog(
					null,
					"No se pudo registrar la disponibilidad.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	
	private void modificarDisponibilidad() {

		ControllerProfesional controllerProfesional =
				new ControllerProfesional();

		Profesional profesional =
				controllerProfesional.obtenerPorUsuario(idUsuario);

		if (profesional == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se pudieron obtener los datos del profesional.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);

			return;
		}

		ControllerDisponibilidad controllerDisponibilidad =
				new ControllerDisponibilidad();

		LinkedList<Disponibilidad> disponibilidades =
				controllerDisponibilidad.obtenerPorProfesional(
						profesional.getIdProfesional()
				);

		if (disponibilidades.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
					"No tiene disponibilidades registradas.",
					"Sin disponibilidades",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		DateTimeFormatter formatoFecha =
				DateTimeFormatter.ofPattern("dd/MM/yyyy");

		DateTimeFormatter formatoHora =
				DateTimeFormatter.ofPattern("HH:mm");

		StringBuilder listado = new StringBuilder();

		for (Disponibilidad disponibilidad : disponibilidades) {

			listado.append("ID: ")
					.append(disponibilidad.getIdDisponibilidad())
					.append("\nFecha: ")
					.append(disponibilidad.getFecha().format(formatoFecha))
					.append("\nHorario: ")
					.append(disponibilidad.getHoraInicio().format(formatoHora))
					.append(" - ")
					.append(disponibilidad.getHoraFin().format(formatoHora))
					.append("\n------------------------------\n");
		}

		String entrada = JOptionPane.showInputDialog(
				null,
				listado.toString()
						+ "\nIngrese el ID de la disponibilidad que desea modificar:",
				"Telemedicina - Modificar disponibilidad",
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

		int idDisponibilidad;

		try {

			idDisponibilidad = Integer.parseInt(entrada);

			if (idDisponibilidad <= 0) {

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

		Disponibilidad disponibilidadActual =
				controllerDisponibilidad.buscarPorId(
						idDisponibilidad,
						profesional.getIdProfesional()
				);

		if (disponibilidadActual == null) {

			JOptionPane.showMessageDialog(
					null,
					"No se encontro una disponibilidad propia con el ID ingresado.",
					"Disponibilidad no encontrada",
					JOptionPane.INFORMATION_MESSAGE
			);

			return;
		}

		JTextField campoFecha = new JTextField(
				disponibilidadActual.getFecha().format(formatoFecha)
		);

		JTextField campoHoraInicio = new JTextField(
				disponibilidadActual.getHoraInicio().format(formatoHora)
		);

		JTextField campoHoraFin = new JTextField(
				disponibilidadActual.getHoraFin().format(formatoHora)
		);

		Object[] campos = {
				"Fecha (dd/MM/yyyy):", campoFecha,
				"Hora inicio (HH:mm):", campoHoraInicio,
				"Hora fin (HH:mm):", campoHoraFin
		};

		int opcion = JOptionPane.showConfirmDialog(
				null,
				campos,
				"Telemedicina - Modificar disponibilidad",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);

		if (opcion != JOptionPane.OK_OPTION) {
			return;
		}

		String fechaTexto = campoFecha.getText().trim();
		String horaInicioTexto = campoHoraInicio.getText().trim();
		String horaFinTexto = campoHoraFin.getText().trim();

		if (fechaTexto.isEmpty()
				|| horaInicioTexto.isEmpty()
				|| horaFinTexto.isEmpty()) {

			JOptionPane.showMessageDialog(
					null,
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

			fecha = LocalDate.parse(fechaTexto, formatoFecha);
			horaInicio = LocalTime.parse(horaInicioTexto, formatoHora);
			horaFin = LocalTime.parse(horaFinTexto, formatoHora);

		} catch (DateTimeParseException e) {

			JOptionPane.showMessageDialog(
					null,
					"Verifique los formatos ingresados.\n\n"
							+ "Fecha: dd/MM/yyyy\n"
							+ "Hora: HH:mm",
					"Fecha u hora invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (fecha.isBefore(LocalDate.now())) {

			JOptionPane.showMessageDialog(
					null,
					"La fecha de disponibilidad no puede ser anterior a hoy.",
					"Fecha invalida",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!horaFin.isAfter(horaInicio)) {

			JOptionPane.showMessageDialog(
					null,
					"La hora de finalizacion debe ser posterior a la hora de inicio.",
					"Horario invalido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Disponibilidad disponibilidadModificada =
				new Disponibilidad(
						disponibilidadActual.getIdDisponibilidad(),
						fecha,
						horaInicio,
						horaFin,
						disponibilidadActual.getRetrasoEstimado()
				);

		int confirmacion = JOptionPane.showConfirmDialog(
				null,
				"¿Desea guardar los cambios?\n\n"
						+ "Fecha: " + fechaTexto
						+ "\nHorario: " + horaInicioTexto
						+ " - " + horaFinTexto,
				"Confirmar modificacion",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
		);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean modificada = controllerDisponibilidad.modificar(
				disponibilidadModificada,
				profesional.getIdProfesional()
		);

		if (modificada) {

			JOptionPane.showMessageDialog(
					null,
					"Disponibilidad actualizada correctamente.",
					"Modificacion exitosa",
					JOptionPane.INFORMATION_MESSAGE
			);

		} else {

			JOptionPane.showMessageDialog(
					null,
					"No se pudo actualizar la disponibilidad.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
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
