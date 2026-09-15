package GUI;
import javax.swing.JOptionPane;

import BLL.Menu;
import BLL.TipoUsuario;
import BLL.Usuario;


public class Main {

	public static void main(String[] args) {

		// Usuarios de demostracion para visualizar los dos perfiles de la maqueta.
		Usuario.getUsuarios().add(
				new Usuario(1, "Ana", "Recepcion", "recepcion", "1234", TipoUsuario.EMPLEADO));

		Usuario.getUsuarios().add(
				new Usuario(2, "Carlos", "Medico", "medico", "1234", TipoUsuario.PROFESIONAL));

		String[] perfiles = {
				"Empleado / Recepcionista",
				"Medico / Profesional",
				"Salir"
		};

		int perfil = JOptionPane.showOptionDialog(
				null,
				"Seleccione un perfil para visualizar la maqueta",
				"Telemedicina - Entrega 1",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				perfiles,
				perfiles[0]);

		if (perfil == 2 || perfil == JOptionPane.CLOSED_OPTION) {
			return;
		}

		String usuarioDemo = perfil == 0 ? "recepcion" : "medico";
		String claveDemo = "1234";

		Usuario logueado = Menu.Login(usuarioDemo, claveDemo);

		if (logueado == null) {
			JOptionPane.showMessageDialog(null, "No se encontro el usuario.");
		} else {
			JOptionPane.showMessageDialog(
					null,
					"Bienvenido: " + logueado.getNombre() + " " + logueado.getApellido()
							+ "\nPerfil: " + logueado.getTipoUsuario());

			logueado.MenuPrincipal();
		}
	}
}
