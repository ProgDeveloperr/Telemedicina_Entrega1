package DLL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import BLL.Paciente;
import repository.PacienteRepository;

public class ControllerPaciente implements PacienteRepository {

	private Connection connection;

	public ControllerPaciente() {
		connection = Conexion.getInstance().getConnection();
	}

	@Override
	public boolean registrar(Paciente paciente) {

		String sql = "INSERT INTO paciente "
				+ "(dni, nombre, apellido, telefono) "
				+ "VALUES (?, ?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, paciente.getDni());
			statement.setString(2, paciente.getNombre());
			statement.setString(3, paciente.getApellido());
			statement.setString(4, paciente.getTelefono());

			int filasAfectadas = statement.executeUpdate();

			return filasAfectadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public Paciente buscarPorId(int idPaciente) {

		String sql = "SELECT id_paciente, dni, nombre, apellido, telefono "
				+ "FROM paciente "
				+ "WHERE id_paciente = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, idPaciente);

			try (ResultSet result = statement.executeQuery()) {

				if (result.next()) {

					return new Paciente(
							result.getInt("id_paciente"),
							result.getString("dni"),
							result.getString("nombre"),
							result.getString("apellido"),
							result.getString("telefono")
					);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}