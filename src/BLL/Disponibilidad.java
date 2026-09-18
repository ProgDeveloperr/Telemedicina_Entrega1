package BLL;

import java.time.LocalDate;
import java.time.LocalTime;

public class Disponibilidad {

	private int idDisponibilidad;
	private LocalDate fecha;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private int retrasoEstimado;

	public Disponibilidad(int idDisponibilidad, LocalDate fecha, LocalTime horaInicio,
			LocalTime horaFin, int retrasoEstimado) {

		this.idDisponibilidad = idDisponibilidad;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.retrasoEstimado = retrasoEstimado;
	}

	public int getIdDisponibilidad() {
		return idDisponibilidad;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public int getRetrasoEstimado() {
		return retrasoEstimado;
	}

	public boolean esHorarioValido(LocalTime hora) {
		return !hora.isBefore(horaInicio) && hora.isBefore(horaFin);
	}

	public boolean tieneRetraso() {
		return retrasoEstimado > 0;
	}
}