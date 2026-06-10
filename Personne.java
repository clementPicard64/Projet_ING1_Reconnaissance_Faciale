package projet;

import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;
import java.io.IOException;



public class Personne {
	
	/*Variable de la classe personne*/
	private String nom; 
    private String prenom;
    private String cheminPersonne;
    private ArrayList<Image> listImage;
    
	/** 
	 * Constructeur de la classe personne. Initialisation de toutes les variable de la classe
	 *
	 * @param nom est un String qui correspond au nom de la personne
	 *
	 * @param prenom est un String qui correspond au prenom de la personne
	*/
	public Personne(String nom, String prenom) {
		this.nom = nom;
		this.prenom = prenom;
		this.cheminPersonne = "imagesReference/" + nom + "_" + prenom;
		this.listImage = new ArrayList<Image>();
	}

	/** 
	 * La fonction getPrenom() permet de renvoyer le prenom de la personne
	 * 
	 * @return prenom est un String qui correspond au prenom de la personne 
	 * 
	*/
	public String getPrenom() {
		return prenom;
	}

	/** 
	 * La fonction getPrenom() permet de renvoyer le nom de la personne
	 * 
	 * @return nom est un String qui correspond au nom de la personne
	 * 
	*/
	public String getNom() {
		return nom;
	}
	
	/** 
	 * La fonction getPrenom() permet de renvoyer le chemin du dossier ou est rangé la personne
	 * 
	 * @return cheminPersonne est un String qui represente le chemin des fichiers ou est ranger l'utilisateur de la personne 
	 * 
	*/
	public String getCheminPersonne() {
		return cheminPersonne;
	}
	
	/** 
	 * La fonction getListImage() permet de renvoyer la liste d'images associer à une personne
	 * 
	 * @return listImage une Liste d'Image de la personne (objet Image)
	 * 
	*/
	public ArrayList<Image> getListImage() {
		return listImage;
	}
	
	/**
	 * La fonction va permettre d'afficher les informations d'une personne
	 *
	 * @return res est le resultat du toString avec le descriptif de la personne
	*/
	@Override
	public String toString() {
		
		//Initialisation du résultat
		String res = "La personne est ";
		
		//Ajout du nom et du prenom au resultat
		res += this.getPrenom() +" "+ this.getNom();
		
		//Ajout du chemin
		res += "\nLe chemin du dossier est " + this.getCheminPersonne();
		
		res += "\nVoici ces photos";
		//Ajout de chaque photo grâce à une boucle for et en allant chercher dans la liste d'image de la personne
		for (int i = 0 ; i < listImage.size(); i++) {
			res += listImage.get(i).toString();
			
		}
		res += "\n";
		return res;
	}
	
	/**
	 * Cette fct permet d'ajouter une nouvelle image à la personne de notre choix
	 * 
	 * @param img est un File, une image a ajouter à la personne 
	*/
	public void ajouterImage(File img) {
		try {
			//On s'assure que le dossier de la personne existe physiquement
	        File dossierCible = new File(this.getCheminPersonne());
	        if (!dossierCible.exists()) {
	            dossierCible.mkdirs(); // Crée le dossier "imagesReference/Dupont_Jean" s'il n'existe pas
	        }

	        //Création du nouveau nom du fichier
	        String nouveauNom = this.getNom() + "_" + this.getPrenom() + "_" + (this.getListImage().size() + 1) + ".png";
	        
	        //Création du chemin absolu/relatif complet pour la copie
	        File fichierDestination = new File(dossierCible, nouveauNom);
			
			//On copie l'image dans le dossier
			Files.copy(img.toPath(), fichierDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			Image img2 = new Image(nouveauNom, dossierCible.getAbsolutePath());
			this.listImage.add(img2);
			
		}catch(IOException e) {
			System.err.println("Impossible de copier ou lire l'image.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Cette fct permet de charger une nouvelle image en creant une nouvelle image 
	 * 
	 * @param img est un File 
	*/
	public void chargerImage(File img) {
		try {
			Image image = new Image(img.getAbsolutePath()); //creer un objet image 
			this.listImage.add(image);
		}catch (IOException e) {
			System.err.println("Impossible de lire l'image");
			e.printStackTrace();
		}
	}

}
