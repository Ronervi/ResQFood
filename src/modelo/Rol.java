package modelo;

/**
 * Roles con los que se puede iniciar sesión en el sistema.
 */
public enum Rol {
    USUARIO("Usuario"),
    ESTABLECIMIENTO("Establecimiento"),
    ADMINISTRADOR("Administrador");
    
    private final String descripcion;
    
    Rol(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
