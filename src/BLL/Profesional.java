package BLL;
public class Profesional {

	private int idProfesional;
	private String matricula;
	private String especialidad;

	public Profesional(int idProfesional, String matricula, String especialidad) {
		this.idProfesional = idProfesional;
		this.matricula = matricula;
		this.especialidad = especialidad;
	}

	public int getIdProfesional() {
		return idProfesional;
	}

	public String getMatricula() {
		return matricula;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	// Metodo preparado segun el diagrama de clases. Sin logica de negocio en Entrega 1.
	public Profesional obtenerDatos() {
		return this;
	}
}
