package projet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList; //AJout Clément
import java.util.List;

/**
 * Description : gère la base d'images de référence utilisée pour la reconnaissance faciale
 * @author CandyCelia
 * @version 2.0
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
	
	private Matrice engenfaces;
	@SuppressWarnings("unused")
	private Matrice matriceTotal;
	@SuppressWarnings("unused")
	private Vecteur visageMoyen;
	private List<Personne> p;
	
	
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
	}
	
	/**
	 * chargerBase ouvre le dossier au chein donnée, lit chaque images et les stock
	 * @param chemin une chaine de caractère
	 */
	public void chargerBase(String chemin) throws IOException {
		File dossier = new File(chemin); //creer un objet File avec le chemin
		File[] fichiers = dossier.listFiles(); //liste les fichiers du dossier avec listFiles()
		images = new Image[fichiers.length]; //initialiser images avec la bonne taille
		int i = 0;
		
		for (File fichier : fichiers) { //pour chaque fichier creer un obj image
			images[i] = new Image(fichier.getName(), chemin); //creer un nouvele obj img et stocker img dans le tab
			i++;
			taille++; //maj la taille
		}
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
	public double[] projeterImage(Image img) {
		Vecteur v = img.getVecteurImage(); //recup le vect de img
		double[] tab = new double[engenfaces.getN()]; //creer un  tab de taille nb de eigenfaces
		int i = 0;
		
		for (Vecteur e : engenfaces.getMatrice()) {//ppur chauqe eigenface dans engenfaces.getMatrice()
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
}
