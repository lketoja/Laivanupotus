import java.util.Scanner;

public class IhmisPelaaja extends Pelaaja {

	/**
	 * T�m� metodi ylikirjoittaa Pelaaja-luokan vastaavan metodin. Metodi
	 * mahdollistaa sen, ett� pelaaja voi sy�tt�� omat laivansa pelilaudalle. Kun
	 * uusi pelaaja-olio, luodaan my�s pelaajalle pelilauta ja t�h�n pelilautaan
	 * tallennetaan kaikki pelaajan laivat. Metodin silmukka k�y l�pi kaikki n�m�
	 * laivat. Metodi kysyy jokaisen laivan kohdalla k�ytt�j�lt� laivan ensimm�ist�
	 * koordinaattia ja suuntaa. Jos laiva ei mahdu laudalle, tai se sit� yritet��n
	 * sijoittaa toisen laivan viereen (tai p��lle), kysyt��n uudelleen
	 * aloituskoordinaattia ja suuntaa. Sallittu sijainti tallennetaan ja
	 * tulostetaan aloitusruudukko, jossa juuri sy�tetty laiva n�kyy.
	 * 
	 */
	public void syotaLaivat(Scanner s) {
		System.out.println("Nyt pelaaja " + nimi
				+ " sy�tt�� kaikki omat laivansa. (Toinen pelaaja ei tietenk��n saa n�hd� t�t�!)");
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
					System.out.println("VIRHE! Laiva ulkona laudalta! Yritet��n uudestaan:");
					ehto = true;
					continue;
				}
				if (!pelilauta.onkoSijaintiSallittu(sijainti)) {
					System.out.println(
							"VIRHE! Laivat saavat koskettaa toisaan vain kulmista. Laivat eiv�t siis saa olla vierekk�in (tai p��llekk�in). Yritet��n uudelleen:");
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
	 * sen, ett� ihmispelaaja voi pelata oman vuoronsa. Ensin tulostetaan
	 * peliruudukko. Sen j�lkeen pyydet��n pelaajaa sy�tt�m��n ammuksen
	 * koordinaatti. Jos pelaaja sy�tt�� 'tallenna', lopetetaan metodin
	 * suorittaminen ja palataan main-metodiin pelin tallennusta varten. Mik�li
	 * pelaajan sy�t� 'tallenna' vaan ampuu normaalisti, tarkistetaan osuuko ammus
	 * vai ei ja suoritetaan tarvittavat toimenpiteet.
	 * 
	 * @param pt PelinTilanne (esim. kasvatetaan vuorolaskuria ja pelaajalaskuria)
	 * @param p  pelaaja, jonka laivoja ammutaan
	 * @param s  skanneri, jolla luetaan pelaajan sy�te
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
			// jos osuttu laiva upposi, niin tallennetaan viittaus t�h�n laivaan muuttujaan
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
	 * Metodi mahdollistaa sen, ett� ihmispelaaja pystyy ampumaan vastustajan
	 * laivoja. Pelaajaa pyydet��n sy�tt�m��n ammuksen koordinaatti. Koska sy�tteen
	 * lukeminen voi heitt�� poikkeuksen, se on ymp�r�ity try-catch -rakenteella.
	 * Mik�li poikkeus heitet��n, metodi pyyt�� pelaajaa sy�tt�m��n koordinaatin
	 * uudellen. Koordinaatti muunnetaan taulukoksi, joka sis�lt�� kaksi
	 * kokonaislukua: ensimm�inen luku kertoo vaakarivin ja toinen pystyrivin. T�m�
	 * taulukko palautetaan kutsujalle. Pelaaja voi my�s ammuksen koordinaatin
	 * sijaan sy�tt�� 'tallenna', jolloin luokan PelinTilanne attribuutin tallennus
	 * arvoksi muutetaan true. T�m�n j�lkeen palautetaan tyhj� kokonaislukutaulukko.
	 * 
	 * @param s  scanneri, jolla luetaan pelaajan sy�te
	 * @param pt PelinTilanne (jos pelaaja haluaa tallentaa pelin, attribuutin
	 *           tallennus arvoksi muutetaan true)
	 * @return kokonaislukutaulukko, joka kertoo ammuksen koordinaatit (tai tyhj�
	 *         taulukko, mik�li pelaaja haluaa tallentaa pelin)
	 */
	public int[] ammu(Scanner s, PelinTilanne pt) {
		boolean oikeaSyote = true;
		int[] koordinaatti = new int[2];
		do {
			oikeaSyote = true;
			System.out.println("Valitse mihin ruutuun haluat ampua ja sy�t� sen koordinaatti (esim 'g6')");
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
						"VIRHE! Sy�t� per�kk�in kaksi merkki�. Ensimm�isen merkin tulee olla kirjain v�lilt� a-j ja toisen numero v�lilt� 0-9 (esim 'd2'. Yritet��n uudelleen:");
			}
			if(koordinaatti[0]<0 || koordinaatti[0]>9 || koordinaatti[1]<0 || koordinaatti[1]>9) {
				oikeaSyote = false;
			}
		} while (!oikeaSyote);
		return koordinaatti;
	}

	/**
	 * Metodi pyyt�� pelaajaa sy�tt�m��n suunnan ("alas" tai "oikealle") laivalle,
	 * jota ollaan asettamassa pelilaudalle. Mik�li pelaajan sy�tt�m� suunta ei ole
	 * "alas" tai "oikealle", metodi pyyt�� pelaajaa sy�tt�m��n suunnan uudelleen.
	 * 
	 * @param s Scanneri, joka lukee pelaajan sy�tteen
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
				System.out.println("VIRHE! Kirjoita 'alas' tai 'oikealle'. Muut suunnat eiv�t ole sallittuja");
			}
		} while (!sallittu);
		return suunta;
	}

	/**
	 * Metodi pyyt�� pelaajaa sy�tt�m��n laivan ensimm�isen koordinaatin (esim.
	 * "d2"). Koordinaatti muunnetaan taulukoksi, joka sis�lt�� kaksi kokonaislukua:
	 * ensimm�inen luku kertoo vaakarivin ja toinen pystyrivin. Sy�tteen lukeminen
	 * voi heitt�� poikkeuksen, joten sy�tteen lukeminen on ymp�r�ity try-catch
	 * -rakenteella. Poikkeuksen sattuessa metodi pyyt�� pelaajaa sy�tt�m��n
	 * koordinaatin uudelleen.
	 * 
	 * @param s            Scanneri, joka lukee pelaajan sy�tteen
	 * @param laiva        Sen laivan nimi, jota ollaan asettamassa pelilaudalle
	 * @param laivanPituus Sen laivan pituus, jota ollaan asettamassa pelilaudalle
	 * @return
	 */
	public static int[] syotaKoordinaatti(Scanner s, String laiva, int laivanPituus) {
		boolean oikeaSyote = true;
		int[] koordinaatti = new int[2];
		do {
			oikeaSyote = true;
			System.out.println("Sy�t� laivan " + laiva + " (pituus " + laivanPituus + ") aloituskoordinaatti.");
			try {
				String syote = s.nextLine();
				koordinaatti[0] = syote.charAt(1) - '0';
				koordinaatti[1] = syote.charAt(0) - 'a';
			} catch (Exception e) {
				oikeaSyote = false;
				System.out.println(
						"VIRHE! Sy�t� per�kk�in kaksi merkki�. Ensimm�isen merkin tulee olla kirjain v�lilt� a-j ja toisen numero v�lilt� 0-9 (esim 'd2'. Yritet��n uudelleen:");
			}
		} while (!oikeaSyote);
		return koordinaatti;
	}

}
