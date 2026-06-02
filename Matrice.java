package projet;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

public class Matrice {

    private Vecteur[] M; //tableau de vecteurs (matrice)
    private int n; // nombre d'images
    private int m; // nombre de pixels

    /**
     * CONSTRUCTEUR
     * @param a le tableau de vecteurs
     */
    public Matrice(Vecteur[] a) {
        M = a;
        n = M.length;
        m = M[0].vecteur.length;
    }

    /**
     * CONSTRUCTEUR VIDE
     */
    public Matrice() {
    }

    /**
     * GETTER M
     * @return M tableau de vecteurs
     */
    public Vecteur[] getMatrice() {
        return M;
    }

    /**
     * GETTER n
     * @return n le nombre d'images
     */
    public int getN() {
        return n;
    }

    /**
     * GETTER m
     * @return m le nombre de pixels par image
     */
    public int getM() {
        return m;
    }
	
    private Matrice calculTransposee(Vecteur[] M) {
    	/**
		 * Fonction calculant la transposée de M
		 * @author Dorian
		 * @param M le tableau de vecteurs
		 * @return MT la transposée
		 */

    	//initialisation de la matrice transposée
        Vecteur[] MT = new Vecteur[m];
        for (int i = 0; i<m; i++) {
            MT[i] = new Vecteur(n); //initialisation de ses éléments
        }

        for (int i = 0; i<n; i++) {
            for (int j = 0; j < m; j++) {
                MT[j].vecteur[i] = M[i].vecteur[j]; //inversion de place de tous les éléments
            }
        }

        return new Matrice(MT);
    }
	
	public Vecteur calculVisageMoyen() {
		/**
		 * Fonction calculant le visage moyen
		 * @author Dorian
		 * @return J vecteur de l'image moyenne
		 */
		
		Matrice B = calculTransposee(M); //travail sur la transposée pour faciliter le calcul
		Vecteur J = new Vecteur(m);
		
		float somme_ligne;
		for (int i = 0; i<m; i++) { //boucle pour calculer la moyenne pour chaque pixel
			somme_ligne = 0;
	        for (int j = 0; j<n; j++) {
	        	somme_ligne = (float) (somme_ligne + B.M[i].vecteur[j]); //somme des premiers pixels de chaque image
	        }
	        J.vecteur[i] = somme_ligne/n; //on divise par le nombre d'images pour obtenir la moyenne
		}
		return J;
	}
	


	/**
	 * centrerDonnees soustrait le visage moyen à chaque vecteur de la base
	 * @author Célia
	 */
	public Vecteur[] centrerDonnees() {
		Vecteur moy = this.calculVisageMoyen(); //calcul du visage moyen v 
		Vecteur[] W = new Vecteur[n]; 
		
		for (int i=0; i<n; i++) { //pour chaque vect de la matrice totale
			W[i] = new Vecteur(M[i].getVecteur().clone()); //clonage du vecteur, sinon on centralise un vecteur null
			W[i].centraliser(moy); //on centralise le visage moyen
		}
		return W;
	}
	
	
	public double[][] matriceEnTableau2D (Vecteur[] w) {
		/**
		 * Fonction qui transforme notre matrice carrée en un tableau 2D pour pouvoir utiliser la fonction SVD de la bibliothèque
		 * @author Yassine
		 * @return le tableau 2D avec les valeurs de M
		 */
		double[][] tab2D = new double[n][n]; // initialisation du tableau
			
		for (int i=0; i < n; i++) {
			for (int j=0; j < n; j++){
				tab2D[i][j] = w[i].vecteur[j]; //conversion élément par élément
				}	
			}
			return tab2D;		
	}
	
	public Matrice tableau2DEnMatrice(double[][] tab) {
		/**
		 * Fonction qui transforme un tableau 2D en matrice carrée
		 * @param tab tableau 2D
		 * @author Dorian
		 * @return Matrice M
		 */
	    int lignes = tab.length; //récupération des dimensions
	    int colonnes = tab[0].length;
	    Vecteur[] W = new Vecteur[lignes]; //initialisation du tableau de vecteurs
	    for (int i = 0; i < lignes; i++) {
	        W[i] = new Vecteur(colonnes); //initialisation des vecteurs
	        for (int j = 0; j < colonnes; j++) {
	            W[i].vecteur[j] = tab[i][j]; //conversion élément par élément
	        }
	    }
	    return new Matrice(W);
	}
	
	public Matrice extraireEigenfaces(int k) {	
		/**
		 * Fonction qui effectue une SVD sur la matrice contenant les images centrées et puis donne les k premières eigenfaces correspondantes
		 * @param entier k
		 * @author Yassine
		 * @return Matrice k x (taille des vecteurs)
		 */		
	    Vecteur[] W = this.centrerDonnees(); //on part de la matrice centrée
	    double[][] p = matriceEnTableau2D(W); // qu'on transforme en tableau 2D pour utiliser la bibliothèque
	    
	    RealMatrix matrix = new Array2DRowRealMatrix(p); //on crée une matrice reconnue par la bibliothèque
	    SingularValueDecomposition svd = new SingularValueDecomposition(matrix); //on effectue la décomposition SVD  W = U × Σ × VT
	        
	    double[] val_sing = svd.getSingularValues(); //on récupère les valeurs singulières (elles sont triées par ordre décroissant)
	    for (int i=0; i<val_sing.length; i++) {
	        System.out.println(val_sing[i]); //on les affiche
	    }
	    
	    RealMatrix U = svd.getU(); //on récupère la matrice U contenant les vecteurs associés aux vecteurs propres.
	    
	    int l = U.getColumnDimension(); //nombre de colonnes de U
	    int c = U.getRowDimension();    //nombre de lignes de U
	    double[][] vecteursPropres = new double[c][l]; //initialisation du tableau des vecteurs propres
	    
	    for (int j=0; j< l; j++) {
	        vecteursPropres[j] = U.getColumn(j); //on stocke chaque vecteur propre colonne par colonne		
	    }
	    // On calcul la matrice transposé de W
	    RealMatrix VSigma = svd.getV().multiply(svd.getS()); //on calcule V * Sigma
	    RealMatrix Wt = VSigma.multiply(svd.getUT());        //on calcule V * Sigma * U^T
	    RealMatrix V_bis = Wt.multiply(U);                   //on calcule V * Sigma * U^T * U
	    
	    // On créer une matrice diagonale contenant les inverses des valeurs singulières 
		double[] inverseValSing = new double[val_sing.length]; //initialisation du tableau des inverses des valeurs singulières
	    for (int h = 0; h < val_sing.length; h++) {
	        if (val_sing[h] != 0) {
	            inverseValSing[h] = 1.0/val_sing[h]; //on calcule l'inverse si la valeur est non nulle
	        }
	        else {
	            inverseValSing[h] = 0.0; //on laisse 0 pour éviter la division par zéro 
	        }
	    }

	    RealMatrix matriceSigmaInverse = MatrixUtils.createRealDiagonalMatrix(inverseValSing); //on crée la matrice diagonale 
	    RealMatrix V = V_bis.multiply(matriceSigmaInverse); //on effectue le calcul et obtient la matrice des eigenfaces tels que Vh = (Wt*Uh)/σh 
	    
	    double[][] eigenfaces = new double[k][l]; //on initialise le tableau pour les k premières eigenfaces
	    for (int n=0; n<k; n++) {
	        eigenfaces[n] = V.getColumn(n); //on extrait les k premières colonnes
	    }
	    return tableau2DEnMatrice(eigenfaces); //on retourne les eigenfaces sous forme de Matrice
	}
}
