public interface Menu {

	// Prototipo visto en clase
	void MenuPrincipal();

	// Metodo estatico: permite buscar un usuario sin instanciar la interfaz
	static Usuario Login(String nombreUsuario, String clave) {

		for (Usuario usuario : Usuario.getUsuarios()) {
			if (usuario.autenticar(nombreUsuario, clave)) {
				return usuario;
			}
		}

		return null;
	}
}
