package excepcion;

/**
 * Excepción base (verificada) de las reglas de negocio y del acceso
 * Todas las excepciones propias del sistema que el programador debe"atender" heredan de esta clase.
 */
public class ResQFoodException extends Exception {
    
    public ResQFoodException(String mensaje) {
        super(mensaje);
    }
    
    public ResQFoodException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
