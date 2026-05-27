public class Matrice {

    private Vecteur[] A;
    private int n; // nombre d'images
    private int m; // nombre de pixels

    public Matrice(Vecteur[] a) {
        A = a;
        n = A.length;
        m = A[0].vecteur.length;
    }

    public Matrice() {
    }

    public Vecteur[] getA() {
        return A;
    }

    public void setA(Vecteur[] a) {
        A = a;
        n = A.length;
        m = A[0].vecteur.length;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }
	
    private Matrice calculTransposee(Vecteur[] A) {

    	//initialisation de la matrice transposée
        Vecteur[] AT = new Vecteur[m];
        for (int i = 0; i<m; i++) {
            AT[i] = new Vecteur(n);
        }

        for (int i = 0; i<n; i++) {
            for (int j = 0; j < m; j++) {
                AT[j].vecteur[i] = A[i].vecteur[j];
            }
        }

        return new Matrice(AT);
    }
	
	public Vecteur calculVisageMoyen() {
		/*
		 * Fonction calculant le visage moyen
		 * @author Dorian
		 * @return J vecteur de l'image moyenne
		 */
		
		Matrice B = calculTransposee(A);
		Vecteur J = new Vecteur(m);
		
		float moy_ligne;
		for (int i = 0; i<m; i++) {
			moy_ligne = 0;
	        for (int j = 0; j<n; j++) {
	        	moy_ligne = (float) (moy_ligne + B.A[i].vecteur[j]);
	        }
	        J.vecteur[i] = moy_ligne/n;
		}
		return J;
	}
	
	public Matrice MatriceCovariance() {
		/*
		 * Fonction calculant A*A^T la matrice de covariance des images
		 * @author Dorian
		 * @return W la matrice de covariance des images
		 */
		
		//initialisation de la matrice
		Vecteur[] AAT = new Vecteur[n];
		for (int i = 0; i<n; i++) {
	        AAT[i] = new Vecteur(n);
	    }
		
		//calcul de la matrice terme par terme
		double somme;
		for (int i = 0; i<n; i++) {
	        for (int j = 0; j<n; j++) {
	        	somme = 0;
	        	for (int k=0; k<m; k++) {
	        		somme += A[i].vecteur[k]*A[j].vecteur[k];
	        	}
        		AAT[i].vecteur[j] = somme;
	        }
		}
		return new Matrice(AAT);
		
	}
}
