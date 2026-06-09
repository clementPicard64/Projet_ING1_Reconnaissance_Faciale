package projet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description : IHM
 * @author CandyCelia
 * @version 2.0
 */

public class Client extends Application {

    private Database database;//bdd d'images
    private File fichierAjouter;//fichier image pour ajouter
    @SuppressWarnings("unused")
	private File fichierIdentifier;//fichier image pour id
    private projet.Image imageTestTemp;

    //affiche le nom des fichiers choisis
    private Label nomFichierGauche;
    private Label nomFichierDroit;

    //label du res
    private Label labelRes;

    @Override
    public void start(Stage s) {

    	//initialisation de la bdd
        String cheminBase = "database/imagesReference";
        database = new Database(cheminBase);
        try {
            File dossier = new File(cheminBase);
            if (dossier.exists()) {
            	//charge les images existantes
                database.chargerBase(cheminBase);
                //calcule les eigenfaces
                database.traiterImages();
            } else {
                System.out.println("dossier base introuvable : " + cheminBase);
            }
        } catch (IOException e) {
            System.out.println("erreur chargement base : " + e.getMessage());
        }

        //visuel
        VBox structureGlobale = new VBox();
        //dégradé du fond
        structureGlobale.setStyle("-fx-background-color: linear-gradient(to bottom right, #ff7c90, #ffe1a4);");

        //en haut a droite
        HBox zoneTop = new HBox();
        zoneTop.setPadding(new Insets(20, 40, 0, 40));
        zoneTop.setAlignment(Pos.CENTER_RIGHT);

        //chargement logo cytech dans /src
        try {
            InputStream is = getClass().getResourceAsStream("/cytech.png");
            if (is != null) {
                Image img = new Image(is);
                ImageView logo = new ImageView(img);
                logo.setFitWidth(120);
                logo.setPreserveRatio(true);
                zoneTop.getChildren().add(logo);
            } else {
                throw new Exception("logo non trouvée");
            }
        } catch (Exception e) {
            //si image absente , logo de secours
            Label logoSecours = new Label("CY Tech");
            logoSecours.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px; " + "-fx-font-weight: bold; -fx-opacity: 0.8;");
            zoneTop.getChildren().add(logoSecours);
        }

        //conteneur des 2 cartes
        HBox conteneurPrincipal = new HBox();
        conteneurPrincipal.setAlignment(Pos.CENTER);
        conteneurPrincipal.setSpacing(100);
        conteneurPrincipal.setPadding(new Insets(40, 100, 80, 100));
        
        //meme style pr les 2 cartes
        String styleCard ="-fx-background-color: #ffe1a4;" +"-fx-background-radius: 40px;" +"-fx-effect: dropshadow(three-pass-box, rgba(224, 91, 118, 0.6), 0, 0, 12, 12)";

        VBox gauche = new VBox(); //carte g
        VBox droite = new VBox(); //carte d
        gauche.setStyle(styleCard);
        droite.setStyle(styleCard);
        gauche.setAlignment(Pos.TOP_CENTER);
        gauche.setPadding(new Insets(40, 30, 40, 30));
        droite.setAlignment(Pos.TOP_CENTER);
        droite.setPadding(new Insets(40, 30, 40, 30));

        conteneurPrincipal.getChildren().addAll(gauche, droite);
        
        //carte g
        //zone depot
        VBox depotG = new VBox();
        depotG.setStyle("-fx-background-color: rgba(255, 255, 255, 0.35);" +"-fx-border-color: #ffffff;" +"-fx-border-style: dashed;" +"-fx-border-width: 3px;" + "-fx-border-radius: 20px;" + "-fx-background-radius: 20px;" +"-fx-cursor: hand;" );
        depotG.setAlignment(Pos.CENTER);
        
        //petit + dans la zone
        Label plusG = new Label("+");
        plusG.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 40px; -fx-font-weight: bold;");

        //affiche le nom du fichier choisi
        nomFichierGauche = new Label("Choisir une photo");
        nomFichierGauche.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 11px;");

        depotG.getChildren().addAll(plusG, nomFichierGauche);

        //clic sur la zone de dépôt gauche = ouvre l'explorateur de fichiers
        depotG.setOnMouseClicked(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une photo à ajouter");
            fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
            );
            File choix = fc.showOpenDialog(s);
            if (choix != null) {
                fichierAjouter = choix;
                nomFichierGauche.setText(choix.getName()); //affiche le nom du fichier
            }
        });

        //nom et prenom
        String styleTextField ="-fx-background-color: #ffffff;" + "-fx-background-radius: 20px;" +"-fx-padding: 12px 20px;" +  "-fx-text-fill: #555555;" + "-fx-font-weight: bold;" + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);";

        TextField nom = new TextField();
        nom.setPromptText("NOM");
        nom.setStyle(styleTextField);

        TextField prenom = new TextField();
        prenom.setPromptText("PRENOM");
        prenom.setStyle(styleTextField);

        Region espaceInter1 = new Region();
        espaceInter1.setPrefHeight(20);
        Region espaceInter2 = new Region();
        espaceInter2.setPrefHeight(20);
        Region ressortG = new Region();
        VBox.setVgrow(ressortG, Priority.ALWAYS);

        //bouton ajouter
        Button btnAjouter = new Button("AJOUTER");
        btnAjouter.setMaxHeight(50);
        btnAjouter.setStyle( "-fx-background-color: #ff7c90; -fx-text-fill: white; " + "-fx-background-radius: 15px; -fx-font-weight: bold; -fx-cursor: hand;");

        //action si le bouton est touché
        btnAjouter.setOnAction(e -> {
            String nomVal    = nom.getText().trim();
            String prenomVal = prenom.getText().trim();

            //si nom et prenom sont vides
            if (nomVal.isEmpty() || prenomVal.isEmpty()) {
                labelRes.setText("remplis NOM et PRENOM !");
                labelRes.setStyle("-fx-text-fill: #cc0000; -fx-font-size: 18px; -fx-font-weight: bold;");
                return;
            }
            //si il n'y a pas de photos deposée
            if (fichierAjouter == null) {
                labelRes.setText("choisis une photo !");
                labelRes.setStyle("-fx-text-fill: #ff7c90; -fx-font-size: 18px; -fx-font-weight: bold;");
                return;
            }

            //ajoute la personne et son image dans la base
            database.ajouterNouvellePersonne(nomVal, prenomVal, fichierAjouter);

            //recalcul des eigenfaces avec la nouvelle image
            try {
                database.traiterImages();
            } catch (Exception ex) {
                labelRes.setText("erreur traitement images");
                labelRes.setStyle("-fx-text-fill: #ff7c90; -fx-font-size: 18px; -fx-font-weight: bold;");
                return;
            }

            //pr verif que la personne a bien été ajoutée
            labelRes.setText(prenomVal + " " + nomVal + "\najoutée !");
            labelRes.setStyle("-fx-text-fill: #ff7c90; -fx-font-size: 18px; -fx-font-weight: bold;");

            //reset
            nom.clear();
            prenom.clear();
            fichierAjouter = null;
            nomFichierGauche.setText("Choisir une photo");
        });

        gauche.getChildren().addAll(depotG, espaceInter1, nom, espaceInter2, prenom, ressortG, btnAjouter);

        //carte droite
        //zone depot
        VBox depotD = new VBox();
        depotD.setStyle("-fx-background-color: rgba(255, 255, 255, 0.35);" +"-fx-border-color: #ffffff;" +"-fx-border-style: dashed;" +"-fx-border-width: 3px;" +"-fx-border-radius: 20px;" +"-fx-background-radius: 20px;" + "-fx-cursor: hand;");
        depotD.setAlignment(Pos.CENTER);

        Label plusD = new Label("+");
        plusD.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 40px; -fx-font-weight: bold;");

        nomFichierDroit = new Label("Choisir une photo");
        nomFichierDroit.setStyle( "-fx-text-fill: #ffffff; -fx-font-size: 11px;");
        depotD.getChildren().addAll(plusD, nomFichierDroit);

        //clic sur la zone de dépôt droite = ouvre l'explorateur de fichier
        depotD.setOnMouseClicked(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une photo à identifier");
            fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
            );
            File choix = fc.showOpenDialog(s);
            if (choix != null) {
                fichierIdentifier = choix;
                nomFichierDroit.setText(choix.getName());
                //prepa de l'image pour le traitement
                try {
                    imageTestTemp = new projet.Image(choix.getAbsolutePath());
                    imageTestTemp.redimensionner();
                    imageTestTemp.convertirEnNiveauDeGris();
                    imageTestTemp.vectoriser();
                } catch (IOException ex) {
                    labelRes.setText("impossible de lire l'image");
                    labelRes.setStyle("-fx-text-fill: #ff7c90; -fx-font-size: 18px; -fx-font-weight: bold;");
                }
            }
        });

        //bloc resultat
        VBox res = new VBox();
        res.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);" +"-fx-border-color: #ff7c90;" +"-fx-border-width: 3px;" +"-fx-border-radius: 20px;" +"-fx-background-radius: 20px;");
        res.setAlignment(Pos.CENTER);
        res.setSpacing(5);

        labelRes = new Label("RESULTAT");
        labelRes.setStyle("-fx-text-fill: #ff7c90; -fx-font-size: 30px; -fx-font-weight: bold;");
        labelRes.setWrapText(true);
        labelRes.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        res.getChildren().add(labelRes);

        Region espaceInter3 = new Region();
        espaceInter3.setPrefHeight(20);
        Region ressortD = new Region();
        VBox.setVgrow(ressortD, Priority.ALWAYS);

        //bouton id
        Button btnIdentifier = new Button("IDENTIFIER");
        btnIdentifier.setMaxHeight(50);
        btnIdentifier.setStyle("-fx-background-color: #ff7c90; -fx-text-fill: white; " +"-fx-background-radius: 15px; -fx-font-weight: bold; -fx-cursor: hand;");

        //si le bouton id est cliqué
        btnIdentifier.setOnAction(e -> {
            if (imageTestTemp == null) { //si ya pas de photo dans le depot
                labelRes.setText("choisis une photo !");
                labelRes.setStyle("-fx-text-fill: #ff7c90; -fx-font-size: 18px; -fx-font-weight: bold;");
                return;
            }
            if (database.getTaille() == 0) { //si la bdd est vide
                labelRes.setText("base vide !\nAjoute des personnes d'abord");
                labelRes.setStyle("-fx-text-fill: #ff7c90; -fx-font-size: 18px; -fx-font-weight: bold;");
                return;
            }

            //identification
            ReconnaissanceFaciale rf;
            try {
                rf = new ReconnaissanceFaciale(null, database);
            } catch (IOException ex) {
                labelRes.setText("erreur initialisation");
                labelRes.setStyle("-fx-text-fill: #ff7c90; -fx-font-size: 18px; -fx-font-weight: bold;");
                return;
            }
            String cheminResultat = rf.identifier(imageTestTemp);

            if (cheminResultat.equals("Inconnu")) { //si on trouve rien de similaire
                labelRes.setText("Inconnu");
                labelRes.setStyle( "-fx-text-fill: #ff7c90; -fx-font-size: 26px; -fx-font-weight: bold;");
            } else {
                //extrait le nom du dossier parent format Nom_Prenom
                java.io.File f = new java.io.File(cheminResultat);
                String dossier = f.getParentFile().getName(); 
                String[] parties = dossier.split("_");
                String nomTrouve = parties.length > 0 ? parties[0] : dossier; //isole le nom
                String prenomTrouve = parties.length > 1 ? parties[1] : ""; //isole le prenom

                labelRes.setText(prenomTrouve + "\n" + nomTrouve);
                labelRes.setStyle("-fx-text-fill: #ff7c90; -fx-font-size: 26px; -fx-font-weight: bold;");
            }
        });

        droite.getChildren().addAll(depotD, espaceInter3, res, ressortD, btnIdentifier);

        //assemblage
        structureGlobale.getChildren().addAll(zoneTop, conteneurPrincipal);
        VBox.setVgrow(conteneurPrincipal, Priority.ALWAYS);

        Scene sc = new Scene(structureGlobale, 950, 680);

        //bind 
        gauche.prefWidthProperty().bind(sc.widthProperty().multiply(0.40));
        droite.prefWidthProperty().bind(sc.widthProperty().multiply(0.40));
        gauche.prefHeightProperty().bind(sc.heightProperty().multiply(0.75));
        droite.prefHeightProperty().bind(sc.heightProperty().multiply(0.75));

        depotG.prefWidthProperty().bind(gauche.widthProperty().multiply(0.80));
        depotG.prefHeightProperty().bind(gauche.heightProperty().multiply(0.35));
        depotD.prefWidthProperty().bind(droite.widthProperty().multiply(0.80));
        depotD.prefHeightProperty().bind(droite.heightProperty().multiply(0.35));

        res.prefWidthProperty().bind(droite.widthProperty().multiply(0.80));
        res.prefHeightProperty().bind(droite.heightProperty().multiply(0.30));

        nom.prefWidthProperty().bind(gauche.widthProperty().multiply(0.80));
        prenom.prefWidthProperty().bind(gauche.widthProperty().multiply(0.80));
        nom.prefHeightProperty().bind(gauche.heightProperty().multiply(0.08));
        prenom.prefHeightProperty().bind(gauche.heightProperty().multiply(0.08));

        btnAjouter.prefWidthProperty().bind(gauche.widthProperty().multiply(0.45));
        btnAjouter.prefHeightProperty().bind(gauche.heightProperty().multiply(0.08));
        btnIdentifier.prefWidthProperty().bind(droite.widthProperty().multiply(0.45));
        btnIdentifier.prefHeightProperty().bind(droite.heightProperty().multiply(0.08));

        s.setScene(sc);
        s.setTitle("Application de reconnaissance Faciale");
        s.show();
    }

    /**
     * main sans lui rien ne compile
     * @param args un tableau de chaine de caractère
     * @return void
     */
    public static void main(String[] args) {
        launch(args);
    }
}
