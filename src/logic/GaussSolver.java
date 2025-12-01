package logic;

import config.LoggerFichero;
import exception.MatrixException;

/**
 * Clase para resolver sistemas de ecuaciones lineales utilizando
 * el método de eliminación gaussiana con pivotaje parcial escalado.
 *
 * @author Anabel Diaz
 * @version 1.0 - 22/11/2025
 */
public class GaussSolver {

    private static final LoggerFichero log = LoggerFichero.getInstance();
    private static final double EPS = 1e-12;

    /**
     * Resuelve el sistema de ecuaciones dado (envoltorio sobre solveGaussian).
     */
    public static ResultadoGauss solve(Sistema sistema) throws MatrixException {
        double[][] A = sistema.getA();
        double[] b = sistema.getB();
        return solveGaussian(A, b);
    }

    /**
     * Implementa el método de eliminación gaussiana con pivotaje parcial escalado.
     *
     * @param A La matriz de coeficientes.
     * @param b El vector de términos independientes.
     * @return Un objeto ResultadoGauss que contiene la matriz triangular superior,
     *         el vector modificado y la solución del sistema.
     * @throws MatrixException Si el sistema es singular o no tiene solución única.
     */
    public static ResultadoGauss solveGaussian(double[][] A, double[] b) throws MatrixException {


        validateDimensions(A, b);

        int n = A.length;

        double[][] M = buildAugmentedMatrix(A, b);

        double[] s = computeScalingFactors(M);

        eliminationWithScaledPartialPivoting(M, s);

        checkSingularityInUpperMatrix(M);

        double[] x = backSubstitution(M);

        double[][] U = extractUpperMatrix(M);
        double[] bMod = extractModifiedRHS(M);

        return new ResultadoGauss(U, bMod, x);
    }


    /**
     * Valida dimensiones básicas de A y b.
     *
     * @param A La matriz de coeficientes.
     * @param b El vector de términos independientes.
     * @throws MatrixException Si A no es cuadrada o las dimensiones no coinciden.
     */
    private static void validateDimensions(double[][] A, double[] b) throws MatrixException {
        if (A == null || b == null) {
            throw new MatrixException("La matriz A y el vector b no pueden ser nulos.");
        }

        int n = A.length;
        if (n == 0) {
            throw new MatrixException("La matriz A no puede estar vacía.");
        }

        // Comprobamos que todas las filas existen y tienen longitud n
        for (int i = 0; i < n; i++) {
            if (A[i] == null || A[i].length != n) {
                throw new MatrixException("La matriz A debe ser cuadrada de tamaño " + n + "x" + n + ".");
            }
        }

        if (b.length != n) {
            throw new MatrixException(
                    "Dimensiones incompatibles: A es de " + n + "x" + n +
                            " pero b tiene longitud " + b.length + "."
            );
        }
    }

    /**
     * Construye la matriz aumentada M = [A | b].
     *
     * @param A La matriz de coeficientes.
     * @param b El vector de términos independientes.
     * @return La matriz aumentada M.
     */
    private static double[][] buildAugmentedMatrix(double[][] A, double[] b) {
        int n = A.length;
        double[][] M = new double[n][n + 1];

        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, M[i], 0, n);
            M[i][n] = b[i];
        }

        return M;
    }

    /**
     * Calcula el vector de factores de escala s[i] = max_j |a_ij|.
     *
     * @param M La matriz aumentada.
     * @return El vector de factores de escala.
     * @throws MatrixException Si alguna fila es completamente nula.
     */
    private static double[] computeScalingFactors(double[][] M) throws MatrixException {
        int n = M.length;
        double[] s = new double[n];

        for (int i = 0; i < n; i++) {
            double max = 0.0;
            for (int j = 0; j < n; j++) {   // solo columnas de A, no la de b
                double abs = Math.abs(M[i][j]);
                if (abs > max) {
                    max = abs;
                }
            }
            if (max == 0.0) {
                log.error("Fila " + i + " es completamente nula. Sistema singular.");
                throw new MatrixException("Fila " + i + " es completamente nula. Sistema singular.");
            }
            s[i] = max;
        }
        return s;
    }

    /**
     * Realiza la eliminación hacia forma triangular superior usando
     * pivotaje parcial escalado sobre la matriz aumentada M.
     *
     * @param M La matriz aumentada.
     * @param s El vector de factores de escala.
     * @throws MatrixException Si se encuentra un pivote casi nulo.
     */
    private static void eliminationWithScaledPartialPivoting(double[][] M, double[] s)
            throws MatrixException {

        int n = M.length;

        for (int k = 0; k < n - 1; k++) {

            // 1 Seleccionar fila pivote usando los factores de escala
            int pivotRow = k;
            double maxRatio = Math.abs(M[k][k]) / s[k];

            for (int i = k + 1; i < n; i++) {
                double ratio = Math.abs(M[i][k]) / s[i];
                if (ratio > maxRatio) {
                    maxRatio = ratio;
                    pivotRow = i;
                }
            }

            // 2 Comprobar pivote casi nulo
            if (Math.abs(M[pivotRow][k]) < EPS) {
                log.error("Pivote casi nulo en la columna " + k +
                        ". Sistema singular o mal condicionado.");
                throw new MatrixException("Pivote casi nulo en la columna " + k +
                        ". Sistema singular o mal condicionado.");
            }

            // 3 Intercambiar filas en M y en s si es necesario
            if (pivotRow != k) {
                double[] tmpRow = M[k];
                M[k] = M[pivotRow];
                M[pivotRow] = tmpRow;

                double tmpS = s[k];
                s[k] = s[pivotRow];
                s[pivotRow] = tmpS;
            }

            // 4 Eliminación por debajo del pivote
            for (int i = k + 1; i < n; i++) {
                double factor = M[i][k] / M[k][k];
                M[i][k] = 0.0;

                for (int j = k + 1; j <= n; j++) {
                    M[i][j] -= factor * M[k][j];
                }
            }
        }
    }

    /**
     * Comprueba si la matriz triangular superior resultante es singular
     *
     * @param M La matriz aumentada ya triangular.
     * @throws MatrixException Si el sistema es singular o no tiene solución única.
     */
    private static void checkSingularityInUpperMatrix(double[][] M) throws MatrixException {
        int n = M.length;

        // Comprobar filas nulas en U
        for (int i = 0; i < n; i++) {
            boolean filaNula = true;
            for (int j = 0; j < n; j++) { // solo parte de A, no la columna de b
                if (Math.abs(M[i][j]) > EPS) {
                    filaNula = false;
                    break;
                }
            }

            if (filaNula) {
                if (Math.abs(M[i][n]) > EPS) {
                    // sistema incompatible
                    throw new MatrixException(
                            "Sistema incompatible: la fila " + (i + 1) +
                                    " es nula en A pero el término independiente es " + M[i][n]
                    );
                } else {
                    // infinitas soluciones (singular)
                    throw new MatrixException(
                            "Sistema singular: la fila " + (i + 1) +
                                    " es completamente nula. No hay solución única."
                    );
                }
            }
        }

        // Comprobar último pivote
        if (Math.abs(M[n - 1][n - 1]) < EPS) {
            log.error("Último pivote casi nulo. Sistema singular o sin solución única.");
            throw new MatrixException("Último pivote casi nulo. Sistema singular o sin solución única.");
        }
    }

    /**
     * Sustitución hacia atrás sobre la matriz aumentada M ya triangular.
     *
     * @param M La matriz aumentada triangular.
     * @return El vector solución x.
     */
    private static double[] backSubstitution(double[][] M) {
        int n = M.length;
        double[] x = new double[n];

        for (int i = n - 1; i >= 0; i--) {
            double suma = M[i][n]; // término independiente ya transformado
            for (int j = i + 1; j < n; j++) {
                suma -= M[i][j] * x[j];
            }
            x[i] = suma / M[i][i];
        }
        return x;
    }

    /**
     * Extrae la matriz triangular superior U desde M.
     *
     * @param M La matriz aumentada.
     * @return La matriz U.
     */
    private static double[][] extractUpperMatrix(double[][] M) {
        int n = M.length;
        double[][] U = new double[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(M[i], 0, U[i], 0, n);
        }
        return U;
    }

    /**
     * Extrae el vector de términos independientes transformado b̃ desde M.
     *
     * @param M La matriz aumentada.
     * @return El vector b̃.
     */
    private static double[] extractModifiedRHS(double[][] M) {
        int n = M.length;
        double[] bMod = new double[n];

        for (int i = 0; i < n; i++) {
            bMod[i] = M[i][n];
        }
        return bMod;
    }
}