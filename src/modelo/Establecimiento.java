package modelo;

import util.Validador;

/**
 * Comercio gastronómico que publica sus alimentos excedentes.
 */
public class Establecimiento extends Cuenta {
    
    private String direccion;
    private String ubicacion;
    
    public Establecimiento(int idEstablecimiento, String nombre,
            String direccion, String telefono, String email,
            String contraseña, String ubicacion, String estado) {
        super(idEstablecimiento, nombre, email, contraseña, telefono, estado);
        setDireccion(direccion);
        setUbicacion(ubicacion);
    }
    
    @Override
    public Rol getRol() {
        return Rol.ESTABLECIMIENTO;
    }
    
    /** Un establecimiento solo opera una vez que el administrador lo aprobó. */
    @Override
    public boolean estaHabilitada() {
        return Estados.APROBADO.equals(getEstado());
    }
    
    public int getIdEstablecimiento() {
        return getId();
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = Validador.texto(direccion, "dirección", 150);
    }
    
    public String getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(String ubicacion) {
        this.ubicacion = Validador.textoopcional(ubicacion, "ubicacion", 150);
    }
    
    @Override
    public String toString() {
        return "[#" + getId() + "] " + getNombre() + " | " + direccion
                + " | " + getEmail() + " | Estado: " + getEstado();
    }
}
