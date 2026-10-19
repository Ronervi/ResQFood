package dao;

import excepcion.AccesoDatosException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Establecimiento;
import modelo.Estados;
import util.Validador;

/**
 * Acceso a la table Establecimiento.
 */
public class EstablecimientoDAO {
    
    private Establecimiento mapear(ResultSet rs) throws SQLException {
        return new Establecimiento(
        rs.getInt("idEstablecimiento"),
        rs.getString("nombre"),
        rs.getString("direccion"),
        rs.getString("telefono"),
        rs.getString("email"),
        rs.getString("contraseña"),
        rs.getString("ubicacion"),
        Validador.valorOPorDefecto(rs.getString("estado"), Estados.PENDIENTE));
    }
    
    public int registrar(Establecimiento e) throws AccesoDatosException {
        String sql = "INSERT INTO Establecimiento "
                + "(nombre, direccion, telefono, email, contraseña, ubicacion, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia
                        = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            sentencia.setString(1, e.getNombre());
            sentencia.setString(2, e.getDireccion());
            sentencia.setString(3, e.getTelefono());
            sentencia.setString(4, e.getEmail());
            sentencia.setString(5, e.getContraseña());
            sentencia.setString(6, e.getUbicacion());
            sentencia.setString(7, e.getEstado());
            sentencia.executeUpdate();
            
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                return claves.next() ? claves.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            throw new AccesoDatosException("Error al registrar el establecimiento: " + ex.getMessage(), ex);
        }
    }
    
    public Establecimiento buscarPorEmail(String email) throws AccesoDatosException {
        String sql = "SELECT * FROM Establecimiento WHERE email = ?";
        try (Connection conexion = ConexionBD.conectar();
               PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, email);
            try (ResultSet rs = sentencia.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException ex) {
            throw new AccesoDatosException("Error al buscar el establecimiento: " + ex.getMessage(), ex);
        }
    }
    
    public Establecimiento buscarPorId(int idEstablecimiento) throws AccesoDatosException {
        String sql = "SELECT * FROM Establecimiento WHERE idEstablecimiento = ?";
        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idEstablecimiento);
            try (ResultSet rs = sentencia.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException ex) {
            throw new AccesoDatosException("Error al buscar el establecimiento: " + ex.getMessage(), ex);
        }
    }
    
    public boolean existeEmail(String email) throws AccesoDatosException {
        return buscarPorEmail(email) != null;
    }
    
    public List<Establecimiento> listarPorEstado(String estado) throws AccesoDatosException {
        String sql = "SELECT * FROM Establecimiento WHERE estado = ? ORDER BY idEstablecimiento";
        List<Establecimiento> lista = new ArrayList<>();
        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, estado);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        } catch (SQLException ex) {
            throw new AccesoDatosException("Error al listar establecimientos: " + ex.getMessage(), ex);
        }
    }
    
    public boolean actualizarEstado(int idEstablecimiento, String estado) throws AccesoDatosException {
        String sql = "UPDATE Establecimiento SET estado = ? WHERE idEstablecimiento = ?";
        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, estado);
            sentencia.setInt(2, idEstablecimiento);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new AccesoDatosException("Error al ectualizar el establecimiento: " + ex.getMessage(), ex);
        }
                
    }
}
