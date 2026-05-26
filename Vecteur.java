public class Vecteur {
	
	double[] vecteur;
	
	public  Vecteur(int dimension) {
		this.vecteur = new double[dimension];
	}
	
	public  Vecteur(double[] valeur) {
		this.vecteur = valeur;
	}

	public void centraliser(Vecteur visageMoyen) {
		/*
		 * Fonction centralise les vecteurs
		 * @author Yassine
		 * @param visageMoyen tableau 1D
		 */
		double[] tabmoy = visageMoyen.getVecteur();
		
		for(int i = 0; i< this.vecteur.length; i++) {
			this.vecteur[i] -= tabmoy[i];
		}
	}

	private double[] getVecteur() {
		return null;
	}
}