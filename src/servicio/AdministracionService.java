package servicio;

import dao.EstablecimientoDAO;
import dao.UsuarioDAO;
import excepcion.RecursoNoEncontradoException;
import excepcion.ResQFoodException;
import java.util.List;
import modelo.Establecimiento;
import modelo.Estados;
import modelo.Usuario;

/**
 * Funciones de moderación del administrador.
 */
public class AdministracionService {
    
    private final EstablecimientoDAO establecimientoDAO = new EstablecimientoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    public List<Establecimiento> listarPendientes() throws ResQFoodException {
        return establecimientoDAO.listarPorEstado(Estados.PENDIENTE);
    }
    
    public void aprobarEstablecimiento(int idEstablecimiento) throws ResQFoodException {
        cambiarEstadoEstablecimiento(idEstablecimiento, Estados.APROBADO);
    }
    
    public void rechazarEstablecimiento(int idEstablecimiento) throws ResQFoodException {
        cambiarEstadoEstablecimiento(idEstablecimiento, Estados.RECHAZADO);
    }
    
    private void cambiarEstadoEstablecimiento(int id, String estado) throws ResQFoodException {
        if (!establecimientoDAO.actualizarEstado(id, estado)) {
            throw new RecursoNoEncontradoException("El establecimiento #" + id + " no existe.");
        }
    }
    
    public List<Usuario> listarUsuarios() throws ResQFoodException {
        return usuarioDAO.listar();
    }
    
    public void bloquearUsuario(int idUsuario) throws ResQFoodException {
        cambiarEstadoUsuario(idUsuario, Estados.BLOQUEADO);
    }
    
    public void activarUsuario(int idUsuario) throws ResQFoodException {
        cambiarEstadoUsuario(idUsuario, Estados.ACTIVO);
    }
    
    private void cambiarEstadoUsuario(int id, String estado) throws ResQFoodException {
        if (!usuarioDAO.actualizarEstado(id, estado)) {
            throw new RecursoNoEncontradoException("El usuario #" + id + " no existe.");
        }
    }
}
