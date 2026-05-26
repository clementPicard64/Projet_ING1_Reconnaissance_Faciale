public class Matrice{
	
	private Vecteur[] A; // matrice
	
	public Matrice(Vecteur[] a) {
		A = a;
	}
	
	public Matrice() {
	}
	
	public Vecteur[] getA() {
		return A;
	}

	public void setA(Vecteur[] a) {
		A = a;
	}
	
	private Matrice calculTransposee(Vecteur[] A) {
		/*
		 * Fonction calculant la transposée de A
		 * @author Dorian
		 * @param A tableau de vecteurs
		 * @return B tableau de vecteurs, transposée de A
		 */
	    Matrice B = new Matrice();

	    for (int i = 0; i < A.length; i++) {
	        for (int j = i + 1; j < A[0].vecteur.length; j++) {
	            B.A[i].vecteur[j] = A[j].vecteur[i];
	            B.A[j].vecteur[i] = A[i].vecteur[j];
	        }
	    }   
	    return B;
	}
	
	public Vecteur calculVisageMoyen() {
		/*
		 * Fonction calculant le visage moyen
		 * @author Dorian
		 * @param A tableau de vecteurs
		 * @return J vecteur de l'image moyenne
		 */
		Matrice B = calculTransposee(A);
		Vecteur J= new Vecteur(A.length);
		float moy_ligne;
		
		for (int i = 0; i < B.A.length; i++) {
			moy_ligne = 0;
	        for (int j = 0; j < A.length; j++) {
	        	moy_ligne = moy_ligne + B.A[i].vecteur[j];
	        }
	        J.vecteur[i] = moy_ligne/A.length;
		}
		return J;
	}
}
