package modelo;

/**
 * Responsable de modelar la plataforma.
 */
public class Administrador extends Cuenta {
    
    
    public Administrador(int idAdministrador, String nombre,
            String email, String contraseña, String estado) {
        super(idAdministrador, nombre, email, contraseña, "", estado);
    }
    
    @Override
    public Rol getRol() {
        return Rol.ADMINISTRADOR;
    }
    
    @Override
    public boolean estaHabilitada() {
        return Estados.ACTIVO.equals(getEstado());
    }
}
