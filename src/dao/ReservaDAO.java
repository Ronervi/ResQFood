package dao;

import excepcion.AccesoDatosException;
import excepcion.OperacionNoPermitidaException;
import excepcion.RecursoNoEncontradoException;
import excepcion.StockInsuficienteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Estados;
import modelo.Reserva;
import util.Fechas;
import util.Validador;

/**
 * Acceso a la tabla Reserva. Las operaciones que tocan el stock usan transacciones.
 */
public class ReservaDAO {
    
    private static final String CONSULTA_BASE
            = "SELECT r.*, p.nombre AS nombrePublicacion "
            + "FROM Reserva r "
            + "JOIN Publicacion p ON r.idPublicacion = p.idPublicacion ";
    
    private Reserva mapear(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva(
                rs.getInt("idReserva"),
                rs.getInt("idUsuario"),
                rs.getInt("idPublicacion"),
                Fechas.formatear(rs.getTimestamp("fecha")),
                rs.getInt("cantidad"),
                rs.getString("horarioRetiro"),
                Validador.valorOPorDefecto(rs.getString("estado"), Estados.PENDIENTE));
        reserva.setNombrePublicacion(rs.getString("nombrePublicacion"));
        return reserva;
    }
    
    private List<Reserva> consultar(String condicion, Object... parametros) throws AccesoDatosException {
        List<Reserva> lista = new ArrayList<>();
        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(CONSULTA_BASE + condicion)) {
            for (int i = 0; i < parametros.length; i++) {
                sentencia.setObject(i + 1, parametros[i]);
            }
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al consultar reservas: " + e.getMessage(), e);
        }
    }
    
    /**
     * Registra la reserva y descuenta el stock de la publicación en UNA SOLA
     * transacción: o se hacen las dos cosas o no se hace ninguna.
     * Devuelve el id de la reserva creada.
     */
    public int crearDescontandoStock(Reserva reserva)
            throws AccesoDatosException, StockInsuficienteException, RecursoNoEncontradoException {
        
        try (Connection conexion = ConexionBD.conectar()) {
            conexion.setAutoCommit(false);
            try {
                int disponible = 0;
                String estadoPublicacion = null;
                
                //Bloquea la fila de la publicación para que nadie más la modifique a la vez
                try (PreparedStatement consulta = conexion.prepareStatement(
                "SELECT cantidad, estado FROM Publicacion WHERE idPublicacion = ? FOR UPDATE")) {
                    consulta.setInt(1, reserva.getIdPublicacion());
                    try (ResultSet rs = consulta.executeQuery()) {
                        if (!rs.next()) {
                            throw new RecursoNoEncontradoException(
                            "La publicación #" + reserva.getIdPublicacion() + " no existe.");
                        }
                        disponible = rs.getInt("cantidad");
                        estadoPublicacion = rs.getString("estado");
                    }
                }
                
                boolean habilitada = Estados.DISPONIBLE.equals(estadoPublicacion);
                if (!habilitada || disponible < reserva.getCantidad()) {
                    throw new StockInsuficienteException(habilitada ? disponible : 0);
                }
                
                int nuevaCantidad = disponible - reserva.getCantidad();
                String nuevoEstado = (nuevaCantidad == 0) ? Estados.AGOTADA : Estados.DISPONIBLE;
                try (PreparedStatement descuento = conexion.prepareStatement(
                "UPDATE Publicacion SET cantidad = ?, estado = ? WHERE idPublicacion = ?")) {
                    descuento.setInt(1, nuevaCantidad);
                    descuento.setString(2, nuevoEstado);
                    descuento.setInt(3, reserva.getIdPublicacion());
                    descuento.executeUpdate();
                }
                
                int idGenerado = 0;
                try (PreparedStatement insercion = conexion.prepareStatement(
                "INSERT INTO Reserva (idUsuario, idPublicacion, fecha, cantidad, horarioRetiro, estado) "
                + "VALUES (?, ?, NOW(), ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    insercion.setInt(1, reserva.getIdUsuario());
                    insercion.setInt(2, reserva.getIdPublicacion());
                    insercion.setInt(3, reserva.getCantidad());
                    insercion.setString(4, reserva.getHorarioRetiro());
                    insercion.setString(5, reserva.getEstado());
                    insercion.executeUpdate();
                    try (ResultSet claves = insercion.getGeneratedKeys()) {
                     if (claves.next()) {
                         idGenerado = claves.getInt(1);
                     }   
                    }
                }
                
                conexion.commit();
                return idGenerado;
                
            } catch (SQLException | StockInsuficienteException | RecursoNoEncontradoException e) {
                conexion.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al registrar la reserva: " + e.getMessage(), e);
        }
    }
    
    /**
     * Cancela una reserva pendiente y devuelve las unidades a la publicación,
     * también dentro de una única transacción.
     */
    public void cancelarRestituyendoStock(int idReserva)
            throws AccesoDatosException, OperacionNoPermitidaException, RecursoNoEncontradoException {
        
        try (Connection conexion = ConexionBD.conectar()) {
            conexion.setAutoCommit(false);
            try {
                int idPublicacion = 0;
                int cantidad = 0;
                String estado = null;
                
                try (PreparedStatement consulta = conexion.prepareStatement(
                "SELECT idPublicacion, cantidad, estado FROMReserva WHERE idReserva = ? FOR UPDATE")) {
                    consulta.setInt(1, idReserva);
                    try (ResultSet rs = consulta.executeQuery()) {
                        if (!rs.next()) {
                            throw new RecursoNoEncontradoException("La reserva #" + idReserva + " no existe.");
                        }
                        idPublicacion = rs.getInt("idPublicacion");
                        cantidad = rs.getInt("cantidad");
                        estado = rs.getString("estado");
                    }
                }
                
                if (!Estados.PENDIENTE.equals(estado)) {
                    throw new OperacionNoPermitidaException(
                    "Solo se pueden cancelar reservas pendientes (estado actual: " + estado + ").");
                }
                
                try (PreparedStatement cancelar = conexion.prepareStatement(
                "UPDATE Reserva SET estado = ? WHERE idReserva = ?")) {
                    cancelar.setString(1, Estados.CANCELADA);
                    cancelar.setInt(2, idReserva);
                    cancelar.executeUpdate();
                }
                
                try (PreparedStatement devolver = conexion.prepareStatement(
                "UPDATE Publicacion SET cantidad = cantidad + ?, estado = ? WHERE idPublicacion = ?")) {
                    devolver.setInt(1, cantidad);
                    devolver.setString(2, Estados.DISPONIBLE);
                    devolver.setInt(3, idPublicacion);
                    devolver.executeUpdate();
                }
                
                conexion.commit();
                
            } catch (SQLException | OperacionNoPermitidaException | RecursoNoEncontradoException e) {
                conexion.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al cancelar la reserva: " + e.getMessage(), e);
        }
    }
    
    public Reserva buscarPorId(int idReserva) throws AccesoDatosException {
        List<Reserva> lista = consultar("WHERE r.idReserva = ?", idReserva);
        return lista.isEmpty() ? null : lista.get(0);
    }
    
    public List<Reserva> listarPorUsuario(int idUsuario) throws AccesoDatosException {
        return consultar("WHERE r.idUsuario = ? ORDER BY r.idReserva DESC", idUsuario);
    }
    
    /** Reservas de las publicaciones de un establecimiento, de la más antigua a la más nueva. */
    public List<Reserva> listarPorEstablecimiento(int idEstablecimiento, String estado)
            throws AccesoDatosException {
        if (estado == null) {
            return consultar("WHERE p.idEstablecimiento = ? ORDER BY r.fecha, r.idReserva",
                    idEstablecimiento);
        }
        return consultar("WHERE p.idEstablecimiento = ? AND r.estado = ? ORDER BY r.fecha, r.idReserva",
                idEstablecimiento, estado);
    }
    
    public boolean actualizarEstado(int idReserva, String estado) throws AccesoDatosException {
        String sql = "UPDATE Reserva SET estado = ? WHERE idReserva = ?";
        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, estado);
            sentencia.setInt(2, idReserva);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar la reserva: " + e.getMessage(), e);
        }
    }
    
    public int contarPorPublicacion(int idPublicacion) throws AccesoDatosException {
        String sql = "SELECT COUNT(*) FROM Reserva WHERE idPublicacion = ?";
        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idPublicacion);
            try (ResultSet rs = sentencia.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al contar reservas: " + e.getMessage(), e);
        }
    }
}
