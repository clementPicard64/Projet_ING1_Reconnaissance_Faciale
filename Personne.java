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
    
	/*Constructeur de la classe personne*/
	public Personne(String nom, String prenom) {
		this.nom = nom;
		this.prenom = prenom;
		this.cheminPersonne = "/?/projet/database/" +nom+ "_" +prenom;
		this.listImage = new ArrayList<Image>();
	}

	/** 
	 * La fonction getPrenom() permet de renvoyer le prenom de la personne
	 * 
	 * @return prenom
	 * 			Renvoie le prenom de la personne 
	 * 
	*/
	public String getPrenom() {
		return prenom;
	}

	/** 
	 * La fonction getPrenom() permet de renvoyer le nom de la personne
	 * 
	 * @return nom
	 * 			Renvoie le nom de la personne 
	 * 
	*/
	public String getNom() {
		return nom;
	}
	
	/** 
	 * La fonction getPrenom() permet de renvoyer le chemin du dossier ou est rangé l'utilisateur de la personne
	 * 
	 * @return cheminPersonne
	 * 			Renvoie le chemin des fichiers ou est ranger l'utilisateur de la personne 
	 * 
	*/
	public String getCheminPersonne() {
		return cheminPersonne;
	}
	
	/** 
	 * La fonction getListImage() permet de renvoyer la liste d'images associer à une personne
	 * 
	 * @return listImage
	 * 			Liste d'image de la personne
	 * 
	*/
	public ArrayList<Image> getListImage() {
		return listImage;
	}
	
	/**
	 * @return res
	 * 			Est le resultat du toString avec le descriptif de la personne
	*/
	@Override
	public String toString() {
		String res = "La personne est ";
		res += this.getPrenom() +" "+ this.getNom();
		res += "\nLe chemin du dossier est " + this.getCheminPersonne();
		res += "\nVoici ces photos";
		for (int i = 0 ; i < listImage.size(); i++) {
			res += listImage.get(i).toString();
			
		}
		res += "\n";
		return res;
	}
	
	/**
	 * Cette fct permet d'ajouter une nouvelle image à la personne de notre choix
	 * 
	 * @param img 
	 * 			C'est File une image a ajouter à la personne 
	*/
	public void ajouterImage(File img) {
		try {
			//Création du nouveau nom du fichier
			String nouveauNom = this.getNom() +"_"+ this.getPrenom() +"_"+ (this.getListImage().size()+1);
			
			//Création du chemin pour l'image
			Path cheminImg = Paths.get(getCheminPersonne(), nouveauNom +".png");
			
			//On copie l'image dans le dossier
			Files.copy(img.toPath(), cheminImg, StandardCopyOption.REPLACE_EXISTING);
			
			Image img2 = new Image(nouveauNom, this.getCheminPersonne());
			this.listImage.add(img2);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
