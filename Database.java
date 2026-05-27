package projet;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Description : gère la base d'images de référence utilisée pour la reconnaissance faciale
 * @author CandyCelia
 * @version 2.0
 */
public class Database {
	private String cheminBaseReference;
	private int taille;
	private double[] valeursPropres; //MODIFIER DANS LE DIAGRAMME
	//POUR STOCKER LES IMAGES DE LA BASE
	private Image[] images; //MODIFIER DANS LE DIAGRAMME
	
	private Matrice engenfaces;
	private Matrice matriceTotal;
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
	 * centrerDonnees soustrait le visage moyen à chaque vecteur de la base
	 */
	public void centrerDonnees() {
		matriceTotal = getMatriceTotal();
		Vecteur v = matriceTotal.calculVisageMoyen(); //calcul du visage moyen v 
		int i = 0;
		
		for (Vecteur v1 : matriceTotal) { //pour chaque vect de matriceTotal 
			v1[i] = centraliser(v); //on centralise le visage moyen
			i++;
			
			
		}
	}
	
	/**
	 * projeterImage méthode qui prends une image en parametre et retourne un tableau d'entier
	 * @return un tableau d'entier
	 * @param img une image
	 */
	public int[] projeterImage(Image img) {
	    return null; // A FAIRE
	}
	
	/**
	 * projeterMatrice méthode qui projete la matrice
	 */
	public void projeterMatrice() {
		// A FAIRE
	}
	
	/**
	 * ajouterNouvellePersonne méthode qui ajoute une personne
	 * @param p une personne 
	 * @param une image
	 */
	public void ajouterNouvellePersonne(Personne p, Image img) {
		// A FAIRE
	}
	
}

