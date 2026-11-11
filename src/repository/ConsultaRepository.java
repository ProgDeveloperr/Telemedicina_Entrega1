package repository;

import java.util.LinkedList;

import BLL.Consulta;

public interface ConsultaRepository {

	boolean registrar(Consulta consulta);

	boolean existePorTurno(int idTurno);

	LinkedList<Consulta> obtenerPorProfesional(
			int idProfesional);

	LinkedList<Consulta> obtenerPorPaciente(
			int idPaciente);
}