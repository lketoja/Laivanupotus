import java.util.Random;
import java.util.Scanner;

public class TietokonePelaaja extends Pelaaja {

	Random r = new Random();
	
	public TietokonePelaaja() {
		this.nimi = "tietokone";
	}

	/**
	 * T‰m‰ metodi ylikirjoittaa Pelaaja-luokan vastaavan metodin. Metodin avulla
	 * tietokonepelaajalle arvotaan laivojen sijainnit. Jokaiselle laivalle arvotaan
	 * aloituskoordinaatti ja suunta. T‰m‰n j‰lkeen generoidaan koordinaatit
	 * kokonailukumatriisiin ja tarkistetaan ovatko kaikki koordinaatit pelilaudan
	 * sis‰ll‰. Sitten tarkistetaan viel‰, ett‰ sijainti on sallittu eli ett‰ uusi
	 * laiva ei ole jo sijoitetun laivan vieress‰ (tai p‰‰ll‰).
	 */
	public void syotaLaivat(Scanner s) {
		String[] suuntaTaulukko = { "alas", "oikealle" };
		for (int j = 0; j < pelilauta.getLaivat().size(); j++) {
			boolean ehto = false;
			do {
				ehto = false;
				Laiva laiva = pelilauta.getLaivat().get(j);
				int[] koordinaatti = { r.nextInt(10), r.nextInt(10) };
				String arvottuSuunta = suuntaTaulukko[r.nextInt(2)];
				int[][] sijainti = generoiKoordinaatit(koordinaatti, arvottuSuunta, laiva.getPituus());
				boolean mahtuuLaudalle = mahtuukoLaudalle(sijainti);
				if (!mahtuuLaudalle) {
					ehto = true;
					continue;
				}
				if (!pelilauta.onkoSijaintiSallittu(sijainti)) {
					ehto = true;
					continue;
				} else {
					pelilauta.tallennaSijainti(sijainti, laiva);
				}
			} while (ehto);
		}
	}

	// parametri Pelaaja p on se pelaaja jonka laivoja ammuttaan
	public void pelaaVuoro(PelinTilanne pt, Pelaaja p, Scanner s) {
		int[] ammuksenKoordinaatit = new int[2];

		if (pt.isAmmutaanSatunnaisesti()) { // ammutaan satunnaisesti, jos mink‰‰n laivan upottaminen ei ole kesken
			ammuksenKoordinaatit = ammu(r, p.getPelilauta(), pt);
		} else {
			if (pt.getOsumatLaivaan() == 1) { // osuttu kerran ja l‰hdet‰‰n hakemaan mihin suuntaan laiva jatkuu
				// katsotaan miss‰ suunnassa on eniten "tilaa" ja ammutaan sinne
				int[] suunta = p.getPelilauta().suuntaEnitenOsumattomiaRuutuja(pt.getEdellinenOsuma());
				ammuksenKoordinaatit[0] = pt.getEdellinenOsuma()[0] + suunta[0];
				ammuksenKoordinaatit[1] = pt.getEdellinenOsuma()[1] + suunta[1];
				pt.setSuunta(suunta);
			} else if (pt.getOsumatLaivaan() > 1) { // osuttu jo kaksi kertaa eli tiedet‰‰n onko laiva pysty- vai
													// vaakasuunnassa
				ammuksenKoordinaatit[0] = pt.getEdellinenOsuma()[0] + pt.getSuunta()[0];
				ammuksenKoordinaatit[1] = pt.getEdellinenOsuma()[1] + pt.getSuunta()[1];
				// tarkistetaan ovatko uudet koordinaatit laudalla, jos ei niin l‰hdet‰‰n
				// ampumaan ensimm‰isest‰ osumasta toiseen suuntaan
				if (ammuksenKoordinaatit[0] < 0 || ammuksenKoordinaatit[0] > 9 || ammuksenKoordinaatit[1] < 0
						|| ammuksenKoordinaatit[1] > 9) {
					pt.setEdellinenOsuma(pt.getLaivanEnsimmainenOsuma());
					pt.setSuunta(muutaSuunta(pt.getSuunta()));
					ammuksenKoordinaatit[0] = pt.getEdellinenOsuma()[0] + pt.getSuunta()[0];
					ammuksenKoordinaatit[1] = pt.getEdellinenOsuma()[1] + pt.getSuunta()[1];
				}
			}
		}
		boolean osuma = p.getPelilauta().osuiko(ammuksenKoordinaatit); // katsotaan osuiko ammus
		if (osuma) {
			if (pt.getOsumatLaivaan() == 0) {
				pt.setLaivanEnsimmainenOsuma(ammuksenKoordinaatit);
				pt.setAmmutaanSatunnaisesti(false);
			}
			pt.kasvataOsumatLaivaan();
			pt.setEdellinenOsuma(ammuksenKoordinaatit);

			p.getPelilauta().tulostaRuudukko(p.getPelilauta().muodostaPeliRuudukko());
			System.out.println("Tietokone osui laivaasi!");

			// jos osuttu laiva upposi, niin tallennetaan viittaus t‰h‰n laivaan
			// muuttujaan uponnut
			Laiva uponnut = p.getPelilauta().upposikoLaiva(ammuksenKoordinaatit);
			if (uponnut != null) { // laiva siis upposi!
				p.getPelilauta().merkitseUponnutLaiva(uponnut);
				pt.setAmmutaanSatunnaisesti(true);
				pt.setLaivanEnsimmainenOsuma(null);
				pt.setEdellinenOsuma(null);
				pt.setOsumatLaivaan(0);
				pt.setSuunta(null);

				if (p.getPelilauta().kaikkiLaivatUponneet()) {
					System.out.println("Tietokone upotti viimeisen laivasi! HÔøΩVISIT PELIN!");
					System.exit(0);
				}
				System.out.println("Laiva upposi! Tietokone saa uuden vuoron.");
				System.out.println("Paina 'Enter'.");
				s.nextLine();
			} else {
				System.out.println("Laiva ei uponnut. Tietokone saa silti uuden vuoron.");
				System.out.println("Paina 'Enter'.");
				s.nextLine();
			}
		} else { // ei osuttu laivaan
			if (pt.getOsumatLaivaan() > 1) { // Ollaan upottamaassa laivaa, ja siihen on osuttu ainakin kaksi kertaa,
												// mutta nyt tuli huti -> ammutaan
				// seuraavaksi ensimm‰isest‰ osumasta toiseen suuntaan
				pt.setEdellinenOsuma(pt.getLaivanEnsimmainenOsuma());
				pt.setSuunta(muutaSuunta(pt.getSuunta()));
			}
			p.getPelilauta().tulostaRuudukko(p.getPelilauta().muodostaPeliRuudukko());
			System.out.println("Tietokone ei osunut laivaan. Vuoro siirtyy sinulle.");
			pt.kasvataPelaajalaskuri();
		}
	}

	/**
	 * T‰t‰ metodia kutsutaan silloin, kun tietokonepelaaja on upottamassa laivaa ja
	 * on osunut siihen jo ainakin kaksi kertaa (eli tiedet‰‰n onko laiva vaaka- vai
	 * pystysuunnassa), mutta ampuu sen j‰lkeen ohi (eli laiva jatkuukin toiseen
	 * suuntaan) tai seuraava koordinaatti samassa linjassa olisi jo pelilaudan
	 * ulkopuolella. Metodi saa parametrinaan alkuper‰isen ampumissuunnan
	 * taulukkona, joka sis‰lt‰‰ kaksi kokonaislukua: ensimm‰inen luku kertoo
	 * muutoksen y-suunnassa (+1, 0 tai -1) ja toinen x-suunnassa (+1, 0 tai -1).
	 * Metodi muuttaa suunnan vastakkaiseksi, jotta voidaan l‰hte‰ ampumaan
	 * ensimm‰isest‰ osumasta toiseen suuntaan.
	 * 
	 * @param suunta alkuper‰inen ampumissuunta (kokonaislukutaulukko)
	 * @return uusi ampumissuunta (kokonaislukutaulukko)
	 */
	public int[] muutaSuunta(int[] suunta) {
		for (int i = 0; i < 2; i++) {
			if (suunta[i] == 1) {
				suunta[i] = -1;
			} else if (suunta[i] == -1) {
				suunta[i] = 1;
			}
		}
		return suunta;
	}

	/**
	 * T‰t‰ metodia kutsutaan silloin kun tietokonepelaaja ampuu satunnaisesti (eli
	 * mink‰‰n laivan upottaminen ei ole kesken). Ennen pelin alkua on arvottu, onko
	 * muuttuja koordinaattienSumma 0 vai 1 eli ett‰ ampuuko tietokonepelaaja
	 * ruutuihin, joiden koordinaattien summa on parillinen vai pariton (koska
	 * lyhimm‰n laivan pituus on 2, niin vierekk‰isiin ruutuihin ei kannata ampua).
	 * 
	 * Aluksi arvotaan muuttujien a ja b arvoiksi kokonaisluvut v‰lilt‰ 0-9. Toisin
	 * sanoen arvotaan koordinaatti, johon mahdollisesti tullaan ampumaan.
	 * Seuraavaksi tarkistetaan, ett‰ a:n ja b:n summa on linjassa muuttujan
	 * koordinaattienSumma kanssa. Jos ei, niin korjataan asia lis‰‰m‰ll‰ muuttujaan
	 * a luku 1.
	 * 
	 * Jos muuttujan etsit‰‰n12Ruutua arvo on true (peli on vasta alkanut eli
	 * ammutaan "harvaan"), niin silloin tarkistetaan, ett‰ lˆytyykˆ koordinaatin
	 * (a,b) ymp‰rilt‰ 12 ruutua, joihin ei viel‰ ole ammuttu. Jos ei, niin
	 * siirryt‰‰n vaakarivill‰ seuraavaan koordinaattiin eli (a, (b+2)%10). (Modulo
	 * 10 tarvitaan, koska b voi tietenkin olla 8 tai 9.) K‰yd‰‰n l‰pi koko
	 * pelilauta ja heti jos lˆydet‰‰n koordinaatti, jonka ymp‰rill‰ on 12
	 * "osumatonta" ruutua, niin palautetaan se kutsujalle. Jos yht‰‰n kelvollista
	 * koordinaattia ei lˆydy, niin asetetaan muuttujan etsit‰‰n12Ruutua arvoksi
	 * false (jotta seuraavalla kerralla voidaan hyp‰t‰ suoraan t‰m‰n tuplasilmukan
	 * yli) ja etsit‰‰n seuraavaksi koordinaattia, jonka ymp‰rill‰ on 4 "osumatonta"
	 * ruutua. Mik‰li t‰llaistakaan ruutua ei lˆydy, niin siirryt‰‰n etsim‰‰n vain
	 * ruutua, johon ei ole viel‰ ammuttu.
	 * 
	 * @param r                Random, tarvitaan koordinaatin arpomiseen
	 * @param ammuttavatLaivat Pelilauta, jossa vastustajan laivat sijaitsevat
	 * @param pt               olio, johon on s‰ilˆtty pelin tilannetta kuvaava data
	 * @return koordinaatti, johon ammutaan (kokonaislukutaulukko)
	 */
	public int[] ammu(Random r, Pelilauta ammuttavatLaivat, PelinTilanne pt) {
		int[] koordinaatti = new int[2];
		boolean onkoAmmuttu = true;
		int a = -1;
		int b = -1;
		a = r.nextInt(10);
		b = r.nextInt(10);
		if ((a + b) % 2 != pt.getKoordinaattienSumma()) {
			a = (a + 1) % 10;
		}
		if (pt.isEtsitaan12Ruutua()) {
			for (int i = a; i < a + 10; i++) {
				for (int j = b + (i - a); j < b + 10; j = j + 2) {
					onkoAmmuttu = ammuttavatLaivat.getRuutu(i % 10, j % 10).isOsuma();
					if (!onkoAmmuttu) {
						boolean osumattomia12 = ammuttavatLaivat.onkoOsumattomiaRuutuja12(i % 10, j % 10);
						if (osumattomia12) {
							koordinaatti[0] = i % 10;
							koordinaatti[1] = j % 10;
							return koordinaatti;
						}
					}
				}
			}
		}
		pt.setEtsitaan12Ruutua(false);
		if (pt.isEtsitaan4Ruutua()) {
			for (int i = a; i < a + 10; i++) {
				for (int j = b + (i - a); j < b + 10; j = j + 2) {
					onkoAmmuttu = ammuttavatLaivat.getRuutu(i % 10, j % 10).isOsuma();
					if (!onkoAmmuttu) {
						boolean tyhjia5 = ammuttavatLaivat.onkoOsumattomiaRuutuja4(i % 10, j % 10);
						if (tyhjia5) {
							koordinaatti[0] = i % 10;
							koordinaatti[1] = j % 10;
							return koordinaatti;
						}
					}
				}
			}
		}
		pt.setEtsitaan4Ruutua(false);
		for (int i = a; i < a + 10; i++) {
			for (int j = b + (i - a); j < b + 10; j = j + 2) {
				onkoAmmuttu = ammuttavatLaivat.getRuutu(i % 10, j % 10).isOsuma();
				if (!onkoAmmuttu) {
					koordinaatti[0] = i % 10;
					koordinaatti[1] = j % 10;
					return koordinaatti;
				}
			}
		}
		return koordinaatti;

	}

}
