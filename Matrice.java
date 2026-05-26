public class Matrice{
	
	private Vecteur[] A; // matrice
	private int n,m;
	
	public Matrice(double[][] a) {
		A = a;
		n=100;
		m=100;
	}
	
	public double[][] getA() {
		return A;
	}

	public void setA(double[][] a) {
		A = a;
	}
	
	public double[][] calculTransposee(Vecteur[] A) {
		/*
		 * Fonction calculant la transposée de A
		 * @author Dorian
		 * @param A tableau 2D
		 * @return B tableau 2D transposée de A
		 */
	    double Vecteur[] B = null;

	    for (int i = 0; i < A.length; i++) {
	        for (int j = i + 1; j < A.length; j++) {
	            B[i][j] = A[j][i];
	            B[j][i] = A[i][j];
	        }
	    }   
	    return B;
	}
}
