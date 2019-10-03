import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Pelilauta implements Serializable {

	private Ruutu[][] pelilauta;
	private ArrayList<Laiva> laivat;

	/**
	 * Konstruktori luo uuden Ruutu-tyyppisiï¿½ muuttujia sisï¿½ltï¿½vï¿½n taulukon
	 * ja tallentaa sen attribuuttiin pelilauta. Jokaiseen taulukon alkioon luodaan
	 * ja tallennetaan uusi, "tyhjï¿½" Ruutu-olio. Konstruktori luo myï¿½s uuden
	 * Laiva-tyyppisiï¿½ muuttujia sisï¿½ltï¿½vï¿½n listan ja tallentaa sen
	 * attribuuttiin laivat. Kaikki pelaajan laivat luodaan ja lisï¿½tï¿½ï¿½n
	 * laivat-listaan yksitellen.
	 */
	public Pelilauta() {
		pelilauta = new Ruutu[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				pelilauta[i][j] = new Ruutu();
			}
		}
		laivat = new ArrayList<Laiva>();
		laivat.add(new Laiva("lentotukialus", 5));
		laivat.add(new Laiva("taistelulaiva", 4));
		laivat.add(new Laiva("taistelulaiva", 4));
		laivat.add(new Laiva("risteilija", 3));
		laivat.add(new Laiva("risteilija", 3));
		laivat.add(new Laiva("havittaja", 2));
		laivat.add(new Laiva("havittaja", 2));

	}

	/**
	 * Metodi saa parametrinaan ammuksen koordinaatin kokonaislukutaulukkona. Metodi
	 * merkitsee, että kyseiseen ruutuun on ammuttu. Sen jälkeen metodi tarkistaa,
	 * onko kyseisessä ruudussa laivaa ja palauttaa true tai false vastaavasti.
	 * 
	 * @param koordinaatti johon ammuttiin (kokonaislukutaulukko)
	 * @return true, jos ruudussa on laiva
	 */
	public boolean osuiko(int[] koordinaatti) {
		int a = koordinaatti[0];
		int b = koordinaatti[1];
		pelilauta[a][b].setOsuma(true);
		if (pelilauta[a][b].getLaiva() != null) {
			return true;
		}
		return false;
	}

	/**
	 * Metodi saa parametrinaan ammuksen koordinaatin kokonaislukutaulukkona. Metodi
	 * etsii ensin sen laivan, joka sijaitsee kyseisessä ruudussa. Sen jälkeen
	 * metodi käy läpi kaikki tämän kyseisen laivan ruudut. Jos johonkin näistä
	 * ruuduista ei ole ammuttu, niin metodi palauttaa 'null'. Mikäli kaikkiin
	 * laivan ruutuihin on jo ammuttu, niin metodi merkitsee laivan uponneeksi ja
	 * palauttaa viittauksen kyseiseen laivaan.
	 * 
	 * @param koordinaatti johon ammuttiin (kokonaislukutaulukko)
	 * @return 'null', mikäli laiva ei uponnut ja viittauksen laivaan mikäli
	 *         kyseinen laiva upposi
	 */
	public Laiva upposikoLaiva(int[] koordinaatti) {
		Laiva laiva = pelilauta[koordinaatti[0]][koordinaatti[1]].getLaiva();
		for (Ruutu r : laiva.getSijainti()) {
			if (r.isOsuma() == false) {
				return null;
			}
		}
		laiva.setUponnut(true);
		return laiva;
	}

	/**
	 * Metodi saa parametrinaan Laivan, joka on juuri upotettu. Metodi merkitsee
	 * laivan viereiset ruudut ikään kuin niihinkin olisi osuttu (koska sääntöjen
	 * mukaan laivat eivät voi sijaita toistensa vieressä). (Nyt ruudulle
	 * tulostuvasta pelilaudasta hahmottaa selkeästi, että laiva on uponnut. Lisäksi
	 * tietokonepelaaja ei ammu uponneen laivan viereen.)
	 * 
	 * @param laiva joka on juuri uponnut
	 */
	public void merkitseUponnutLaiva(Laiva laiva) {
		int[][] koordinaatit = laiva.getKoordinaatit();
		for (int i = 0; i < koordinaatit.length; i++) {
			int a = koordinaatit[i][0];
			int b = koordinaatit[i][1];
			if (a > 0) {
				pelilauta[a - 1][b].setOsuma(true); // ylï¿½puolelle
			}
			if (a < 9) {
				pelilauta[a + 1][b].setOsuma(true); // alapuolelle
			}
			if (b < 9) {
				pelilauta[a][b + 1].setOsuma(true); // oikealle
			}
			if (b > 0) {
				pelilauta[a][b - 1].setOsuma(true); // vasemmalle
			}
		}
	}

	/**
	 * Metodi tarkistaa, että ovatko kaikki laivat uponneet. Jos kaikki laivat ovat
	 * uponneet, peli on päättynyt.
	 * 
	 * @return true, jos kaikki laivat ovat uponneet
	 */
	public boolean kaikkiLaivatUponneet() {
		for (Laiva laiva : laivat) {
			if (laiva.getUponnut() == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Metodi luo uuden char-tyyppisen 10x10-matriisin. Metodi tarkistaa jokaisen
	 * matriisin alkion kohdalla, ettï¿½ onko pelilaudan vastaavaan kohtaan
	 * tallennettu laiva. Mikï¿½li laivaa ei lï¿½ydy, metodi tallentaa tï¿½hï¿½n
	 * kohtaan merkin '-'. Mikï¿½li laiva lï¿½ytyy, matriisin alkioksi tallennetaan
	 * merkki 'X'.
	 * 
	 * @return ruudukko char-tyyppinen matriisi, joka kuvaa tilannetta laivojen syöttämisen
	 *         aikana
	 */
	public char[][] muodostaAloitusRuudukko() {
		char[][] ruudukko = new char[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (pelilauta[i][j].getLaiva() == null) {
					ruudukko[i][j] = '-';
				} else {
					ruudukko[i][j] = 'X';
				}
			}
		}
		return ruudukko;
	}

	/**
	 * Metodi luo uuden char-tyyppisen 10x10-matriisin. Metodi tarkistaa jokaisen
	 * matriisin alkion kohdalla, ettï¿½ onko pelilaudan vastaavaan kohtaan osunut
	 * ammus. Jos ruutuun ei ole osuttu, se jätetään tyhjäksi. Mikäli ruutuun on
	 * ammuttu, metodi tarkistaa onko ruudussa laiva vai ei. Jos ruudussa on laiva,
	 * tallennetaan 'X', jos ei, talleneetaan '-'.
	 * 
	 * @return ruudukko char-tyyppinen matriisi, joka kuvaa tilannetta laivojen syöttämisen
	 *         aikana
	 */
	public char[][] muodostaPeliRuudukko() {
		char[][] ruudukko = new char[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (pelilauta[i][j].isOsuma()) {
					if (pelilauta[i][j].getLaiva() == null) {
						ruudukko[i][j] = '-';
					} else {
						ruudukko[i][j] = 'X';
					}

				}
			}
		}
		return ruudukko;
	}

	/**
	 * Metodi tulostaa joko aloitusruudukon (laivojen syöttäminen) tai pelin
	 * tilannetta kuvaavan peliruudukon.
	 * 
	 * @param ruudukko char-tyyppinen matriisi, joka on luotu joko
	 *        muodostaAloitusRuudukko()-metodin TAI muodostaPeliRuudukko()-metodin
	 *        avulla.
	 */
	public void tulostaRuudukko(char[][] ruudukko) {
		System.out.println("  A B C D E F G H I J");
		for (int i = 0; i < 10; i++) {
			System.out.print(i);
			for (int j = 0; j < 10; j++) {
				System.out.print(" " + ruudukko[i][j]);
			}
			System.out.println("");
		}
	}

	/**
	 * Metodi saa parametrinaan laivan sijainnin kokonaislukumatriisina (jokainen
	 * rivi vastaa yhtï¿½ koordinaattiparia), muuntaa informaation oikeaan muotoon
	 * (ArrayList<Ruutu>) ja lisï¿½ï¿½ sen parametrina saamansa laivan sijainniksi:
	 * Metodi luo uuden Ruutu-tyyppisiï¿½ viittauksia sisï¿½ltï¿½vï¿½n listan ja
	 * lisï¿½ï¿½ tï¿½hï¿½n listaan viittaukset pelilaudan vastaaviin ruutu-olioihin.
	 * Tï¿½mï¿½n jï¿½lkeen metodi kï¿½y ruutu-oliot lï¿½pi ja lisï¿½ï¿½ jokaiseen
	 * tiedon laivasta. Lopuksi metodi tallentaa Ruutu-olioita sisï¿½ltï¿½vï¿½n
	 * listan parametrina saamansa laivan sijainniksi.
	 * 
	 * @param koordinaatit kokonaislukumatriisi, jonka riveinï¿½ ovat laivan
	 *                     koordinaatit
	 * @param              laiva, jonka sijainti halutaan tallentaa sekï¿½
	 *                     pelilaudan Ruutu-olioihin ettï¿½ laivan attribuutiksi.
	 */
	public void tallennaSijainti(int[][] koordinaatit, Laiva laiva) {
		ArrayList<Ruutu> sijainti = new ArrayList<>();
		for (int i = 0; i < koordinaatit.length; i++) {
			int a = koordinaatit[i][0];
			int b = koordinaatit[i][1];
			sijainti.add(pelilauta[a][b]);
		}
		for (int i = 0; i < sijainti.size(); i++) {
			sijainti.get(i).setLaiva(laiva);
		}
		laiva.setSijainti(sijainti);
		laiva.setKoordinaatit(koordinaatit);
	}

	/**
	 * Metodi saa parametrinaan listan koordinaatteja (kokonaislukumatriisina) ja
	 * tarkistaa voiko nï¿½ihin koordinaatteihin tallentaa uuden laivan. Metodi
	 * kï¿½y jokaisen koordinaatin yksitellen lï¿½pi ja tarkistaa, ettei
	 * ylï¿½puolella tai alapuolella tai vasemmalla tai oikealla ole laivaa.
	 * 
	 * @param koordinaatit
	 * @return false, jonkin koordinaattiparin viereisessï¿½ ruudussa on jo laiva.
	 *         True, mikï¿½li sijainti on sallittu
	 */
	public boolean onkoSijaintiSallittu(int[][] koordinaatit) {
		for (int i = 0; i < koordinaatit.length; i++) {
			int a = koordinaatit[i][0];
			int b = koordinaatit[i][1];
			if (pelilauta[a][b].getLaiva() != null || !onkoYlhaallaTilaa(a, b) || !onkoAlhaallaTilaa(a, b)
					|| !onkoVasemmallaTilaa(a, b) || !onkoOikeallaTilaa(a, b)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Metodi saa parametrinaan ruudun koordinaatit ja tarkistaa onko kyseisen
	 * ruudun ympärillä 12 ruutua, joihin ei ole vielä ammuttu. (Nämä 12 osumatonta
	 * ruutua muodostavat "salmiakkikuvion" parametrina saadun ruudun ympärille: 2
	 * ruutua jokaiseen pääilmansuuntaan ja yhdet ruudut väli-ilmansuuntiin.)
	 * 
	 * @param i ruudun vaakarivi
	 * @param j ruudun pystyrivi
	 * @return true, jos parametrina saadun ruudun ympärillä on 12 sellaista ruutua,
	 *         joihin ei ole vielä ammuttu
	 */
	public boolean onkoOsumattomiaRuutuja12(int i, int j) {
		boolean ylos = false;
		boolean alas = false;
		boolean vasemmalle = false;
		boolean oikealle = false;
		if (i == 0) {
			ylos = true;
		} else if (ylhaallaOsumatonRuutu(i, j) && ylhaallaOsumatonRuutu(i - 1, j) && vasemmallaOsumatonRuutu(i - 1, j)
				&& oikeallaOsumatonRuutu(i - 1, j)) {
			ylos = true;
		}
		if (i == 9) {
			alas = true;
		} else if (alhaallaOsumatonRuutu(i, j) && alhaallaOsumatonRuutu(i + 1, j) && vasemmallaOsumatonRuutu(i + 1, j)
				&& oikeallaOsumatonRuutu(i + 1, j)) {
			alas = true;
		}
		if (j == 0) {
			vasemmalle = true;
		} else if (vasemmallaOsumatonRuutu(i, j) && vasemmallaOsumatonRuutu(i, j - 1)) {
			vasemmalle = true;
		}
		if (j == 9) {
			oikealle = true;
		} else if (oikeallaOsumatonRuutu(i, j) && j + 1 <= 9 && oikeallaOsumatonRuutu(i, j + 1)) {
			oikealle = true;
		}
		if (ylos && alas && vasemmalle && oikealle) {
			return true;
		}
		return false;
	}

	/**
	 * Metodi saa parametrinaan ruudun koordinaatit ja tarkistaa onko kyseisen
	 * ruudun ympärillä 4 sellaista ruutua, joihin ei ole vielä ammuttu.
	 * 
	 * @param i ruudun vaakarivi
	 * @param j ruudun pystyrivi
	 * @return true, jos parametrina saadun ruudun ympärillä on 4 sellaista ruutua,
	 *         joihin ei ole vielä ammuttu
	 */
	public boolean onkoOsumattomiaRuutuja4(int i, int j) {
		if (ylhaallaOsumatonRuutu(i, j) && alhaallaOsumatonRuutu(i, j) && oikeallaOsumatonRuutu(i, j)
				&& vasemmallaOsumatonRuutu(i, j)) {
			return true;
		}
		return false;
	}

	/**
	 * Tietokonepelaaja on saanut ensimmäisen osuman vastustajan laivaan ja miettii
	 * mihin suuntaan kannattaisi jatkaa ampumista. Tämä metodi tarkistaa missä
	 * suunnassa on eniten osumattomia ruutuja ja ilmoittaa tämän suunnan
	 * taulukkona, joka sisältää kaksi kokonaislukua: ensimmäinen luku kertoo
	 * muutoksen y-suunnassa (+1, 0 tai -1) ja toinen x-suunnassa (+1, 0 tai -1).
	 * 
	 * @param koordinaatti kokonaislukutaulukko, joka kertoo ensimmäisen osuman koordinaatit
	 * @return kokonaislukutaulukko, joka kertoo mihin suuntaan kannattaa ampua seuraavaksi
	 */
	public int[] suuntaEnitenOsumattomiaRuutuja(int[] koordinaatti) {
		int i = koordinaatti[0];
		int j = koordinaatti[1];
		int[] ylhaalla = { 0, -1, 0 };
		int[] alhaalla = { 0, 1, 0 };
		int[] vasemmalla = { 0, 0, -1 };
		int[] oikealla = { 0, 0, 1 };
		ylhaalla[0] = osumattomiaRuutujaYlhaalla(i, j);
		alhaalla[0] = osumattomiaRuutujaAlhaalla(i, j);
		vasemmalla[0] = osumattomiaRuutujaVasemmalla(i, j);
		oikealla[0] = osumattomiaRuutujaOikealla(i, j);
		int[] max = ylhaalla;
		if (alhaalla[0] > max[0]) {
			max = alhaalla;
		}
		if (vasemmalla[0] > max[0]) {
			max = vasemmalla;
		}
		if (oikealla[0] > max[0]) {
			max = oikealla;
		}
		int[] suunta = { max[1], max[2] };
		return suunta;
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mikï¿½li
	 * kyseinen ruutu on pelilaudan ylï¿½rivillï¿½ tai kyseisen ruudun ylï¿½puolella
	 * on tyhjï¿½ ruutu, metodi palauttaa true. (Käytetään laivojen syöttämisessä
	 * pelilaudalle.)
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return true, mikï¿½li koordinaattipari sijaitsee ylï¿½rivillï¿½ tai sen
	 *         ylï¿½puolella olevassa ruudussa ei ole laivaa.
	 */
	public boolean onkoYlhaallaTilaa(int i, int j) {
		if (i == 0) {
			return true;
		} else if (pelilauta[i - 1][j].getLaiva() == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mikï¿½li
	 * kyseinen ruutu on pelilaudan ylï¿½rivillï¿½ tai kyseisen ruudun ylï¿½puolella
	 * olevaan ruutuun ei ole ammuttu, metodi palauttaa true.
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return true, mikäli yläpuolella olevaan ruutuun ei ole ammuttu
	 */
	public boolean ylhaallaOsumatonRuutu(int i, int j) {
		if (i == 0) {
			return true;
		} else if (pelilauta[i - 1][j].isOsuma()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Metodi laskee
	 * rekursiivisesti kyseisen ruudun yläpuolella olevien "osumattomien" (=ruutu,
	 * johon ei ole vielä ammuttu) ruutujen lukumäärän. Jos kyseinen ruutu on
	 * ylärivillä tai siihen on jo ammuttu, niin laskeminen loppuu.
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return kokonaisluku, joka kertoo parametrina saadun ruudun yläpuolella
	 *         olevien "osumattomien" ruutujen lukumäärän.
	 */
	public int osumattomiaRuutujaYlhaalla(int i, int j) {
		if (i == 0 || pelilauta[i - 1][j].isOsuma()) {
			return 0;
		} else {
			return 1 + osumattomiaRuutujaYlhaalla(i - 1, j);
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mikï¿½li
	 * kyseinen ruutu on pelilaudan alarivillï¿½ tai kyseisen ruudun alapuolella on
	 * tyhjï¿½ ruutu, metodi palauttaa true. (Käytetään laivojen syöttämisessä
	 * pelilaudalle.)
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return true, mikï¿½li koordinaattipari sijaitsee alarivillï¿½ tai sen
	 *         alapuolella olevassa ruudussa ei ole laivaa.
	 */
	public boolean onkoAlhaallaTilaa(int i, int j) {
		if (i == 9) {
			return true;
		} else if (pelilauta[i + 1][j].getLaiva() == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mikï¿½li
	 * kyseinen ruutu on pelilaudan alarivillï¿½ tai kyseisen ruudun alapuolella on
	 * ruutu, johon ei ole ammuttu, metodi palauttaa true.
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return true, jos alapuolella olevaan ruutuun ei ole ammuttu
	 */
	public boolean alhaallaOsumatonRuutu(int i, int j) {
		if (i == 9) {
			return true;
		} else if (pelilauta[i + 1][j].isOsuma()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Metodi laskee
	 * rekursiivisesti kyseisen ruudun alapuolella olevien "osumattomien" (=ruutu,
	 * johon ei ole vielä ammuttu) ruutujen lukumäärän. Jos kyseinen ruutu on
	 * alarivillä tai siihen on jo ammuttu, niin laskeminen loppuu.
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return kokonaisluku, joka kertoo parametrina saadun ruudun alapuolella
	 *         olevien "osumattomien" ruutujen lukumäärän.
	 */
	public int osumattomiaRuutujaAlhaalla(int i, int j) {
		if (i == 9 || pelilauta[i + 1][j].isOsuma()) {
			return 0;
		} else {
			return 1 + osumattomiaRuutujaAlhaalla(i + 1, j);
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mikï¿½li
	 * kyseinen ruutu on pelilaudan ensimmï¿½isellï¿½ pystyrivillï¿½ tai kyseisen
	 * ruudun vasemmalla puolella on tyhjï¿½ ruutu, metodi palauttaa true.
	 * (Käytetään laivojen syöttämisessä pelilaudalle.)
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return true, mikï¿½li koordinaattipari sijaitsee ensimmï¿½isellï¿½
	 *         pystyrivillï¿½ tai sen vasemmalla puolella olevassa ruudussa ei ole
	 *         laivaa.
	 */
	public boolean onkoVasemmallaTilaa(int i, int j) {
		if (j == 0) {
			return true;
		} else if (pelilauta[i][j - 1].getLaiva() == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mikï¿½li
	 * kyseinen ruutu on pelilaudan ensimmäisellä pystyrivillä tai kyseisen ruudun
	 * vasemmalla puolella olevaan ruutuun ei ole ammuttu, metodi palauttaa true.
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return true, jos vasemmalla olevaan ruutuun ei ole ammuttu
	 */
	public boolean vasemmallaOsumatonRuutu(int i, int j) {
		if (j == 0) {
			return true;
		} else if (pelilauta[i][j - 1].isOsuma()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Metodi laskee
	 * rekursiivisesti kyseisen ruudun vasemmalla puolella olevien "osumattomien"
	 * (=ruutu, johon ei ole vielä ammuttu) ruutujen lukumäärän. Jos kyseinen ruutu
	 * on ensimmäisellä pystyrivillä tai siihen on jo ammuttu, niin laskeminen
	 * loppuu.
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return kokonaisluku, joka kertoo parametrina saadun ruudun vasemmalla
	 *         puolella olevien "osumattomien" ruutujen lukumäärän.
	 */
	public int osumattomiaRuutujaVasemmalla(int i, int j) {
		if (j == 0 || pelilauta[i][j - 1].isOsuma()) {
			return 0;
		} else {
			return 1 + osumattomiaRuutujaVasemmalla(i, j - 1);
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mikï¿½li
	 * kyseinen ruutu on pelilaudan viimeisellï¿½ pystyrivillï¿½ tai kyseisen ruudun
	 * oikealla puolella on tyhjï¿½ ruutu, metodi palauttaa true. (Käytetään
	 * laivojen syöttämisessä pelilaudalle.)
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return true, mikï¿½li koordinaattipari sijaitsee viimeisellï¿½
	 *         pystyrivillï¿½ tai sen oikealla puolella olevassa ruudussa ei ole
	 *         laivaa.
	 */
	public boolean onkoOikeallaTilaa(int i, int j) {
		if (j == 9) {
			return true;
		} else if (pelilauta[i][j + 1].getLaiva() == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mikï¿½li
	 * kyseinen ruutu on pelilaudan viimeisellä pystyrivillä tai kyseisen ruudun
	 * oikealla puolella olevaan ruutuun ei ole ammuttu, metodi palauttaa true.
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return true, jos oikealla olevaan ruutuun ei ole ammuttu
	 */
	public boolean oikeallaOsumatonRuutu(int i, int j) {
		if (j == 9) {
			return true;
		} else if (pelilauta[i][j + 1].isOsuma()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Metodi laskee
	 * rekursiivisesti kyseisen ruudun oikealla puolella olevien "osumattomien"
	 * (=ruutu, johon ei ole vielä ammuttu) ruutujen lukumäärän. Jos kyseinen ruutu
	 * on viimeisellä pystyrivillä tai siihen on jo ammuttu, niin laskeminen loppuu.
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return kokonaisluku, joka kertoo parametrina saadun ruudun oikealla puolella
	 *         olevien "osumattomien" ruutujen lukumäärän.
	 */
	public int osumattomiaRuutujaOikealla(int i, int j) {
		if (j == 9 || pelilauta[i][j + 1].isOsuma()) {
			return 0;
		} else {
			return 1 + osumattomiaRuutujaOikealla(i, j + 1);
		}
	}

	public Ruutu[][] getPelilauta() {
		return pelilauta;
	}

	public Ruutu getRuutu(int i, int j) {
		return pelilauta[i][j];
	}

	public ArrayList<Laiva> getLaivat() {
		return laivat;
	}

	public void lisaaLaiva(Laiva laiva) {
		laivat.add(laiva);
	}

}
