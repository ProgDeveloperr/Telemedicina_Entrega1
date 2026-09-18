package repository;

import java.util.LinkedList;

import BLL.Disponibilidad;

public interface DisponibilidadRepository {

	boolean registrar(Disponibilidad disponibilidad, int idProfesional);

	LinkedList<Disponibilidad> obtenerPorProfesional(int idProfesional);

}