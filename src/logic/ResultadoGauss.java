package logic;

/**
 * Clase que representa el resultado del método de eliminación gaussiana.
 *
 * @author Anabel Diaz
 * @version 1.0 - 22/11/2025
 */
public class ResultadoGauss {

    private final double[][] U;   // matriz triangular superior
    private final double[] bMod;  // terminos independientes tras la eliminación
    private final double[] x;     // solucion por sustitución regresiva

    public ResultadoGauss(double[][] U, double[] bMod, double[] x) {
        this.U = U;
        this.bMod = bMod;
        this.x = x;
    }

    public double[][] getU() {
        return U;
    }

    public double[] getbMod() {
        return bMod;
    }

    public double[] getX() {
        return x;
    }
}

