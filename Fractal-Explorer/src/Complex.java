
public class Complex {

	// the real number
	protected double real;

	// the imaginary number
	protected double imaginary;

	public Complex() {}

	// constructor - a complex number has real and imaginary parts
	public Complex(double realPart, double imaginaryPart) {
		this.real = realPart;
		this.imaginary = imaginaryPart;
	}

	// method to get the real part of the complex number
	public double getRealPart() {
		return real;
	}

	// method to get the imaginary part of the complex number
	public double getImaginaryPart() {
		return imaginary;
	}

	// method to square the absolute value of the complex number (a+bi) where a
	// is the real part and bi is the imaginary part
	public Complex square() {
		double realPart = real * real - imaginary * imaginary;
		double imaginaryPart = real * imaginary + imaginary * real;
		return new Complex(realPart, imaginaryPart);
	}

	// this method returns the modulus of the complex number - this is the
	// distance from the origin in polar coordinates
	public double modulus() {
		if (real != 0 || imaginary != 0) {
			return Math.sqrt(real * real + imaginary * imaginary);
		} else {
			return 0d;
		}
	}

	public double modulusSquared() {
		return modulus() * modulus();
	}

	// method to add the complex number d to this complex number
	public Complex add(Complex c) {

		return new Complex(this.real + c.real, this.imaginary + c.imaginary);
		
		// invoking object
//		Complex a = this;
//		double re = a.real + d.real;
//		double im = a.imaginary + d.imaginary;
//		return new Complex(re, im);

	}
}
