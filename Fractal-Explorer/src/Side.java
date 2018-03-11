import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Side extends JPanel {

	BufferedImage newImage;
	FractalGUI gui;
	

	public Side(BufferedImage screenFullImage, FractalGUI gui2) {
		this.newImage = newImage;
		this.gui = gui;
			}

	
	@Override
	  public void paintComponent(Graphics g) {
	    super.paintComponent(g);

	    // do your drawing
	    // e.g. - if you have an image that you want to draw...
	    // this draws the image at coordinate (0, 0) = upper left corner in your panel
	    BufferedImage image = newImage; 
	    g.drawImage(image, 0, 0, this);
	  }
	
}
