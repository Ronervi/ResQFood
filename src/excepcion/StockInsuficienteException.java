package excepcion;

/**
 * Se lanza cuando se intenta reservar más unidades de las disponibles.
 */
public class StockInsuficienteException extends ResQFoodException {
    
    private final int disponible;
    
    public StockInsuficienteException(int disponible) {
        super("No hay stock suficiente. Unidades disponibles: " + disponible);
        this.disponible = disponible;
    }
    
    public int getDisponible() {
        return disponible;
    }
}
