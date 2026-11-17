package DLL;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import BLL.Profesional;
import BLL.TipoUsuario;
import BLL.Usuario;
import repository.Hashing;
import repository.ProfesionalRepository;

public class ControllerProfesional implements ProfesionalRepository {

	private static final int CLAVE_MIN_BYTES = 8;
	private static final int CLAVE_MAX_BYTES = 72;

	private static final int MATRICULA_MIN_CARACTERES = 4;
	private static final int MATRICULA_MAX_CARACTERES = 20;

	private Connection connection;

	public ControllerProfesional() {

		connection =
				Conexion
						.getInstance()
						.getConnection();
	}

	/*
	 * =========================================================
	 * OBTENER PROFESIONAL POR USUARIO
	 * =========================================================
	 */
	@Override
	public Profesional obtenerPorUsuario(
			int idUsuario) {

		if (connection == null
				|| idUsuario <= 0) {

			return null;
		}

		String sql =
				"SELECT id_profesional, matricula, especialidad "
				+ "FROM profesional "
				+ "WHERE usuario_id = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idUsuario
			);

			try (ResultSet result =
					statement.executeQuery()) {

				if (result.next()) {

					return new Profesional(
							result.getInt(
									"id_profesional"
							),
							result.getString(
									"matricula"
							),
							result.getString(
									"especialidad"
							)
					);
				}
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al obtener el profesional por usuario."
			);

			e.printStackTrace();
		}

		return null;
	}

	/*
	 * =========================================================
	 * REGISTRAR PROFESIONAL
	 * =========================================================
	 */
	@Override
	public boolean registrar(
			Usuario usuario,
			Profesional profesional) {

		if (!validarDatosRegistro(
				usuario,
				profesional)) {

			return false;
		}

		String nombreUsuario =
				usuario
						.getNombreUsuario()
						.trim();

		String matricula =
				profesional
						.getMatricula()
						.trim()
						.toUpperCase();

		/*
		 * El controller vuelve a comprobar los duplicados.
		 *
		 * No se depende solamente de la GUI.
		 */
		if (existeNombreUsuarioInterno(
				nombreUsuario)) {

			return false;
		}

		if (existeMatricula(
				matricula)) {

			return false;
		}

		String sqlUsuario =
				"INSERT INTO usuario "
				+ "(nombre, apellido, nombre_usuario, clave, tipo_usuario) "
				+ "VALUES (?, ?, ?, ?, ?)";

		String sqlProfesional =
				"INSERT INTO profesional "
				+ "(matricula, especialidad, usuario_id) "
				+ "VALUES (?, ?, ?)";

		boolean autoCommitOriginal = true;

		try {

			autoCommitOriginal =
					connection.getAutoCommit();

			connection.setAutoCommit(
					false
			);

			int idUsuario;

			/*
			 * =================================================
			 * INSERT USUARIO
			 * =================================================
			 */
			try (PreparedStatement statementUsuario =
					connection.prepareStatement(
							sqlUsuario,
							Statement.RETURN_GENERATED_KEYS
					)) {

				statementUsuario.setString(
						1,
						usuario
								.getNombre()
								.trim()
				);

				statementUsuario.setString(
						2,
						usuario
								.getApellido()
								.trim()
				);

				statementUsuario.setString(
						3,
						nombreUsuario
				);

				statementUsuario.setString(
						4,
						Hashing.hash(
								usuario.getClave()
						)
				);

				statementUsuario.setString(
						5,
						TipoUsuario.PROFESIONAL.name()
				);

				int filasUsuario =
						statementUsuario.executeUpdate();

				if (filasUsuario != 1) {

					connection.rollback();

					return false;
				}

				try (ResultSet claves =
						statementUsuario.getGeneratedKeys()) {

					if (!claves.next()) {

						connection.rollback();

						return false;
					}

					idUsuario =
							claves.getInt(
									1
							);
				}
			}

			/*
			 * =================================================
			 * INSERT PROFESIONAL
			 * =================================================
			 */
			try (PreparedStatement statementProfesional =
					connection.prepareStatement(
							sqlProfesional
					)) {

				statementProfesional.setString(
						1,
						matricula
				);

				statementProfesional.setString(
						2,
						profesional
								.getEspecialidad()
								.trim()
				);

				statementProfesional.setInt(
						3,
						idUsuario
				);

				int filasProfesional =
						statementProfesional.executeUpdate();

				if (filasProfesional != 1) {

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

				System.err.println(
						"No se pudo realizar rollback al registrar el profesional."
				);

				rollbackException.printStackTrace();
			}

			System.err.println(
					"Error al registrar el profesional."
			);

			e.printStackTrace();

			return false;

		} finally {

			try {

				connection.setAutoCommit(
						autoCommitOriginal
				);

			} catch (SQLException e) {

				System.err.println(
						"No se pudo restaurar el autocommit."
				);

				e.printStackTrace();
			}
		}
	}

	/*
	 * =========================================================
	 * EXISTE MATRICULA
	 * =========================================================
	 */
	@Override
	public boolean existeMatricula(
			String matricula) {

		/*
		 * FAIL CLOSED.
		 *
		 * Si los datos son invalidos o no existe conexion,
		 * bloqueamos el alta.
		 */
		if (connection == null
				|| matricula == null
				|| matricula.trim().isEmpty()) {

			return true;
		}

		String matriculaNormalizada =
				matricula
						.trim()
						.toUpperCase();

		String sql =
				"SELECT id_profesional "
				+ "FROM profesional "
				+ "WHERE matricula = ? "
				+ "LIMIT 1";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setString(
					1,
					matriculaNormalizada
			);

			try (ResultSet result =
					statement.executeQuery()) {

				return result.next();
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al verificar la matricula."
			);

			e.printStackTrace();

			/*
			 * Antes se devolvia false.
			 *
			 * Ahora ante un error de base de datos
			 * bloqueamos la operacion.
			 */
			return true;
		}
	}

	/*
	 * =========================================================
	 * BUSCAR PROFESIONAL POR ID
	 * =========================================================
	 */
	@Override
	public Profesional buscarPorId(
			int idProfesional) {

		if (connection == null
				|| idProfesional <= 0) {

			return null;
		}

		String sql =
				"SELECT id_profesional, matricula, especialidad "
				+ "FROM profesional "
				+ "WHERE id_profesional = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idProfesional
			);

			try (ResultSet result =
					statement.executeQuery()) {

				if (result.next()) {

					return new Profesional(
							result.getInt(
									"id_profesional"
							),
							result.getString(
									"matricula"
							),
							result.getString(
									"especialidad"
							)
					);
				}
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al buscar el profesional."
			);

			e.printStackTrace();
		}

		return null;
	}

	/*
	 * =========================================================
	 * BUSCAR PROFESIONALES
	 * =========================================================
	 */
	@Override
	public LinkedList<Profesional> buscar(
			String filtro) {

		LinkedList<Profesional> profesionales =
				new LinkedList<Profesional>();

		if (connection == null) {
			return profesionales;
		}

		if (filtro == null) {
			filtro = "";
		}

		String sql =
				"SELECT p.id_profesional, p.matricula, p.especialidad "
				+ "FROM profesional p "
				+ "INNER JOIN usuario u "
				+ "ON p.usuario_id = u.id_usuario "
				+ "WHERE u.nombre LIKE ? "
				+ "OR u.apellido LIKE ? "
				+ "OR u.nombre_usuario LIKE ? "
				+ "OR p.matricula LIKE ? "
				+ "OR p.especialidad LIKE ? "
				+ "ORDER BY u.apellido, u.nombre";

		String criterio =
				"%"
				+ filtro.trim()
				+ "%";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setString(
					1,
					criterio
			);

			statement.setString(
					2,
					criterio
			);

			statement.setString(
					3,
					criterio
			);

			statement.setString(
					4,
					criterio
			);

			statement.setString(
					5,
					criterio
			);

			try (ResultSet result =
					statement.executeQuery()) {

				while (result.next()) {

					profesionales.add(
							new Profesional(
									result.getInt(
											"id_profesional"
									),
									result.getString(
											"matricula"
									),
									result.getString(
											"especialidad"
									)
							)
					);
				}
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al buscar profesionales."
			);

			e.printStackTrace();
		}

		return profesionales;
	}

	/*
	 * =========================================================
	 * OBTENER USUARIO POR PROFESIONAL
	 * =========================================================
	 */
	@Override
	public Usuario obtenerUsuarioPorProfesional(
			int idProfesional) {

		if (connection == null
				|| idProfesional <= 0) {

			return null;
		}

		String sql =
				"SELECT u.id_usuario, u.nombre, u.apellido, "
				+ "u.nombre_usuario, u.tipo_usuario "
				+ "FROM usuario u "
				+ "INNER JOIN profesional p "
				+ "ON p.usuario_id = u.id_usuario "
				+ "WHERE p.id_profesional = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idProfesional
			);

			try (ResultSet result =
					statement.executeQuery()) {

				if (result.next()) {

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

		} catch (SQLException e) {

			System.err.println(
					"Error al obtener el usuario del profesional."
			);

			e.printStackTrace();
		}

		return null;
	}

	/*
	 * =========================================================
	 * VALIDAR DATOS DEL REGISTRO
	 * =========================================================
	 */
	private boolean validarDatosRegistro(
			Usuario usuario,
			Profesional profesional) {

		if (connection == null
				|| usuario == null
				|| profesional == null) {

			return false;
		}

		String nombre =
				usuario.getNombre();

		String apellido =
				usuario.getApellido();

		String nombreUsuario =
				usuario.getNombreUsuario();

		String clave =
				usuario.getClave();

		String matricula =
				profesional.getMatricula();

		String especialidad =
				profesional.getEspecialidad();

		if (nombre == null
				|| apellido == null
				|| nombreUsuario == null
				|| clave == null
				|| matricula == null
				|| especialidad == null) {

			return false;
		}

		nombre =
				nombre.trim();

		apellido =
				apellido.trim();

		nombreUsuario =
				nombreUsuario.trim();

		matricula =
				matricula.trim();

		especialidad =
				especialidad.trim();

		/*
		 * Todos los campos son obligatorios.
		 */
		if (nombre.isEmpty()
				|| apellido.isEmpty()
				|| nombreUsuario.isEmpty()
				|| clave.isEmpty()
				|| matricula.isEmpty()
				|| especialidad.isEmpty()) {

			return false;
		}

		/*
		 * Respeta los limites definidos
		 * por las columnas de la base.
		 */
		if (nombre.length() > 50
				|| apellido.length() > 50
				|| nombreUsuario.length() > 50
				|| especialidad.length() > 100) {

			return false;
		}

		/*
		 * BCrypt trabaja como maximo con 72 bytes.
		 *
		 * Aplicamos la regla sobre bytes UTF-8,
		 * no solamente cantidad de caracteres.
		 */
		int bytesClave =
				clave
						.getBytes(
								StandardCharsets.UTF_8
						)
						.length;

		if (bytesClave < CLAVE_MIN_BYTES
				|| bytesClave > CLAVE_MAX_BYTES) {

			return false;
		}

		/*
		 * Matricula:
		 *
		 * - entre 4 y 20 caracteres
		 * - letras
		 * - numeros
		 * - guion
		 * - sin espacios
		 */
		if (!matricula.matches(
				"[A-Za-z0-9-]{"
				+ MATRICULA_MIN_CARACTERES
				+ ","
				+ MATRICULA_MAX_CARACTERES
				+ "}")) {

			return false;
		}

		return true;
	}

	/*
	 * =========================================================
	 * EXISTE NOMBRE DE USUARIO
	 * =========================================================
	 */
	private boolean existeNombreUsuarioInterno(
			String nombreUsuario) {

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
			 * FAIL CLOSED.
			 */
			return true;
		}
	}
}