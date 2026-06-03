package projet;


import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Image {
	
	private String id;
	private BufferedImage img; //img stockée avec BufferedImage
	private String cheminImage; //Chemin de l'image
	private double[][] matriceImage; //Matrice de l'image (tableau 2D)
	private Vecteur vecteurImage; //Vecteur colonne de l'image de type de la classe Vecteur
	
	
	/**
	 * 
	 * @author Clément
	 * Constructeur lorqu'on veut ajouter une image
	 * @param CheminPersonne : chemin de l'image
	 * @param id : nom de l'image 
	 * @throws IOException (ajout de l'exception plus tard)
	 * 
	 */
	public Image(String id,String CheminPersonne) throws IOException {
		this.id = id;
		this.cheminImage = CheminPersonne + "/" + id;
		this.img = ImageIO.read(new File(this.cheminImage));
	}
    
	/**
	*
	* Constructeur lorsqu'on veut 
	* @param CheminImage : chemin de l'image 
	*/
	public Image(String CheminImage) throws IOException {
		this.cheminImage = CheminImage;
		this.img = ImageIO.read(new File(this.cheminImage));
	}
	
	
	
	
    /**
     * @author Clément 
     * Procédure permettant de convertir la matrice en niveau de gris (3)
     */
    public void convertirEnNiveauDeGris() {
    	this.matriceImage = new double[112][92];
        for (int y = 0; y < 92; y++) {
            for (int x = 0; x < 112; x++) {
            	int pixel = this.img.getRGB(x, y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8)  & 0xFF;
                int b =  pixel & 0xFF;

                this.matriceImage[x][y] = (double) (r + g + b) / 3;
            }
        }
    }
    
    /**
     * @author Clément
     * Procédure permettant de vectoriser la matrice (transformation en vecteur colonne) (4)
     * 
     */f
    public void vectoriser() {
    	int cpt = 0;
    	double[] tab = new double[10304];
    	for (int y = 0; y < 112; y++) {
            for (int x = 0; x < 92; x++) {
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
    public Vecteur getVecteurImage() {
    	return this.vecteurImage;
    }
    
    
	/**
	 * @author Clément
	 * Getteur qui retourne la photo 
	 * @return (BufferedImage)
	 */
    public BufferedImage getImage() {
        return this.img;
    }
    
    /**
     * 
     * @author Clément
     * Méthode permettant de redimensionner l'image dans les dimensions de la base d'images (2)
     */
    public void redimensionner() {
        BufferedImage redim = new BufferedImage(112, 92, this.img.getType());
        java.awt.Graphics2D g = redim.createGraphics(); //Dessinne l'image redim avec g 
        g.drawImage(this.img.getScaledInstance(112, 92, java.awt.Image.SCALE_SMOOTH), 0, 0, null); //Prends l'image, la redimentionne en 112x92,et la met dans redim
        g.dispose(); //Libere l'espace utlisé par g
        this.img = redim;
    }
    
    /**
     * @author Clement
     * @return (String) le chemin de l'image
     */
    public String getCheminImage() {
    	return this.cheminImage;
    }
    
    
}
