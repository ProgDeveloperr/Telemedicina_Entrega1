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
import java.util.LinkedList;

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
	
	
	
	@Override
	public Profesional buscarPorId(int idProfesional) {

		String sql = "SELECT id_profesional, matricula, especialidad "
				+ "FROM profesional "
				+ "WHERE id_profesional = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, idProfesional);

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
	public LinkedList<Profesional> buscar(String filtro) {

		LinkedList<Profesional> profesionales =
				new LinkedList<Profesional>();

		String sql = "SELECT p.id_profesional, p.matricula, p.especialidad "
				+ "FROM profesional p "
				+ "INNER JOIN usuario u "
				+ "ON p.usuario_id = u.id_usuario "
				+ "WHERE u.nombre LIKE ? "
				+ "OR u.apellido LIKE ? "
				+ "OR u.nombre_usuario LIKE ? "
				+ "OR p.matricula LIKE ? "
				+ "OR p.especialidad LIKE ? "
				+ "ORDER BY u.apellido, u.nombre";

		String criterio = "%" + filtro + "%";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, criterio);
			statement.setString(2, criterio);
			statement.setString(3, criterio);
			statement.setString(4, criterio);
			statement.setString(5, criterio);

			try (ResultSet result = statement.executeQuery()) {

				while (result.next()) {

					profesionales.add(
							new Profesional(
									result.getInt("id_profesional"),
									result.getString("matricula"),
									result.getString("especialidad")
							)
					);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return profesionales;
	}
	
	
	@Override
	public Usuario obtenerUsuarioPorProfesional(int idProfesional) {

		String sql = "SELECT u.id_usuario, u.nombre, u.apellido, "
				+ "u.nombre_usuario, u.tipo_usuario "
				+ "FROM usuario u "
				+ "INNER JOIN profesional p "
				+ "ON p.usuario_id = u.id_usuario "
				+ "WHERE p.id_profesional = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, idProfesional);

			try (ResultSet result = statement.executeQuery()) {

				if (result.next()) {

					return new Usuario(
							result.getInt("id_usuario"),
							result.getString("nombre"),
							result.getString("apellido"),
							result.getString("nombre_usuario"),
							"",
							TipoUsuario.valueOf(
									result.getString("tipo_usuario")
							)
					);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
}