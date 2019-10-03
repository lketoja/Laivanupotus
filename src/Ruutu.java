import java.io.Serializable;

public class Ruutu implements Serializable{

	private Laiva laiva;
	private boolean osuma;
	
	public Ruutu() {}

	public Laiva getLaiva() {
		return laiva;
	}

	public void setLaiva(Laiva laiva) {
		this.laiva = laiva;
	}

	public boolean isOsuma() {
		return osuma;
	}

	public void setOsuma(boolean osuma) {
		this.osuma = osuma;
	}
	
}
