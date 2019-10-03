import java.io.Serializable;

public class Tallennus implements Serializable{
	
	private Pelaaja pelaaja1;
	private Pelaaja pelaaja2;
	private PelinTilanne pelinTilanne;
	
	public Tallennus() {}
	
	public Tallennus(Pelaaja p1, Pelaaja p2, PelinTilanne pt) {
		pelaaja1=p1;
		pelaaja2=p2;
		pelinTilanne=pt;
	}
	
	public PelinTilanne getPelinTilanne() {
		return pelinTilanne;
	}

	public Pelaaja getPelaaja1() {
		return pelaaja1;
	}

	public Pelaaja getPelaaja2() {
		return pelaaja2;
	}
	
	

}
