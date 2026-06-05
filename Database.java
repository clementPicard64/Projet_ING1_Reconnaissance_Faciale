package projet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList; //Ajout Clément
import java.util.List;

import com.aspose.cells.*;

/**
 * Description : gère la base d'images de référence utilisée pour la reconnaissance faciale
 * @author CandyCelia
 * @version 3.0
 */
public class Database {
	private String cheminBaseReference;
	private int taille;
	@SuppressWarnings("unused")
	private double[] valeursPropres; //MODIFIER DANS LE DIAGRAMME
	//POUR STOCKER LES IMAGES DE LA BASE
	private Image[] images; //MODIFIER DANS LE DIAGRAMME
	//UTILE DANS PROJETER MATRICE
	private double[][] projections; //MODIFIER DANS LE DIAGRAMME
	
	private Matrice eigenfaces;
	@SuppressWarnings("unused")
	private Matrice matriceTotal;
	@SuppressWarnings("unused")
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
     * GETTER p
     * @return liste de personnes
     */
	public List<Personne> getListPersonne() {
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
	 * projeterImage méthode qui prends une image en parametre et retourne un tableau de double
	 * @return un tableau de double
	 * @param img une image
	 */
	//MAJ DANS LE DIAGRAMME
	//calcule coordonnées d'une image dans le sous espace de eigenface via prod scalaire
	public double[] projeterImage(Image img) {
		Vecteur v = img.getVecteurImage(); //recup le vect de img
		double[] tab = new double[eigenfaces.getN()]; //creer un  tab de taille nb de eigenfaces
		int i = 0;
		
		for (Vecteur e : eigenfaces.getMatrice()) {//ppur chauqe eigenface dans eigenfaces.getMatrice()
			tab[i] = e.produitScalaire(v); //stocker chaque prod scalaire du vect img dans le tab
			i++;
		}
	    return tab;
	}
	
	/**
	 * projeterMatrice méthode qui prends toutes les images de la base en parametre et retourne un tableau de double
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
	 * ajouterNouvellePersonne méthode qui ajoute une personne et son image a la base
	 * @param p une personne 
	 * @param une image
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
	        dossier.mkdirs(); //On crée le dossier
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
	
	//Verif 
	public void getListNomPersonne() {
		for (Personne pers : this.p) {
			System.out.println(pers.getNom() + "_" + pers.getPrenom());
		}
	}
	//Verif
	public void getListNomImage() {
		for (Image img : this.images) {
			System.out.println(img.toString());
		}
	}

	
	/**
	 * traiterImages()
	 * Traitement de toutes les images pour les transformer en vecteurs puis pour y faire les opérations mathématiques
	 * 
	 */
	public void traiterImages() {
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
		this.eigenfaces = this.matriceTotal.extraireEigenfaces(9);
		//afficherMatrice(eigenfaces);
		//verifOrthogonalite(eigenfaces);
		double[][] mat = new double[n][9];
		int cpt1 = -1;
		for (Image img : this.images) {
			cpt1 += 1;
			ReconnaissanceFaciale rc = new ReconnaissanceFaciale(this, img);
			mat[cpt1] = rc.reconstruire(img.getVecteurImage().getVecteur(), 9);
			for (double d : mat[cpt1]) {
				System.out.print(" | "+ d);
			}
			System.out.println("");
		}
		
		Workbook workbook = new Workbook();

	    int index = workbook.getWorksheets().add();
	    Worksheet sheet = workbook.getWorksheets().get(index);
	    sheet.setName("Projection");

	    ReconnaissanceFaciale rcExcel = new ReconnaissanceFaciale(this, sheet);

	    rcExcel.miseAJourWorksheet(mat);    // on remplit la feuille Excel

	    rcExcel.afficherNuage(mat); // on crée le graphique (nuage de points)

	    try {
	        workbook.save("resultat_reconnaissance.xlsx");// on sauvegarde finale (sécurisée)
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * methode qui affiche les résultats des produits scalaires des eigenfaces entre elles
	 * @param eigenfaces matrice des eigenfaces
	 */
	public void verifOrthogonalite(Matrice eigenfaces) {
	    for (int i = 0; i < eigenfaces.getMatrice().length; i++) {
	    	double scal=0;
	        Vecteur v1 = eigenfaces.getMatrice()[i]; //on recupere le i-ème vecteur
	        for (int j = i + 1; j < eigenfaces.getMatrice().length; j++) { //on boucle pour que chaque produit scalaire ne soit calculé qu'une fois
	            Vecteur v2 = eigenfaces.getMatrice()[j]; //on recupere les j-ème vecteurs (où j>i)
	            scal = v1.produitScalaire(v2); //calcul du produit scalaire
	            System.out.println("v" + i + ".v" + j + " = " + scal); //affichage
	        }
	    }
	}
	
	//Verif
	public static void afficherVecteur(Vecteur v) {

        double[] tab = v.getVecteur();

        System.out.print("[ ");

        for (double valeur : tab) {
            System.out.print(valeur + " ");
        }

        System.out.println("]");
    }
	
	public static void afficherMatrice(Matrice m) {

        Vecteur[] lignes = m.getMatrice();

        for (Vecteur v : lignes) {
            afficherVecteur(v);
        }
    }

	/*
	//Verif
	public void ImagePersonne() {
		Personne pers;
		pers = this.p[0];
		
	}*/
	
}
