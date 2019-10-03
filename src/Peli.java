import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Scanner;

public class Peli {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Random r = new Random();

		Pelaaja p1 = null;
		Pelaaja p2 = null;
		PelinTilanne pt = null;
		boolean virheLatauksessa = false;

		System.out.println("Tervetuloa pelaamaan laivanupotusta!");
		do { // Tämä silmukka on siltä varalta, että pelaaja haluaa ladata pelin, mutta
				// syöttää esim väärän pelin nimen.
			System.out.println(
					"Jos haluat ladata tallennetun pelin, syötä 'lataa'. Jos haluat aloittaa uuden pelin, syötä mitä tahansa.");
			String ladataanko = s.nextLine();
			if (ladataanko.contentEquals("lataa")) {
				Tallennus t;
				try {
					t = lataa(s);
					pt = t.getPelinTilanne();
					p1 = t.getPelaaja1();
					p2 = t.getPelaaja2();
					System.out.println("Jatketaan peliä!");
				} catch (Exception e) {
					System.out.println("VIRHE! Tallennusta ei löydetty.");
					virheLatauksessa = true;
				}
			} else { // aloitetaan uusi peli
				p1 = new IhmisPelaaja();
				System.out.println("Syötä nimesi.");
				String pelaajan1Nimi = s.nextLine();
				p1.setNimi(pelaajan1Nimi);

				System.out.println("Haluatko pelata kaverin kanssa vai tietokonetta vastaan?");
				System.out.println("Syötä 'kaveri' tai 'tietokone'.");
				boolean virheSyotteessa = false;
				do { // Tämä silmukka on siltä varalta, että pelaajan syöte ei ole 'kaveri' eikä
						// 'tietokone'
					String peli = s.nextLine();
					if (peli.equals("kaveri")) {
						p2 = new IhmisPelaaja();
						System.out.println("Pelaaja 2 syötä nimesi.");
						String pelaajan2Nimi = s.nextLine();
						p2.setNimi(pelaajan2Nimi);
					} else if (peli.equals("tietokone")) {
						p2 = new TietokonePelaaja();
					} else {
						System.out.println("VIRHE! Syötä 'kaveri' tai 'tietokone'.");
						virheSyotteessa = true;
					}
				} while (virheSyotteessa);

				System.out.println("Aluksi pelaajat syöttävät kaikki laivansa pelilaudalle");
				System.out.println(
						"Laivat saavat koskettaa toisiaan vain kulmittain. Laivat eivät siis saa viereisissä ruuduissa.");
				System.out.println(
						"Laiva syötetään antamalla ensin aloituskoordinaatti (esim. 'f6') ja sen jälkeen suunta (alas tai oikealle).");

				p1.syotaLaivat(s);
				p2.syotaLaivat(s);
				pt = new PelinTilanne();
				System.out.println("Aloitetaan peli!");
				System.out.println(
						"Voit tallentaa pelin haluamassasi kohdassa syöttämällä 'tallenna' (ammuksen koordinaatin sijaan)");
			}
		} while (virheLatauksessa);

		// käynnistetään peli!
		pt.setKoordinaattienSumma(r.nextInt(2)); // tietokonepelaaja ampuu vain joka toiseen ruutuun, koska lyhimmän
													// laivan pituus on 2
		while (true) {
			if (pt.getPelaajaLaskuri() % 2 == 0) {
				System.out.println("Pelaajan " + p1.getNimi() + " vuoro:");
				p1.pelaaVuoro(pt, p2, s);
				if (pt.isTallennus()) {
					pt.setTallennus(false);
					tallenna(s, p1, p2, pt);
				}
			} else {
				System.out.println("Pelaajan " + p2.getNimi() + " vuoro:");
				p2.pelaaVuoro(pt, p1, s);
				if (pt.isTallennus()) {
					pt.setTallennus(false);
					tallenna(s, p1, p2, pt);
				}
			}
		}
	}

	public static void tallenna(Scanner s, Pelaaja p1, Pelaaja p2, PelinTilanne pt) {
		Tallennus t = new Tallennus(p1, p2, pt);
		System.out.println("Syötä tallennettavan pelin nimi.");
		String nimi = s.nextLine();
		String filename = nimi + ".ser";
		try {
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(t);
			out.close();
			file.close();
			System.out.println("Peli tallennettu!");
			;
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Peli lopetettu!");
		s.close();
		System.exit(0);
	}

	public static Tallennus lataa(Scanner s) throws Exception {
		System.out.println("Syötä ladattavan pelin nimi.");
		String nimi = s.nextLine();
		String filename = nimi + ".ser";
		Tallennus t = new Tallennus();

		FileInputStream file = new FileInputStream(filename);
		ObjectInputStream in = new ObjectInputStream(file);
		t = (Tallennus) in.readObject();
		in.close();
		file.close();
		System.out.println("Peli ladattu!");
		return t;

	}

}
