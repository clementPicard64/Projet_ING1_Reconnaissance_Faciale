package projet;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Vecteur v1 = new Vecteur(new double[]{0, 2, 9,1,2});
        Vecteur v2 = new Vecteur(new double[]{4, 7, 6,4,0});
        Vecteur v3 = new Vecteur(new double[]{1,2,3,4,5});
        Vecteur v4 = new Vecteur(new double[]{2,4,6,8,0});

        Vecteur[] tableauVecteurs = {v1, v2,v3,v4};
        Matrice matrice = new Matrice(tableauVecteurs);

        System.out.println("MATRICE INITIALE");
        afficherMatrice(matrice);
        
        Vecteur visageMoyen = matrice.calculVisageMoyen();
        System.out.println("\nVISAGE MOYEN");
        afficherVecteur(visageMoyen);
        

        Vecteur[] v = matrice.centrerDonnees();
        Matrice matriceCentree = new Matrice(v);

        System.out.println("\nMATRICE CENTRÉE"); //celia
        afficherMatrice(matriceCentree);

/*        System.out.println("\n MATRICE DE COVARIANCE DES IMAGES");


        try {
            Matrice covariance = matrice.MatriceCovariance();
            afficherMatrice(covariance);
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul de la covariance : " + e.getMessage());
        }*/
        
        System.out.println("\nVALEURS PROPRES ET EIGENFACES");

        try {
            Matrice eigenfaces = matrice.extraireEigenfaces(4);
            afficherMatrice(eigenfaces);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'extraction des eigenfaces : " + e.getMessage());
        }
        
        
        Database db1 = new Database("/home/cytech/Projet/base_images");
		try {
			db1.chargerBase("/home/cytech/Projet/base_images");
		} catch (IOException e) {
			e.printStackTrace();
		}
		db1.getListPersonne();
		db1.getListNomImage();
		db1.traiterImages(); 
		
		try {
			Image img = new Image("/home/cytech/Projet/imagesAjoutComparaison/im2.png");
			img.redimensionner();
			img.convertirEnNiveauDeGris();
			img.vectoriser();
			ReconnaissanceFaciale rf = new ReconnaissanceFaciale(db1,img);
			System.out.println(rf.identifier(img));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

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
  
    
    
    
    
}
