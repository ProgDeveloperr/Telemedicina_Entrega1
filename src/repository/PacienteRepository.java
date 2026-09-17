package repository;

import java.util.LinkedList;

import BLL.Paciente;

public interface PacienteRepository {

	boolean registrar(Paciente paciente);

	Paciente buscarPorId(int idPaciente);

	Paciente buscarPorDni(String dni);
	
	LinkedList<Paciente> buscar(String filtro);
}