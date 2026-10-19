package modelo;

import excepcion.StockInsuficienteException;
import java.time.LocalDate;
import util.Fechas;
import util.Validador;

/**
 * ABSTRACCIÓN: alimento excedente publicado por un establecimiento.
 * Cada tipo de publicación calcula su precio final de forma distinta.
 * (POLIMORFISMO), por eso calcularPrecioFinal() es abstracto.
 */
public abstract class Publicacion {
    
    private int idPublicacion;
    private int idEstablecimiento;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private double precio;
    private double descuento;
    private String fechaVencimiento;
    private String fechaPublicacion;
    private String horarioRetiro;
    private String estado;
    // Dato impormativo que completa la consulta del JOIN (no es una columna de publicación).
    private String nombreEstablecimiento;
    
    public Publicacion(int idPublicacion,
            int idEstablecimiento, String nombre,
            String descripcion, int cantidad,
            double precio, double dewscuento,
            String fechaVencimiento, String fechaPublicacion,
            String horarioRetiro, String estado) {
        
        this.idPublicacion = idPublicacion;
        this.idEstablecimiento = idEstablecimiento;
        setNombre(nombre);
        setDescripcion(descripcion);
        setCantidad(cantidad);
        setPrecio(precio);
        setDescuento(descuento);
        setFechaVencimiento(fechaVencimiento);
        this.fechaPublicacion = fechaPublicacion;
        setHorarioRetiro(horarioRetiro);
        setEstado(estado);
    }
    
    // ---- Métodos abstractos ----
    
    public abstract double calcularPrecioFinal();
    
    public abstract String getTipo();
    
    // ---- Lógica propia de la publicación ----
    
    /** Reduce el stock al reservar.  Lanza excepción si no alcanza. */
    public void descontarCantidad(int unidades) throws StockInsuficienteException {
        if (unidades > cantidad) {
            throw new StockInsuficienteException(cantidad);
        }
        cantidad -= unidades;
        if (cantidad == 0) {
            estado = Estados.AGOTADA;
        } 
    }
    
    public boolean estaVencida() {
        if (fechaVencimiento == null || fechaVencimiento.isEmpty()) {
            return false;
        }
        return LocalDate.parse(fechaVencimiento).isBefore(LocalDate.parse(Fechas.hoy()));
    }
    
    public boolean estaDisponible() {
        return Estados.DISPONIBLE.equals(estado) && cantidad > 0 && !estaVencida();
    }
    
    /** Descripción en una o dos líneas para mostrar por consola. */
    public String resumen() {
        String precios;
        if (calcularPrecioFinal() == 0) {
            precios = "GRATIS";
        } else {
            precios = String.format("$%.2f -> $%.2f (%.0f%% off)",
                    precio, calcularPrecioFinal(), descuento);
        }
        String origen = (nombreEstablecimiento == null) ? "" : " - " + nombreEstablecimiento;
        return String.format("[#%d] %s (%s)%s%n      Cantidad: %d | %s | Vence: %s | Retiro: %s | Estado: %s",
                idPublicacion, nombre, getTipo(), origen,
                cantidad, precios, fechaVencimiento, horarioRetiro, estado);
    }
    
    @Override
    public String toString() {
        return resumen();
    }
    
    // ---- Getters y setters (ENCAPSULAMIENTO, con validaciones) ----
    
    public int getIdPublicacion() {
        return idPublicacion;
    }
    
   public void setIdPublicacion(int idPublicacion) {
       this.idPublicacion = idPublicacion;
   }
   
   public int getIdEstablecimiento() {
       return idEstablecimiento;
   }
   
   public void setIdestablecimiento(int idEstablecimiento) {
       this.idEstablecimiento = idEstablecimiento;
   }
   
   public String getNombre() {
       return nombre;
   }
   
   public void setNombre(String nombre) {
       this.nombre = Validador.texto(nombre, "nombre del alimento", 100);
   }
   
   public String getDescripcion() {
       return descripcion;
   }
   
   public void setDescripcion(String descripcion) {
       this.descripcion = descripcion == null ? "" : descripcion.trim();
   }
   
   public int getCantidad() {
       return cantidad;
   }
   
   public void setCantidad(int cantidad) {
       this.cantidad = Validador.enteroNoNegativo(cantidad, "cantidad");
   }
   
   public double getPrecio() {
       return precio;
   }
   
   public void setPrecio(double precio) {
       this.precio = Validador.decimalNoNegativo(precio, "precio");
   }
   
   public double getDescuento() {
       return descuento;
   }
   
   public void setDescuento(double descuento) {
           this.descuento = Validador.porcentaje(descuento, "decuento");
   }
   
   public String getFechaVencimiento() {
       return fechaVencimiento;
   }
   
   public void setFechaVencimiento(String fechaVencimiento) {
       if (fechaVencimiento != null && !fechaVencimiento.trim().isEmpty()) {
           Validador.fecha(fechaVencimiento, "fecha de vencimiento");
       }
       this.fechaVencimiento = fechaVencimiento == null ? "" : fechaVencimiento.trim();
   }
   
   public String getFechaPublicacion() {
       return fechaPublicacion;
   }
   
   public void setFechaPublicacion(String fechaPublicacion) {
       this.fechaPublicacion = fechaPublicacion;
   }
   
   public String getHorarioRetiro() {
       return horarioRetiro;
   }
   
   public void setHorarioRetiro(String horarioRetiro) {
       this.horarioRetiro = Validador.texto(horarioRetiro, "horario de retiro", 50);
   }
   
   public String getEstado() {
       return estado;
   }
   
   public void setEstado(String estado) {
       this.estado = Validador.texto(estado, "estado", 20);
   }
   
   public String getNombreEstablecimiento() {
       return nombreEstablecimiento;
   }
   
   public void setNombreEstablecimiento(String nombreEstablecimiento) {
       this.nombreEstablecimiento = nombreEstablecimiento;
   }
}
