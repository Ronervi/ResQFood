package excepcion;

/**
 * Se lanza cuando un dato ingresado no es válido (vacío, fuera de rango, con formato incorrecto).
 * Es una excepción NO verificada porque la validan los constructores y setters de las clases del modelo.
 */
public class DatoInvalidoException extends IllegalArgumentException {
    
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
