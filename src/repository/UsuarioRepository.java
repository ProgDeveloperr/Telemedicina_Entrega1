package repository;

import BLL.Usuario;

public interface UsuarioRepository {

	Usuario login(String nombreUsuario, String clave);

	boolean existeNombreUsuario(String nombreUsuario);

}