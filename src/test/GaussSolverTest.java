package test;

import exception.MatrixException;
import logic.GaussSolver;
import logic.ResultadoGauss;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para GaussSolver.
 * Prueba la resolución de sistemas de ecuaciones lineales 4x4
 * utilizando el método de eliminación gaussiana con pivotaje parcial escalado.
 *
 * @author Anabel Diaz
 * @version 1.0 - 22/11/2025
 */
class GaussSolverTest {

    /**
     * Prueba la resolución de un sistema 4x4 con solución única.
     * CE-01: Sistema regular.
     * CE-03: Sin pivotaje.
     *
     * @throws MatrixException
     */
	@Test
	void solves4x4SystemWithUniqueSolution() throws MatrixException {
	    double[][] A = {
	        {4, 1, 0, 0},
	        {1, 4, 1, 0},
	        {0, 1, 4, 1},
	        {0, 0, 1, 3}
	    };
	    double[] b = {6, 12, 18, 15};

	    ResultadoGauss result = GaussSolver.solveGaussian(A, b);

	    assertArrayEquals(new double[]{1, 2, 3, 4}, result.getX(), 1e-6);
	}

    /**
     * Prueba la resolución de un sistema 4x4 con matriz diagonal.
     * CE-02: Matriz diagonal.
     *
     * @throws MatrixException
     */
    @Test
    void solvesDiagonal4x4System() throws MatrixException {
        double[][] A = {
                {1, 0, 0, 0},
                {0, 2, 0, 0},
                {0, 0, 3, 0},
                {0, 0, 0, 4}
        };
        double[] b = {1, 4, 9, 8};

        ResultadoGauss result = GaussSolver.solveGaussian(A, b);

        assertArrayEquals(new double[]{1, 2, 3, 2}, result.getX(), 1e-6);
    }

    /**
     * Prueba la resolución de un sistema 4x4 con valores negativos.
     * CE-03: Comprueba rendimiento con valores negativos.
     * @throws MatrixException
     */
    @Test
    void solves4x4SystemWithNegativeValues() throws MatrixException {
        double[][] A = {
                {-5, 1, 0, 0},
                {1, -6, 1, 0},
                {0, 1, -7, 1},
                {0, 0, 1, -8}
        };
        double[] b = {-6, 9, -17, 18};

        ResultadoGauss result = GaussSolver.solveGaussian(A, b);

        assertArrayEquals(new double[]{1, -1, 2, -2}, result.getX(), 1e-6);
    }

    /**
     * Prueba la resolución de un sistema 4x4 con pivote pequeño
     * utilizando pivotaje parcial escalado.
     * CE-04: Pivote pequeño con pivotaje parcial escalado.
     *
     * @throws MatrixException
     */
    @Test
    void handlesSmallPivotWithScaledPartialPivoting() throws MatrixException {
        double[][] A = {
                {1e-12, 1, 0, 0},
                {1,     4, 1, 0},
                {0,     1, 4, 1},
                {0,     0, 1, 3}
        };
        double[] b = {2 + 1e-12, 12, 18, 15};

        ResultadoGauss result = GaussSolver.solveGaussian(A, b);

        assertArrayEquals(new double[]{1, 2, 3, 4}, result.getX(), 1e-6);
    }

    /**
     * Prueba que se lanza una excepción para una matriz casi singular.
     *
     * CE-05: Matriz casi singular
     */
    void throwsExceptionForNearlySingularPivot() {
        double tiny = 1e-16;

        double[][] A = {
                {tiny, tiny,    0,    0},
                {0,    tiny, tiny,    0},
                {0,       0, tiny, tiny},
                {0,       0,    0, tiny}
        };
        double[] b = {1, 1, 1, 1};

        assertThrows(MatrixException.class, () -> GaussSolver.solveGaussian(A, b));
    }

    /**
     * Prueba que se lanza una excepción para un sistema 4x4 singular.
     *
     * CE-06: Sistema singular.
     */
    @Test
    void throwsExceptionForSingular4x4Matrix() {
        double[][] A = {
                {1, 2, 3, 4},
                {2, 4, 6, 8},
                {3, 6, 9, 12},
                {4, 8, 12, 16}
        };
        double[] b = {10, 20, 30, 40};

        assertThrows(MatrixException.class, () -> GaussSolver.solveGaussian(A, b));
    }

    /**
     * Prueba que se lanza una excepción para un sistema 4x4 con una fila nula.
     *
     * CE-06: Sistema singular.
     */
    @Test
    void throwsExceptionForZeroRowIn4x4Matrix() {
        double[][] A = {
                {4, 1, 0, 0},
                {1, 4, 1, 0},
                {0, 0, 0, 0},
                {0, 0, 1, 3}
        };
        double[] b = {6, 12, 0, 15};

        assertThrows(MatrixException.class, () -> GaussSolver.solveGaussian(A, b));
    }



    /**
     * Prueba que se lanza una excepción para dimensiones incompatibles entre A y b.
     *
     * CE-07: Dimensiones incompatibles.
     */
	@Test
	void throwsExceptionForMismatchedDimensionsBetweenAandB() {
	    double[][] A = {
	        {4, 1, 0, 0},
	        {1, 4, 1, 0},
	        {0, 1, 4, 1},
	        {0, 0, 1, 3}
	    };
	    double[] b = {6, 12, 18}; // longitud incorrecta

	    assertThrows(MatrixException.class, () -> GaussSolver.solveGaussian(A, b));
	}

}

