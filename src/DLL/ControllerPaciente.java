package DLL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

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
	@Override
	public Paciente buscarPorDni(String dni) {

		String sql = "SELECT id_paciente, dni, nombre, apellido, telefono "
				+ "FROM paciente "
				+ "WHERE dni = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, dni);

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
	
	@Override
	public LinkedList<Paciente> buscar(String filtro) {

		LinkedList<Paciente> pacientes = new LinkedList<Paciente>();

		String sql = "SELECT id_paciente, dni, nombre, apellido, telefono "
				+ "FROM paciente "
				+ "WHERE dni LIKE ? "
				+ "OR nombre LIKE ? "
				+ "OR apellido LIKE ? "
				+ "ORDER BY apellido, nombre";

		String criterio = "%" + filtro + "%";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, criterio);
			statement.setString(2, criterio);
			statement.setString(3, criterio);

			try (ResultSet result = statement.executeQuery()) {

				while (result.next()) {

					pacientes.add(
							new Paciente(
									result.getInt("id_paciente"),
									result.getString("dni"),
									result.getString("nombre"),
									result.getString("apellido"),
									result.getString("telefono")
							)
					);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pacientes;
	}
	
	
	@Override
	public boolean modificar(Paciente paciente) {

		String sql = "UPDATE paciente "
				+ "SET dni = ?, nombre = ?, apellido = ?, telefono = ? "
				+ "WHERE id_paciente = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, paciente.getDni());
			statement.setString(2, paciente.getNombre());
			statement.setString(3, paciente.getApellido());
			statement.setString(4, paciente.getTelefono());
			statement.setInt(5, paciente.getIdPaciente());

			int filasAfectadas = statement.executeUpdate();

			return filasAfectadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	
}