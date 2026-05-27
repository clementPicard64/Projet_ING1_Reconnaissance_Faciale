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

	public double[] getVecteur() {
		return vecteur;
	}

	public void setVecteur(double[] vecteur) {
		this.vecteur = vecteur;
	}

		/**
	 * Description : prends deux vect et en fait le produit scalaire
	 * @author CandyCelia
	 * @param autre un vecteur
	 * @return somme un double
	 */
	//MAJ DIAGRAMME
	public double produitScalaire(Vecteur autre) {
	    double somme = 0;
	    for (int i = 0; i < this.vecteur.length; i++) {
	        somme += this.vecteur[i] * autre.vecteur[i];
	    }
	    return somme;
	}
}
