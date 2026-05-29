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
     * CONSTRUCTEUR
     * @param img une image
     */
	public ReconnaissanceFaciale(Image img) {
		this.img = img;
	}
	
	/**
	 * reconstruire methode qui prend un tableau de double et un entier et retourne un tableau de double
	 * @return un tableau de double
	 * @param vecteurProjete un tableau d'entier
	 * @param K un entier
	 */
	public double[] reconstruire(double[] vecteurProjete, int K) {
		Matrice matrice = new Matrice();
		Matrice V = matrice.extraireEigenfaces(K);
		double[] z_k;
		for (int i=0; i<matrice.getM(); i++) {
			double somme = 0;
			for (int j=0; j<K; j++) {
				somme = somme + vecteurProjete[i]*V.getMatrice()[i].vecteur[j]; //calcul du vecteur
			}
			z_k[i] = somme;
		}
		return z_k; 
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
	 * evaluerTauxIdentification prend une liste d'images et renvoie un double. Evalue le seuil de distance nécessaire pour que les images soient reconnues
	 * @return un double (le seuil)
	 * @param baseTest une liste d'images projetées dans la nouvelle base censées être reconnues par notre reconnaissance faciale
	 */
	public double evaluerTauxIdentification(List<Image> baseTest) {
		double[] dist = new double[baseTest.size()];
		int i=0;
		List<Personne> p;
		try {
			p = database.getListPersonne();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Image I : baseTest) { // triple boucle pour chercher la distance maximale parmi les distances minimales entre toutes les images de la base test et les images de la base de données
			ReconnaissanceFaciale rf = new ReconnaissanceFaciale(I); //objet pour calculer la distance
			for (Personne P : p) {
				ArrayList<Image> L = ((Personne) p).getListImage(); //on récupère la liste des images par personne
				double min = rf.calculeDistance(L.get(0)); //on définit le minimum au premier élément
				for (Image J : ((Personne) p).getListImage()) { //boucle sur toutes les images
					if (min>rf.calculeDistance(J)) {
						min = rf.calculeDistance(J);
					}
				}
				dist[i] = min;
			}
			i++;
		}
		double seuil=dist[0]; //on cherche le seuil le plus grand
		for (int j=0; j<i; j++) {
			if (dist[i] > seuil){
				seuil=dist[i];
			}
		}
		return 1.2*seuil; // erreur anticipée du calcul du seuil de 20%
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
	public double calculeDistance(Vecteur vecteur) {
		
		double[] vecteurTest = this.img.getVecteurImage().getVecteur();
		double[] tabVecteur = vecteur.getVecteur();
		double somme = 0;
		
		for (int i = 0; i < tabVecteur.length; i++) {
			somme += Math.pow(vecteurTest[i] - tabVecteur[i], 2);
		}
		somme = Math.sqrt(somme);
		return somme;
	}

}
