package BLL;
import java.time.LocalDate;
import java.time.LocalTime;

public class Disponibilidad {

	private int idDisponibilidad;
	private LocalDate fecha;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private int retrasoEstimado;

	public Disponibilidad(int idDisponibilidad, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin,
			int retrasoEstimado) {
		this.idDisponibilidad = idDisponibilidad;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.retrasoEstimado = retrasoEstimado;
	}

	// Stubs preparados para una etapa posterior.
	public boolean esHorarioValido(LocalTime hora) {
		return false;
	}

	public boolean tieneRetraso() {
		return false;
	}
}
