package projet;

import java.util.ArrayList;
import java.util.List;
import com.aspose.cells.*;

/**
 * Description : gère les interactions avec l'utilisateur
 */
public class ReconnaissanceFaciale {
	
	private double seuil;
	private Database database;
	private Image img;
	private Worksheet worksheet;
	
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
	public ReconnaissanceFaciale(Database database, Image img) {
		this.img = img;
		this.database = database;
	}
	
	public ReconnaissanceFaciale(Database database, Worksheet worksheet) {
		this.worksheet = worksheet;
		this.database = database;
	}
	
	/**
	 * reconstruire methode qui prend un tableau de double et un entier et retourne un tableau de double
	 * @return un tableau de double
	 * @param vecteurProjete un tableau d'entier
	 * @param K un entier
	 */
	public double[] reconstruire(double[] vecteurProjete){
		//Matrice V = matrice.extraireEigenfaces(K);
		Matrice V = database.getEngenfaces();
		double[] z_k = new double[V.getM()];
		for (int i=0; i< V.getN(); i++) {
			double somme = 0;
			for (int j=0; j<V.getM(); j++) {
				somme = somme + vecteurProjete[j]*V.getMatrice()[i].vecteur[j]; //calcul du vecteur
			}
			z_k[i] = somme;
		}
		return z_k; 
	}
	
	
	/**
	 * procédure qui initialise / met à jour les valeurs des coordonnées quand on ajoute une image
	 * @param Z_k un tableau 2D contenant les coordonnées de toutes les images projetées dans la nouvelle base
	 */
	public void miseAJourWorksheet(double[][] Z_k) {
		Cells cells = worksheet.getCells();
		cells.get("A1").putValue("Nom");
		cells.get("A2").putValue("X");
		cells.get("A3").putValue("Y");
		for (int i=0; i<Z_k.length; i++) {
			for (int j=0; j<Z_k[0].length; j++) {
				//cells.get(i+1, 0).putValue(); récupérer le nom de l'image concernée, potentiellement en tableau en parametre
		        cells.get(i+1, 1).putValue(Z_k[i][0]);
		        cells.get(i+1, 2).putValue(Z_k[i][1]);
			}
		}
	}
	
	
	/**
	 * procédure qui affiche le nuage de points
	 * @param Z_k un tableau 2D contenant les coordonnées de toutes les images projetées dans la nouvelle base (pour avoir la taille)
	 */
	public void afficherNuage(double[][] Z_k) {
		int chartIndex = this.worksheet.getCharts().add(ChartType.SCATTER, 5, 5, 25, 15);
	    Chart chart = this.worksheet.getCharts().get(chartIndex);

	    //Y
	    chart.getNSeries().add("C2:C" + (Z_k.length + 1), true);
	    Series serie = chart.getNSeries().get(0);

	    //X
	    String sheetName = worksheet.getName();
	    serie.setXValues("=" + sheetName + "!B2:B" + (Z_k.length + 1));
	    serie.setName("Images");

	    chart.getTitle().setText("Nuage de points");
	    chart.getCategoryAxis().getTitle().setText("X");
	    chart.getValueAxis().getTitle().setText("Y");

	    try {
	        Workbook workbook = worksheet.getWorkbook();
	        workbook.save("nuage_points.xlsx");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	/**
	 * identifier prends une image et retourne une chaine de caractère
	 * @return une chaine de caractère
	 * @param imageTest une image
	 */
	public String identifier(Image imageTest) {
		
		this.img = imageTest;
		this.img.redimensionner();
		this.img.convertirEnNiveauDeGris();
		this.img.vectoriser();
		
		Image res = calculerPlusCourtDistance();

        if (res == null) {
            return "Inconnu";
        }
        double distance  = calculeDistance(res.getVecteurImage());
        /*if (diffDistanceSeuil(distance)) {
        	return "Inconnu";
        }*/
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
		List<Personne> p = null;
		try {
			p = database.getListPersonne(); //récupération de la liste des personnes
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Image I : baseTest) { // triple boucle pour chercher la distance maximale parmi les distances minimales entre toutes les images de la base test et les images de la base de données
		    ReconnaissanceFaciale rf = new ReconnaissanceFaciale(this.database,I); //objet pour calculer la distance
		    double minGlobal = -1; //on initialise le minimum global
		    
		    for (Personne P : p) {
		        ArrayList<Image> L = ((Personne) P).getListImage(); //on récupère la liste des images par personne
		        double min = rf.calculeDistance(L.get(0).getVecteurImage()); //on définit le minimum au premier élément
		        
		        for (Image J : ((Personne) P).getListImage()) { //boucle sur toutes les images
		            if (min > rf.calculeDistance(J.getVecteurImage())) {
		                min = rf.calculeDistance(J.getVecteurImage()); //on met à jour le minimum
		            }
		        }
		        if (minGlobal == -1 || min < minGlobal) {
		            minGlobal = min; //on garde le minimum pour toutes les personnes
		        }
		    }
		    dist[i] = minGlobal; //on stocke la distance minimale pour cette image de test
		    i++;
		}
		double seuil=dist[0]; //on cherche le seuil le plus grand
		for (int j=0; j<i; j++) {
			if (dist[j] > seuil){
				seuil=dist[j];
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
		
		for (Image img : toutesLesImages) {
			img.redimensionner();
			img.convertirEnNiveauDeGris();
			img.vectoriser();
		}
		
        for (int i = 0; i < toutesLesImages.size(); i++) {

            if (toutesLesImages.get(i).getVecteurImage() == null) continue;

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
