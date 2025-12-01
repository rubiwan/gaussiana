package controller;

import exception.InputException;
import exception.MatrixException;
import logic.GaussSolver;
import logic.ResultadoGauss;
import logic.Sistema;
import view.VentanaPrincipal;

import javax.swing.*;

import config.LoggerFichero;

/**
 * Controlador para la aplicación de resolución de sistemas de ecuaciones
 * mediante el método de eliminación gaussiana.
 *
 * @author Anabel Diaz
 * @version 1.0 - 22/11/2025
 */
public class GaussController {

    private final VentanaPrincipal view;
    private static final LoggerFichero log = LoggerFichero.getInstance();

    /**
     * Constructor del controlador.
     *
     * @param view La vista principal de la aplicación.
     */
    public GaussController(VentanaPrincipal view) {
        this.view = view;
        initController();
    }

    /**
     * Inicializa los listeners de los botones en la vista.
     */
    private void initController() {
        view.addCalcularListener(e -> onCalcular());
        view.addBorrarListener(e -> onBorrar());
        view.addCargarListener(e -> onCargar());
        view.addMostrarListener(e -> onMostrar());

        log.info("Controlador inicializado correctamente");
    }

    /**
     * Maneja el evento de cálculo del sistema de ecuaciones.
     */
    private void onCalcular() {
        try {
            Sistema sistema = view.leerSistema();
            ResultadoGauss res = GaussSolver.solve(sistema);

            view.mostrarResultados(res.getX());
            view.mostrarMatrizTriangular(res.getU(), res.getbMod());

        } catch (InputException ex) {
            log.error("Error de entrada de datos", ex);
            view.mostrarMensaje(ex.getMessage(), "Error en los datos", JOptionPane.WARNING_MESSAGE);
            view.limpiarResultados();

        } catch (MatrixException ex) {
            log.error("Error en el cálculo del sistema", ex);
            view.mostrarMensaje(ex.getMessage(), "Error de cálculo", JOptionPane.ERROR_MESSAGE);
            view.limpiarResultados();
        }
    }

    /**
     * Maneja el evento de borrado de los campos de entrada.
     */
    private void onBorrar() {
        view.limpiarCampos();
    }

    /**
     * Maneja el evento de carga de un ejemplo predefinido.
     */
    private void onCargar() {
        double[][] A = view.getEjemploA();
        double[] b = view.getEjemploB();
        view.cargarEjemplo(A, b);
    }

    /**
     * Maneja el evento de mostrar el sistema de ecuaciones introducido.
     */
    private void onMostrar() {
        try {
            Sistema sistema = view.leerSistema();
            double[][] A = sistema.getA();
            double[] b = sistema.getB();

            StringBuilder sb = new StringBuilder();
            int n = A.length;
            for (int i = 0; i < n; i++) {
                sb.append("Ecuación ").append(i+1).append(": ");

                for (int j = 0; j < n; j++) {
                    double val = A[i][j];
                    if (j == 0) {
                        sb.append(val).append("x").append(j+1);
                    } else if (val >= 0) {
                        sb.append(" + ").append(val).append("x").append(j+1);
                    } else {
                        sb.append(" - ").append(Math.abs(val)).append("x").append(j+1);
                    }
                }

                sb.append(" = ").append(b[i]).append("\n");
            }

            view.mostrarMensaje(sb.toString(), "Sistema introducido", JOptionPane.INFORMATION_MESSAGE);

        } catch (InputException ex) {
            log.error("Error de entrada de datos al mostrar el sistema", ex);
            view.mostrarMensaje(ex.getMessage(), "Error en los datos", JOptionPane.WARNING_MESSAGE);
        }
    }

}

