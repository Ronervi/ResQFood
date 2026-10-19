
package modelo;

/**
 * Publicación con un porcentaje de descuento sobre el precio original.
 */
public class PublicacionDescuento extends Publicacion {
    
    public PublicacionDescuento(int idPublicacion,
            int idEstablecimiento,
            String nombre,
            String descripcion,
            int cantidad,
            double precio,
            double descuento,
            String fechaVencimiento,
            String fechaPublicacion,
            String horarioRetiro,
            String estado) {
        
        super(idPublicacion, idEstablecimiento, nombre,
                descripcion, cantidad, precio, descuento,
                fechaVencimiento, fechaPublicacion,
                horarioRetiro, estado);
    }
    
    @Override
    public double calcularPrecioFinal() {
        return getPrecio() - (getPrecio() * getDescuento() / 100);
    }
    
    @Override
    public String getTipo() {
        return "Con descuento";
    }
}
