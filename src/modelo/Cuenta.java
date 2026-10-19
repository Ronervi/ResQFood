package modelo;

import util.Validador;

/**
 * ABSTRACCIÓN: representa cualquier cuenta que puede iniciar sesión.
 * Reúne los datos comunes de Usuario, Establecimiento y Administrador.
 * No se puede instanciar directamente (es abstracta).
 */
public abstract class Cuenta {
    
    private int id;
    private String nombre;
    private String email;
    private String contraseña;  // se guarda el HASH, nunca el texto plano
    private String telefono;
    private String estado;
    
    public Cuenta(int id, String nombre, String email,
            String contraseña, String telefono, String estado) {
        this.id = id;
        setNombre(nombre);
        setEmail(email);
        this.contraseña = contraseña;
        setTelefono(telefono);
        setEstado(estado);
    }
    
    // ---- Métodos abstractos: cada subclase los implementa a su manera ----
    
    /** Rol con el que esta cuenta inicia sesión. */
    public abstract Rol getRol();
    
    /** Indica si la cuenta puede iniciar sesión según su estado actual. */
    public abstract boolean estaHabilitada();
    
    // ---- Métodos con comportamiento por defecto (pueden sobrescribirse) ----
    
    /** Longitud máxima permitida para el nombre (coincide con la columna de la BD). */
    protected int longitudMaximaNombre() {
        return 100;
    }
    
    public String getNombreCompleto() {
        return nombre;
    }
    
    // ---- Getters y setters (ENCAPSULAMIENTO) ----
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = Validador.texto(nombre, "nombre", longitudMaximaNombre());
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = Validador.email(email);
    }
    
    public String getContraseña() {
        return contraseña;
    }
    
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = Validador.textoopcional(telefono, "teléfono", 20);
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = Validador.texto(estado, "estado", 20);
    }
}
