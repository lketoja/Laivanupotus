import java.io.Serializable;
import java.util.Scanner;

public class Pelaaja implements Serializable{
	protected Pelilauta pelilauta;
	protected String nimi;
	
	public String getNimi() {
		return nimi;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}

	public Pelaaja() {
		pelilauta = new Pelilauta();
	}
	
	public Pelilauta getPelilauta() {
		return pelilauta;
	}
	
	public void setPelilauta(Pelilauta pl) {
		pelilauta=pl;
	}
	
	public void syotaLaivat(Scanner s) {}
	
	public void pelaaVuoro(PelinTilanne pt, Pelaaja p, Scanner s) {}
	
	
	
	/**
	 * Metodi saa parametrinaan laivan ensimm�isen koordinaattiparin
	 * (kokonaislukutaulukko-muodossa) sek� laivan halutun suunnan ("alas" tai
	 * "oikealle") ja laivan pituuden. N�iden tietojen perusteella metodi generoi
	 * kaikkien niiden ruutujen koordinaatit, joissa kyseisen laivan halutaan
	 * olevan. Metodi palauttaa koordinaatit kokonaislukumatriisina (yksi rivi
	 * vastaa yht� koordinaattiparia).
	 * 
	 * @param koordinaatti kaksi lukua sis�lt�v� kokonaislukutaulukko, joka kertoo
	 *                     laivan ensimm�isen ruudun sijainnin
	 * @param suunta       merkkijono, joka kertoo mihin suuntaan laivaa halutaan
	 *                     jatkaa l�ht�ruudusta ("alas" tai "oikealle")
	 * @param laivanPituus kertoo laivan pituuden (kokonaislukuna)
	 * @return
	 */
	public static int[][] generoiKoordinaatit(int[] koordinaatti, String suunta, int laivanPituus) {
		int[][] koordinaatit = new int[laivanPituus][2];
		if (suunta.equals("alas")) {
			for (int i = 0; i < laivanPituus; i++) {
				koordinaatit[i][0] = koordinaatti[0] + i;
			}
			for (int i = 0; i < laivanPituus; i++) {
				koordinaatit[i][1] = koordinaatti[1];
			}
		}
		if (suunta.equals("oikealle")) {
			for (int i = 0; i < laivanPituus; i++) {
				koordinaatit[i][0] = koordinaatti[0];
			}
			for (int i = 0; i < laivanPituus; i++) {
				koordinaatit[i][1] = koordinaatti[1] + i;
			}
		}
		return koordinaatit;
	}
	
	/**
	 * Metodi saa parametrinaan edellisen metodin (generoiKoordinaatit())
	 * palauttaman kokonaislukumatriisin, joka sis�lt�� koordinaattipareja (yksi per
	 * rivi). N�m� koordinaatit kertovat minne pelaaja haluaisi asettaa laivansa.
	 * T�m� metodi tarkistaa, ovatko kaikki koordinaatit sallittuja.
	 * Koordinaattipari on sallittu, mik�li molemmat luvut ovat v�lill� 0-9 (t�ll�in
	 * laiva mahtuu laudalle).
	 * 
	 * @param koordinaatit kokonaislukumatriisi, jonka jokainen rivi sis�lt�� tiedon
	 *                     yhdest� koordinaatista
	 * @return true, jos laiva mahtuu laudalle eli matriisin kaikki numerot ovat
	 *         v�lill� 0-9.
	 */
	public static boolean mahtuukoLaudalle(int[][] koordinaatit) {
		for (int i = 0; i < koordinaatit.length; i++) {
			for (int j = 0; j < koordinaatit[0].length; j++) {
				if (koordinaatit[i][j] < 0 || koordinaatit[i][j] > 9) {
					return false;
				}
			}
		}
		return true;
	}
}
