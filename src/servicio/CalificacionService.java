package servicio;

import dao.CalificacionDAO;
import dao.PublicacionDAO;
import dao.ReservaDAO;
import excepcion.OperacionNoPermitidaException;
import excepcion.RecursoNoEncontradoException;
import excepcion.ResQFoodException;
import java.util.List;
import modelo.Calificacion;
import modelo.Estados;
import modelo.Publicacion;
import modelo.Reserva;
import modelo.Usuario;

/**
 * Reglas de negocio de las calificaciones a establecimientos.
 */
public class CalificacionService {
    
    private final CalificacionDAO calificacionDAO = new CalificacionDAO();
    private final ReservaDAO reservaDAO = new ReservaDAO();
    private final PublicacionDAO publicacionDAO = new PublicacionDAO();
    
    /** Solo se puede calificar a partir de una reserva propia cuyo retiro ya fue confirmado. */
    public void calificar(Usuario usuario, int idReserva, int puntuacion, String comentario)
            throws ResQFoodException {
        
        Reserva reserva = reservaDAO.buscarPorId(idReserva);
        if (reserva == null) {
            throw new RecursoNoEncontradoException("La reserva #" + idReserva + " no existe.");
        }
        if (reserva.getIdUsuario() != usuario.getId()) {
            throw new OperacionNoPermitidaException("Solo podés calificar a partir de tus propias reservas.");
        }
        if (!Estados.RETIRADA.equals(reserva.getEstado())) {
            throw new OperacionNoPermitidaException(
            "Solo podés calificar reservas cuyo retiro ya fue confirmado.");
        }
        Publicacion publicacion = publicacionDAO.buscarPorId(reserva.getIdPublicacion());
        if (publicacion == null) {
            throw new RecursoNoEncontradoException("No se encontró la publicación de la reserva.");
        }
        // El contructor valida la puntuación (1 a 5) y el comentario
        Calificacion calificacion = new Calificacion(0, usuario.getId(),
        publicacion.getIdEstablecimiento(), puntuacion, comentario, "");
        calificacionDAO.insertar(calificacion);
    }
    
    public List<Calificacion> listarDe(int idEstablecimiento) throws ResQFoodException {
        return calificacionDAO.listarPorEstablecimiento(idEstablecimiento);
    }
    
    public double promedioDe(int idEstablecimiento) throws ResQFoodException {
        return calificacionDAO.promedio(idEstablecimiento);
    }
}
