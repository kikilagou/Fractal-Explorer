import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GenerateMandelbrot extends JPanel {

	private static final long serialVersionUID = 1L;

	boolean dragEnd = false;
	static double DEFAULT_MAXX = 1.5;
	static double DEFAULT_MINX = -2.5;
	static double DEFAULT_MAXY = 1.6;
	static double DEFAULT_MINY = -1.6;
	static int WIDTH = 1000;
	static int HEIGHT = 750;
	protected static int iterations = 100;
	protected static int max_iterations = 100;
	private BufferedImage bufferedImage;
	int colour;
	float smoothColour;

	protected GenerateMandelbrot() {
		bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	}
	

	// Generate the Mandlebrot set
	protected void computeMandel(int width, int height) {
		// Go through every pixel
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Visit each point
				checkPoint(getComplexValues(x, y, width, height), x, y);
			}
		}
	}

	// Returns the number of iterations that each point takes
	protected void checkPoint(Complex c, int x, int y) {
		int iterations = 0;
		Complex c2 = null;

		// If normal mandlebrot is selected
		if (!FractalGUI.fractalSelect.getSelectedItem().equals("Burning Ship")) {
	
			c2 = c;

			// Check if it goes to infinity
			while (iterations < max_iterations && c2.modulusSquared() <= 4) {
				// (zn) = (zn-1)^2 + c
				c2 = c2.square().add(c);
				iterations++;
			}
		} else if (!FractalGUI.fractalSelect.getSelectedItem().equals("Mandelbrot Set")) {
			double zreal = 0;
			double zimaginary = 0;

			while (iterations < max_iterations && zreal * zreal + zimaginary * zimaginary < 4) {
				double zrealUpdated = zreal * zreal - zimaginary * zimaginary + c.getRealPart();
				double zimaginaryUpdated = 2 * Math.abs(zreal) * Math.abs(zimaginary) + c.getImaginaryPart();

				zreal = zrealUpdated;
				zimaginary = zimaginaryUpdated;

				iterations += 1;
			}
			c2 = new Complex(zreal, zimaginary);
		}

		// Colour point
		bufferedImage.setRGB(x, y, colourPoint(iterations, c2));
	}

	protected void init() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}

	public void setIterations(int i) {
		max_iterations = new Integer(i);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		computeMandel(WIDTH, HEIGHT);
		g.drawImage(bufferedImage, 0, 0, this);

		// --- Zoom rectangle ---//

		g.setColor(Color.RED);

//		if(FractalGUI.dragging == true) {
		FractalGUI.drawPerfectRect(g, FractalGUI.x, FractalGUI.y, FractalGUI.x2, FractalGUI.y2);
		
  

//		}
		
		
//		if(dragEnd) {
//			
//			dragEnd = false;
//		
//		Complex min = null;
//		Complex max = null;
//
//		if (FractalGUI.y2 - FractalGUI.y > 0) {
//			if (FractalGUI.x2 - FractalGUI.x > 0) {
//				min = getComplexValues(FractalGUI.x, FractalGUI.y, WIDTH, HEIGHT);
//				max = getComplexValues(FractalGUI.x2, FractalGUI.y2, WIDTH, HEIGHT);
//			} else {
//				// End is bottom left of start
//				min = getComplexValues(FractalGUI.x2, FractalGUI.y, WIDTH, HEIGHT);
//				max = getComplexValues(FractalGUI.x, FractalGUI.y2, WIDTH, HEIGHT);
//			}
//		} else {
//			if (FractalGUI.x2 - FractalGUI.x > 0) {
//				// End is top right of start
//				min = getComplexValues(FractalGUI.x, FractalGUI.y2, WIDTH, HEIGHT);
//				max = getComplexValues(FractalGUI.x2, FractalGUI.y, WIDTH, HEIGHT);
//			} else {
//				// End is top left of start
//				min = getComplexValues(FractalGUI.x2, FractalGUI.y2, WIDTH, HEIGHT);
//				max = getComplexValues(FractalGUI.x, FractalGUI.y, WIDTH, HEIGHT);
//			}
//		}
//		
//		DEFAULT_MINX = min.getRealPart();
//		DEFAULT_MAXX = max.getRealPart();
//		DEFAULT_MINY = min.getImaginaryPart();
//		DEFAULT_MAXY = max.getImaginaryPart();
//		
//		repaint();
//		}
//		DEFAULT_MINX = getComplexValues(FractalGUI.x, FractalGUI.y, WIDTH, HEIGHT).getRealPart();
//		DEFAULT_MAXX = getComplexValues(FractalGUI.x2, FractalGUI.y2, WIDTH, HEIGHT).getRealPart();
//		DEFAULT_MINY = getComplexValues(FractalGUI.x, FractalGUI.y, WIDTH, HEIGHT).getImaginaryPart();
//		DEFAULT_MAXY = getComplexValues(FractalGUI.x2, FractalGUI.y2, WIDTH, HEIGHT).getImaginaryPart();
//		
	

		
//		Complex min = null;
//		Complex max = null;
//		
//		min = getComplexValues(FractalGUI.x, FractalGUI.y, WIDTH, HEIGHT);
//		max = getComplexValues(FractalGUI.x2, FractalGUI.y2, WIDTH, HEIGHT);
//
//		DEFAULT_MINX = min.getRealPart();
//		DEFAULT_MAXX = max.getRealPart();
//		DEFAULT_MINY = min.getImaginaryPart();
//		DEFAULT_MAXY = max.getImaginaryPart();
//		
	}

	public Color pixelColor(int c, Complex i) {

		
		if (c == max_iterations) {
			return Color.BLACK;

			// if it is not in the set then the pixel is coloured according to
			// how close it gets to max iterations
		} else {
			// return new Color((c * 10) % 255, (c * 3) % 189, 55);
			smoothColour = (float) (iterations + 5 - Math.log(Math.log(i.modulus())) / Math.log(2))
					/ max_iterations;

			colour = Color.HSBtoRGB((float) (0.5 * smoothColour), 0.9f, 0.9f);

			// create new color from the RGB value
			Color color = new Color(colour);

			return color;
		}
	}

	protected int colourPoint(int iterations, Complex c) {
		int colour;

		// Sets the colour of the pixel
		if (iterations == max_iterations) {
			colour = Color.BLACK.getRGB();
		} else {
			// Smooths the colouring
			float smoothColour = (float) (iterations + 5 - Math.log(Math.log(c.modulus())) / Math.log(2))
					/ max_iterations;

			colour = Color.HSBtoRGB((float) (0.9 * smoothColour), 0.9f, 0.9f);
		}
		return colour;
	}

	protected static Complex getComplexValues(int r, int i, int width, int height) {
		double a = DEFAULT_MINX + r * (DEFAULT_MAXX - DEFAULT_MINX) / WIDTH;
		double b = DEFAULT_MINY + i * (DEFAULT_MAXY - DEFAULT_MINY) / HEIGHT;

		return new Complex(a, b);

	}

	public void setMaxX(double x) {
		this.DEFAULT_MAXX = x;
	}

	public void setMinX(double x) {
		this.DEFAULT_MINX = x;
	}

	public void setMaxY(double x) {
		this.DEFAULT_MAXY = x;
	}

	public void setMinY(double x) {
		this.DEFAULT_MINY = x;
	}

	protected int getIterations() {
		return max_iterations;
	}

	protected void setHeight(int h) {
		this.HEIGHT = h;
	}

	protected void setWidth(int w) {
		this.WIDTH = w;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public int getWidth() {
		return WIDTH;
	}
}
