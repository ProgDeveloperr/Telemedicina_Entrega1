package BLL;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {

	private int idTurno;
	private LocalDate fecha;
	private LocalTime hora;
	private EstadoTurno estado;
	private Paciente paciente;
	private Disponibilidad disponibilidad;

	public Turno(
			int idTurno,
			LocalDate fecha,
			LocalTime hora,
			EstadoTurno estado,
			Paciente paciente,
			Disponibilidad disponibilidad) {

		this.idTurno = idTurno;
		this.fecha = fecha;
		this.hora = hora;
		this.estado = estado;
		this.paciente = paciente;
		this.disponibilidad = disponibilidad;
	}

	public int getIdTurno() {
		return idTurno;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public LocalTime getHora() {
		return hora;
	}

	public EstadoTurno getEstado() {
		return estado;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public Disponibilidad getDisponibilidad() {
		return disponibilidad;
	}

	public void confirmar() {
		estado = EstadoTurno.CONFIRMADO;
	}

	public void cancelar() {
		estado = EstadoTurno.CANCELADO;
	}

	public void marcarLlegada() {
		estado = EstadoTurno.PRESENTE;
	}

	public boolean estaPendiente() {
		return estado == EstadoTurno.PENDIENTE;
	}

	public boolean estaConfirmado() {
		return estado == EstadoTurno.CONFIRMADO;
	}

	public boolean estaCancelado() {
		return estado == EstadoTurno.CANCELADO;
	}

	public boolean estaPresente() {
		return estado == EstadoTurno.PRESENTE;
	}
}