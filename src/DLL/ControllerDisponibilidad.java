package DLL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.LinkedList;

import BLL.Disponibilidad;
import repository.DisponibilidadRepository;

public class ControllerDisponibilidad implements DisponibilidadRepository {

	private Connection connection;

	public ControllerDisponibilidad() {
		connection = Conexion.getInstance().getConnection();
	}

	@Override
	public boolean registrar(Disponibilidad disponibilidad, int idProfesional) {

		String sql = "INSERT INTO disponibilidad "
				+ "(fecha, hora_inicio, hora_fin, profesional_id) "
				+ "VALUES (?, ?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setDate(
					1,
					Date.valueOf(disponibilidad.getFecha())
			);

			statement.setTime(
					2,
					Time.valueOf(disponibilidad.getHoraInicio())
			);

			statement.setTime(
					3,
					Time.valueOf(disponibilidad.getHoraFin())
			);

			statement.setInt(4, idProfesional);

			int filasAfectadas = statement.executeUpdate();

			return filasAfectadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public LinkedList<Disponibilidad> obtenerPorProfesional(
			int idProfesional) {

		LinkedList<Disponibilidad> disponibilidades =
				new LinkedList<Disponibilidad>();

		String sql = "SELECT id_disponibilidad, fecha, hora_inicio, "
				+ "hora_fin, retraso_estimado "
				+ "FROM disponibilidad "
				+ "WHERE profesional_id = ? "
				+ "ORDER BY fecha, hora_inicio";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, idProfesional);

			try (ResultSet result = statement.executeQuery()) {

				while (result.next()) {

					disponibilidades.add(
							new Disponibilidad(
									result.getInt("id_disponibilidad"),
									result.getDate("fecha").toLocalDate(),
									result.getTime("hora_inicio").toLocalTime(),
									result.getTime("hora_fin").toLocalTime(),
									result.getInt("retraso_estimado")
							)
					);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return disponibilidades;
	}
}