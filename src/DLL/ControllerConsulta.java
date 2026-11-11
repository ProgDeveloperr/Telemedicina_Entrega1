package DLL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;

import BLL.Consulta;
import BLL.Disponibilidad;
import BLL.EstadoTurno;
import BLL.Paciente;
import BLL.Turno;
import repository.ConsultaRepository;

public class ControllerConsulta
		implements ConsultaRepository {

	private Connection connection;

	public ControllerConsulta() {
		connection =
				Conexion.getInstance().getConnection();
	}

	@Override
	public boolean registrar(Consulta consulta) {

		if (!validarConsulta(consulta)) {
			return false;
		}

		String sql =
				"INSERT INTO consulta "
				+ "(fecha_hora_atencion, "
				+ "observaciones, "
				+ "seguimiento, "
				+ "turno_id) "
				+ "VALUES (?, ?, ?, ?)";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setTimestamp(
					1,
					Timestamp.valueOf(
							consulta.getFechaHoraAtencion()
					)
			);

			statement.setString(
					2,
					consulta.getObservaciones()
			);

			statement.setString(
					3,
					consulta.getSeguimiento()
			);

			statement.setInt(
					4,
					consulta.getTurno().getIdTurno()
			);

			int filasAfectadas =
					statement.executeUpdate();

			return filasAfectadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean existePorTurno(int idTurno) {

		String sql =
				"SELECT id_consulta "
				+ "FROM consulta "
				+ "WHERE turno_id = ?";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(1, idTurno);

			try (ResultSet result =
					statement.executeQuery()) {

				return result.next();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public LinkedList<Consulta> obtenerPorProfesional(
			int idProfesional) {

		LinkedList<Consulta> consultas =
				new LinkedList<Consulta>();

		String sql =
				consultaBase()
				+ " WHERE d.profesional_id = ? "
				+ "ORDER BY c.fecha_hora_atencion DESC";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idProfesional
			);

			try (ResultSet result =
					statement.executeQuery()) {

				while (result.next()) {

					consultas.add(
							crearConsultaDesdeResultSet(
									result
							)
					);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return consultas;
	}

	@Override
	public LinkedList<Consulta> obtenerPorPaciente(
			int idPaciente) {

		LinkedList<Consulta> consultas =
				new LinkedList<Consulta>();

		String sql =
				consultaBase()
				+ " WHERE p.id_paciente = ? "
				+ "ORDER BY c.fecha_hora_atencion DESC";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idPaciente
			);

			try (ResultSet result =
					statement.executeQuery()) {

				while (result.next()) {

					consultas.add(
							crearConsultaDesdeResultSet(
									result
							)
					);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return consultas;
	}

	private boolean validarConsulta(
			Consulta consulta) {

		if (consulta == null
				|| consulta.getFechaHoraAtencion() == null
				|| consulta.getObservaciones() == null
				|| consulta.getObservaciones()
						.trim().isEmpty()
				|| consulta.getTurno() == null
				|| consulta.getTurno()
						.getIdTurno() <= 0) {

			return false;
		}

		if (!turnoEstaPresente(
				consulta.getTurno().getIdTurno())) {

			return false;
		}

		return !existePorTurno(
				consulta.getTurno().getIdTurno()
		);
	}

	private boolean turnoEstaPresente(
			int idTurno) {

		String sql =
				"SELECT id_turno "
				+ "FROM turno "
				+ "WHERE id_turno = ? "
				+ "AND estado = 'PRESENTE'";

		try (PreparedStatement statement =
				connection.prepareStatement(sql)) {

			statement.setInt(
					1,
					idTurno
			);

			try (ResultSet result =
					statement.executeQuery()) {

				return result.next();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	private String consultaBase() {

		return "SELECT "
				+ "c.id_consulta, "
				+ "c.fecha_hora_atencion, "
				+ "c.observaciones, "
				+ "c.seguimiento, "

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

				+ "FROM consulta c "

				+ "INNER JOIN turno t "
				+ "ON c.turno_id = t.id_turno "

				+ "INNER JOIN paciente p "
				+ "ON t.paciente_id = p.id_paciente "

				+ "INNER JOIN disponibilidad d "
				+ "ON t.disponibilidad_id = "
				+ "d.id_disponibilidad";
	}

	private Consulta crearConsultaDesdeResultSet(
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

		Turno turno =
				new Turno(
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

		return new Consulta(
				result.getInt(
						"id_consulta"
				),
				result.getTimestamp(
						"fecha_hora_atencion"
				).toLocalDateTime(),
				result.getString(
						"observaciones"
				),
				result.getString(
						"seguimiento"
				),
				turno
		);
	}
}