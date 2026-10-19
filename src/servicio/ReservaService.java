package servicio;

import dao.PublicacionDAO;
import dao.ReservaDAO;
import excepcion.OperacionNoPermitidaException;
import excepcion.RecursoNoEncontradoException;
import excepcion.ResQFoodException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import modelo.Establecimiento;
import modelo.Estados;
import modelo.Publicacion;
import modelo.Reserva;
import modelo.Usuario;
import util.Validador;

/**
 * Reglas de negocio de las reservas.
 */
public class ReservaService {
    
    private final ReservaDAO reservaDAO = new ReservaDAO();
    private final PublicacionDAO publicacionDAO = new PublicacionDAO();
    
    public Reserva reservar(Usuario usuario, int idPublicacion, int cantidad) throws ResQFoodException {
        Validador.enteroPositivo(cantidad, "cantidad");
        
        Publicacion publicacion = publicacionDAO.buscarPorId(idPublicacion);
        if (publicacion == null) {
            throw new RecursoNoEncontradoException("La publicación #" + idPublicacion + " no existe.");
        }
        if (!publicacion.estaDisponible()) {
            throw new OperacionNoPermitidaException("Esa publicación ya no está disponible.");
        }
        // Verificación previa del stock (lanza StockInsuficienteException si no alcanza)
        publicacion.descontarCantidad(cantidad);
        
        Reserva reserva = new Reserva(0, usuario.getId(), idPublicacion, "", cantidad,
        publicacion.getHorarioRetiro(), Estados.PENDIENTE);
        // La verificación definitiva y el descuento real ocurren dentro de una transacción
        reserva.setIdReserva(reservaDAO.crearDescontandoStock(reserva));
        reserva.setNombrePublicacion(publicacion.getNombre());
        return reserva;
    }
    
    public void cancelar(Usuario usuario, int idReserva) throws ResQFoodException {
        Reserva reserva = reservaDAO.buscarPorId(idReserva);
        if (reserva == null) {
            throw new RecursoNoEncontradoException("La reserva #" + idReserva + " no existe.");
        }
        if (reserva.getIdUsuario() != usuario.getId()) {
            throw new OperacionNoPermitidaException("Solo podés cancelar tus propias reservas.");
        }
        reservaDAO.cancelarRestituyendoStock(idReserva);
    }
    
    public List<Reserva> historial(Usuario usuario) throws ResQFoodException {
        return reservaDAO.listarPorUsuario(usuario.getId());
    }
    
    public List<Reserva> pendientesDe(Establecimiento establecimiento) throws ResQFoodException {
        return reservaDAO.listarPorEstablecimiento(establecimiento.getId(), Estados.PENDIENTE);
    }
    
    /**
     * Cola (FIFO) de reservas pendientes: la primera en reservar es la primera en ser atendida
     */
    public Queue<Reserva> colaDeRetiros(Establecimiento establecimiento) throws ResQFoodException {
        Queue<Reserva> cola = new LinkedList<>();
        cola.addAll(pendientesDe(establecimiento));   // ya vienen ordenadas por fecha
        return cola;
    }
    
    public void confirmarRetiro(Establecimiento establecimiento, int idReserva) throws ResQFoodException {
        Reserva reserva = reservaDAO.buscarPorId(idReserva);
        if (reserva == null) {
            throw new RecursoNoEncontradoException("La reserva #" + idReserva + " no existe.");
        }
        Publicacion publicacion = publicacionDAO.buscarPorId(reserva.getIdPublicacion());
        if (publicacion == null || publicacion.getIdEstablecimiento() != establecimiento.getId()) {
            throw new OperacionNoPermitidaException("Esa reserva no corresponde a tu establecimiento.");
        }
        if (!reserva.estaPendiente()) {
            throw new OperacionNoPermitidaException(
            "Solo se puede confirmar el retiro de reservas pendientes (estado actual: "
            + reserva.getEstado() + ").");
        }
        reservaDAO.actualizarEstado(idReserva, Estados.RETIRADA);
    }
}
