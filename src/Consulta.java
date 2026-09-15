import java.time.LocalDateTime;

public class Consulta {

	private int idConsulta;
	private LocalDateTime fechaHoraAtencion;
	private String observaciones;
	private String seguimiento;
	private Turno turno;

	public Consulta(int idConsulta, LocalDateTime fechaHoraAtencion, String observaciones, String seguimiento,
			Turno turno) {
		this.idConsulta = idConsulta;
		this.fechaHoraAtencion = fechaHoraAtencion;
		this.observaciones = observaciones;
		this.seguimiento = seguimiento;
		this.turno = turno;
	}

	// Stub preparado para una etapa posterior.
	public String obtenerResumen() {
		return "";
	}
}
