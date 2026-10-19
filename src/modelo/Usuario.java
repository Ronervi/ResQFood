package modelo;

import util.Validador;

/**
 * Persona que consulta y reserva alimentos excedentes.
 */
public class Usuario extends Cuenta {
    
    private String apellido;
    
    public Usuario(int idUsuario, String nombre, String apellido,
            String email, String contraseña, String telefono, String estado) {
        super(idUsuario, nombre, email, contraseña, telefono, estado);
        setApellido(apellido);
    }
    
    @Override
    public Rol getRol() {
        return Rol.USUARIO;
    }
    
    @Override
    public boolean estaHabilitada() {
        return Estados.ACTIVO.equals(getEstado());
    }
    
    @Override 
    protected int longitudMaximaNombre() {
        return 50;
    }
    
    @Override
    public String getNombreCompleto() {
        return getNombre() + " " + apellido;
    }
    
    public int getIdUsuario() {
        return getId();
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = Validador.texto(apellido, "apellido", 50);
    }
    
    @Override
    public String toString() {
        return "[#" + getId() + "] " + getNombreCompleto()
                + " | " + getEmail() + " | Estado: " + getEstado();
    }
}
