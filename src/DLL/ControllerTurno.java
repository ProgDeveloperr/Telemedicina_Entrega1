package DLL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.LinkedList;

import BLL.Disponibilidad;
import BLL.EstadoTurno;
import BLL.Paciente;
import BLL.Turno;
import repository.TurnoRepository;

public class ControllerTurno implements TurnoRepository {

	private Connection connection;

	public ControllerTurno() {
		connection = Conexion.getInstance().getConnection();
	}

	@Override
	public boolean registrar(Turno turno) {

		if (!validarTurno(turno)) {
			return false;
		}

		String sql = "INSERT INTO turno "
				+ "(fecha, hora, estado, paciente_id, disponibilidad_id) "
				+ "VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setDate(
					1,
					Date.valueOf(turno.getFecha())
			);

			statement.setTime(
					2,
					Time.valueOf(turno.getHora())
			);

			statement.setString(
					3,
					turno.getEstado().name()
			);

			statement.setInt(
					4,
					turno.getPaciente().getIdPaciente()
			);

			statement.setInt(
					5,
					turno.getDisponibilidad().getIdDisponibilidad()
			);

			int filasAfectadas = statement.executeUpdate();

			return filasAfectadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public Turno buscarPorId(int idTurno) {

		String sql = consultaBase()
				+ " WHERE t.id_turno = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, idTurno);

			try (ResultSet result = statement.executeQuery()) {

				if (result.next()) {
					return crearTurnoDesdeResultSet(result);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public LinkedList<Turno> obtenerTodos() {

		LinkedList<Turno> turnos =
				new LinkedList<Turno>();

		String sql = consultaBase()
				+ " ORDER BY t.fecha, t.hora";

		try (PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet result = statement.executeQuery()) {

			while (result.next()) {

				turnos.add(
						crearTurnoDesdeResultSet(result)
				);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return turnos;
	}

	@Override
	public LinkedList<Turno> obtenerPorProfesional(
			int idProfesional) {

		LinkedList<Turno> turnos =
				new LinkedList<Turno>();

		String sql = consultaBase()
				+ " WHERE d.profesional_id = ? "
				+ "ORDER BY t.fecha, t.hora";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, idProfesional);

			try (ResultSet result = statement.executeQuery()) {

				while (result.next()) {

					turnos.add(
							crearTurnoDesdeResultSet(result)
					);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return turnos;
	}

	@Override
	public boolean modificar(Turno turno) {

		if (!validarTurno(turno)) {
			return false;
		}

		String sql = "UPDATE turno "
				+ "SET fecha = ?, "
				+ "hora = ?, "
				+ "paciente_id = ?, "
				+ "disponibilidad_id = ? "
				+ "WHERE id_turno = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setDate(
					1,
					Date.valueOf(turno.getFecha())
			);

			statement.setTime(
					2,
					Time.valueOf(turno.getHora())
			);

			statement.setInt(
					3,
					turno.getPaciente().getIdPaciente()
			);

			statement.setInt(
					4,
					turno.getDisponibilidad().getIdDisponibilidad()
			);

			statement.setInt(
					5,
					turno.getIdTurno()
			);

			int filasAfectadas = statement.executeUpdate();

			return filasAfectadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean actualizarEstado(
			int idTurno,
			EstadoTurno estado) {

		if (estado == null) {
			return false;
		}

		String sql = "UPDATE turno "
				+ "SET estado = ? "
				+ "WHERE id_turno = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(
					1,
					estado.name()
			);

			statement.setInt(
					2,
					idTurno
			);

			int filasAfectadas = statement.executeUpdate();

			return filasAfectadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean validarTurno(Turno turno) {

		if (turno == null
				|| turno.getFecha() == null
				|| turno.getHora() == null
				|| turno.getEstado() == null
				|| turno.getPaciente() == null
				|| turno.getDisponibilidad() == null) {

			return false;
		}

		Disponibilidad disponibilidad =
				obtenerDisponibilidadPorId(
						turno.getDisponibilidad()
								.getIdDisponibilidad()
				);

		if (disponibilidad == null) {
			return false;
		}

		if (!turno.getFecha().equals(
				disponibilidad.getFecha())) {

			return false;
		}

		return disponibilidad.esHorarioValido(
				turno.getHora()
		);
	}

	private Disponibilidad obtenerDisponibilidadPorId(
			int idDisponibilidad) {

		String sql = "SELECT "
				+ "id_disponibilidad, "
				+ "fecha, "
				+ "hora_inicio, "
				+ "hora_fin, "
				+ "retraso_estimado "
				+ "FROM disponibilidad "
				+ "WHERE id_disponibilidad = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idDisponibilidad
			);

			try (ResultSet result = statement.executeQuery()) {

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
			e.printStackTrace();
		}

		return null;
	}

	
	@Override
	public boolean horarioOcupado(
			int idDisponibilidad,
			java.time.LocalTime hora) {

		String sql = "SELECT id_turno "
				+ "FROM turno "
				+ "WHERE disponibilidad_id = ? "
				+ "AND hora = ? "
				+ "AND estado <> 'CANCELADO'";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, idDisponibilidad);

			statement.setTime(
					2,
					Time.valueOf(hora)
			);

			try (ResultSet result = statement.executeQuery()) {
				return result.next();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
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

	private Turno crearTurnoDesdeResultSet(
			ResultSet result) throws SQLException {

		Paciente paciente = new Paciente(
				result.getInt("id_paciente"),
				result.getString("dni"),
				result.getString("paciente_nombre"),
				result.getString("paciente_apellido"),
				result.getString("telefono")
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
				result.getInt("id_turno"),
				result.getDate(
						"turno_fecha"
				).toLocalDate(),
				result.getTime(
						"turno_hora"
				).toLocalTime(),
				EstadoTurno.valueOf(
						result.getString("estado")
				),
				paciente,
				disponibilidad
		);
	}
}