public class Partido {
	String fecha;
	int espectadores;
	String estadio;
	
	String jugadorLocal;
	String puntosLocal;
	String jugadorVisitante;
	String puntosVisitante;
	
	
	// Constructores
	//---------------------------------------------------------------
	public Partido() {
		this.fecha = "";
		this.espectadores = 0;
		this.estadio = "";
		this.jugadorLocal = "";
		this.puntosLocal = "";
		this.jugadorVisitante = "";
		this.puntosVisitante = "";
	}
	
	public Partido(String fecha, int espectadores, String estadio, String local, String puntosLocal, String visitante, String puntosVisitante) {
		this.fecha = fecha;
		this.espectadores = espectadores;
		this.estadio = estadio;
		this.jugadorLocal = local;
		this.puntosLocal = puntosLocal;
		this.jugadorVisitante = visitante;
		this.puntosVisitante = puntosVisitante;
	}
	
	// Getters & Setters
	//---------------------------------------------------------------
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public int getEspectadores() {
		return espectadores;
	}
	public void setEspectadores(int espectadores) {
		this.espectadores = espectadores;
	}
	public String getEstadio() {
		return estadio;
	}
	public void setEstadio(String estadio) {
		this.estadio = estadio;
	}
	public String getJugadorLocal() {
		return jugadorLocal;
	}
	public void setJugadorLocal(String local) {
		this.jugadorLocal = local;
	}
	public String getPuntosLocal() {
		return puntosLocal;
	}
	public void setPuntosLocal(String puntosLocal) {
		this.puntosLocal = puntosLocal;
	}
	public String getJugadorVisitante() {
		return jugadorVisitante;
	}
	public void setJugadorVisitante(String visitante) {
		this.jugadorVisitante = visitante;
	}
	public String getPuntosVisitante() {
		return puntosVisitante;
	}
	public void setPuntosVisitante(String puntosVisitante) {
		this.puntosVisitante = puntosVisitante;
	}
	
	
}
