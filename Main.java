package projet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String chemin = "VOTRECHEMIN"; // Mettre votre chemin où sont les images ici

        Database db = new Database(chemin + "/imagesReference");
        try {
            db.chargerBase(chemin + "/imagesReference");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        db.traiterImages();
        System.out.println("Nombre d'images de base de référence chargées : " + db.getTaille());

        Database dbSeuil = new Database(chemin + "/imagesTestSeuil");
        try {
            dbSeuil.chargerBase(chemin + "/imagesTestSeuil");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        List<Image> imagesTestSeuil = new ArrayList<>();
        for (Personne p : dbSeuil.getListPersonne()) {
            for (Image img : p.getListImage()) {
                img.redimensionner();
                img.convertirEnNiveauDeGris();
                img.vectoriser();
                imagesTestSeuil.add(img);
            }
        }
        System.out.println("Nombre d'images test chargées : " + imagesTestSeuil.size());

        ReconnaissanceFaciale rfSeuil = new ReconnaissanceFaciale(0.0, db);
        double seuil = rfSeuil.evaluerTauxIdentification(imagesTestSeuil);
        System.out.println("Seuil calculé : " + seuil);

        Database dbValidation = new Database(chemin + "/imagesValidation");
        try {
            dbValidation.chargerBase(chemin + "/imagesValidation");
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

        ReconnaissanceFaciale rf = new ReconnaissanceFaciale(seuil, db);

        int correct = 0;
        int mauvaisePersonne = 0;
        int inconnu = 0;
        int total = imagesValidation.size();

        for (Image img : imagesValidation) {
            String resultat = rf.identifier(img);
            File fTest = new File(img.getCheminImage());
            String nomTest = fTest.getName().replace(".png", "");

            if (resultat.startsWith("Inconnu")) {
                inconnu++;
                System.out.println(nomTest + " -> " + resultat);
            } else {
                File fResultat = new File(resultat);
                String nomResultat = fResultat.getName().replace(".png", "");
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
