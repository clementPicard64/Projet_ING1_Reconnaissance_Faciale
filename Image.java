package code;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Image {
	
	private String id;
	private BufferedImage img; //img stockée avec BufferedImage
	
	//private boolean estDeReference;
	private int largeurImage; //Largeur de l'image 
	private int hauteurImage; //Longueur de l'image 
	private String cheminImage; //Chemin de l'image
	private double[][] matriceImage; //Matrice de l'image (tableau 2D)
	private Vecteur vecteurImage; //Vecteur colonne de l'image de type de la classe Vecteur
	
	
	/**
	 * 
	 * @author Clément
	 * @param Chemin : chemin de l'image 
	 * @throws IOException (ajout de l'exception plus tard)
	 */
	public Image(String id,String CheminPersonne) throws IOException {
		this.id = id;
		this.cheminImage = CheminPersonne+ "/" + id;
		this.img = ImageIO.read(new File(this.cheminImage));
		this.largeurImage = this.img.getWidth();
		this.hauteurImage = this.img.getHeight();	
	}
	
	/**
	 * @author Clément
	 * Getteur qui retourne la photo 
	 * @return
	 */
    public BufferedImage getImage() {
        return this.img;
    }
    
    /**
     * @author Clément 
     * Procédure permettant de convertir la matrice en niveau de gris 
     */
    public void convertirEnNiveauDeGris() {
        for (int y = 0; y < this.hauteurImage; y++) {
            for (int x = 0; x < this.largeurImage; x++) {
            	int pixel = this.img.getRGB(x, y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8)  & 0xFF;
                int b =  pixel & 0xFF;

                this.matriceImage[y][x] = (double) (r + g + b) / 3;
            }
        }
    }
    
    /**
     * Procédure permettant de vectoriser la matrice (transformation en vecteur colonne)
     */
    public void vectoriser() {
    	int cpt = 0;
    	double[] tab = new double[this.hauteurImage * this.largeurImage];
    	for (int y = 0; y < this.hauteurImage; y++) {
            for (int x = 0; x < this.largeurImage; x++) {
            	tab[cpt] = this.matriceImage[y][x];
            	cpt += 1;
            }
    	}
    	this.vecteurImage = new Vecteur(tab);
    }
    
    @Override
    /**
     * Retourne le chemin de l'image sous forme de chaine de caractere
     * @return (String)
     */
    public String toString() {
    	return this.id;
    }
    
    
   /**
    * Retourne le vecteur colonne associé à l'image
    * @return (Vecteur) 
    */
    public Vecteur getVecteurImage() {
    	return this.vecteurImage;
    }
    
    
}
