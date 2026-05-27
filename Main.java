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

        System.out.println("\nCENTRALISATION DES VECTEURS");

        for (Vecteur v : tableauVecteurs) {
            v.centraliser(visageMoyen);
            afficherVecteur(v);
        }

        System.out.println("\n MATRICE CENTRALISÉE ");
        afficherMatrice(matrice);

        System.out.println("\n MATRICE DE COVARIANCE DES IMAGES");

        try {
            Matrice covariance = matrice.MatriceCovariance();
            afficherMatrice(covariance);
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul de la covariance : " + e.getMessage());
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

        Vecteur[] lignes = m.getA();

        for (Vecteur v : lignes) {
            afficherVecteur(v);
        }
    }
}