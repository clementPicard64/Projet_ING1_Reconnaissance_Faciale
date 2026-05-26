public class Vecteur {
	
	private int[] vecteur;
	
	public  Vecteur(int dimension) {
		this.vecteur = new int[dimension];
	}
	
	public  Vecteur(int[] valeur) {
		this.vecteur = valeur;
	}

	public void centraliser(Vecteur visageMoyen) {
		/*
		 * Fonction centralise les vecteurs
		 * @author Yassine
		 * @param visageMoyen tableau 1D
		 */
		int[] tabmoy = visageMoyen.getVecteur();
		
		for(int i = 0; i< this.vecteur.length; i++) {
			this.vecteur[i] -= tabmoy[i];
		}
	}

	private int[] getVecteur() {
		return null;
	}
}