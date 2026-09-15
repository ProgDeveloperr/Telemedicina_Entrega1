public class Paciente {

	private int idPaciente;
	private String dni;
	private String nombre;
	private String apellido;
	private String telefono;

	public Paciente(int idPaciente, String dni, String nombre, String apellido, String telefono) {
		this.idPaciente = idPaciente;
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
	}

	public int getIdPaciente() {
		return idPaciente;
	}

	public String getDni() {
		return dni;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	// Metodo preparado segun el diagrama de clases. Sin logica de negocio en Entrega 1.
	public Paciente obtenerDatos() {
		return this;
	}
}
