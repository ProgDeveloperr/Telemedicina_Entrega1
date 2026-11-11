package GUI;

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

			Conexion.getInstance();

			LoginFrame login =
					new LoginFrame();

			login.setVisible(true);
		});
	}
}