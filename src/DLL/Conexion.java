package DLL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

	private static final String ENV_URL =
			"TELEMEDICINA_DB_URL";

	private static final String ENV_USER =
			"TELEMEDICINA_DB_USER";

	private static final String ENV_PASSWORD =
			"TELEMEDICINA_DB_PASSWORD";

	private static Conexion instance;

	private final String url;
	private final String user;
	private final String password;

	private Conexion() {

		/*
		 * Las credenciales ya no se encuentran
		 * hardcodeadas dentro del codigo fuente.
		 *
		 * Se leen desde variables de entorno
		 * configuradas localmente.
		 */
		url =
				obtenerVariableObligatoria(
						ENV_URL
				);

		user =
				obtenerVariableObligatoria(
						ENV_USER
				);

		password =
				obtenerVariableObligatoria(
						ENV_PASSWORD
				);
	}

	/*
	 * =========================================================
	 * SINGLETON
	 * =========================================================
	 */
	public static synchronized Conexion getInstance() {

		if (instance == null) {

			instance =
					new Conexion();
		}

		return instance;
	}

	/*
	 * =========================================================
	 * OBTENER CONEXION
	 * =========================================================
	 */
	public Connection getConnection() {

		try {

			/*
			 * IMPORTANTE:
			 *
			 * Ya no existe una Connection global compartida.
			 *
			 * Cada controller que solicita una conexion
			 * obtiene su propia instancia JDBC.
			 *
			 * De esta manera una transaccion que modifica
			 * autoCommit no afecta a los demas controllers.
			 */
			return DriverManager.getConnection(
					url,
					user,
					password
			);

		} catch (SQLException e) {

			/*
			 * Nunca devolvemos null.
			 *
			 * Antes:
			 *
			 * fallo de conexion
			 * -> conect = null
			 * -> NullPointerException posterior
			 *
			 * Ahora:
			 *
			 * fallo de conexion
			 * -> excepcion controlada
			 */
			throw new IllegalStateException(
					"No se pudo establecer la conexion "
					+ "con la base de datos Telemedicina.",
					e
			);
		}
	}

	/*
	 * =========================================================
	 * PRUEBA DE CONEXION
	 * =========================================================
	 */
	public void probarConexion() {

		try (Connection connection =
				getConnection()) {

			if (!connection.isValid(2)) {

				throw new IllegalStateException(
						"La conexion con Telemedicina "
						+ "no es valida."
				);
			}

			System.out.println(
					"Se conecto correctamente a Telemedicina"
			);

		} catch (SQLException e) {

			throw new IllegalStateException(
					"No se pudo validar la conexion "
					+ "con Telemedicina.",
					e
			);
		}
	}

	/*
	 * =========================================================
	 * VARIABLES DE ENTORNO
	 * =========================================================
	 */
	private String obtenerVariableObligatoria(
			String nombre) {

		String valor =
				System.getenv(
						nombre
				);

		if (valor == null
				|| valor.trim().isEmpty()) {

			throw new IllegalStateException(
					"Falta configurar la variable "
					+ "de entorno: "
					+ nombre
			);
		}

		return valor.trim();
	}
}