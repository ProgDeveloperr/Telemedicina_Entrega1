package repository;

import BLL.Profesional;
import BLL.Usuario;

public interface ProfesionalRepository {

	Profesional obtenerPorUsuario(int idUsuario);

	boolean registrar(Usuario usuario, Profesional profesional);

}