package modelo;

/**
 * Publicación sin costo para el usuario (descuento del 100%).
 */
public class PublicacionGratuita extends Publicacion {
    
    public PublicacionGratuita(int idPublicacion,
            int idEstablecimiento, String nombre,
            String descripcion,
            int cantidad,
            String fechaVencimiento, String fechaPublicacion,
            String horarioRetiro, String estado) {
        
        super(idPublicacion, idEstablecimiento, nombre,
                descripcion, cantidad, 0, 100,
                fechaVencimiento, fechaPublicacion,
                horarioRetiro, estado);
    }
    
    @Override
    public double calcularPrecioFinal() {
        return 0;
    }
    
    @Override
    public String getTipo() {
        return "Gratuita";
    }
}
