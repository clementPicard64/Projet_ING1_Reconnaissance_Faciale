package projet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.aspose.cells.*;
import java.io.IOException;

/**
 * Description : gère la reconnaissance faciale, le calcul des seuils et l'identification des images
 */
public class ReconnaissanceFaciale {
	
	private double seuil;
	private Database database;
	private Image img;
	private Worksheet worksheet;
	
    /**
     * CONSTRUCTEUR utilisé pour calculer le seuil theta_d à partir des deux bases
     * @param baseConnue liste d'images censées être reconnues par la base de référence
     * @param baseInconnue liste d'images inconnues (non présentes dans la base de référence)
     * @param database la base de données de référence
     * @throws IOException
     */
	public ReconnaissanceFaciale(List<Image> baseConnue,List<Image> baseInconnue, Database database) throws IOException {
		this.database = database;
		this.seuil = evaluerTauxIdentification(baseConnue, baseInconnue);
	}
	
	/**
     * CONSTRUCTEUR utilisé pour identifier une image spécifique
     * @param database la base de données de référence
     * @param img l'image à identifier
     * @param baseConnue liste d'images censées être reconnues (pour le calcul du seuil theta_d)
     * @param baseInconnue liste d'images inconnues (pour le calcul du seuil theta_d)
	 * @throws IOException
     */
	public ReconnaissanceFaciale(Database database, Image img, List<Image> baseConnue,List<Image> baseInconnue) throws IOException {
		this.database = database;
		this.img = img;
		this.seuil = evaluerTauxIdentification(baseConnue, baseInconnue);
	}
	
	/**
	 * CONSTRUCTEUR utilisé pour générer le nuage de points dans une feuille Excel
	 * @param database la base de données de référence
	 * @param worksheet la feuille Excel dans laquelle les projections seront écrites
	 * @param baseConnue liste d'images censées être reconnues (pour le calcul du seuil theta_d)
	 * @param baseInconnue liste d'images inconnues (pour le calcul du seuil theta_d)
	 * @throws IOException
	 */
	public ReconnaissanceFaciale(Database database, Worksheet worksheet, List<Image> baseConnue,List<Image> baseInconnue) throws IOException {
		this.database = database;
		this.worksheet = worksheet;
		this.worksheet = worksheet;
		this.seuil = evaluerTauxIdentification(baseConnue, baseInconnue);
	}
	
	/**
	 * GETTER seuil
	 * @return seuil le seuil de distance theta_d calculé à partir des bases connue et inconnue
	 */
	public double getSeuil() {
		return seuil;
	}
	
	/**
	 * reconstruire projette un vecteur image dans l'espace des eigenfaces et retourne ses coordonnées
	 * @param vecteurProjete un tableau de double représentant le vecteur de l'image à projeter
	 * @return z_k un tableau de double contenant les coordonnées dans la base des eigenfaces
	 */
	public double[] reconstruire(double[] vecteurProjete){
		Matrice V = database.getEigenfaces();
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
	 * identifier prends une image et regarde si il y a une ressemblance avec les images de la base de donnée
	 *
	 * @return res.getCheminImage() est un String qui reprensente le chemin de l'image avec la plus courte distance
	 *
	 * @param imageTest est l'Image de comparaison 
	 */
	public String identifier(Image imageTest) {
		//Initialise l'image dans les variables de la classe
		this.img = imageTest;
		
		if (this.img.getVecteurImage() == null) {
	        this.img.redimensionner();
	        this.img.convertirEnNiveauDeGris();
	        this.img.vectoriser();
	    }
		
		Image res = calculerPlusCourtDistance(); //trouve la plus courte distance
		
		//Verifie qu'il retourne bien une valeur
        if (res == null) {
            return "Inconnu";
        }
        
        //Compare avec la distance de Hottelling
        if (calculT2(imageTest) >= database.calculT2Alpha()) {
            return "Inconnu (hors population)";
        }
        
        //Verifie si la distance est inferieur au seuil
        double dmin = calculeDistance(res.getVecteurImage());
        if (!diffDistanceSeuil(dmin)) { 
            return "Inconnu (trop distant)";
        }
        
        return res.getCheminImage();
	}
	
	/**
	 * evaluerTauxIdentification évalue le seuil de distance theta_d optimal comme moyenne entre la distance minimale moyenne des images connues et celle des images inconnues
	 * @param baseConnue liste d'images censées être reconnues par la base de référence
	 * @param baseInconnue liste d'images inconnues (non présentes dans la base de référence)
	 * @return un double représentant le seuil theta_d calculé (0.0 si la base connue est vide)
	 * @throws IOException
	 */
	public double evaluerTauxIdentification(List<Image> baseConnue, List<Image> baseInconnue) throws IOException {
		if (baseConnue == null || baseConnue.isEmpty()) {
	        return 0.0;
	    }
		if (baseInconnue == null || baseInconnue.isEmpty()) {
	        return 0.0;
	    }
		double[] dist1 = new double[baseConnue.size()];
		int i=0;
		List<Personne> p = null;
		try {
			p = database.getListPersonne(); //récupération de la liste des personnes
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Image I : baseConnue) { // triple boucle pour chercher la distance maximale parmi les distances minimales entre toutes les images de la base test et les images de la base de données
		    ReconnaissanceFaciale rf = new ReconnaissanceFaciale(null,null, this.database); //objet pour calculer la distance
		    rf.setImg(I);
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
		            minGlobal = min; //on garde le minimum toutes personnes confondues
		        }
		    }
		    dist1[i] = minGlobal; //on stocke la distance minimale pour cette image de test
		    i++;
		}
		
		double[] dist2 = new double[baseInconnue.size()];
		i=0;
		for (Image I : baseInconnue) { // triple boucle pour chercher la distance maximale parmi les distances minimales entre toutes les images de la base test et les images de la base de données
		    ReconnaissanceFaciale rf = new ReconnaissanceFaciale(null,null, this.database); //objet pour calculer la distance
		    rf.setImg(I);
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
		            minGlobal = min; //on garde le minimum toutes personnes confondues
		        }
		    }
		    dist2[i] = minGlobal; //on stocke la distance minimale pour cette image de test
		    i++;
		}
		double seuilConnus = Arrays.stream(dist1).average().orElse(0.0);
		double seuilInconnus = Arrays.stream(dist2).average().orElse(0.0);
		if (dist1.length == 0) return seuilInconnus;
		if (dist2.length == 0) return seuilConnus;
		double seuil = (seuilConnus + seuilInconnus) / 2;
		return seuil;
	}
	
	/**
	 * SETTER img
	 * @param img l'image à affecter comme image courante de comparaison
	 */
	public void setImg(Image img) {
	    this.img = img;
	}
	
	/**
	 * calculerPlusCourtDistance qui va calculer et comparer toutes les distances entre l'image test et les images que nous avons dans la base de donnée pour retourner celle avec la plus courte distance
	 *
	 * @return imagePlusProche un objet image qui correspond à l'image la plus proche de notre image test
	 */
	public Image calculerPlusCourtDistance() {
		
		//Initialisation des varriables
		double minDistance = -1; //Distance la plus courte
		Image imagePlusProche = null; //Stock l'image avec la distance la plus courte
		List<Image> toutesLesImages = new ArrayList<>(); //Creation d'un tableau qui stockera toutes les images de notre base de donnée
		
		//Parcours toutes les personnes et ajoute toutes leurs images dans le tableau d'image toutesLesImages
		for (Personne personne : database.getListPersonne() ) {
			toutesLesImages.addAll(personne.getListImage());
		}
		
		//Parcours toutes les images de notre liste d'image pour calculer les distances et trouver la plus courte
        for (int i = 0; i < toutesLesImages.size(); i++) {

            if (toutesLesImages.get(i).getVecteurImage() == null) continue;
            
            //Calcule la distance entre l'image test et l'image i de la liste d'image
            double nouvelleDistance = calculeDistance(toutesLesImages.get(i).getVecteurImage());

            //Si aucune distance rentré ou que la distance est plus petite que celle actuel la remplace
            if (minDistance == -1 || nouvelleDistance < minDistance) {
                minDistance = nouvelleDistance;
                imagePlusProche = toutesLesImages.get(i);
            }
        }
        //Renvoie l'image
        System.out.println(minDistance);
        return imagePlusProche;
	}
	
	/**
	 * diffDistanceSeuil compare une distance et le seuil definie.
	 *
	 * @param distance un double qui représente une distance entre deux images (img test et une autre de la base de donnnée).
	 *
	 * @return un boolean qui est vrai si la distance est plus petite que le seuil et Faux si la distance est plus grande ou égale.
	 */
	public Boolean diffDistanceSeuil(double distance) {
        return distance < this.seuil;
	}
	
	/**
	 * calculeDistance permet de calculer la distance entre deux images, l'image test et une autre de la base de donnée
	 *
	 * @return somme est un double qui represente la distance entre deux image
	 *
	 * @param vecteur qui est un Vecteur d'une Image
	 */
	public double calculeDistance(Vecteur vecteur) {
		
		//Initialise les variables
		double[] vecteurTest = this.img.getVecteurImage().getVecteur();
		double[] tabVecteur = vecteur.getVecteur();
		double somme = 0;
		
		//Parcours chaque valeur des deux vecteurs, les soustraits et les mets au carré. Tout en les additionnants à la somme
		for (int i = 0; i < tabVecteur.length; i++) {
			somme += Math.pow(vecteurTest[i] - tabVecteur[i], 2);
		}
		//Fait la racine de la somme
		somme = Math.sqrt(somme);
		return somme;
	}



	/**
	 * calculT2 calcule la statistique de Hotelling T^2 d'une image dans l'espace des eigenfaces
	 * @param img l'image dont on souhaite calculer la statistique T^2
	 * @return T2 un double représentant la valeur de la statistique de Hotelling
	 */
	public double calculT2(Image img) {
	    int k = database.getEigenfaces().getN();
	    double[] beta = reconstruire(img.getVecteurImage().getVecteur());
	    double[] sigmas = database.getValeursPropres();
	
	    double T2 = 0;
	    for (int j = 0; j < k; j++) {
	        double lambda = sigmas[j] * sigmas[j];
	        T2 += (beta[j] * beta[j]) / lambda;
	    }
	    return T2;
	}
	
	/**
	 * evaluerSeuilT2 affiche le seuil T^2_alpha et le nombre d'images rejetées parmi la base de test
	 * @param baseTest liste d'images sur lesquelles évaluer le seuil T^2
	 */
	public void evaluerSeuilT2(List<Image> baseTest) {
	    double T2_alpha = database.calculT2Alpha();
	    int rejetes = 0;
	    for (Image img : baseTest) {
	        if (calculT2(img) >= T2_alpha) rejetes++;
	    }
	    System.out.println("Seuil T_alpha : " + T2_alpha);
	    System.out.println("Rejetés : " + rejetes + "/" + baseTest.size());
	}
	
	
}
