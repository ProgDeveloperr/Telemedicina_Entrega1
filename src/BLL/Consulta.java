package BLL;

import java.time.LocalDateTime;

public class Consulta {

	private int idConsulta;
	private LocalDateTime fechaHoraAtencion;
	private String observaciones;
	private String seguimiento;
	private Turno turno;

	public Consulta(
			int idConsulta,
			LocalDateTime fechaHoraAtencion,
			String observaciones,
			String seguimiento,
			Turno turno) {

		this.idConsulta = idConsulta;
		this.fechaHoraAtencion = fechaHoraAtencion;
		this.observaciones = observaciones;
		this.seguimiento = seguimiento;
		this.turno = turno;
	}

	public int getIdConsulta() {
		return idConsulta;
	}

	public LocalDateTime getFechaHoraAtencion() {
		return fechaHoraAtencion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public String getSeguimiento() {
		return seguimiento;
	}

	public Turno getTurno() {
		return turno;
	}

	public String obtenerResumen() {

		String resumen =
				"Observaciones: " + observaciones;

		if (seguimiento != null
				&& !seguimiento.trim().isEmpty()) {

			resumen += "\nSeguimiento: "
					+ seguimiento;
		}

		return resumen;
	}
}