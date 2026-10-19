package excepcion;

/**
 * Se lanza cuando falla una operación contra la base de datos MySQL.
 */
public class AccesoDatosException extends ResQFoodException {
    
    public AccesoDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
