import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {

	private int idTurno;
	private LocalDate fecha;
	private LocalTime hora;
	private EstadoTurno estado;
	private Paciente paciente;
	private Disponibilidad disponibilidad;

	public Turno(int idTurno, LocalDate fecha, LocalTime hora, EstadoTurno estado, Paciente paciente,
			Disponibilidad disponibilidad) {
		this.idTurno = idTurno;
		this.fecha = fecha;
		this.hora = hora;
		this.estado = estado;
		this.paciente = paciente;
		this.disponibilidad = disponibilidad;
	}

	// Metodos declarados como base. No implementan logica en la Entrega 1.
	public void confirmar() {
	}

	public void cancelar() {
	}

	public void marcarLlegada() {
	}

	public boolean estaCancelado() {
		return false;
	}

	public boolean estaConfirmado() {
		return false;
	}
}
