package dao;

import excepcion.AccesoDatosException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Estados;
import modelo.Publicacion;
import modelo.PublicacionDescuento;
import modelo.PublicacionGratuita;
import util.Fechas;
import util.Validador;

/**
 * Acceso a la tabla Publicacion.
 */
public class PublicacionDAO {
    
    private static final String CONSULTA_BASE
            = "SELECT p.*, e.nombre AS nombreEstablecimiento "
            + "FROM Publicacion p " 
            + "JOIN Establecimiento e ON p.idEstablecimiento = e.idEstablecimiento ";
    
    /**
     * Convierte una fila en el objeto del tipo correcto (POLIMORFISMO):
     * si el precio es 0 o el descuento es 100 es gratuita; si no, con descuento.
     */
    private Publicacion mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("idPublicacion");
        int idEstablecimiento = rs.getInt("idEstablecimiento");
        String nombre = rs.getString("nombre");
        String descripcion = rs.getString("descripcion");
        int cantidad = rs.getInt("cantidad");
        double precio = rs.getDouble("precio");
        double descuento = rs.getDouble("descuento");
        Date vencimiento = rs.getDate("fechaVencimiento");
        String fechaVencimiento = (vencimiento == null) ? "" : vencimiento.toString();
        String fechaPublicacion = Fechas.formatear(rs.getTimestamp("fechaPublicacion"));
        String horario = rs.getString("horarioRetiro");
        String estado = Validador.valorOPorDefecto(rs.getString("estado"), Estados.DISPONIBLE);
        
        Publicacion publicacion;
        if (precio == 0 || descuento >= 100) {
            publicacion = new PublicacionGratuita(id, idEstablecimiento, nombre, descripcion,
            cantidad, fechaVencimiento, fechaPublicacion, horario, estado);
        } else {
            publicacion = new PublicacionDescuento(id, idEstablecimiento, nombre, descripcion,
            cantidad, precio, descuento, fechaVencimiento, fechaPublicacion, horario, estado);
        }
        publicacion.setNombreEstablecimiento(rs.getString("nombreEstablecimiento"));
        return publicacion;
    }
    
    private List<Publicacion> consultar(String condicion, Object... parametros)
            throws AccesoDatosException {
        List<Publicacion> lista = new ArrayList<>();
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
            throw new AccesoDatosException("Error al consultar publicaciones: " + e.getMessage(), e);
        }
    }
    
    /** Inserta la publicación y devuelve el id generado. */
    public int insertar(Publicacion p) throws AccesoDatosException {
        String sql = "INSERT INTO Publicacion (idEstablecimiento, nombre, descripcion, cantidad, "
                + "precio, descuento, fechaVencimiento, fechaPublicacion, horarioRetiro, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?)";
        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia
                        = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            sentencia.setInt(1, p.getIdEstablecimiento());
            sentencia.setString(2, p.getNombre());
            sentencia.setString(3, p.getDescripcion());
            sentencia.setInt(4, p.getCantidad());
            sentencia.setDouble(5, p.getPrecio());
            sentencia.setDouble(6, p.getDescuento());
            sentencia.setDate(7, Date.valueOf(p.getFechaVencimiento()));
            sentencia.setString(8, p.getHorarioRetiro());
            sentencia.setString(9, p.getEstado());
            sentencia.executeUpdate();
            
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                return claves.next() ? claves.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar la publicación: " + e.getMessage(), e);
        }
    }
    
   public Publicacion buscarPorId(int idPublicacion) throws AccesoDatosException {
       List<Publicacion> lista = consultar("WHERE p.idPublicacion = ?", idPublicacion);
       return lista.isEmpty() ? null : lista.get(0);
   }
   
   /** Publicaciones que se pueden reservar hoy, ordenadas por id. */
   public List<Publicacion> listarDisponibles() throws AccesoDatosException {
       return consultar("WHERE p.estado = ? AND p.cantidad > 0 AND p.fechaVencimiento >= CURDATE() "
       + "AND e.estado = ? ORDER BY p.idPublicacion", Estados.DISPONIBLE, Estados.APROBADO);
   }
   
   public List<Publicacion> listarPorEstablecimiento(int idEstablecimiento) throws AccesoDatosException {
       return consultar("WHERE p.idEstablecimiento = ? ORDER BY p.idPublicacion", idEstablecimiento);
   }
   
   public List<Publicacion> listarTodas() throws AccesoDatosException {
       return consultar("ORDER BY p.idPublicacion");
   }
   
   public boolean actualizar(Publicacion p) throws AccesoDatosException {
       String sql = "UPDATE Publicacion SET nombre = ?, descripcion = ?, cantidad = ?, precio = ?, "
               + "descuento = ?, fechaVencimiento = ?, horarioRetiro = ?, estado = ? "
               + "WHERE idPublicacion = ?";
       try (Connection conexion = ConexionBD.conectar();
               PreparedStatement sentencia = conexion.prepareStatement(sql)) {
           sentencia.setString(1, p.getNombre());
           sentencia.setString(2, p.getDescripcion());
           sentencia.setInt(3, p.getCantidad());
           sentencia.setDouble(4, p.getPrecio());
           sentencia.setDouble(5, p.getDescuento());
           sentencia.setDate(6, Date.valueOf(p.getFechaVencimiento()));
           sentencia.setString(7, p.getHorarioRetiro());
           sentencia.setString(8, p.getEstado());
           sentencia.setInt(9, p.getIdPublicacion());
           return sentencia.executeUpdate() > 0;
       } catch (SQLException e) {
           throw new AccesoDatosException("Error al actualizar la publicación: " + e.getMessage(), e);
       }
   }
   
   public boolean eliminar(int idPublicacion) throws AccesoDatosException {
       String sql = "DELETE FROM Publicacion WHERE idPublicacion = ?";
       try (Connection conexion = ConexionBD.conectar();
               PreparedStatement sentencia = conexion.prepareStatement(sql)) {
           sentencia.setInt(1, idPublicacion);
           return sentencia.executeUpdate() > 0;
       } catch (SQLException e) {
           throw new AccesoDatosException("Error al eliminar la publicación: " + e.getMessage(), e);
       }
   }
}
