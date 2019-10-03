import java.io.Serializable;
import java.util.ArrayList;

public class Laiva implements Serializable {

	private ArrayList<Ruutu> sijainti;
	private int[][] koordinaatit;
	private boolean uponnut;
	private String nimi;
	private int pituus;
	
	public String getNimi() {
		return nimi;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}

	public int getPituus() {
		return pituus;
	}

	public Laiva(String nimi, int pituus) {
		this.nimi=nimi;
		this.pituus=pituus;
	}
	
	public Laiva(ArrayList<Ruutu> sijainti) {
		super();
		this.sijainti=sijainti;
		uponnut=false;
	}
	
	public ArrayList<Ruutu> getSijainti() {
		return sijainti;
	}

	public void setSijainti(ArrayList<Ruutu> sijainti) {
		this.sijainti = sijainti;
	}
	
	public void setKoordinaatit(int[][] koordinaatit) {
		this.koordinaatit=koordinaatit;
	}
	
	public int[][] getKoordinaatit(){
		return koordinaatit;
	}
	
	
	public boolean isUponnut() {
		return uponnut;
	}
	
	public void setUponnut(boolean onkoUponnut) {
		this.uponnut = onkoUponnut;
	}
	
	public boolean getUponnut() {
		return uponnut;
	}
	
}
