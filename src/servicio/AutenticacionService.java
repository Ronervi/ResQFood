package servicio;

import dao.AdministradorDAO;
import dao.EstablecimientoDAO;
import dao.UsuarioDAO;
import excepcion.CredencialesInvalidasException;
import excepcion.OperacionNoPermitidaException;
import excepcion.ResQFoodException;
import modelo.Cuenta;
import modelo.Establecimiento;
import modelo.Estados;
import modelo.Rol;
import modelo.Usuario;
import util.Seguridad;
import util.Validador;

/**
 * Registro e inicio de sesión de usuarios, establecimientos y administradores.
 */
public class AutenticacionService {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final EstablecimientoDAO establecimientoDAO = new EstablecimientoDAO();
    private final AdministradorDAO administradorDAO = new AdministradorDAO();
    
    public Usuario registrarUsuario(String nombre, String apellido, String email,
            String contraseña, String telefono) throws ResQFoodException {
        
        Validador.contrasena(contraseña);
        // El constructor valida el resto de los datos (lanza DatoInvalidoException)
        Usuario usuario = new Usuario(0, nombre, apellido, email,
        Seguridad.hasher(contraseña), telefono, Estados.ACTIVO);
        
        if (usuarioDAO.existeEmail(usuario.getEmail())) {
            throw new OperacionNoPermitidaException("Ya existe un usuario registrado con ese email.");
        }
        usuario.setId(usuarioDAO.registrar(usuario));
        return usuario;
    }
    
    /** El establecimiento queda "Pendiente" hasta que el administrador lo apruebe. */
    public Establecimiento registrarEstablecimiento(String nombre, String direccion, String telefono,
            String email, String contraseña, String ubicacion) throws ResQFoodException {
        
        Validador.contrasena(contraseña);
        Establecimiento establecimiento = new Establecimiento(0, nombre, direccion, telefono, email,
        Seguridad.hasher(contraseña), ubicacion, Estados.PENDIENTE);
        
        if (establecimientoDAO.existeEmail(establecimiento.getEmail())) {
            throw new OperacionNoPermitidaException("Ya existe un establecimiento registrado con ese email.");
        }
        establecimiento.setId(establecimientoDAO.registrar(establecimiento));
        return establecimiento;
    }
    
    /**
     * Inicia sesión según el rol elegido. Devuelve una Cuenta: el objeto real
     * Será un Usuario, un Establecimiento o un Administrador (POLIMORFISMO).
     */
    public Cuenta iniciarSesion(Rol rol, String email, String contraseña) throws ResQFoodException {
        String emailLimpio = (email == null) ? "" : email.trim().toLowerCase();
        
        Cuenta cuenta;
                switch (rol) {
                    case USUARIO:
                        cuenta = usuarioDAO.buscarPorEmail(emailLimpio);
                        break;
                    case ESTABLECIMIENTO:
                        cuenta = establecimientoDAO.buscarPorEmail(emailLimpio);
                        break;
                    case ADMINISTRADOR:
                        cuenta = administradorDAO.buscarPorEmail(emailLimpio);
                        break;
                    default:
                        cuenta = null;
                }
                
                if (cuenta == null || !Seguridad.verificar(contraseña, cuenta.getContraseña())) {
                    throw new CredencialesInvalidasException("Email o contraseña incorrectos.");
                }
                if (!cuenta.estaHabilitada()) {
                    throw new OperacionNoPermitidaException(
                    "Tu cuenta no está habilitada para ingresar (estado: " + cuenta.getEstado() + ").");
                }
                return cuenta;
    }
}
