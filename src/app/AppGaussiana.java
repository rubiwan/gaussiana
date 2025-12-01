package app;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;
import config.LoggerFichero;
import controller.GaussController;
import view.VentanaPrincipal;

/**
 * Clase principal para iniciar la aplicación de matrices gaussianas.
 * Ejecuta la interfaz gráfica y el controlador.
 * Realiza el cierre del log al cerrar la ventana.
 *
 * @author Anabel Diaz
 * @version 1.0 - 22/11/2025
 */
public class AppGaussiana {

	private static final LoggerFichero log = LoggerFichero.getInstance();
	
    public static void main(String[] args) {

        try {
            log.info("Inicio del sistema");
            SwingUtilities.invokeLater(() -> {
                VentanaPrincipal vista = new VentanaPrincipal();
                new GaussController(vista);
                vista.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        log.info("Cierre de la aplicación - cerrando log");
                        log.closeLog();
                    }
                });
                vista.setVisible(true);
            });
        } catch (Exception e) {
            log.error("Error al inicializar el sistema", e);
            try {
                log.closeLog();
            } catch (Exception ex) {
                log.error("Error al cerrar recurso", e);
            }
        }
    }
}
