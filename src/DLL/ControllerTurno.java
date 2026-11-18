package DLL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;

import BLL.Disponibilidad;
import BLL.EstadoTurno;
import BLL.Paciente;
import BLL.Turno;
import repository.TurnoRepository;

public class ControllerTurno implements TurnoRepository {

	/*
	 * Duracion fija de cada turno.
	 *
	 * Esto permite trabajar con intervalos reales:
	 * un turno de 10:00 ocupa desde 10:00 hasta 10:30.
	 */
	private static final int DURACION_TURNO_MINUTOS = 30;

	private Connection connection;

	public ControllerTurno() {
		connection = Conexion.getInstance().getConnection();
	}

	/*
	 * =========================================================
	 * REGISTRAR TURNO
	 * =========================================================
	 */
	@Override
	public boolean registrar(Turno turno) {

		if (!validarDatosBasicos(turno)) {
			return false;
		}

		/*
		 * Todo turno nuevo debe comenzar como PENDIENTE.
		 *
		 * No se permite crear directamente un turno
		 * CONFIRMADO, PRESENTE o CANCELADO.
		 */
		if (turno.getEstado() != EstadoTurno.PENDIENTE) {
			return false;
		}

		if (connection == null) {
			return false;
		}

		
		synchronized (connection) {

			boolean autoCommitOriginal = true;

			try {

				autoCommitOriginal =
						connection.getAutoCommit();

				connection.setAutoCommit(false);

				/*
				 * Bloqueamos la disponibilidad.
				 *
				 * Si dos recepcionistas intentan reservar
				 * sobre la misma disponibilidad al mismo
				 * tiempo, uno debera esperar al otro.
				 */
				Disponibilidad disponibilidad =
						obtenerDisponibilidadPorId(
								connection,
								turno
										.getDisponibilidad()
										.getIdDisponibilidad(),
								true
						);

				if (!validarTurnoContraDisponibilidad(
						turno,
						disponibilidad)) {

					rollbackSilencioso();

					return false;
				}

				/*
				 * La comprobacion se realiza dentro de la
				 * misma transaccion y con bloqueo.
				 */
				if (horarioOcupadoInterno(
						connection,
						disponibilidad
								.getIdDisponibilidad(),
						turno.getHora(),
						0,
						true)) {

					rollbackSilencioso();

					return false;
				}

				String sql =
						"INSERT INTO turno "
						+ "(fecha, hora, estado, paciente_id, disponibilidad_id) "
						+ "VALUES (?, ?, ?, ?, ?)";

				try (PreparedStatement statement =
						connection.prepareStatement(sql)) {

					statement.setDate(
							1,
							Date.valueOf(
									turno.getFecha()
							)
					);

					statement.setTime(
							2,
							Time.valueOf(
									turno.getHora()
							)
					);

					statement.setString(
							3,
							turno
									.getEstado()
									.name()
					);

					statement.setInt(
							4,
							turno
									.getPaciente()
									.getIdPaciente()
					);

					statement.setInt(
							5,
							turno
									.getDisponibilidad()
									.getIdDisponibilidad()
					);

					int filasAfectadas =
							statement.executeUpdate();

					if (filasAfectadas != 1) {

						rollbackSilencioso();

						return false;
					}
				}

				connection.commit();

				return true;

			} catch (SQLException e) {

				rollbackSilencioso();

				System.err.println(
						"Error al registrar el turno."
				);

				e.printStackTrace();

				return false;

			} finally {

				restaurarAutoCommit(
						autoCommitOriginal
				);
			}
		}
	}

	/*
	 * =========================================================
	 * BUSCAR TURNO
	 * =========================================================
	 */
	@Override
	public Turno buscarPorId(int idTurno) {

		if (connection == null
				|| idTurno <= 0) {

			return null;
		}

		String sql =
				consultaBase()
				+ " WHERE t.id_turno = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idTurno
			);

			try (ResultSet result =
					statement.executeQuery()) {

				if (result.next()) {

					return crearTurnoDesdeResultSet(
							result
					);
				}
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al buscar el turno."
			);

			e.printStackTrace();
		}

		return null;
	}

	/*
	 * =========================================================
	 * OBTENER TODOS
	 * =========================================================
	 */
	@Override
	public LinkedList<Turno> obtenerTodos() {

		LinkedList<Turno> turnos =
				new LinkedList<Turno>();

		if (connection == null) {
			return turnos;
		}

		String sql =
				consultaBase()
				+ " ORDER BY t.fecha, t.hora";

		try (PreparedStatement statement =
				connection.prepareStatement(sql);

				ResultSet result =
						statement.executeQuery()) {

			while (result.next()) {

				turnos.add(
						crearTurnoDesdeResultSet(
								result
						)
				);
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al obtener los turnos."
			);

			e.printStackTrace();
		}

		return turnos;
	}

	/*
	 * =========================================================
	 * OBTENER POR PROFESIONAL
	 * =========================================================
	 */
	@Override
	public LinkedList<Turno> obtenerPorProfesional(
			int idProfesional) {

		LinkedList<Turno> turnos =
				new LinkedList<Turno>();

		if (connection == null
				|| idProfesional <= 0) {

			return turnos;
		}

		String sql =
				consultaBase()
				+ " WHERE d.profesional_id = ? "
				+ "ORDER BY t.fecha, t.hora";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idProfesional
			);

			try (ResultSet result =
					statement.executeQuery()) {

				while (result.next()) {

					turnos.add(
							crearTurnoDesdeResultSet(
									result
							)
					);
				}
			}

		} catch (SQLException e) {

			System.err.println(
					"Error al obtener los turnos del profesional."
			);

			e.printStackTrace();
		}

		return turnos;
	}

	/*
	 * =========================================================
	 * MODIFICAR TURNO
	 * =========================================================
	 */
	@Override
	public boolean modificar(Turno turno) {

		if (!validarDatosBasicos(turno)
				|| turno.getIdTurno() <= 0
				|| connection == null) {

			return false;
		}

		synchronized (connection) {

			boolean autoCommitOriginal = true;

			try {

				autoCommitOriginal =
						connection.getAutoCommit();

				connection.setAutoCommit(false);

				/*
				 * Bloqueamos primero la disponibilidad destino.
				 */
				Disponibilidad disponibilidad =
						obtenerDisponibilidadPorId(
								connection,
								turno
										.getDisponibilidad()
										.getIdDisponibilidad(),
								true
						);

				if (disponibilidad == null) {

					rollbackSilencioso();

					return false;
				}

				/*
				 * Bloqueamos tambien el turno que se modifica.
				 */
				EstadoTurno estadoActual =
						obtenerEstadoTurno(
								connection,
								turno.getIdTurno(),
								true
						);

				if (estadoActual == null) {

					rollbackSilencioso();

					return false;
				}

				/*
				 * Solo pueden modificarse turnos que todavia
				 * esten PENDIENTES o CONFIRMADOS.
				 *
				 * Un PRESENTE o CANCELADO queda cerrado.
				 */
				if (estadoActual != EstadoTurno.PENDIENTE
						&& estadoActual != EstadoTurno.CONFIRMADO) {

					rollbackSilencioso();

					return false;
				}

				if (!validarTurnoContraDisponibilidad(
						turno,
						disponibilidad)) {

					rollbackSilencioso();

					return false;
				}

				/*
				 * Se controla el solapamiento ignorando
				 * el propio turno que estamos editando.
				 */
				if (horarioOcupadoInterno(
						connection,
						disponibilidad
								.getIdDisponibilidad(),
						turno.getHora(),
						turno.getIdTurno(),
						true)) {

					rollbackSilencioso();

					return false;
				}

				String sql =
						"UPDATE turno "
						+ "SET fecha = ?, "
						+ "hora = ?, "
						+ "paciente_id = ?, "
						+ "disponibilidad_id = ? "
						+ "WHERE id_turno = ?";

				try (PreparedStatement statement =
						connection.prepareStatement(sql)) {

					statement.setDate(
							1,
							Date.valueOf(
									turno.getFecha()
							)
					);

					statement.setTime(
							2,
							Time.valueOf(
									turno.getHora()
							)
					);

					statement.setInt(
							3,
							turno
									.getPaciente()
									.getIdPaciente()
					);

					statement.setInt(
							4,
							turno
									.getDisponibilidad()
									.getIdDisponibilidad()
					);

					statement.setInt(
							5,
							turno.getIdTurno()
					);

					int filasAfectadas =
							statement.executeUpdate();

					if (filasAfectadas != 1) {

						rollbackSilencioso();

						return false;
					}
				}

				connection.commit();

				return true;

			} catch (SQLException e) {

				rollbackSilencioso();

				System.err.println(
						"Error al modificar el turno."
				);

				e.printStackTrace();

				return false;

			} finally {

				restaurarAutoCommit(
						autoCommitOriginal
				);
			}
		}
	}

	/*
	 * =========================================================
	 * ACTUALIZAR ESTADO
	 * =========================================================
	 */
	@Override
	public boolean actualizarEstado(
			int idTurno,
			EstadoTurno nuevoEstado) {

		if (connection == null
				|| idTurno <= 0
				|| nuevoEstado == null) {

			return false;
		}

		/*
		 * Las transiciones se validan directamente en el
		 * UPDATE para que la operacion sea atomica.
		 *
		 * TRANSICIONES VALIDAS:
		 *
		 * PENDIENTE  -> CONFIRMADO
		 * PENDIENTE  -> CANCELADO
		 * CONFIRMADO -> PRESENTE
		 * CONFIRMADO -> CANCELADO
		 *
		 * PRESENTE y CANCELADO son estados finales.
		 */

		String sql;

		switch (nuevoEstado) {

		case CONFIRMADO:

			sql =
					"UPDATE turno "
					+ "SET estado = 'CONFIRMADO' "
					+ "WHERE id_turno = ? "
					+ "AND estado = 'PENDIENTE'";

			break;

		case PRESENTE:

			sql =
					"UPDATE turno "
					+ "SET estado = 'PRESENTE' "
					+ "WHERE id_turno = ? "
					+ "AND estado = 'CONFIRMADO'";

			break;

		case CANCELADO:

			sql =
					"UPDATE turno "
					+ "SET estado = 'CANCELADO' "
					+ "WHERE id_turno = ? "
					+ "AND estado IN "
					+ "('PENDIENTE', 'CONFIRMADO')";

			break;

		case PENDIENTE:
		default:

			/*
			 * No se permite retroceder a PENDIENTE.
			 */
			return false;
		}

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idTurno
			);

			int filasAfectadas =
					statement.executeUpdate();

			return filasAfectadas == 1;

		} catch (SQLException e) {

			System.err.println(
					"Error al actualizar el estado del turno."
			);

			e.printStackTrace();

			return false;
		}
	}

	/*
	 * =========================================================
	 * HORARIO OCUPADO
	 * =========================================================
	 */
	@Override
	public boolean horarioOcupado(
			int idDisponibilidad,
			LocalTime hora) {

		if (connection == null
				|| idDisponibilidad <= 0
				|| hora == null) {

			/*
			 * FAIL CLOSED:
			 * ante datos invalidos, bloqueamos la operacion.
			 */
			return true;
		}

		try {

			return horarioOcupadoInterno(
					connection,
					idDisponibilidad,
					hora,
					0,
					false
			);

		} catch (SQLException e) {

			System.err.println(
					"Error al verificar el horario del turno."
			);

			e.printStackTrace();

			/*
			 * FAIL CLOSED.
			 */
			return true;
		}
	}

	/*
	 * =========================================================
	 * HORARIO OCUPADO EXCLUYENDO TURNO
	 * =========================================================
	 */
	@Override
	public boolean horarioOcupadoExceptoTurno(
			int idDisponibilidad,
			LocalTime hora,
			int idTurno) {

		if (connection == null
				|| idDisponibilidad <= 0
				|| hora == null
				|| idTurno <= 0) {

			return true;
		}

		try {

			return horarioOcupadoInterno(
					connection,
					idDisponibilidad,
					hora,
					idTurno,
					false
			);

		} catch (SQLException e) {

			System.err.println(
					"Error al verificar el horario del turno."
			);

			e.printStackTrace();

			return true;
		}
	}

	/*
	 * =========================================================
	 * VALIDACION BASICA
	 * =========================================================
	 */
	private boolean validarDatosBasicos(
			Turno turno) {

		if (turno == null
				|| turno.getFecha() == null
				|| turno.getHora() == null
				|| turno.getEstado() == null
				|| turno.getPaciente() == null
				|| turno.getDisponibilidad() == null) {

			return false;
		}

		if (turno
				.getPaciente()
				.getIdPaciente() <= 0) {

			return false;
		}

		if (turno
				.getDisponibilidad()
				.getIdDisponibilidad() <= 0) {

			return false;
		}

		return true;
	}

	/*
	 * =========================================================
	 * VALIDAR TURNO CONTRA DISPONIBILIDAD
	 * =========================================================
	 */
	private boolean validarTurnoContraDisponibilidad(
			Turno turno,
			Disponibilidad disponibilidad) {

		if (disponibilidad == null) {
			return false;
		}

		/*
		 * La fecha del turno debe coincidir con
		 * la fecha de la disponibilidad.
		 */
		if (!turno
				.getFecha()
				.equals(
						disponibilidad.getFecha()
				)) {

			return false;
		}

		LocalDate hoy =
				LocalDate.now();

		/*
		 * No se permiten turnos en fechas pasadas.
		 */
		if (turno
				.getFecha()
				.isBefore(
						hoy
				)) {

			return false;
		}

		/*
		 * Si el turno es para hoy, su hora tiene que
		 * estar todavia en el futuro.
		 *
		 * Esto corrige el caso señalado por el profesor:
		 * antes solo se comparaba LocalDate.
		 */
		if (turno
				.getFecha()
				.isEqual(
						hoy
				)
				&& !turno
						.getHora()
						.isAfter(
								LocalTime.now()
						)) {

			return false;
		}

		LocalTime inicioTurno =
				turno.getHora();

		LocalTime finTurno =
				inicioTurno.plusMinutes(
						DURACION_TURNO_MINUTOS
				);

		/*
		 * Proteccion ante un eventual desborde de medianoche.
		 */
		if (!finTurno.isAfter(
				inicioTurno)) {

			return false;
		}

		/*
		 * El turno no puede comenzar antes
		 * de la disponibilidad.
		 */
		if (inicioTurno.isBefore(
				disponibilidad.getHoraInicio())) {

			return false;
		}

		/*
		 * El turno completo debe entrar dentro de
		 * la disponibilidad.
		 *
		 * Ejemplo:
		 *
		 * disponibilidad: 10:00 - 14:00
		 *
		 * 13:30 -> valido, termina 14:00
		 * 13:45 -> invalido, termina 14:15
		 */
		if (finTurno.isAfter(
				disponibilidad.getHoraFin())) {

			return false;
		}

		return true;
	}

	/*
	 * =========================================================
	 * HORARIO OCUPADO INTERNO
	 * =========================================================
	 */
	private boolean horarioOcupadoInterno(
			Connection conexion,
			int idDisponibilidad,
			LocalTime nuevaHora,
			int idTurnoExcluir,
			boolean bloquear)
			throws SQLException {

		String sql =
				"SELECT id_turno, hora "
				+ "FROM turno "
				+ "WHERE disponibilidad_id = ? "
				+ "AND estado <> 'CANCELADO' ";

		if (idTurnoExcluir > 0) {

			sql +=
					"AND id_turno <> ? ";
		}

		if (bloquear) {

			sql +=
					"FOR UPDATE";
		}

		try (PreparedStatement statement =
				conexion.prepareStatement(sql)) {

			statement.setInt(
					1,
					idDisponibilidad
			);

			if (idTurnoExcluir > 0) {

				statement.setInt(
						2,
						idTurnoExcluir
				);
			}

			try (ResultSet result =
					statement.executeQuery()) {

				while (result.next()) {

					LocalTime horaExistente =
							result
									.getTime(
											"hora"
									)
									.toLocalTime();

					if (horariosSeSuperponen(
							nuevaHora,
							horaExistente)) {

						return true;
					}
				}
			}
		}

		return false;
	}

	/*
	 * =========================================================
	 * SUPERPOSICION ENTRE TURNOS
	 * =========================================================
	 */
	private boolean horariosSeSuperponen(
			LocalTime horaA,
			LocalTime horaB) {

		LocalTime finA =
				horaA.plusMinutes(
						DURACION_TURNO_MINUTOS
				);

		LocalTime finB =
				horaB.plusMinutes(
						DURACION_TURNO_MINUTOS
				);

		/*
		 * Dos intervalos se superponen cuando:
		 *
		 * inicioA < finB
		 * Y
		 * finA > inicioB
		 *
		 * Ejemplos:
		 *
		 * 10:00 y 10:01 -> superposicion
		 * 10:00 y 10:29 -> superposicion
		 * 10:00 y 10:30 -> NO se superponen
		 */
		return horaA.isBefore(finB)
				&& finA.isAfter(horaB);
	}

	/*
	 * =========================================================
	 * OBTENER DISPONIBILIDAD
	 * =========================================================
	 */
	private Disponibilidad obtenerDisponibilidadPorId(
			Connection conexion,
			int idDisponibilidad,
			boolean bloquear)
			throws SQLException {

		String sql =
				"SELECT "
				+ "id_disponibilidad, "
				+ "fecha, "
				+ "hora_inicio, "
				+ "hora_fin, "
				+ "retraso_estimado "
				+ "FROM disponibilidad "
				+ "WHERE id_disponibilidad = ?";

		if (bloquear) {

			sql +=
					" FOR UPDATE";
		}

		try (PreparedStatement statement =
				conexion.prepareStatement(sql)) {

			statement.setInt(
					1,
					idDisponibilidad
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
		}

		return null;
	}

	/*
	 * =========================================================
	 * OBTENER ESTADO DEL TURNO
	 * =========================================================
	 */
	private EstadoTurno obtenerEstadoTurno(
			Connection conexion,
			int idTurno,
			boolean bloquear)
			throws SQLException {

		String sql =
				"SELECT estado "
				+ "FROM turno "
				+ "WHERE id_turno = ?";

		if (bloquear) {

			sql +=
					" FOR UPDATE";
		}

		try (PreparedStatement statement =
				conexion.prepareStatement(sql)) {

			statement.setInt(
					1,
					idTurno
			);

			try (ResultSet result =
					statement.executeQuery()) {

				if (result.next()) {

					return EstadoTurno.valueOf(
							result.getString(
									"estado"
							)
					);
				}
			}
		}

		return null;
	}

	/*
	 * =========================================================
	 * TRANSACCIONES
	 * =========================================================
	 */
	private void rollbackSilencioso() {

		if (connection == null) {
			return;
		}

		try {

			connection.rollback();

		} catch (SQLException e) {

			System.err.println(
					"No se pudo realizar rollback."
			);

			e.printStackTrace();
		}
	}

	private void restaurarAutoCommit(
			boolean autoCommitOriginal) {

		if (connection == null) {
			return;
		}

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

	/*
	 * =========================================================
	 * CONSULTA BASE
	 * =========================================================
	 */
	private String consultaBase() {

		return "SELECT "
				+ "t.id_turno, "
				+ "t.fecha AS turno_fecha, "
				+ "t.hora AS turno_hora, "
				+ "t.estado, "
				+ "p.id_paciente, "
				+ "p.dni, "
				+ "p.nombre AS paciente_nombre, "
				+ "p.apellido AS paciente_apellido, "
				+ "p.telefono, "
				+ "d.id_disponibilidad, "
				+ "d.fecha AS disponibilidad_fecha, "
				+ "d.hora_inicio, "
				+ "d.hora_fin, "
				+ "d.retraso_estimado "
				+ "FROM turno t "
				+ "INNER JOIN paciente p "
				+ "ON t.paciente_id = p.id_paciente "
				+ "INNER JOIN disponibilidad d "
				+ "ON t.disponibilidad_id = d.id_disponibilidad";
	}

	/*
	 * =========================================================
	 * MAPEO RESULTSET -> TURNO
	 * =========================================================
	 */
	private Turno crearTurnoDesdeResultSet(
			ResultSet result)
			throws SQLException {

		Paciente paciente =
				new Paciente(
						result.getInt(
								"id_paciente"
						),
						result.getString(
								"dni"
						),
						result.getString(
								"paciente_nombre"
						),
						result.getString(
								"paciente_apellido"
						),
						result.getString(
								"telefono"
						)
				);

		Disponibilidad disponibilidad =
				new Disponibilidad(
						result.getInt(
								"id_disponibilidad"
						),
						result.getDate(
								"disponibilidad_fecha"
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

		return new Turno(
				result.getInt(
						"id_turno"
				),
				result.getDate(
						"turno_fecha"
				).toLocalDate(),
				result.getTime(
						"turno_hora"
				).toLocalTime(),
				EstadoTurno.valueOf(
						result.getString(
								"estado"
						)
				),
				paciente,
				disponibilidad
		);
	}
}