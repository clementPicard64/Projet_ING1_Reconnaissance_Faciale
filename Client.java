package projet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class Client extends Application {

    @Override
    public void start(Stage s) {
        // Conteneur tout en haut pour intégrer le logo au-dessus du reste
        VBox structureGlobale = new VBox();
        structureGlobale.setStyle("-fx-background-color: linear-gradient(to bottom right, #ff7c90, #ffe1a4);");
        
        // --- ZONE DU LOGO (EN HAUT À DROITE) ---
        HBox zoneTop = new HBox();
        zoneTop.setPadding(new Insets(20, 40, 0, 40));
        zoneTop.setAlignment(Pos.CENTER_RIGHT); // Aligne le contenu à droite
        
        // Tentative de chargement du logo CY Tech
        try {
            // Remplace "logo_cytech.png" par le bon chemin si nécessaire
            Image img = new Image(getClass().getResourceAsStream("/cytech.png"));
            ImageView logo = new ImageView(img);
            logo.setFitWidth(120);  // Largeur du logo
            logo.setPreserveRatio(true);
            zoneTop.getChildren().add(logo);
        } catch (Exception e) {
            // Si l'image n'est pas trouvée, on met un label propre pour ne pas faire crasher l'appli
            Label logoSecours = new Label("CY Tech");
            logoSecours.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px; -fx-font-weight: bold; -fx-opacity: 0.8;");
            zoneTop.getChildren().add(logoSecours);
        }

        // --- ZONE DES CARTES PRINCIPALES ---
        HBox conteneurPrincipal = new HBox();
        conteneurPrincipal.setAlignment(Pos.CENTER);
        conteneurPrincipal.setSpacing(100);
        conteneurPrincipal.setPadding(new Insets(40, 100, 80, 100)); // Moins de padding haut car il y a le logo
        
        VBox gauche = new VBox();
        VBox droite = new VBox();
        
        String styleCard = "-fx-background-color: #ffe1a4; -fx-background-radius: 40px; -fx-effect: dropshadow(three-pass-box, rgba(224, 91, 118, 0.6), 0, 0, 12, 12)";
        gauche.setStyle(styleCard);
        droite.setStyle(styleCard);
        
        conteneurPrincipal.getChildren().addAll(gauche, droite);
        
        gauche.setAlignment(Pos.TOP_CENTER);
        gauche.setPadding(new Insets(40, 30, 40, 30));
        
        droite.setAlignment(Pos.TOP_CENTER);
        droite.setPadding(new Insets(40, 30, 40, 30));

        // --- BLOC GAUCHE ---
        VBox depotG = new VBox();
        // CORRECTION 1 : Fond blanc avec une légère opacité (0.3) pour correspondre à la maquette
        depotG.setStyle("-fx-background-color: rgba(255, 255, 255, 0.35); -fx-border-color: #ffffff; -fx-border-style: dashed; -fx-border-width: 3px; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        depotG.setAlignment(Pos.CENTER);
        Label plusG = new Label("+");
        plusG.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 40px; -fx-font-weight: bold;");
        depotG.getChildren().add(plusG);

        // CORRECTION 3 : Style des blocs de texte (NOM/PRENOM) plus stylisés, arrondis, texte gris foncé
        String styleTextField = "-fx-background-color: #ffffff;" +
                                "-fx-background-radius: 20px;" +
                                "-fx-padding: 12px 20px;" +
                                "-fx-text-fill: #555555;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);";

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

        Button a = new Button("AJOUTER");
        a.setMaxHeight(50);
        a.setStyle("-fx-background-color: #ff7c90; -fx-text-fill: white; -fx-background-radius: 15px; -fx-font-weight: bold;");

        gauche.getChildren().addAll(depotG, espaceInter1, nom, espaceInter2, prenom, ressortG, a);

        // --- BLOC DROIT ---
        VBox depotD = new VBox();
        // CORRECTION 1 : Même fond blanc translucide pour le dépôt droit
        depotD.setStyle("-fx-background-color: rgba(255, 255, 255, 0.35); -fx-border-color: #ffffff; -fx-border-style: dashed; -fx-border-width: 3px; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        depotD.setAlignment(Pos.CENTER);
        Label plusD = new Label("+");
        plusD.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 40px; -fx-font-weight: bold;");
        depotD.getChildren().add(plusD);

        VBox res = new VBox();
        // CORRECTION 1 bis : Fond du bloc résultat blanc opaque ou translucide selon maquette (ici mis en blanc légèrement transparent pour faire ressortir)
        res.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-border-color: #ff7c90; -fx-border-width: 3px; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        res.setAlignment(Pos.CENTER);
        Label labelRes = new Label("RESULTAT");
        labelRes.setStyle("-fx-text-fill: #ff7c90; -fx-font-size: 30px; -fx-font-weight: bold;");
        res.getChildren().add(labelRes);

        Region espaceInter3 = new Region();
        espaceInter3.setPrefHeight(20);

        Region ressortD = new Region();
        VBox.setVgrow(ressortD, Priority.ALWAYS);

        Button i = new Button("IDENTIFIER");
        i.setMaxHeight(50);
        i.setStyle("-fx-background-color: #ff7c90; -fx-text-fill: white; -fx-background-radius: 15px; -fx-font-weight: bold;");

        droite.getChildren().addAll(depotD, espaceInter3, res, ressortD, i);

        // Ajout des deux sous-parties dans la structure globale
        structureGlobale.getChildren().addAll(zoneTop, conteneurPrincipal);
        VBox.setVgrow(conteneurPrincipal, Priority.ALWAYS);

        // --- SCENE ET BINDS DE PROPORTIONS ---
        Scene sc = new Scene(structureGlobale, 950, 680); // Légèrement agrandi pour le confort du logo

        gauche.prefWidthProperty().bind(sc.widthProperty().multiply(0.40));
        droite.prefWidthProperty().bind(sc.widthProperty().multiply(0.40));
        gauche.prefHeightProperty().bind(sc.heightProperty().multiply(0.75));
        droite.prefHeightProperty().bind(sc.heightProperty().multiply(0.75));
        
        depotG.prefWidthProperty().bind(gauche.widthProperty().multiply(0.80));
        depotG.prefHeightProperty().bind(gauche.heightProperty().multiply(0.35));
        depotD.prefWidthProperty().bind(droite.widthProperty().multiply(0.80));
        depotD.prefHeightProperty().bind(droite.heightProperty().multiply(0.35));
        
        // CORRECTION 2 : Augmentation de la hauteur du bloc résultat (0.30 au lieu de 0.24) pour être plus long !
        res.prefWidthProperty().bind(droite.widthProperty().multiply(0.80));
        res.prefHeightProperty().bind(droite.heightProperty().multiply(0.30));
        
        nom.prefWidthProperty().bind(gauche.widthProperty().multiply(0.80)); 
        prenom.prefWidthProperty().bind(gauche.widthProperty().multiply(0.80));
        nom.prefHeightProperty().bind(gauche.heightProperty().multiply(0.08));
        prenom.prefHeightProperty().bind(gauche.heightProperty().multiply(0.08));
        
        a.prefWidthProperty().bind(gauche.widthProperty().multiply(0.45));
        a.prefHeightProperty().bind(gauche.heightProperty().multiply(0.08));
        i.prefWidthProperty().bind(droite.widthProperty().multiply(0.45));
        i.prefHeightProperty().bind(droite.heightProperty().multiply(0.08));
        
        s.setScene(sc);
        s.setTitle("Application de reconnaissance Faciale");
        s.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
