package excepcion;

/**
 * Se lanza cuando la operación viola una regla del negocio o de permisos.
 */
public class OperacionNoPermitidaException extends ResQFoodException {
    
    public OperacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}
