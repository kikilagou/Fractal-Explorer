import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

class JuliaSet extends JPanel {
	static double DEFAULT_MAXX = 2.0;
	final static double DEFAULT_MINX = -2.0;
	final static double DEFAULT_MAXY = 1.6;
	final static double DEFAULT_MINY = -1.6;
	protected static int MAX_ITERATIONS = 100;

	
	// This stores whether the mouse is in the mandlebrot or not
	private boolean outOfMandel = true;

	private Complex z;
	
	// x, y coordinate ranges of displayed area
	protected static double maxX, minX, maxY, minY;

	// Constant screen dimensions
	final static int JULIA_WIDTH = 200;
	final static int JULIA_HEIGHT = 200;

	private BufferedImage juliaSetImg;

	protected JuliaSet(Complex z) {
		this.z = z;
		juliaSetImg = new BufferedImage(JULIA_WIDTH, JULIA_HEIGHT, BufferedImage.TYPE_INT_RGB);

		maxX = DEFAULT_MAXX;
		minX = DEFAULT_MINX;
		minY = DEFAULT_MINY;
		maxY = DEFAULT_MAXY;
		this.setPreferredSize(new Dimension(JULIA_HEIGHT, JULIA_WIDTH));

	}

	protected JuliaSet() {

		juliaSetImg = new BufferedImage(JULIA_WIDTH, JULIA_HEIGHT, BufferedImage.TYPE_INT_RGB);

		maxX = DEFAULT_MAXX;
		minX = DEFAULT_MINX;
		minY = DEFAULT_MINY;
		maxY = DEFAULT_MAXY;
		this.setPreferredSize(new Dimension(JULIA_HEIGHT, JULIA_WIDTH));

	}

	protected void setOutOfMandel(boolean b) {
		outOfMandel = b;
	}

//	protected void init() {
//		this.setPreferredSize(new Dimension(JULIA_HEIGHT, JULIA_WIDTH));
//	}

	// Updates the complex number which is used
	protected void setComplex(Complex z) {
		this.z = z;
	}
	
	// Generate the Mandlebrot set
	protected void computeMandel() {
		// Go through every pixel
		for (int y = 0; y < JULIA_HEIGHT; y++) {
			for (int x = 0; x < JULIA_WIDTH; x++) {
				
				//Visit each point
				checkPoint(coordToComplex(x, y), x, y);
			}
		}
	}
	
	//Converts the coordinates to its complex number equivalent
	protected static Complex coordToComplex(int x, int y) {
		// Mapping points to complex number
		double a = minX + x * (maxX - minX) / JULIA_WIDTH;
		double b = minY + y * (maxY - minY) / JULIA_HEIGHT;

		return new Complex(a, b);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// If in Mandel display Julia set in relation to position; otherwise
		// show black
//		if (!outOfMandel) {
			computeMandel();
			g.drawImage(juliaSetImg, 0, 0, this);
//		} else {
//			g.setColor(Color.BLACK);
//			g.fillRect(0, 0, JULIA_WIDTH, JULIA_HEIGHT);
//		}
	}

	// Returns the number of iterations that each point takes
	protected void checkPoint(Complex c, int x, int y) {
		int iterations;
		boolean k = false;
		for (iterations = 0; iterations < JULIA_HEIGHT + JULIA_WIDTH; iterations++) {

			c = c.square().add(z);

			if (c.modulusSquared() > 4) {
				k = true;
				break;
			}
		}
		if (!k) {
			iterations = JULIA_HEIGHT + JULIA_WIDTH - 1;
		}
		// Colour point
		juliaSetImg.setRGB(x, y, colourPoint(iterations, c));
	}
	
	protected int colourPoint(int iterations, Complex c) {
		int colour;

		// Sets the colour of the pixel
		if (iterations == MAX_ITERATIONS) {
			colour = Color.BLACK.getRGB();
		} else {
			// Smooths the colouring
			float smoothColour = (float) (iterations + 5 - Math.log(Math.log(c.modulus())) / Math.log(2))
					/ MAX_ITERATIONS;

			colour = Color.HSBtoRGB((float) (0.9 * smoothColour), 0.9f, 0.9f);
		}
		return colour;
	}
//	static int WIDTH = 200;
//	static int HEIGHT = 200;
//	private static Complex c;
//	
//	protected int iterations = 500;
//	protected static boolean clicked = true;
//	protected static boolean firstTime = true;
//	private Complex maxPoint = new Complex(2.0, 1.6);
//	private Complex minPoint = new Complex(-2.1, -1.6);
//	private Complex[][] pixels = new Complex[WIDTH][HEIGHT];
//
//	// the julia set takes a complex number that it will be based off
//	public JuliaSet(Complex complex) {
//		this.c = complex;
//		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
//	}
//	
//	protected void init() {
//		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
//	}
//
//	public void paint(Graphics g) {
//		super.paintComponent(g);
//
//		// this checks if it is the first time the cursor has entered the
//		// mandelbrot panel, before the cursor enters for the first time the
//		// julia panel is black
//		if (firstTime) {
//			g.setColor(Color.BLACK);
//			g.fillRect(0, 0, WIDTH, HEIGHT);
//
//			// sets the first time to false
//			firstTime = false;
//		} else {
//			// otherwise if converts each pixel in the panel to a complec number
//			// in prder to display the julia set
//			for (int y = 0; y < HEIGHT; y++) {
//				for (int x = 0; x < WIDTH; x++) {
//					pixels[x][y] = getComplexValues(x, y);
//
//					// colours in each pixel accoring to the julia set formula
//					int it = isInSet(pixels[x][y]);
//					g.setColor(pixelColor(it));
//					g.fillRect(x, y, 1, 1);
//				}
//			}
//		}
//
//	}
//
//	// colouring in the pixels
//	public Color pixelColor(int c) {
//
//		// checks to see it the iterations have been achieved, if yes the point
//		// is in the julia set and the point is coloured in black
//		if (c == iterations) {
//			return Color.BLACK;
//			// else is is coloured accordingly to how close it got to the set
//		} else {
//			return new Color((c * 10) % 255, (c * 3) % 205, 55);
//		}
//
//	}
//
//	public void update(Graphics g) {
//		paint(g);
//	}
//
//	// method that forms the axis for the real and imaginary parts of the
//	// complex number
//	protected Complex getComplexValues(double i, double j) {
//
//		// the complex point that the pixel will be transformed into
//		Complex point = new Complex();
//
//		point.real = minPoint.real + ((i / WIDTH) * (maxPoint.real - minPoint.real));
//		point.imaginary = maxPoint.imaginary - ((j / HEIGHT) * (maxPoint.imaginary - minPoint.imaginary));
//		return point;
//	}
//
//	public int isInSet(Complex z) {
//		for (int i = 0; i < pixels.length; i++) {
//			// Compute z = z*z + c;
//			// carrying out the julia set formula
//			double newx = z.square().real + c.real;
//			double newy = z.square().imaginary + c.imaginary;
//
//			z.real = newx;
//			z.imaginary = newy;
//
//			// Check magnitude of z and return iteration number
//			if (z.modulusSquared() > 4)
//
//				return i;
//		}
//		return pixels.length - 1;
//	}

}
