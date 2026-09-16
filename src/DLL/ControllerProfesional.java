package DLL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import BLL.Profesional;
import repository.ProfesionalRepository;

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
}