package BLL;

public class Usuario {

	private int idUsuario;
	private String nombre;
	private String apellido;
	private String nombreUsuario;
	private String clave;
	private TipoUsuario tipoUsuario;

	public Usuario(
			int idUsuario,
			String nombre,
			String apellido,
			String nombreUsuario,
			String clave,
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
}