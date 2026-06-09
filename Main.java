package projet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        
        Database db = new Database("VOTRECHEMIN/imagesReference"); // chargement base de référence
        try {
            db.chargerBase("VOTRECHEMIN/imagesReference");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        db.traiterImages(); // vectorise + calcule eigenfaces
        System.out.println("Nombre d'images de base de référence chargées : " + db.getTaille());


        Database dbSeuil = new Database("VOTRECHEMIN/imagesTestSeuil"); // chargement de la base des images de test
        try {
            dbSeuil.chargerBase("VOTRECHEMIN/imagesTestSeuil");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        List<Image> imagesTestSeuil = new ArrayList<>();
        for (Personne p : dbSeuil.getListPersonne()) {  // on récupère toutes les images de test dans une liste
            for (Image img : p.getListImage()) {
                img.redimensionner();
                img.convertirEnNiveauDeGris();
                img.vectoriser();
                imagesTestSeuil.add(img);
            }
        }
        System.out.println("Nombre d'images test chargées : " + imagesTestSeuil.size());


        ReconnaissanceFaciale rfSeuil = new ReconnaissanceFaciale(0.0, db); //calcul du seuil avec la base de seuil
        double seuil = rfSeuil.evaluerTauxIdentification(imagesTestSeuil);
        System.out.println("Seuil calculé : " + seuil);

        
        Database dbValidation = new Database("VOTRECHEMIN/imagesValidation"); // chargement images de validation
        try {
            dbValidation.chargerBase("VOTRECHEMIN/imagesValidation");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        List<Image> imagesValidation = new ArrayList<>();
        for (Personne p : dbValidation.getListPersonne()) {
            for (Image img : p.getListImage()) {
                img.redimensionner();
                img.convertirEnNiveauDeGris();
                img.vectoriser();
                imagesValidation.add(img);
            }
        }
        System.out.println("Nombre d'images de validation chargées : " + imagesValidation.size());

        
        
        //test de la reconnaissance faciale
        ReconnaissanceFaciale rf = new ReconnaissanceFaciale(seuil, db);

        int correct = 0;
        int mauvaisePersonne = 0;
        int inconnu = 0;
        int total = imagesValidation.size();

        for (Image img : imagesValidation) {
            String resultat = rf.identifier(img);
            File fTest = new File(img.getCheminImage()); // on extrait nom_prenom_n de l'image testée pour comparer les noms reconnus
            String nomTest = fTest.getName().replace(".png", "");

            if (resultat.startsWith("Inconnu")) {
                inconnu++;
                System.out.println(nomTest + " -> " + resultat);
            } else {
                File fResultat = new File(resultat);
                String nomResultat = fResultat.getName().replace(".png", ""); // "faure_paul_5"
                String dossierTest = fTest.getParentFile().getName();
                String dossierResultat = fResultat.getParentFile().getName();

                if (dossierTest.equals(dossierResultat)) {
                    correct++;
                    System.out.println("✓ " + nomTest + " -> " + nomResultat);
                } else {
                    mauvaisePersonne++;
                    System.out.println("✗ " + nomTest + " -> " + nomResultat + " (mauvaise personne)");
                }
            }
        }
        System.out.println("Total images testées : " + total);
        System.out.println("Personnes correctement reconnues : " + correct);
        System.out.println("Personnes mal reconnues : " + mauvaisePersonne);
        System.out.println("Personnes inconnues : " + inconnu);
        System.out.println("Taux de reconnaissance : " + String.format("%.2f", correct * 100.0 / total) + "%");
        rf.evaluerSeuilT2(imagesTestSeuil);
    }
}
