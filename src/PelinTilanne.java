import java.io.Serializable;

public class PelinTilanne implements Serializable {

	private int pelaajalaskuri = 0;
	private boolean tallennus = false;
	private boolean ammutaanSatunnaisesti = true;
	private int[] laivanEnsimmainenOsuma = null;
	private int[] edellinenOsuma = null;
	private int[] suunta = null;
	private int osumatLaivaan = 0;
	private int koordinaattienSumma = 0; // Pelin lyhimmän laivan pituus on 2, joten tietokonepelaaja ampuu vain joka
											// toiseen ruutuun. Ammutaan siis vain koordinaatteihin, joiden summa on
											// parillinen tai niihin joiden summa on pariton. Ennen pelin alkua arvotaan
											// onko tämän attribuutin arvo 0 vai 1 (parillinen vai pariton).
	private boolean etsitaan12Ruutua = true;
	private boolean etsitaan4Ruutua = true;

	public boolean isEtsitaan12Ruutua() {
		return etsitaan12Ruutua;
	}

	public void setEtsitaan12Ruutua(boolean etsitaan12Ruutua) {
		this.etsitaan12Ruutua = etsitaan12Ruutua;
	}

	public boolean isEtsitaan4Ruutua() {
		return etsitaan4Ruutua;
	}

	public void setEtsitaan4Ruutua(boolean etsitaan4Ruutua) {
		this.etsitaan4Ruutua = etsitaan4Ruutua;
	}

	public int getKoordinaattienSumma() {
		return koordinaattienSumma;
	}

	public void setKoordinaattienSumma(int yksiVaiNolla) {
		this.koordinaattienSumma = yksiVaiNolla;
	}

	public void kasvataPelaajalaskuri() {
		pelaajalaskuri += 1;
	}

	public void kasvataOsumatLaivaan() {
		osumatLaivaan += 1;
	}

	public int getPelaajaLaskuri() {
		return pelaajalaskuri;
	}

	public void setPelaajaLaskuri(int pelaajaLaskuri) {
		this.pelaajalaskuri = pelaajaLaskuri;
	}

	public boolean isTallennus() {
		return tallennus;
	}

	public void setTallennus(boolean tallennus) {
		this.tallennus = tallennus;
	}

	public boolean isAmmutaanSatunnaisesti() {
		return ammutaanSatunnaisesti;
	}

	public void setAmmutaanSatunnaisesti(boolean ammutaanSatunnaisesti) {
		this.ammutaanSatunnaisesti = ammutaanSatunnaisesti;
	}

	public int[] getLaivanEnsimmainenOsuma() {
		return laivanEnsimmainenOsuma;
	}

	public void setLaivanEnsimmainenOsuma(int[] laivanEnsimmainenOsuma) {
		this.laivanEnsimmainenOsuma = laivanEnsimmainenOsuma;
	}

	public int[] getEdellinenOsuma() {
		return edellinenOsuma;
	}

	public void setEdellinenOsuma(int[] edellinenOsuma) {
		this.edellinenOsuma = edellinenOsuma;
	}

	public int[] getSuunta() {
		return suunta;
	}

	public void setSuunta(int[] suunta) {
		this.suunta = suunta;
	}

	public int getOsumatLaivaan() {
		return osumatLaivaan;
	}

	public void setOsumatLaivaan(int osumatLaivaan) {
		this.osumatLaivaan = osumatLaivaan;
	}

}
