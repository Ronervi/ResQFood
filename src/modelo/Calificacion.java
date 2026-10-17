package modelo;

import util.Validador;

/**
 * Valoración (1 a 5) que un usuario hace de un establecimiento.
 */
public class Calificacion {
    
    private int idCalificacion;
    private int idUsuario;
    private int idEstablecimiento;
    private int puntuacion;
    private String comentario;
    private String fecha;
    
    public Calificacion(int idCalificacion,
            int idUsuario, int idEstablecimiento,
            int puntuacion, String comentario, String fecha) {
        
        this.idCalificacion = idCalificacion;
        this.idUsuario = idUsuario;
        this.idEstablecimiento = idEstablecimiento;
        setPuntuacion(puntuacion);
        setComentario(comentario);
        this.fecha = fecha;
    }
    
    @Override
    public String toString() {
        String texto = comentario.isEmpty() ? "(sin comentario)" : comentario;
        return puntuacion + "/5 - " + texto + " [" + fecha + "]";
    }
    
    public int getIdCalificacion() {
        return idCalificacion;
    }
    
    public void setIdCalificacion(int idCalificacion) {
        this.idCalificacion = idCalificacion;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public int getIdEstablecimiento() {
        return idEstablecimiento;
    }
    
    public void setIdEstablecimiento(int idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }
    
    public int getPuntuacion() {
        return puntuacion;
    }
    
    public void setPuntuacion(int puntuacion) {
        this.puntuacion = Validador.rango(puntuacion, 1, 5, "puntuación");
    }
    
    public String getComentario() {
        return comentario;
    }
    
    public void setComentario(String comentario) {
        this.comentario = Validador.textoopcional(comentario, "comentario", 255);
    }
    
    public String getFecha() {
        return fecha;
    }
    
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
}
