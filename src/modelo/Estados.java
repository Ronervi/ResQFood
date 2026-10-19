package modelo;

/**
 * Valores posibles del campo "estado" de las distintas entidades.
 */
public final class Estados {
    
    
    // Usuario y administradores
    public static final String ACTIVO = "Activo";
    public static final String BLOQUEADO = "Bloqueado";
    
    // Establecimientos (y reservas pendientes)
    public static final String PENDIENTE = "Pendiente";
    public static final String APROBADO = "Aprobado";
    public static final String RECHAZADO = "Rechazado";
    
    // Publicaciones
    public static final String DISPONIBLE = "Disponible";
    public static final String AGOTADA = "Agotada";
    
    // Reservas
    public static final String RETIRADA = "Retirada";
    public static final String CANCELADA = "Cancelada";
    
    private Estados() {
    }
}
