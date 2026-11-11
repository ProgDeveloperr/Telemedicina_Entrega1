package repository;

import java.util.LinkedList;
import java.time.LocalTime;
import BLL.EstadoTurno;
import BLL.Turno;

public interface TurnoRepository {

	boolean registrar(Turno turno);

	Turno buscarPorId(int idTurno);

	LinkedList<Turno> obtenerTodos();

	LinkedList<Turno> obtenerPorProfesional(int idProfesional);

	boolean modificar(Turno turno);

	boolean actualizarEstado(int idTurno, EstadoTurno estado);
	
	boolean horarioOcupado(int idDisponibilidad, LocalTime hora);
	
	boolean horarioOcupadoExceptoTurno(
			int idDisponibilidad,
			LocalTime hora,
			int idTurno);

}