package projet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList; 
import java.util.List;

import org.apache.commons.math3.distribution.FDistribution;

import com.aspose.cells.*;

/**
 * Description : gère la base d'images de référence utilisée pour la reconnaissance faciale
 * @author CandyCelia
 * @version 3.0
 */
public class Database {
	private String cheminBaseReference;
	private String cheminBaseTest;
	private int taille;
	private double[] valeursPropres; //MODIFIER DANS LE DIAGRAMME
	//POUR STOCKER LES IMAGES DE LA BASE
	private Image[] images; //MODIFIER DANS LE DIAGRAMME
	//UTILE DANS PROJETER MATRICE
	private double[][] projections; //MODIFIER DANS LE DIAGRAMME
	
	private Matrice eigenfaces;
	private Matrice matriceTotal;
	private Vecteur visageMoyen;
	private List<Personne> p;
	
	private Vecteur[] vecteurs_image;
	
	
    /**
     * CONSTRUCTEUR
     * @param cheminBaseReference chemin vers le dossier contenant les images de référence
     */
	public Database(String cheminBaseReference) {
		this.cheminBaseReference = cheminBaseReference;
		taille = 0; 
		valeursPropres = null;
		images = null;
		projections = null;
		p = null;
	}
	
	/**
	 * chargerBase ouvre le dossier au chein donnée, lit chaque images et les stock
	 * @param chemin une chaine de caractère
	 */
	public void chargerBase(String chemin) throws IOException {
		File dossier = new File(chemin); //creer un objet File avec le chemin
		File[] sousDossiers = dossier.listFiles();
		//File[] fichiers = dossier.listFiles(); //liste les fichiers du dossier avec listFiles()
		//images = new Image[fichiers.length]; //initialiser images avec la bonne taille
		//int i = 0;
		
		List<Image> listeTemporaireImages = new ArrayList<>();
		
		for (File sousDossier : sousDossiers) { //parcours dossier principal
			if (sousDossier.isDirectory()) {
                // Extraction du nom et prénom à partir du nom du dossier "Nom_Prenom"
                String nomDossier = sousDossier.getName();
                String[] parties = nomDossier.split("_");
                if (parties.length <2) continue;
                String nom;
                nom = parties[0];
                String prenom;
                prenom = parties[1];
                Personne personne = new Personne(nom, prenom); //creer une personne pour chaques sous dossiers
                if (p == null) { // Si c'est la premiere personne de la base d'images 
        	        p = new ArrayList<Personne>();
        	    }
				//charge images et les stocks
                this.p.add(personne);
                File[] fichiersImages = sousDossier.listFiles();
                if (fichiersImages != null) {
                    for (File fichierImg : fichiersImages) {
                        if (fichierImg.isFile()) {
                            personne.ajouterImage(fichierImg);
                            ArrayList<Image> imgs = personne.getListImage();
                            listeTemporaireImages.add(imgs.get(imgs.size()-1));
                        }
                    }
                }
			}
		}
		this.taille = listeTemporaireImages.size(); //la taille : le nb d'images chargés
	    this.images = listeTemporaireImages.toArray(new Image[0]);
	}
	
    /**
     * GETTER taille
     * @return entier taille
     */
	public int getTaille() {
		return taille;
	}
	
    /**
     * GETTER cheminBaseReference
     * @return chaine de caractère cheminBaseReference
     */
	public String getChemin() {
		return cheminBaseReference;
	}
	
	/**
	 * GETTER cheminBaseTest
	 * @return chaine de caractère cheminBaseTest
	 */
	public String getCheminTest() {
		return cheminBaseTest;
	}

	 /**
     * GETTER p
     * @return liste de personnes
     */
	public List<Personne> getListPersonne() {
	    if (p == null) return new ArrayList<>();
	    return p;
	}
	
	 /**
     * GETTER eigenfaces
     * @return eigenfaces
     */
	public Matrice getEigenfaces() {
	    return eigenfaces;
	}
	
	/**
     * GETTER valeurs propres
     * @return valeursPropres
     */
	public double[] getValeursPropres() {
		// TODO Auto-generated method stub
		return valeursPropres;
	}
	
	/**
	 * getMatriceTotal méthode construit une mat a partir de tous les vecteurs des images
	 * @return une Matrice qui contient tous les vecteursImages
	 */
	public Matrice getMatriceTotal() {
		Vecteur[] v = new Vecteur[getTaille()]; //creer un tab de taille taille
		int index = 0;
		
		for (Image i : images) { //pour chaques images
			v[index] = i.getVecteurImage(); //recupere son vecteur et on le met dans le tab
			index++;
		}
		Matrice m = new Matrice(v); //creer la mat
	    return m;
	}
	
	/**
	 * GETTER visageMoyen
	 * @return visageMoyen le vecteur moyen de la base d'apprentissage
	 */
	public Vecteur getVisageMoyen() {
		return visageMoyen;
	}

	/**
	 * projeterVecteur projette un vecteur brut dans l'espace ACP (après centrage)
	 * @param v le vecteur brut à projeter
	 * @return tableau de coordonnées dans la base ACP
	 */
	public double[] projeterVecteur(Vecteur v) {
		// Centrage : on soustrait le visage moyen
		double[] vCentre = v.getVecteur().clone();
		double[] moy = visageMoyen.getVecteur();
		for (int j = 0; j < vCentre.length; j++) {
			vCentre[j] -= moy[j];
		}
		Vecteur vecteurCentre = new Vecteur(vCentre);

		double[] tab = new double[eigenfaces.getN()];
		int i = 0;
		for (Vecteur e : eigenfaces.getMatrice()) {
			tab[i] = e.produitScalaire(vecteurCentre);
			i++;
		}
		return tab;
	}

	/**
	 * projeterImage projette une image dans l'espace ACP (après centrage)
	 * @param img l'image à projeter
	 * @return tableau de coordonnées dans la base ACP
	 */
	public double[] projeterImage(Image img) {
		return projeterVecteur(img.getVecteurImage());
	}
	
	/**
	 * projeterMatrice projette toutes les images de la base dans l'espace ACP et stocke leurs coordonnées dans le tableau projections
	 */
	public void projeterMatrice() {
		projections = new double[taille][]; //initialiser projections
		int index = 0;
		
		for (Image i : images) { //pour chaque image 
			projections[index] = projeterImage(i); 
			index++;
		}
	}
	
	/**
	 * ajouterNouvellePersonne ajoute une personne et son image à la base. Si la personne existe déjà, l'image est simplement ajoutée à son dossier existant.
	 * @param nom le nom de la personne à ajouter
	 * @param prenom le prénom de la personne à ajouter
	 * @param img le fichier image à associer à la personne
	 */
	public void ajouterNouvellePersonne(String nom, String prenom, File img) {
		if (p == null) { // Si c'est la premiere personne de la base d'images 
	        p = new ArrayList<Personne>();
	    }
		Image[] newImages = new Image[taille + 1]; //agrandit le tableau images[] de 1
		for (int i = 0; i < taille; i++) {
		    newImages[i] = images[i]; //on met tous les elements de images[] dans le nouveaux tab
		}
		// Création du dossier de la personne - Ajout Clement
		String chemin = "/home/cytech/Projet/" + nom + "_" + prenom; //On créé le chemin
	    File dossier = new File(chemin); 
	    Personne personne = null;
	    
	    if (!dossier.exists()) {
		    //On créé la personne
		    personne = new Personne(nom,prenom);
		    p.add(personne); //ajoute la personne a la liste
	    }
	    
	    else {
	    	for (Personne pers : this.p) {
	    		if ((pers.getNom().equals(nom)) && (pers.getPrenom().equals(prenom))) {
	    			personne = pers;	
	    		}
	    	}
	    }
	    
	    personne.ajouterImage(img);
	    
	    int n = personne.getListImage().size();
	    Image image = personne.getListImage().get(n-1); //Dans le cas ou il y a plusieurs images
	    // Fin Ajout Clement
	    
		newImages[taille] = image; //on ajoute img a la fin
		taille = taille +1; //maj la taille du tab
		images = newImages; //on maj le tab de base
		
	}
	
	/**
	 * getListNomPersonne affiche dans la console le nom et prénom de chaque personne de la base
	 */
	//Verif 
	public void getListNomPersonne() {
		for (Personne pers : this.p) {
			System.out.println(pers.getNom() + "_" + pers.getPrenom());
		}
	}
	/**
	 * getListNomImage affiche dans la console l'identifiant de chaque image de la base
	 */
	//Verif
	public void getListNomImage() {
		for (Image img : this.images) {
			System.out.println(img.toString());
		}
	}
	/*
	//Verif
	public void ImagePersonne() {
		Personne pers;
		pers = this.p[0];
		
	}*/
	
	/**
	 * traiterImages redimensionne, convertit en niveaux de gris et vectorise toutes les images de la base, puis calcule le visage moyen, extrait les eigenfaces, projette toutes les images dans la nouvelle base et génère un fichier Excel contenant le nuage de points des projections.
	 * @throws IOException
	 */
	//Ajout traiter toutes les images pour les transformer en vecteurs puis pour y faire les opérations mathématiques
	public void traiterImages() throws IOException {
		int n = images.length;
		this.vecteurs_image = new Vecteur[n];
		int cpt = -1;
		for (Image img : this.images) {
			cpt += 1;
			img.redimensionner();
			img.convertirEnNiveauDeGris();
			img.vectoriser();
			Vecteur v = img.getVecteurImage();
			this.vecteurs_image[cpt] = v;
		}
		this.matriceTotal = new Matrice(this.vecteurs_image);
		this.visageMoyen = this.matriceTotal.calculVisageMoyen(); // sauvegarde du visage moyen pour centrer lors des projections
		this.eigenfaces = this.matriceTotal.extraireEigenfaces();
		this.valeursPropres = this.matriceTotal.getValSing();
		//verifOrthogonalite(eigenfaces);
		//afficherMatrice(eigenfaces);
		int nb_val = this.matriceTotal.getValSing().length;
		double[][] mat = new double[n][nb_val];
		int cpt1 = -1;
		for (Image img : this.images) {
			cpt1 += 1;
			ReconnaissanceFaciale rc = new ReconnaissanceFaciale(this, img, null, null);
			mat[cpt1] = rc.reconstruire(img.getVecteurImage().getVecteur());
		}
		this.projections = mat;
		Workbook workbook = new Workbook();

	    int index = workbook.getWorksheets().add();
	    Worksheet sheet = workbook.getWorksheets().get(index);
	    sheet.setName("Projection");

	    ReconnaissanceFaciale rcExcel = new ReconnaissanceFaciale(this, sheet, null, null);

	    rcExcel.miseAJourWorksheet(mat);    // on remplit la feuille Excel

	    rcExcel.afficherNuage(mat); // on crée le graphique (nuage de points)

	    try {
	        workbook.save("resultat_reconnaissance.xlsx");// on sauvegarde finale (sécurisée)
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * afficherVecteur affiche dans la console toutes les valeurs d'un vecteur sous forme de tableau.
	 * @param v le Vecteur à afficher
	 */
	//Verif
	public static void afficherVecteur(Vecteur v) {

        double[] tab = v.getVecteur();

        System.out.print("[ ");

        for (double valeur : tab) {
            System.out.print(valeur + " ");
        }

        System.out.println("]");
    }
	
	/**
	 * afficherMatrice affiche dans la console chaque vecteur ligne d'une Matrice.
	 * @param m la Matrice à afficher
	 */
	public static void afficherMatrice(Matrice m) {

        Vecteur[] lignes = m.getMatrice();

        for (Vecteur v : lignes) {
            afficherVecteur(v);
        }
    }
	
	/**
	 * calculT2Alpha calcule le seuil théorique T^2_alpha de la statistique de Hotelling à partir d'un quantile de la loi de Fisher F(k, n-k) au niveau de confiance 1-alpha.
	 * @return un double représentant le seuil T^2_alpha
	 */
	public double calculT2Alpha() {
		double alpha = 0.95;
	    int k = eigenfaces.getN();  // nombre d'eigenfaces retenues
	    int n = taille;             // nombre d'images d'apprentissage

	    FDistribution fisher = new FDistribution(k, n-k); // quantile de la loi de Fisher F(K, n-K) 1-alpha
	    double quantileFisher = fisher.inverseCumulativeProbability(1-alpha);

	    return ((double) k*(n-1))/(n-k)*quantileFisher; //formule seuil théorique
	}
}
