import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Pelilauta implements Serializable {

	private Ruutu[][] pelilauta;
	private ArrayList<Laiva> laivat;

	/**
	 * Konstruktori luo uuden Ruutu-tyyppisi� muuttujia sis�lt�v�n taulukon
	 * ja tallentaa sen attribuuttiin pelilauta. Jokaiseen taulukon alkioon luodaan
	 * ja tallennetaan uusi, "tyhj�" Ruutu-olio. Konstruktori luo my�s uuden
	 * Laiva-tyyppisi� muuttujia sis�lt�v�n listan ja tallentaa sen
	 * attribuuttiin laivat. Kaikki pelaajan laivat luodaan ja lis�t��n
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
	 * merkitsee, ett� kyseiseen ruutuun on ammuttu. Sen j�lkeen metodi tarkistaa,
	 * onko kyseisess� ruudussa laivaa ja palauttaa true tai false vastaavasti.
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
	 * etsii ensin sen laivan, joka sijaitsee kyseisess� ruudussa. Sen j�lkeen
	 * metodi k�y l�pi kaikki t�m�n kyseisen laivan ruudut. Jos johonkin n�ist�
	 * ruuduista ei ole ammuttu, niin metodi palauttaa 'null'. Mik�li kaikkiin
	 * laivan ruutuihin on jo ammuttu, niin metodi merkitsee laivan uponneeksi ja
	 * palauttaa viittauksen kyseiseen laivaan.
	 * 
	 * @param koordinaatti johon ammuttiin (kokonaislukutaulukko)
	 * @return 'null', mik�li laiva ei uponnut ja viittauksen laivaan mik�li
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
	 * laivan viereiset ruudut ik��n kuin niihinkin olisi osuttu (koska s��nt�jen
	 * mukaan laivat eiv�t voi sijaita toistensa vieress�). (Nyt ruudulle
	 * tulostuvasta pelilaudasta hahmottaa selke�sti, ett� laiva on uponnut. Lis�ksi
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
				pelilauta[a - 1][b].setOsuma(true); // yl�puolelle
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
	 * Metodi tarkistaa, ett� ovatko kaikki laivat uponneet. Jos kaikki laivat ovat
	 * uponneet, peli on p��ttynyt.
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
	 * matriisin alkion kohdalla, ett� onko pelilaudan vastaavaan kohtaan
	 * tallennettu laiva. Mik�li laivaa ei l�ydy, metodi tallentaa t�h�n
	 * kohtaan merkin '-'. Mik�li laiva l�ytyy, matriisin alkioksi tallennetaan
	 * merkki 'X'.
	 * 
	 * @return ruudukko char-tyyppinen matriisi, joka kuvaa tilannetta laivojen sy�tt�misen
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
	 * matriisin alkion kohdalla, ett� onko pelilaudan vastaavaan kohtaan osunut
	 * ammus. Jos ruutuun ei ole osuttu, se j�tet��n tyhj�ksi. Mik�li ruutuun on
	 * ammuttu, metodi tarkistaa onko ruudussa laiva vai ei. Jos ruudussa on laiva,
	 * tallennetaan 'X', jos ei, talleneetaan '-'.
	 * 
	 * @return ruudukko char-tyyppinen matriisi, joka kuvaa tilannetta laivojen sy�tt�misen
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
	 * Metodi tulostaa joko aloitusruudukon (laivojen sy�tt�minen) tai pelin
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
	 * rivi vastaa yht� koordinaattiparia), muuntaa informaation oikeaan muotoon
	 * (ArrayList<Ruutu>) ja lis�� sen parametrina saamansa laivan sijainniksi:
	 * Metodi luo uuden Ruutu-tyyppisi� viittauksia sis�lt�v�n listan ja
	 * lis�� t�h�n listaan viittaukset pelilaudan vastaaviin ruutu-olioihin.
	 * T�m�n j�lkeen metodi k�y ruutu-oliot l�pi ja lis�� jokaiseen
	 * tiedon laivasta. Lopuksi metodi tallentaa Ruutu-olioita sis�lt�v�n
	 * listan parametrina saamansa laivan sijainniksi.
	 * 
	 * @param koordinaatit kokonaislukumatriisi, jonka rivein� ovat laivan
	 *                     koordinaatit
	 * @param              laiva, jonka sijainti halutaan tallentaa sek�
	 *                     pelilaudan Ruutu-olioihin ett� laivan attribuutiksi.
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
	 * tarkistaa voiko n�ihin koordinaatteihin tallentaa uuden laivan. Metodi
	 * k�y jokaisen koordinaatin yksitellen l�pi ja tarkistaa, ettei
	 * yl�puolella tai alapuolella tai vasemmalla tai oikealla ole laivaa.
	 * 
	 * @param koordinaatit
	 * @return false, jonkin koordinaattiparin viereisess� ruudussa on jo laiva.
	 *         True, mik�li sijainti on sallittu
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
	 * ruudun ymp�rill� 12 ruutua, joihin ei ole viel� ammuttu. (N�m� 12 osumatonta
	 * ruutua muodostavat "salmiakkikuvion" parametrina saadun ruudun ymp�rille: 2
	 * ruutua jokaiseen p��ilmansuuntaan ja yhdet ruudut v�li-ilmansuuntiin.)
	 * 
	 * @param i ruudun vaakarivi
	 * @param j ruudun pystyrivi
	 * @return true, jos parametrina saadun ruudun ymp�rill� on 12 sellaista ruutua,
	 *         joihin ei ole viel� ammuttu
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
	 * ruudun ymp�rill� 4 sellaista ruutua, joihin ei ole viel� ammuttu.
	 * 
	 * @param i ruudun vaakarivi
	 * @param j ruudun pystyrivi
	 * @return true, jos parametrina saadun ruudun ymp�rill� on 4 sellaista ruutua,
	 *         joihin ei ole viel� ammuttu
	 */
	public boolean onkoOsumattomiaRuutuja4(int i, int j) {
		if (ylhaallaOsumatonRuutu(i, j) && alhaallaOsumatonRuutu(i, j) && oikeallaOsumatonRuutu(i, j)
				&& vasemmallaOsumatonRuutu(i, j)) {
			return true;
		}
		return false;
	}

	/**
	 * Tietokonepelaaja on saanut ensimm�isen osuman vastustajan laivaan ja miettii
	 * mihin suuntaan kannattaisi jatkaa ampumista. T�m� metodi tarkistaa miss�
	 * suunnassa on eniten osumattomia ruutuja ja ilmoittaa t�m�n suunnan
	 * taulukkona, joka sis�lt�� kaksi kokonaislukua: ensimm�inen luku kertoo
	 * muutoksen y-suunnassa (+1, 0 tai -1) ja toinen x-suunnassa (+1, 0 tai -1).
	 * 
	 * @param koordinaatti kokonaislukutaulukko, joka kertoo ensimm�isen osuman koordinaatit
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
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mik�li
	 * kyseinen ruutu on pelilaudan yl�rivill� tai kyseisen ruudun yl�puolella
	 * on tyhj� ruutu, metodi palauttaa true. (K�ytet��n laivojen sy�tt�misess�
	 * pelilaudalle.)
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return true, mik�li koordinaattipari sijaitsee yl�rivill� tai sen
	 *         yl�puolella olevassa ruudussa ei ole laivaa.
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
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mik�li
	 * kyseinen ruutu on pelilaudan yl�rivill� tai kyseisen ruudun yl�puolella
	 * olevaan ruutuun ei ole ammuttu, metodi palauttaa true.
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return true, mik�li yl�puolella olevaan ruutuun ei ole ammuttu
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
	 * rekursiivisesti kyseisen ruudun yl�puolella olevien "osumattomien" (=ruutu,
	 * johon ei ole viel� ammuttu) ruutujen lukum��r�n. Jos kyseinen ruutu on
	 * yl�rivill� tai siihen on jo ammuttu, niin laskeminen loppuu.
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return kokonaisluku, joka kertoo parametrina saadun ruudun yl�puolella
	 *         olevien "osumattomien" ruutujen lukum��r�n.
	 */
	public int osumattomiaRuutujaYlhaalla(int i, int j) {
		if (i == 0 || pelilauta[i - 1][j].isOsuma()) {
			return 0;
		} else {
			return 1 + osumattomiaRuutujaYlhaalla(i - 1, j);
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mik�li
	 * kyseinen ruutu on pelilaudan alarivill� tai kyseisen ruudun alapuolella on
	 * tyhj� ruutu, metodi palauttaa true. (K�ytet��n laivojen sy�tt�misess�
	 * pelilaudalle.)
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return true, mik�li koordinaattipari sijaitsee alarivill� tai sen
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
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mik�li
	 * kyseinen ruutu on pelilaudan alarivill� tai kyseisen ruudun alapuolella on
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
	 * johon ei ole viel� ammuttu) ruutujen lukum��r�n. Jos kyseinen ruutu on
	 * alarivill� tai siihen on jo ammuttu, niin laskeminen loppuu.
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return kokonaisluku, joka kertoo parametrina saadun ruudun alapuolella
	 *         olevien "osumattomien" ruutujen lukum��r�n.
	 */
	public int osumattomiaRuutujaAlhaalla(int i, int j) {
		if (i == 9 || pelilauta[i + 1][j].isOsuma()) {
			return 0;
		} else {
			return 1 + osumattomiaRuutujaAlhaalla(i + 1, j);
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mik�li
	 * kyseinen ruutu on pelilaudan ensimm�isell� pystyrivill� tai kyseisen
	 * ruudun vasemmalla puolella on tyhj� ruutu, metodi palauttaa true.
	 * (K�ytet��n laivojen sy�tt�misess� pelilaudalle.)
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return true, mik�li koordinaattipari sijaitsee ensimm�isell�
	 *         pystyrivill� tai sen vasemmalla puolella olevassa ruudussa ei ole
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
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mik�li
	 * kyseinen ruutu on pelilaudan ensimm�isell� pystyrivill� tai kyseisen ruudun
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
	 * (=ruutu, johon ei ole viel� ammuttu) ruutujen lukum��r�n. Jos kyseinen ruutu
	 * on ensimm�isell� pystyrivill� tai siihen on jo ammuttu, niin laskeminen
	 * loppuu.
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return kokonaisluku, joka kertoo parametrina saadun ruudun vasemmalla
	 *         puolella olevien "osumattomien" ruutujen lukum��r�n.
	 */
	public int osumattomiaRuutujaVasemmalla(int i, int j) {
		if (j == 0 || pelilauta[i][j - 1].isOsuma()) {
			return 0;
		} else {
			return 1 + osumattomiaRuutujaVasemmalla(i, j - 1);
		}
	}

	/**
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mik�li
	 * kyseinen ruutu on pelilaudan viimeisell� pystyrivill� tai kyseisen ruudun
	 * oikealla puolella on tyhj� ruutu, metodi palauttaa true. (K�ytet��n
	 * laivojen sy�tt�misess� pelilaudalle.)
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return true, mik�li koordinaattipari sijaitsee viimeisell�
	 *         pystyrivill� tai sen oikealla puolella olevassa ruudussa ei ole
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
	 * Metodi saa parametrikseen pelilaudan yhden ruudun koordinaatit. Mik�li
	 * kyseinen ruutu on pelilaudan viimeisell� pystyrivill� tai kyseisen ruudun
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
	 * (=ruutu, johon ei ole viel� ammuttu) ruutujen lukum��r�n. Jos kyseinen ruutu
	 * on viimeisell� pystyrivill� tai siihen on jo ammuttu, niin laskeminen loppuu.
	 * 
	 * @param i on vaakarivin numero
	 * @param j on pystyrivin numero
	 * @return kokonaisluku, joka kertoo parametrina saadun ruudun oikealla puolella
	 *         olevien "osumattomien" ruutujen lukum��r�n.
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
