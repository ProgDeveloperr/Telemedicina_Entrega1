package DLL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import BLL.TipoUsuario;
import BLL.Usuario;
import repository.Hashing;
import repository.UsuarioRepository;

public class ControllerUsuario implements UsuarioRepository {

	private Connection connection;

	public ControllerUsuario() {
		connection = Conexion.getInstance().getConnection();
	}

	@Override
	public Usuario login(String nombreUsuario, String clave) {

		String sql = "SELECT id_usuario, nombre, apellido, nombre_usuario, clave, tipo_usuario "
				+ "FROM usuario "
				+ "WHERE nombre_usuario = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, nombreUsuario);

			try (ResultSet result = statement.executeQuery()) {

				if (result.next()) {

					String hashGuardado = result.getString("clave");

					if (Hashing.verificar(clave, hashGuardado)) {

						return new Usuario(
								result.getInt("id_usuario"),
								result.getString("nombre"),
								result.getString("apellido"),
								result.getString("nombre_usuario"),
								hashGuardado,
								TipoUsuario.valueOf(result.getString("tipo_usuario"))
						);
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	@Override
	public boolean existeNombreUsuario(String nombreUsuario) {

		String sql = "SELECT id_usuario "
				+ "FROM usuario "
				+ "WHERE nombre_usuario = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, nombreUsuario);

			try (ResultSet result = statement.executeQuery()) {
				return result.next();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	
}