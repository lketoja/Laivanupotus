import java.util.Scanner;

public class IhmisPelaaja extends Pelaaja {

	/**
	 * Tämä metodi ylikirjoittaa Pelaaja-luokan vastaavan metodin. Metodi
	 * mahdollistaa sen, että pelaaja voi syöttää omat laivansa pelilaudalle. Kun
	 * uusi pelaaja-olio, luodaan myös pelaajalle pelilauta ja tähän pelilautaan
	 * tallennetaan kaikki pelaajan laivat. Metodin silmukka käy läpi kaikki nämä
	 * laivat. Metodi kysyy jokaisen laivan kohdalla käyttäjältä laivan ensimmäistä
	 * koordinaattia ja suuntaa. Jos laiva ei mahdu laudalle, tai se sitä yritetään
	 * sijoittaa toisen laivan viereen (tai päälle), kysytään uudelleen
	 * aloituskoordinaattia ja suuntaa. Sallittu sijainti tallennetaan ja
	 * tulostetaan aloitusruudukko, jossa juuri syötetty laiva näkyy.
	 * 
	 */
	public void syotaLaivat(Scanner s) {
		System.out.println("Nyt pelaaja " + nimi
				+ " syöttää kaikki omat laivansa. (Toinen pelaaja ei tietenkään saa nähdä tätä!)");
		for (int i = 0; i < pelilauta.getLaivat().size(); i++) {
			char[][] ruudukko = pelilauta.muodostaAloitusRuudukko();
			pelilauta.tulostaRuudukko(ruudukko);
			boolean ehto = false;
			do {
				ehto = false;
				Laiva laiva = pelilauta.getLaivat().get(i);
				int[] koordinaatti = syotaKoordinaatti(s, laiva.getNimi(), laiva.getPituus());
				String suunta = syotaSuunta(s);
				int[][] sijainti = generoiKoordinaatit(koordinaatti, suunta, laiva.getPituus());
				boolean mahtuuLaudalle = mahtuukoLaudalle(sijainti);
				if (!mahtuuLaudalle) {
					System.out.println("VIRHE! Laiva ulkona laudalta! Yritetään uudestaan:");
					ehto = true;
					continue;
				}
				if (!pelilauta.onkoSijaintiSallittu(sijainti)) {
					System.out.println(
							"VIRHE! Laivat saavat koskettaa toisaan vain kulmista. Laivat eivät siis saa olla vierekkäin (tai päällekkäin). Yritetään uudelleen:");
					ehto = true;
					continue;
				} else {
					pelilauta.tallennaSijainti(sijainti, laiva);
				}
			} while (ehto);
		}
		char[][] ruudukko = pelilauta.muodostaAloitusRuudukko();
		pelilauta.tulostaRuudukko(ruudukko);
	}

	/**
	 * Metodi ylikirjoittaa Pelaaja-luokan vastaavan metodin. Metodi mahdollistaa
	 * sen, että ihmispelaaja voi pelata oman vuoronsa. Ensin tulostetaan
	 * peliruudukko. Sen jälkeen pyydetään pelaajaa syöttämään ammuksen
	 * koordinaatti. Jos pelaaja syöttää 'tallenna', lopetetaan metodin
	 * suorittaminen ja palataan main-metodiin pelin tallennusta varten. Mikäli
	 * pelaajan syötä 'tallenna' vaan ampuu normaalisti, tarkistetaan osuuko ammus
	 * vai ei ja suoritetaan tarvittavat toimenpiteet.
	 * 
	 * @param pt PelinTilanne (esim. kasvatetaan vuorolaskuria ja pelaajalaskuria)
	 * @param p  pelaaja, jonka laivoja ammutaan
	 * @param s  skanneri, jolla luetaan pelaajan syöte
	 * 
	 */
	public void pelaaVuoro(PelinTilanne pt, Pelaaja p, Scanner s) {
		p.getPelilauta().tulostaRuudukko(p.getPelilauta().muodostaPeliRuudukko());
		int[] ammuksenKoordinaatit = ammu(s, pt);
		if (pt.isTallennus()) {
			return;
		}
		boolean osuma = p.getPelilauta().osuiko(ammuksenKoordinaatit);
		if (osuma) {
			System.out.println("Osuit laivaan!");
			// jos osuttu laiva upposi, niin tallennetaan viittaus tähän laivaan muuttujaan
			// uponnut
			Laiva uponnut = p.getPelilauta().upposikoLaiva(ammuksenKoordinaatit);
			if (uponnut != null) {
				p.getPelilauta().merkitseUponnutLaiva(uponnut);
				if (p.getPelilauta().kaikkiLaivatUponneet()) {
					System.out.println("Vastustajan viimeinen laiva upposi! Onneksi olkoon! VOITIT PELIN!");
					System.exit(0);
				}
				System.out.println("Laiva upposi! Saat uuden vuoron.");
			} else {
				System.out.println("Laiva ei uponnut. Saat silti uuden vuoron.");
			}
		} else {
			System.out.println("Et osunut laivaan. Vuoro siirtyy vastustajalle.");
			pt.kasvataPelaajalaskuri();
			System.out.println("Paina 'Enter'.");
			s.nextLine();
		}
	}

	/**
	 * Metodi mahdollistaa sen, että ihmispelaaja pystyy ampumaan vastustajan
	 * laivoja. Pelaajaa pyydetään syöttämään ammuksen koordinaatti. Koska syötteen
	 * lukeminen voi heittää poikkeuksen, se on ympäröity try-catch -rakenteella.
	 * Mikäli poikkeus heitetään, metodi pyytää pelaajaa syöttämään koordinaatin
	 * uudellen. Koordinaatti muunnetaan taulukoksi, joka sisältää kaksi
	 * kokonaislukua: ensimmäinen luku kertoo vaakarivin ja toinen pystyrivin. Tämä
	 * taulukko palautetaan kutsujalle. Pelaaja voi myös ammuksen koordinaatin
	 * sijaan syöttää 'tallenna', jolloin luokan PelinTilanne attribuutin tallennus
	 * arvoksi muutetaan true. Tämän jälkeen palautetaan tyhjä kokonaislukutaulukko.
	 * 
	 * @param s  scanneri, jolla luetaan pelaajan syöte
	 * @param pt PelinTilanne (jos pelaaja haluaa tallentaa pelin, attribuutin
	 *           tallennus arvoksi muutetaan true)
	 * @return kokonaislukutaulukko, joka kertoo ammuksen koordinaatit (tai tyhjä
	 *         taulukko, mikäli pelaaja haluaa tallentaa pelin)
	 */
	public int[] ammu(Scanner s, PelinTilanne pt) {
		boolean oikeaSyote = true;
		int[] koordinaatti = new int[2];
		do {
			oikeaSyote = true;
			System.out.println("Valitse mihin ruutuun haluat ampua ja syötä sen koordinaatti (esim 'g6')");
			try {
				String syote = s.nextLine();
				if (syote.contentEquals("tallenna")) {
					pt.setTallennus(true);
					int[] tyhja = new int[2];
					return tyhja;
				}
				koordinaatti[0] = syote.charAt(1) - '0';
				koordinaatti[1] = syote.charAt(0) - 'a';
			} catch (Exception e) {
				oikeaSyote = false;
				System.out.println(
						"VIRHE! Syötä peräkkäin kaksi merkkiä. Ensimmäisen merkin tulee olla kirjain väliltä a-j ja toisen numero väliltä 0-9 (esim 'd2'. Yritetään uudelleen:");
			}
			if(koordinaatti[0]<0 || koordinaatti[0]>9 || koordinaatti[1]<0 || koordinaatti[1]>9) {
				oikeaSyote = false;
			}
		} while (!oikeaSyote);
		return koordinaatti;
	}

	/**
	 * Metodi pyytää pelaajaa syöttämään suunnan ("alas" tai "oikealle") laivalle,
	 * jota ollaan asettamassa pelilaudalle. Mikäli pelaajan syöttämä suunta ei ole
	 * "alas" tai "oikealle", metodi pyytää pelaajaa syöttämään suunnan uudelleen.
	 * 
	 * @param s Scanneri, joka lukee pelaajan syötteen
	 * @return laivan suunta ('alas' tai 'oikealle')
	 */
	public static String syotaSuunta(Scanner s) {
		System.out.println("Syota 'alas' tai 'oikealle'");
		boolean sallittu = false;
		String suunta = null;
		do {
			suunta = s.nextLine();
			if (suunta.equals("alas") || suunta.equals("oikealle")) {
				sallittu = true;
			} else {
				System.out.println("VIRHE! Kirjoita 'alas' tai 'oikealle'. Muut suunnat eivät ole sallittuja");
			}
		} while (!sallittu);
		return suunta;
	}

	/**
	 * Metodi pyytää pelaajaa syöttämään laivan ensimmäisen koordinaatin (esim.
	 * "d2"). Koordinaatti muunnetaan taulukoksi, joka sisältää kaksi kokonaislukua:
	 * ensimmäinen luku kertoo vaakarivin ja toinen pystyrivin. Syötteen lukeminen
	 * voi heittää poikkeuksen, joten syötteen lukeminen on ympäröity try-catch
	 * -rakenteella. Poikkeuksen sattuessa metodi pyytää pelaajaa syöttämään
	 * koordinaatin uudelleen.
	 * 
	 * @param s            Scanneri, joka lukee pelaajan syötteen
	 * @param laiva        Sen laivan nimi, jota ollaan asettamassa pelilaudalle
	 * @param laivanPituus Sen laivan pituus, jota ollaan asettamassa pelilaudalle
	 * @return
	 */
	public static int[] syotaKoordinaatti(Scanner s, String laiva, int laivanPituus) {
		boolean oikeaSyote = true;
		int[] koordinaatti = new int[2];
		do {
			oikeaSyote = true;
			System.out.println("Syötä laivan " + laiva + " (pituus " + laivanPituus + ") aloituskoordinaatti.");
			try {
				String syote = s.nextLine();
				koordinaatti[0] = syote.charAt(1) - '0';
				koordinaatti[1] = syote.charAt(0) - 'a';
			} catch (Exception e) {
				oikeaSyote = false;
				System.out.println(
						"VIRHE! Syötä peräkkäin kaksi merkkiä. Ensimmäisen merkin tulee olla kirjain väliltä a-j ja toisen numero väliltä 0-9 (esim 'd2'. Yritetään uudelleen:");
			}
		} while (!oikeaSyote);
		return koordinaatti;
	}

}
