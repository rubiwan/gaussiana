package exception;

/**
 * Excepción personalizada para errores relacionados con la entrada de datos.
 *
 * @author Anabel Diaz
 * @version 1.0 - 22/11/2025
 */
public class InputException extends Exception {

	public InputException(String message) {
        super(message);
    }
}
