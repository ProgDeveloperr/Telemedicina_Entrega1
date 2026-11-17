package DLL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import BLL.Paciente;
import repository.PacienteRepository;

public class ControllerPaciente implements PacienteRepository {

	/*
	 * =========================================================
	 * REGLAS DE VALIDACION
	 * =========================================================
	 */

	private static final int DNI_MIN_DIGITOS = 7;
	private static final int DNI_MAX_DIGITOS = 8;

	private static final int TELEFONO_MIN_DIGITOS = 8;
	private static final int TELEFONO_MAX_DIGITOS = 15;

	private static final int NOMBRE_MAX_CARACTERES = 50;
	private static final int APELLIDO_MAX_CARACTERES = 50;

	private Connection connection;

	public ControllerPaciente() {
		connection = Conexion.getInstance().getConnection();
	}

	/*
	 * =========================================================
	 * REGISTRAR PACIENTE
	 * =========================================================
	 */
	@Override
	public boolean registrar(Paciente paciente) {

		/*
		 * La validacion existe tambien en backend.
		 * No dependemos exclusivamente de Swing.
		 */
		if (!validarPaciente(paciente)) {
			return false;
		}

		String dni =
				paciente
						.getDni()
						.trim();

		/*
		 * Control de duplicados desde el controller.
		 *
		 * Si ocurre un error SQL al consultar,
		 * existeDni() devuelve true y bloquea el alta.
		 */
		if (existeDni(
				dni,
				0)) {

			return false;
		}

		String sql =
				"INSERT INTO paciente "
				+ "(dni, nombre, apellido, telefono) "
				+ "VALUES (?, ?, ?, ?)";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setString(
					1,
					dni
			);

			statement.setString(
					2,
					paciente
							.getNombre()
							.trim()
			);

			statement.setString(
					3,
					paciente
							.getApellido()
							.trim()
			);

			statement.setString(
					4,
					paciente
							.getTelefono()
							.trim()
			);

			int filasAfectadas =
					statement.executeUpdate();

			return filasAfectadas == 1;

		} catch (SQLException e) {

			System.err.println(
					"Error al registrar el paciente."
			);

			e.printStackTrace();

			return false;
		}
	}

	/*
	 * =========================================================
	 * BUSCAR POR ID
	 * =========================================================
	 */
	@Override
	public Paciente buscarPorId(
			int idPaciente) {

		if (connection == null
				|| idPaciente <= 0) {

			return null;
		}

		String sql =
				"SELECT id_paciente, dni, nombre, apellido, telefono "
				+ "FROM paciente "
				+ "WHERE id_paciente = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idPaciente
			);

			try (ResultSet result =
					statement.executeQuery()) {

				if (result.next()) {

					return crearPacienteDesdeResultSet(
							result
					);
				}
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al buscar el paciente por ID."
			);

			e.printStackTrace();
		}

		return null;
	}

	/*
	 * =========================================================
	 * BUSCAR POR DNI
	 * =========================================================
	 */
	@Override
	public Paciente buscarPorDni(
			String dni) {

		if (connection == null
				|| dni == null
				|| dni.trim().isEmpty()) {

			return null;
		}

		String sql =
				"SELECT id_paciente, dni, nombre, apellido, telefono "
				+ "FROM paciente "
				+ "WHERE dni = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setString(
					1,
					dni.trim()
			);

			try (ResultSet result =
					statement.executeQuery()) {

				if (result.next()) {

					return crearPacienteDesdeResultSet(
							result
					);
				}
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al buscar el paciente por DNI."
			);

			e.printStackTrace();
		}

		return null;
	}

	/*
	 * =========================================================
	 * BUSCAR PACIENTES
	 * =========================================================
	 */
	@Override
	public LinkedList<Paciente> buscar(
			String filtro) {

		LinkedList<Paciente> pacientes =
				new LinkedList<Paciente>();

		if (connection == null) {
			return pacientes;
		}

		if (filtro == null) {
			filtro = "";
		}

		String sql =
				"SELECT id_paciente, dni, nombre, apellido, telefono "
				+ "FROM paciente "
				+ "WHERE dni LIKE ? "
				+ "OR nombre LIKE ? "
				+ "OR apellido LIKE ? "
				+ "ORDER BY apellido, nombre";

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

			try (ResultSet result =
					statement.executeQuery()) {

				while (result.next()) {

					pacientes.add(
							crearPacienteDesdeResultSet(
									result
							)
					);
				}
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al buscar pacientes."
			);

			e.printStackTrace();
		}

		return pacientes;
	}

	/*
	 * =========================================================
	 * MODIFICAR PACIENTE
	 * =========================================================
	 */
	@Override
	public boolean modificar(
			Paciente paciente) {

		if (paciente == null
				|| paciente.getIdPaciente() <= 0) {

			return false;
		}

		if (!validarPaciente(
				paciente)) {

			return false;
		}

		String dni =
				paciente
						.getDni()
						.trim();

		/*
		 * Controlamos duplicados excluyendo
		 * al propio paciente que se modifica.
		 */
		if (existeDni(
				dni,
				paciente.getIdPaciente())) {

			return false;
		}

		String sql =
				"UPDATE paciente "
				+ "SET dni = ?, nombre = ?, apellido = ?, telefono = ? "
				+ "WHERE id_paciente = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setString(
					1,
					dni
			);

			statement.setString(
					2,
					paciente
							.getNombre()
							.trim()
			);

			statement.setString(
					3,
					paciente
							.getApellido()
							.trim()
			);

			statement.setString(
					4,
					paciente
							.getTelefono()
							.trim()
			);

			statement.setInt(
					5,
					paciente.getIdPaciente()
			);

			int filasAfectadas =
					statement.executeUpdate();

			return filasAfectadas == 1;

		} catch (SQLException e) {

			System.err.println(
					"Error al modificar el paciente."
			);

			e.printStackTrace();

			return false;
		}
	}

	/*
	 * =========================================================
	 * TIENE TURNOS
	 * =========================================================
	 */
	@Override
	public boolean tieneTurnos(
			int idPaciente) {

		/*
		 * FAIL CLOSED:
		 *
		 * Si los datos son invalidos o no existe conexion,
		 * bloqueamos cualquier intento de eliminacion.
		 */
		if (connection == null
				|| idPaciente <= 0) {

			return true;
		}

		String sql =
				"SELECT id_turno "
				+ "FROM turno "
				+ "WHERE paciente_id = ? "
				+ "LIMIT 1";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idPaciente
			);

			try (ResultSet result =
					statement.executeQuery()) {

				return result.next();
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al verificar los turnos del paciente."
			);

			e.printStackTrace();

			/*
			 * Antes devolvia false.
			 *
			 * Eso permitia continuar con la eliminacion
			 * ante un error de base de datos.
			 *
			 * Ahora se bloquea la operacion.
			 */
			return true;
		}
	}

	/*
	 * =========================================================
	 * ELIMINAR PACIENTE
	 * =========================================================
	 */
	@Override
	public boolean eliminar(
			int idPaciente) {

		if (connection == null
				|| idPaciente <= 0) {

			return false;
		}

		if (tieneTurnos(
				idPaciente)) {

			return false;
		}

		String sql =
				"DELETE FROM paciente "
				+ "WHERE id_paciente = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idPaciente
			);

			int filasAfectadas =
					statement.executeUpdate();

			return filasAfectadas == 1;

		} catch (SQLException e) {

			System.err.println(
					"Error al eliminar el paciente."
			);

			e.printStackTrace();

			return false;
		}
	}

	/*
	 * =========================================================
	 * VALIDACION DE PACIENTE
	 * =========================================================
	 */
	private boolean validarPaciente(
			Paciente paciente) {

		if (connection == null
				|| paciente == null) {

			return false;
		}

		String dni =
				paciente.getDni();

		String nombre =
				paciente.getNombre();

		String apellido =
				paciente.getApellido();

		String telefono =
				paciente.getTelefono();

		if (dni == null
				|| nombre == null
				|| apellido == null
				|| telefono == null) {

			return false;
		}

		dni =
				dni.trim();

		nombre =
				nombre.trim();

		apellido =
				apellido.trim();

		telefono =
				telefono.trim();

		/*
		 * Todos los campos son obligatorios.
		 */
		if (dni.isEmpty()
				|| nombre.isEmpty()
				|| apellido.isEmpty()
				|| telefono.isEmpty()) {

			return false;
		}

		/*
		 * DNI argentino:
		 *
		 * Para este sistema academico se aceptan
		 * 7 u 8 digitos numericos.
		 */
		if (!dni.matches(
				"\\d{"
				+ DNI_MIN_DIGITOS
				+ ","
				+ DNI_MAX_DIGITOS
				+ "}")) {

			return false;
		}

		/*
		 * Telefono:
		 *
		 * Se guarda normalizado solo con numeros.
		 * Aceptamos entre 8 y 15 digitos.
		 */
		if (!telefono.matches(
				"\\d{"
				+ TELEFONO_MIN_DIGITOS
				+ ","
				+ TELEFONO_MAX_DIGITOS
				+ "}")) {

			return false;
		}

		if (nombre.length()
				> NOMBRE_MAX_CARACTERES) {

			return false;
		}

		if (apellido.length()
				> APELLIDO_MAX_CARACTERES) {

			return false;
		}

		return true;
	}

	/*
	 * =========================================================
	 * EXISTE DNI
	 * =========================================================
	 */
	private boolean existeDni(
			String dni,
			int idPacienteExcluir) {

		if (connection == null
				|| dni == null
				|| dni.trim().isEmpty()) {

			/*
			 * FAIL CLOSED.
			 */
			return true;
		}

		String sql =
				"SELECT id_paciente "
				+ "FROM paciente "
				+ "WHERE dni = ? ";

		if (idPacienteExcluir > 0) {

			sql +=
					"AND id_paciente <> ? ";
		}

		sql +=
				"LIMIT 1";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setString(
					1,
					dni.trim()
			);

			if (idPacienteExcluir > 0) {

				statement.setInt(
						2,
						idPacienteExcluir
				);
			}

			try (ResultSet result =
					statement.executeQuery()) {

				return result.next();
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al verificar duplicados de DNI."
			);

			e.printStackTrace();

			/*
			 * FAIL CLOSED:
			 *
			 * ante un fallo no permitimos registrar
			 * ni modificar.
			 */
			return true;
		}
	}

	/*
	 * =========================================================
	 * MAPEO RESULTSET
	 * =========================================================
	 */
	private Paciente crearPacienteDesdeResultSet(
			ResultSet result)
			throws SQLException {

		return new Paciente(
				result.getInt(
						"id_paciente"
				),
				result.getString(
						"dni"
				),
				result.getString(
						"nombre"
				),
				result.getString(
						"apellido"
				),
				result.getString(
						"telefono"
				)
		);
	}
}