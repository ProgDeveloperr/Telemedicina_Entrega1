package repository;

import org.mindrot.jbcrypt.BCrypt;

public interface Hashing {

	// Se usa cuando guardamos una contraseña.
	public static String hash(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	// Se usa cuando traemos el hash desde la base de datos.
	public static boolean verificar(String password, String hash) {
		return BCrypt.checkpw(password, hash);
	}

}