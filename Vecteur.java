package projet;

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
		double[] tabmoy = visageMoyen.getVecteur(); //On récupère le vecteur visage Moyen
		
		for(int i = 0; i< this.vecteur.length; i++) {
			this.vecteur[i] -= tabmoy[i]; // On soustrait chaque vecteur image par le vecteur visage moyen pour les centrés
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
	public double produitScalaire(Vecteur autre) {
	    double somme = 0;
	    for (int i = 0; i < this.vecteur.length; i++) {
	        somme += this.vecteur[i] * autre.vecteur[i];
	    }
	    return somme;
	}
}
