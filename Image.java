package code;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Image {
	
	private String id; //Pas encore fait (initialisation)
	private BufferedImage img;
	
	//private boolean estDeReference;
	private int largeurImage;
	private int hauteurImage;
	private String cheminImage;
	private double[][] matriceImage;
	private Vecteur vecteurImage;
	
	public Image(String Chemin) throws IOException {
		this.cheminImage = Chemin;
		this.img = ImageIO.read(new File(this.cheminImage));
		this.largeurImage = this.img.getWidth();
		this.hauteurImage = this.img.getHeight();
		
	}
	
    public BufferedImage getImage() {
        return this.img;
    }
    
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
}
