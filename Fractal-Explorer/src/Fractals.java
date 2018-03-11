import java.awt.Color;

public class Fractals {

	final static double DEFAULT_MAXX = 1.5;
	final static double DEFAULT_MINX = -2.5;
	final static double DEFAULT_MAXY = 1.6;
	final static double DEFAULT_MINY = -1.6;
	static int WIDTH = 1000;
	static int HEIGHT = 750;
	static Complex maxPoint = new Complex(DEFAULT_MAXX, DEFAULT_MAXY);
	static Complex minPoint = new Complex(DEFAULT_MINX, DEFAULT_MINY);
	protected static int iterations = 100;
	protected static int max_iterations = 100;
	
	
	protected static Complex getComplexValues(double r, double i) {

		Complex point = new Complex();

		point.real = minPoint.real + ((r / WIDTH) * (maxPoint.real - minPoint.real));
		point.imaginary = maxPoint.imaginary - ((i / HEIGHT) * (maxPoint.imaginary - minPoint.imaginary));

		return point;
	}
	
	public Color pixelColor(int c, Complex i) {
		int colour;
		if (c == max_iterations) {
			return Color.BLACK;

			// if it is not in the set then the pixel is coloured according to
			// how close it gets to max iterations
		} else {
//			return new Color((c * 10) % 255, (c * 3) % 189, 55);
			float smoothColour = (float) (iterations + 5 - Math.log(Math.log(i.modulus())) / Math.log(2))
					/ max_iterations;

			colour = Color.HSBtoRGB((float) (0.5 * smoothColour), 0.9f, 0.9f);  
			
              
              //create new color from the RGB value
              Color color = new Color(colour);
              
			return color;
		}
	}

}
