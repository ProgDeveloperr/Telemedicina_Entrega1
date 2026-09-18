package repository;

import java.util.LinkedList;

import BLL.Disponibilidad;

public interface DisponibilidadRepository {

	boolean registrar(Disponibilidad disponibilidad, int idProfesional);

	LinkedList<Disponibilidad> obtenerPorProfesional(int idProfesional);

	Disponibilidad buscarPorId(int idDisponibilidad, int idProfesional);

	boolean modificar(Disponibilidad disponibilidad, int idProfesional);

}