package modelo;

import util.Validador;

/**
 * Reserva de una publicacion realizada por un usuario. 
 */
public class Reserva {
    
    private int idReserva;
    private int idUsuario;
    private int idPublicacion;
    private String fecha;
    private int cantidad;
    private String horarioRetiro;
    private String estado;
    // Dato informativo que completa la consulta con JOIN (no es una columna de Reserva).
    private String nombrePublicacion;
    
    public Reserva(int idReserva,
            int idUsuario, int idPublicacion,
            String fecha, int cantidad,
            String horarioRetiro, String estado) {
        
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.idPublicacion = idPublicacion;
        this.fecha = fecha;
        setCantidad(cantidad);
        setHorarioRetiro(horarioRetiro);
        setEstado(estado);
    }
    
    public boolean estaPendiente() {
        return Estados.PENDIENTE.equals(estado);
    }
    
    public String resumen() {
        String nombre = (nombrePublicacion == null) ? "publicacion #" + idPublicacion : nombrePublicacion;
        return String.format("[#%d] %s x%d | Retiro: %s | Estado: %s | Fecha: %s",
                idReserva, nombre, cantidad, horarioRetiro, estado, fecha);
    }
    
    @Override
    public String toString() {
        return resumen();
    }
    
    public int getIdReserva() {
        return idReserva;
    }
    
    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }
    
   public int getIdUsuario() {
       return idUsuario;
   }
   
   public void setIdUsuario(int idUsuario) {
       this.idUsuario = idUsuario;
   }
   
   public int getIdPublicacion() {
       return idPublicacion;
   }
   
   public void setIdPublicacion(int idPublicacion) {
       this.idPublicacion = idPublicacion;
   }
   
   public String getFecha() {
       return fecha;
   }
   
   public void setFecha(String fecha) {
       this.fecha = fecha;
   }
   
   public int getCantidad() {
       return cantidad;
   }
   
   public void setCantidad(int cantidad) {
       this.cantidad = Validador.enteroPositivo(cantidad, "cantidad");
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
   
   public String getNombrePublicacion() {
       return nombrePublicacion;
   }
   
   public void setNombrePublicacion(String nombrePublicacion) {
       this.nombrePublicacion = nombrePublicacion;
   }
}
