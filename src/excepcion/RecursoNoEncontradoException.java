package excepcion;

/**
 * Se lanza cuando no existe el registro buscado (publicacion,
 * reserva, etc.).
 */
public class RecursoNoEncontradoException extends ResQFoodException {
    
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
