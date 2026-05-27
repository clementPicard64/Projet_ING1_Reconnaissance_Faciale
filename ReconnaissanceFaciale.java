package projet;

import java.util.List;

/**
 * Description : gère les interactions avec l'utilisateur
 * @author CandyCelia
 * @version 1.0
 */
public class ReconnaissanceFaciale {
	private double seuil;
	private Database database;
	private Image img;
	
    /**
     * CONSTRUCTEUR
     * @param seuil un double
     */
	public ReconnaissanceFaciale(double seuil) {
		this.seuil = seuil;
	}
	
	/**
	 * reconstruire methode qui prend un tableau d'entier et un entier et retourne un tableau d'entier
	 * @return un tableau d'entier
	 * @param vecteurProjete un tableau d'entier
	 * @param K un entier
	 */
	public double[] reconstruire(double[] vecteurProjete, int K) {
		return null;  // A FAIRE
	}
	
	/**
	 * identifier prends une image et retourne une chaine de caractère
	 * @return une chaine de caractère
	 * @param imageTest une image
	 */
	public String identifier(Image imageTest) {
		return "";  // A FAIRE
	}
	
	/**
	 * evaluerTauxIdentification prends une liste d'images et renvoie un double
	 * @return un double
	 * @param baseTest une liste d'images
	 */
	public double evaluerTauxIdentification(List<Image> baseTest) {
		return 0;  // A FAIRE
	}
	
	/**
	 * calculerPlusCourtDistance retourne une image
	 * @return une image
	 */
	public Image calculerPlusCourtDistance() {
		return null;  // A FAIRE
	}
	
	/**
	 * diffDistanceSeuil retourne un booleen
	 * @return vrai ou faux
	 */
	public Boolean diffDistanceSeuil() {
		return false;  // A FAIRE
	}
	
	/**
	 * calculeDistance prend une image et retourne un double
	 * @return un double
	 * @param img une Image
	 */
	public double calculeDistance(Image img) {
		return 0;  // A FAIRE
	}

}
