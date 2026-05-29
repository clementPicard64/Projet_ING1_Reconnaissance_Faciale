package projet;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : gère les interactions avec l'utilisateur
 */
public class ReconnaissanceFaciale {
	
	private double seuil;
	private Database database;
	private Image img;
	
    /**
     * CONSTRUCTEUR
     * @param seuil un double
     */
	public ReconnaissanceFaciale(double seuil, Database database) {
		this.seuil = seuil;
		this.database = database;
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
		
		this.img = imageTest;
		
		Image res = calculerPlusCourtDistance();

        if (res == null) {
            return "Inconnu";
        }
        double distance  = calculeDistance(res.getVecteurImage());
        if (diffDistanceSeuil(distance)) {
        	return "Inconnu";
        }
        return res.getCheminImage();
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
		
		double minDistance = -1;
		Image imagePlusProche = null;
		
		List<Image> toutesLesImages = new ArrayList<>();
		for (Personne personne : database.getListPersonne() ) {
			toutesLesImages.addAll(personne.getListImage());
		}

        for (int i = 0; i < toutesLesImages.size(); i++) {

            double nouvelleDistance = calculeDistance(toutesLesImages.get(i).getVecteurImage());

            if (minDistance == -1 || nouvelleDistance < minDistance) {
                minDistance = nouvelleDistance;
                imagePlusProche = toutesLesImages.get(i);
            }
        }

		return imagePlusProche;
	}
	
	/**
	 * diffDistanceSeuil retourne un booleen
	 * @return vrai ou faux
	 */
	public Boolean diffDistanceSeuil(double distance) {
        return distance < this.seuil;
	}
	
	/**
	 * calculeDistance prend une image et retourne un double
	 * @return un double
	 * @param img une Image
	 */
	public double calculeDistance(double[] vecteur) {
		
		double[] vecteurTest = this.img.getVecteurImage();
		double somme = 0;
		
		for (int i = 0; i < vecteur.length; i++) {
			somme += Math.pow(vecteurTest[i] - vecteur[i], 2);
		}
		somme = Math.sqrt(somme);
		return somme;
	}

}
