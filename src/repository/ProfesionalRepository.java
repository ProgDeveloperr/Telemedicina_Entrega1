package repository;

import java.util.LinkedList;

import BLL.Profesional;
import BLL.Usuario;

public interface ProfesionalRepository {

	Profesional obtenerPorUsuario(int idUsuario);

	boolean registrar(Usuario usuario, Profesional profesional);

	boolean existeMatricula(String matricula);

	Profesional buscarPorId(int idProfesional);

	LinkedList<Profesional> buscar(String filtro);

	Usuario obtenerUsuarioPorProfesional(int idProfesional);

}