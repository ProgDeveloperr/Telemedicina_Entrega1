package DLL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.LinkedList;

import BLL.Disponibilidad;
import repository.DisponibilidadRepository;

public class ControllerDisponibilidad implements DisponibilidadRepository {

	private Connection connection;

	public ControllerDisponibilidad() {
		connection = Conexion.getInstance().getConnection();
	}

	/*
	 * =========================================================
	 * REGISTRAR DISPONIBILIDAD
	 * =========================================================
	 */
	@Override
	public boolean registrar(
			Disponibilidad disponibilidad,
			int idProfesional) {

		/*
		 * La validacion se realiza tambien en el backend.
		 * No se depende solamente de la interfaz grafica.
		 */
		if (!validarDatosDisponibilidad(
				disponibilidad,
				idProfesional)) {

			return false;
		}

		/*
		 * Un profesional no puede tener dos disponibilidades
		 * superpuestas durante la misma fecha.
		 */
		if (existeSuperposicion(
				disponibilidad,
				idProfesional,
				false)) {

			return false;
		}

		String sql =
				"INSERT INTO disponibilidad "
				+ "(fecha, hora_inicio, hora_fin, profesional_id) "
				+ "VALUES (?, ?, ?, ?)";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setDate(
					1,
					Date.valueOf(
							disponibilidad.getFecha()
					)
			);

			statement.setTime(
					2,
					Time.valueOf(
							disponibilidad.getHoraInicio()
					)
			);

			statement.setTime(
					3,
					Time.valueOf(
							disponibilidad.getHoraFin()
					)
			);

			statement.setInt(
					4,
					idProfesional
			);

			int filasAfectadas =
					statement.executeUpdate();

			return filasAfectadas > 0;

		} catch (SQLException e) {

			System.err.println(
					"Error al registrar la disponibilidad."
			);

			e.printStackTrace();

			return false;
		}
	}

	/*
	 * =========================================================
	 * OBTENER DISPONIBILIDADES POR PROFESIONAL
	 * =========================================================
	 */
	@Override
	public LinkedList<Disponibilidad> obtenerPorProfesional(
			int idProfesional) {

		LinkedList<Disponibilidad> disponibilidades =
				new LinkedList<Disponibilidad>();

		if (idProfesional <= 0) {
			return disponibilidades;
		}

		String sql =
				"SELECT id_disponibilidad, fecha, hora_inicio, "
				+ "hora_fin, retraso_estimado "
				+ "FROM disponibilidad "
				+ "WHERE profesional_id = ? "
				+ "ORDER BY fecha, hora_inicio";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idProfesional
			);

			try (ResultSet result =
					statement.executeQuery()) {

				while (result.next()) {

					disponibilidades.add(
							new Disponibilidad(
									result.getInt(
											"id_disponibilidad"
									),
									result.getDate(
											"fecha"
									).toLocalDate(),
									result.getTime(
											"hora_inicio"
									).toLocalTime(),
									result.getTime(
											"hora_fin"
									).toLocalTime(),
									result.getInt(
											"retraso_estimado"
									)
							)
					);
				}
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al obtener las disponibilidades del profesional."
			);

			e.printStackTrace();
		}

		return disponibilidades;
	}

	/*
	 * =========================================================
	 * BUSCAR DISPONIBILIDAD
	 * =========================================================
	 */
	@Override
	public Disponibilidad buscarPorId(
			int idDisponibilidad,
			int idProfesional) {

		if (idDisponibilidad <= 0
				|| idProfesional <= 0) {

			return null;
		}

		String sql =
				"SELECT id_disponibilidad, fecha, hora_inicio, "
				+ "hora_fin, retraso_estimado "
				+ "FROM disponibilidad "
				+ "WHERE id_disponibilidad = ? "
				+ "AND profesional_id = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idDisponibilidad
			);

			statement.setInt(
					2,
					idProfesional
			);

			try (ResultSet result =
					statement.executeQuery()) {

				if (result.next()) {

					return new Disponibilidad(
							result.getInt(
									"id_disponibilidad"
							),
							result.getDate(
									"fecha"
							).toLocalDate(),
							result.getTime(
									"hora_inicio"
							).toLocalTime(),
							result.getTime(
									"hora_fin"
							).toLocalTime(),
							result.getInt(
									"retraso_estimado"
							)
					);
				}
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al buscar la disponibilidad."
			);

			e.printStackTrace();
		}

		return null;
	}

	/*
	 * =========================================================
	 * MODIFICAR DISPONIBILIDAD
	 * =========================================================
	 */
	@Override
	public boolean modificar(
			Disponibilidad disponibilidad,
			int idProfesional) {

		if (disponibilidad == null
				|| disponibilidad.getIdDisponibilidad() <= 0) {

			return false;
		}

		/*
		 * Las mismas validaciones utilizadas al registrar
		 * se vuelven a ejecutar al modificar.
		 */
		if (!validarDatosDisponibilidad(
				disponibilidad,
				idProfesional)) {

			return false;
		}

		/*
		 * Se controla la superposicion excluyendo
		 * la propia disponibilidad que se esta editando.
		 */
		if (existeSuperposicion(
				disponibilidad,
				idProfesional,
				true)) {

			return false;
		}

		/*
		 * No se permite modificar fecha u horario si algun
		 * turno asociado quedaria fuera de la disponibilidad.
		 */
		if (!turnosCompatiblesConModificacion(
				disponibilidad)) {

			return false;
		}

		String sql =
				"UPDATE disponibilidad "
				+ "SET fecha = ?, hora_inicio = ?, hora_fin = ? "
				+ "WHERE id_disponibilidad = ? "
				+ "AND profesional_id = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setDate(
					1,
					Date.valueOf(
							disponibilidad.getFecha()
					)
			);

			statement.setTime(
					2,
					Time.valueOf(
							disponibilidad.getHoraInicio()
					)
			);

			statement.setTime(
					3,
					Time.valueOf(
							disponibilidad.getHoraFin()
					)
			);

			statement.setInt(
					4,
					disponibilidad.getIdDisponibilidad()
			);

			statement.setInt(
					5,
					idProfesional
			);

			int filasAfectadas =
					statement.executeUpdate();

			return filasAfectadas > 0;

		} catch (SQLException e) {

			System.err.println(
					"Error al modificar la disponibilidad."
			);

			e.printStackTrace();

			return false;
		}
	}

	/*
	 * =========================================================
	 * ACTUALIZAR RETRASO
	 * =========================================================
	 */
	@Override
	public boolean actualizarRetraso(
			int idDisponibilidad,
			int idProfesional,
			int minutos) {

		if (idDisponibilidad <= 0
				|| idProfesional <= 0
				|| minutos < 0) {

			return false;
		}

		String sql =
				"UPDATE disponibilidad "
				+ "SET retraso_estimado = ? "
				+ "WHERE id_disponibilidad = ? "
				+ "AND profesional_id = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					minutos
			);

			statement.setInt(
					2,
					idDisponibilidad
			);

			statement.setInt(
					3,
					idProfesional
			);

			int filasAfectadas =
					statement.executeUpdate();

			return filasAfectadas > 0;

		} catch (SQLException e) {

			System.err.println(
					"Error al actualizar el retraso."
			);

			e.printStackTrace();

			return false;
		}
	}

	/*
	 * =========================================================
	 * VALIDACION INTERNA DE DISPONIBILIDAD
	 * =========================================================
	 */
	private boolean validarDatosDisponibilidad(
			Disponibilidad disponibilidad,
			int idProfesional) {

		if (disponibilidad == null
				|| idProfesional <= 0
				|| disponibilidad.getFecha() == null
				|| disponibilidad.getHoraInicio() == null
				|| disponibilidad.getHoraFin() == null) {

			return false;
		}

		/*
		 * No se permite registrar ni modificar
		 * una disponibilidad hacia una fecha pasada.
		 */
		if (disponibilidad
				.getFecha()
				.isBefore(
						LocalDate.now()
				)) {

			return false;
		}

		/*
		 * La hora final debe ser estrictamente
		 * posterior a la hora inicial.
		 */
		if (!disponibilidad
				.getHoraFin()
				.isAfter(
						disponibilidad.getHoraInicio()
				)) {

			return false;
		}

		return true;
	}

	/*
	 * =========================================================
	 * VALIDAR SUPERPOSICIONES
	 * =========================================================
	 */
	private boolean existeSuperposicion(
			Disponibilidad disponibilidad,
			int idProfesional,
			boolean excluirActual) {

		String sql =
				"SELECT id_disponibilidad "
				+ "FROM disponibilidad "
				+ "WHERE profesional_id = ? "
				+ "AND fecha = ? "
				+ "AND hora_inicio < ? "
				+ "AND hora_fin > ? ";

		if (excluirActual) {

			sql +=
					"AND id_disponibilidad <> ? ";
		}

		sql +=
				"LIMIT 1";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idProfesional
			);

			statement.setDate(
					2,
					Date.valueOf(
							disponibilidad.getFecha()
					)
			);

			/*
			 * Dos rangos se superponen cuando:
			 *
			 * existente.inicio < nuevo.fin
			 * Y
			 * existente.fin > nuevo.inicio
			 *
			 * Ejemplo:
			 * 10:00 - 14:00
			 * 13:00 - 15:00
			 *
			 * => se superponen.
			 */
			statement.setTime(
					3,
					Time.valueOf(
							disponibilidad.getHoraFin()
					)
			);

			statement.setTime(
					4,
					Time.valueOf(
							disponibilidad.getHoraInicio()
					)
			);

			if (excluirActual) {

				statement.setInt(
						5,
						disponibilidad.getIdDisponibilidad()
				);
			}

			try (ResultSet result =
					statement.executeQuery()) {

				return result.next();
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al verificar superposiciones de disponibilidad."
			);

			e.printStackTrace();

			/*
			 * FAIL CLOSED:
			 *
			 * Ante un error de base de datos se bloquea
			 * la operacion en lugar de permitirla.
			 */
			return true;
		}
	}

	/*
	 * =========================================================
	 * VALIDAR TURNOS ASOCIADOS ANTES DE MODIFICAR
	 * =========================================================
	 */
	private boolean turnosCompatiblesConModificacion(
			Disponibilidad disponibilidad) {

		String sql =
				"SELECT fecha, hora "
				+ "FROM turno "
				+ "WHERE disponibilidad_id = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					disponibilidad.getIdDisponibilidad()
			);

			try (ResultSet result =
					statement.executeQuery()) {

				while (result.next()) {

					LocalDate fechaTurno =
							result
									.getDate(
											"fecha"
									)
									.toLocalDate();

					java.time.LocalTime horaTurno =
							result
									.getTime(
											"hora"
									)
									.toLocalTime();

					/*
					 * La fecha del turno debe seguir
					 * coincidiendo con la disponibilidad.
					 */
					if (!fechaTurno.equals(
							disponibilidad.getFecha())) {

						return false;
					}

					/*
					 * La hora debe continuar dentro
					 * del nuevo rango.
					 *
					 * inicio <= turno < fin
					 */
					if (horaTurno.isBefore(
							disponibilidad.getHoraInicio())) {

						return false;
					}

					if (!horaTurno.isBefore(
							disponibilidad.getHoraFin())) {

						return false;
					}
				}
			}

			return true;

		} catch (SQLException e) {

			System.err.println(
					"Error al verificar los turnos asociados a la disponibilidad."
			);

			e.printStackTrace();

			/*
			 * FAIL CLOSED:
			 *
			 * Si no se puede comprobar la integridad,
			 * la modificacion queda bloqueada.
			 */
			return false;
		}
	}
}