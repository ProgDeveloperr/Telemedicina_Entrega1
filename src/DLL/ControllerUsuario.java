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

		connection =
				Conexion
						.getInstance()
						.getConnection();
	}

	/*
	 * =========================================================
	 * LOGIN
	 * =========================================================
	 */
	@Override
	public Usuario login(
			String nombreUsuario,
			String clave) {

		if (connection == null
				|| nombreUsuario == null
				|| clave == null
				|| nombreUsuario.trim().isEmpty()
				|| clave.isEmpty()) {

			return null;
		}

		String sql =
				"SELECT id_usuario, nombre, apellido, "
				+ "nombre_usuario, clave, tipo_usuario "
				+ "FROM usuario "
				+ "WHERE nombre_usuario = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setString(
					1,
					nombreUsuario.trim()
			);

			try (ResultSet result =
					statement.executeQuery()) {

				if (result.next()) {

					String hashGuardado =
							result.getString(
									"clave"
							);

					if (Hashing.verificar(
							clave,
							hashGuardado)) {

						/*
						 * IMPORTANTE:
						 *
						 * El hash se utiliza solamente para verificar
						 * la contraseña.
						 *
						 * Ya no se guarda dentro del objeto Usuario
						 * que circula por la aplicacion.
						 */
						return new Usuario(
								result.getInt(
										"id_usuario"
								),
								result.getString(
										"nombre"
								),
								result.getString(
										"apellido"
								),
								result.getString(
										"nombre_usuario"
								),
								"",
								TipoUsuario.valueOf(
										result.getString(
												"tipo_usuario"
										)
								)
						);
					}
				}
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al autenticar el usuario."
			);

			e.printStackTrace();
		}

		return null;
	}

	/*
	 * =========================================================
	 * EXISTE NOMBRE DE USUARIO
	 * =========================================================
	 */
	@Override
	public boolean existeNombreUsuario(
			String nombreUsuario) {

		/*
		 * FAIL CLOSED:
		 *
		 * Si no podemos verificar correctamente,
		 * bloqueamos cualquier operacion que dependa
		 * de saber si el usuario ya existe.
		 */
		if (connection == null
				|| nombreUsuario == null
				|| nombreUsuario.trim().isEmpty()) {

			return true;
		}

		String sql =
				"SELECT id_usuario "
				+ "FROM usuario "
				+ "WHERE nombre_usuario = ? "
				+ "LIMIT 1";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setString(
					1,
					nombreUsuario.trim()
			);

			try (ResultSet result =
					statement.executeQuery()) {

				return result.next();
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al verificar el nombre de usuario."
			);

			e.printStackTrace();

			/*
			 * Antes se devolvia false.
			 *
			 * Eso podia permitir continuar con un alta
			 * aunque la consulta de validacion hubiese fallado.
			 *
			 * Ahora se bloquea la operacion.
			 */
			return true;
		}
	}
}