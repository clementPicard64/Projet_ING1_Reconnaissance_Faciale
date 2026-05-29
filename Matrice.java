
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;


public class Matrice {

    private Vecteur[] M;
    private int n; // nombre d'images
    private int m; // nombre de pixels

    public Matrice(Vecteur[] a) {
        M = a;
        n = M.length;
        m = M[0].vecteur.length;
    }

    public Matrice() {
    }

    public Vecteur[] getMatrice() {
        return M;
    }

    public void setA(Vecteur[] a) {
        M = a;
        n = M.length;
        m = M[0].vecteur.length;
    }

    public int getN() {
        return n;
    }

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
            MT[i] = new Vecteur(n);
        }

        for (int i = 0; i<n; i++) {
            for (int j = 0; j < m; j++) {
                MT[j].vecteur[i] = M[i].vecteur[j];
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
		
		float moy_ligne;
		for (int i = 0; i<m; i++) {
			moy_ligne = 0;
	        for (int j = 0; j<n; j++) {
	        	moy_ligne = (float) (moy_ligne + B.M[i].vecteur[j]); //somme des premiers pixels de chaque image
	        }
	        J.vecteur[i] = moy_ligne/n;
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
	
	public Matrice MatriceCovariance() {
		/**
		 * Fonction calculant M*M^T la matrice de covariance des images
		 * @author Dorian
		 * @return la matrice de covariance des images
		 */
			
		//calcul de la matrice terme par terme
		Vecteur[] W= this.centrerDonnees();
		Vecteur[] C = new Vecteur[n];
		
		double somme;
		for (int i=0; i<n; i++) {
			C[i] = new Vecteur(n); //initialisation des vecteurs de taille n, sinon éléments null
	        for (int j=0; j<n; j++) {
	        	somme = 0;
	        	for (int k=0; k<m; k++) {
	        		somme += W[i].vecteur[k]*W[j].vecteur[k]; //formule pour calculer l'élément de la i-ème ligne et j-ième colonne
	        	}
        		C[i].vecteur[j] = somme;
	        }
		}
		return new Matrice(C);
	}
	
	public double[][] tableau2D () {
		/**
		 * Fonction qui transforme notre matrice en un tableau 2D pour pouvoir utiliser la fonction SVD de la bibliothèque
		 * @author Yassine
		 * @return le tableau 2D avec les valeurs de M
		 */
		double[][] tab2D = new double[n][m]; // transformer en une liste de flotant
			
		for (int i=0; i < n; i++) {
			for (int j=0; j < m; j++){
				tab2D[i][j] = this.M[i].vecteur[j];
				}	
			}
			return tab2D;		
	}

	public double[][] extraireEigenfaces(int k) {	
		// SVD
		double[][] p = tableau2D();
		
		RealMatrix matrix = new Array2DRowRealMatrix(p);
		SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
			
		double[] val_sing = svd.getSingularValues();	
		double[] val_propre = new double[val_sing.length];
			
		for (int i = 0; i< val_sing.length; i++) {
			val_propre[i] = Math.sqrt(val_sing[i]);
		}
		
		RealMatrix U = svd.getU();
		
		int l = U.getColumnDimension();
		int c = U.getRowDimension();
		
		double[][] vecteursPropres = new double[l][c];
		
		for (int j=0; j< l; j++) {
			vecteursPropres[j] = U.getColumn(j);				
		}
		
		RealMatrix VSigma = svd.getV().multiply(svd.getS());
		RealMatrix Wt = VSigma.multiply(svd.getUT());
		RealMatrix V_bis = Wt.multiply(U);
		
		double [] inverseSigma = new double[val_sing.length];
		for (int h = 0; h< val_sing.length; h++ ) {
			if (val_sing[h] != 0) {
				inverseSigma[h] = 1.0/val_sing[h];
			}
			else {
				inverseSigma[h] = 0.0;
			}
		}

		RealMatrix matriceSigmaInverse = MatrixUtils.createRealDiagonalMatrix(inverseSigma);
		RealMatrix V = V_bis.multiply(matriceSigmaInverse);
		
		double[][] eingenfaces  = new double[k][l];
		for (int n=0; n<k; n++) {
			eingenfaces[n] = V.getColumn(n);
		}
		return eingenfaces;
	}
}
