package GUI;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import DLL.Conexion;

public class Main {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {

			try {

				UIManager.setLookAndFeel(
						UIManager
								.getSystemLookAndFeelClassName()
				);

			} catch (Exception e) {

				e.printStackTrace();
			}

			/*
			 * =================================================
			 * PREFLIGHT DE BASE DE DATOS
			 * =================================================
			 *
			 * Antes de construir controllers o abrir el login,
			 * verificamos que la configuracion y la conexion
			 * sean validas.
			 *
			 * De esta manera evitamos continuar con una
			 * Connection null.
			 */
			try {

				Conexion
						.getInstance()
						.probarConexion();

			} catch (IllegalStateException e) {

				JOptionPane.showMessageDialog(
						null,
						"No se pudo iniciar el sistema porque "
								+ "no fue posible conectar con la base de datos.\n\n"
								+ "Verifique que MySQL se encuentre iniciado "
								+ "y que las variables de entorno de "
								+ "Telemedicina esten configuradas correctamente.",
						"Error de conexion",
						JOptionPane.ERROR_MESSAGE
				);

				System.err.println(
						e.getMessage()
				);

				e.printStackTrace();

				return;
			}

			LoginFrame login =
					new LoginFrame();

			login.setVisible(
					true
			);
		});
	}
}