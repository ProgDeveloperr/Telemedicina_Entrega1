package DLL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

	private static String URL = "jdbc:mysql://localhost:3306/telemedicina";
	private static String USER = "root";
	private static String PASSWORD = "";

	private static Connection conect;
	private static Conexion instance;

	private Conexion() {
		try {
			conect = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Se conecto correctamente a Telemedicina");
		} catch (SQLException e) {
			System.out.println("No se pudo conectar a Telemedicina");
			e.printStackTrace();
		}
	}

	public static Conexion getInstance() {
		if (instance == null) {
			instance = new Conexion();
		}

		return instance;
	}

	public Connection getConnection() {
		return conect;
	}
}