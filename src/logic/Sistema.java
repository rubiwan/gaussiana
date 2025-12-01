package logic;

/**
 * Clase que representa un sistema de ecuaciones lineales Ax = b.
 *
 * @author Anabel Diaz
 * @version 1.0 - 22/11/2025
 */
public class Sistema {

    private final double[][] A;
    private final double[] b;

    public Sistema(double[][] A, double[] b) {
        this.A = A;
        this.b = b;
    }

    public double[][] getA() {
        return A;
    }

    public double[] getB() {
        return b;
    }
}

