public class Complex implements Cloneable{
    public double real, imag;
    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }
    public Complex(double real) {
        this.real = real;
        this.imag = 0;
    }
    public Complex() {
        this.real = 0;
        this.imag = 0;
    }
    public Complex add(Complex other) {
        return new Complex(this.real+other.real, this.imag+other.imag);
    }
    public Complex sub(Complex other) {
        return new Complex(this.real-other.real, this.imag-other.imag);
    }
    public Complex mult(Complex other) {
        return new Complex(this.real*other.real-this.imag*other.imag, this.real*other.imag+this.imag*other.real);
    }

    public Complex round() {
        return new Complex(Math.round(this.real), Math.round(this.imag));
    }

    @Override
    public String toString() {
        return String.format("%f + %fi", real, imag);
    }

    @Override
    public Complex clone() {
        return new Complex(this.real, this.imag);
    }
}
