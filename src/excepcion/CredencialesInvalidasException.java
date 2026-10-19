package excepcion;

/**
 * Se lanza cuando el email o la contraseña ingresados no son correctos.
 */
public class CredencialesInvalidasException extends ResQFoodException {
    
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
