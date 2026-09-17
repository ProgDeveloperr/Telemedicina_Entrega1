package repository;

import BLL.Paciente;

public interface PacienteRepository {

	boolean registrar(Paciente paciente);

	Paciente buscarPorId(int idPaciente);

}