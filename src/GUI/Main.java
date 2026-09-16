package GUI;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import BLL.Usuario;
import DLL.Conexion;
import DLL.ControllerUsuario;

public class Main {

	public static void main(String[] args) {

		Conexion.getInstance();

		ControllerUsuario controllerUsuario = new ControllerUsuario();

		JTextField campoUsuario = new JTextField();
		JPasswordField campoClave = new JPasswordField();

		Object[] campos = {
				"Usuario:", campoUsuario,
				"Contraseña:", campoClave
		};

		int opcion = JOptionPane.showConfirmDialog(
				null,
				campos,
				"Telemedicina - Inicio de sesión",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);

		if (opcion != JOptionPane.OK_OPTION) {
			return;
		}

		String nombreUsuario = campoUsuario.getText().trim();
		String clave = new String(campoClave.getPassword());

		if (nombreUsuario.isEmpty() || clave.isEmpty()) {
			JOptionPane.showMessageDialog(
					null,
					"Debe ingresar usuario y contraseña.",
					"Datos incompletos",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		Usuario logueado = controllerUsuario.login(nombreUsuario, clave);

		if (logueado == null) {

			JOptionPane.showMessageDialog(
					null,
					"Usuario o contraseña incorrectos.",
					"Error de autenticación",
					JOptionPane.ERROR_MESSAGE
			);

			return;
		}

		JOptionPane.showMessageDialog(
				null,
				"Bienvenido: "
						+ logueado.getNombre() + " "
						+ logueado.getApellido()
						+ "\nPerfil: "
						+ logueado.getTipoUsuario(),
				"Inicio de sesión correcto",
				JOptionPane.INFORMATION_MESSAGE
		);

		logueado.MenuPrincipal();
	}
}