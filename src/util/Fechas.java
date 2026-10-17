package util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utilidades para fechas.
 */
public final class Fechas {
    
    private static final DateTimeFormatter FECHA_HORA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    private Fechas() {
    }
    
    public static String hoy() {
        return LocalDate.now().toString();
    }
    
    /** Convierte un Timestamp de MySQL a texto legible; devuelve " " si es null. */
    public static String formatear(Timestamp marca) {
        return marca == null ? "" : marca.toLocalDateTime().format(FECHA_HORA);
    }
}
