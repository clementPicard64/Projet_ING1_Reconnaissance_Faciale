package code;


import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Image {
	
	private String id;
	private BufferedImage img; //img stockée avec BufferedImage
	
	//private boolean estDeReference;
	private String cheminImage; //Chemin de l'image
	private double[][] matriceImage; //Matrice de l'image (tableau 2D)
	private Vecteur vecteurImage; //Vecteur colonne de l'image de type de la classe Vecteur
	
	
	/**
	 * 1
	 * @author Clément
	 * @param Chemin : chemin de l'image 
	 * @throws IOException (ajout de l'exception plus tard)
	 * 
	 */
	public Image(String id,String CheminPersonne) throws IOException {
		this.id = id;
		this.cheminImage = CheminPersonne + "/" + id + ".png";
		this.img = ImageIO.read(new File(this.cheminImage));
		//this.largeurImage = this.img.getWidth();
		//this.hauteurImage = this.img.getHeight();	
	}
    
    /**3
     * @author Clément 
     * Procédure permettant de convertir la matrice en niveau de gris 
     */
    public void convertirEnNiveauDeGris() {
    	this.matriceImage = new double[100][100];
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
            	int pixel = this.img.getRGB(x, y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8)  & 0xFF;
                int b =  pixel & 0xFF;

                this.matriceImage[y][x] = (double) (r + g + b) / 3;
            }
        }
    }
    
    /**4
     * @author Clément
     * Procédure permettant de vectoriser la matrice (transformation en vecteur colonne)
     * 
     */
    public void vectoriser() {
    	int cpt = 0;
    	double[] tab = new double[10000];
    	for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
            	tab[cpt] = this.matriceImage[y][x];
            	cpt += 1;
            }
    	}
    	this.vecteurImage = new Vecteur(tab);
    }
    
    @Override
    /**
     * @author Clément
     * Retourne le nom de l'image sous forme de chaine de caractere
     * @return (String)
     */
    public String toString() {
    	return this.id;
    }
    
    
   /**
    * @author Clément
    * Retourne le vecteur colonne associé à l'image
    * @return (Vecteur) 
    */
    public double[] getVecteurImage() {
    	return this.vecteurImage.getVecteur();
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
     * @author Clement
     * @return le chemin de l'image 
     */
    public String getCheminImage() {
    	return this.cheminImage;
    }
    
    /**
     * 2
     * @author Clément
     * @param nouvelleLargeur
     * @param nouvelleHauteur
     */
    public void redimensionner() {
        BufferedImage redim = new BufferedImage(100, 100, this.img.getType());
        java.awt.Graphics2D g = redim.createGraphics(); //Dessinne l'image redim avec g 
        g.drawImage(this.img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH), 0, 0, null); //Prends l'image, la redimentionne en 100x100,et la met dans redim
        g.dispose(); //Libere l'espace utlisé par g
        this.img = redim;
    }
    

   
    /**
     * Test avec 1 photo
     * @param args
     * @throws IOException 
     */
    /**
    public static void main(String[] args) throws IOException {
    	Image Benji = new Image("Benji","/home/cytech/Projet/");
    	Benji.redimensionner();
    	Benji.convertirEnNiveauDeGris();
    	Benji.vectoriser();
    	System.out.println(Arrays.toString(Benji.getVecteurImage())); //import java.util.Arrays; pour print les vecteurs
    }*/
    
    
}
