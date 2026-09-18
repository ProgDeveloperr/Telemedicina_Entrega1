package DLL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import BLL.Profesional;
import BLL.TipoUsuario;
import BLL.Usuario;
import repository.Hashing;
import repository.ProfesionalRepository;


public class ControllerProfesional implements ProfesionalRepository {

	private Connection connection;

	public ControllerProfesional() {
		connection = Conexion.getInstance().getConnection();
	}

	@Override
	public Profesional obtenerPorUsuario(int idUsuario) {

		String sql = "SELECT id_profesional, matricula, especialidad "
				+ "FROM profesional "
				+ "WHERE usuario_id = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, idUsuario);

			try (ResultSet result = statement.executeQuery()) {

				if (result.next()) {

					return new Profesional(
							result.getInt("id_profesional"),
							result.getString("matricula"),
							result.getString("especialidad")
					);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	@Override
	public boolean registrar(Usuario usuario, Profesional profesional) {

		String sqlUsuario = "INSERT INTO usuario "
				+ "(nombre, apellido, nombre_usuario, clave, tipo_usuario) "
				+ "VALUES (?, ?, ?, ?, ?)";

		String sqlProfesional = "INSERT INTO profesional "
				+ "(matricula, especialidad, usuario_id) "
				+ "VALUES (?, ?, ?)";

		try {

			connection.setAutoCommit(false);

			int idUsuario;

			try (PreparedStatement statementUsuario = connection.prepareStatement(
					sqlUsuario,
					Statement.RETURN_GENERATED_KEYS)) {

				statementUsuario.setString(1, usuario.getNombre());
				statementUsuario.setString(2, usuario.getApellido());
				statementUsuario.setString(3, usuario.getNombreUsuario());
				statementUsuario.setString(4, Hashing.hash(usuario.getClave()));
				statementUsuario.setString(5, TipoUsuario.PROFESIONAL.name());

				int filasUsuario = statementUsuario.executeUpdate();

				if (filasUsuario == 0) {
					connection.rollback();
					return false;
				}

				try (ResultSet claves = statementUsuario.getGeneratedKeys()) {

					if (!claves.next()) {
						connection.rollback();
						return false;
					}

					idUsuario = claves.getInt(1);
				}
			}

			try (PreparedStatement statementProfesional =
					connection.prepareStatement(sqlProfesional)) {

				statementProfesional.setString(1, profesional.getMatricula());
				statementProfesional.setString(2, profesional.getEspecialidad());
				statementProfesional.setInt(3, idUsuario);

				int filasProfesional = statementProfesional.executeUpdate();

				if (filasProfesional == 0) {
					connection.rollback();
					return false;
				}
			}

			connection.commit();

			return true;

		} catch (SQLException e) {

			try {
				connection.rollback();
			} catch (SQLException rollbackException) {
				rollbackException.printStackTrace();
			}

			e.printStackTrace();

			return false;

		} finally {

			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public boolean existeMatricula(String matricula) {

		String sql = "SELECT id_profesional "
				+ "FROM profesional "
				+ "WHERE matricula = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, matricula);

			try (ResultSet result = statement.executeQuery()) {
				return result.next();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	
	
}