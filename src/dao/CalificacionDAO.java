package dao;

import excepcion.AccesoDatosException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Calificacion;
import util.Fechas;

/**
 * Acceso a la tabla Calificacion.
 */
public class CalificacionDAO {
    
    public int insertar(Calificacion c) throws AccesoDatosException {
        String sql = "INSERT INTO Calificacion (idUsuario, idEstablecimiento, puntuacion, comentario, fecha) "
                + "VALUES (?, ?, ?, ?, NOW())";
        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia
                        = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setInt(1, c.getIdUsuario());
            sentencia.setInt(2, c.getIdEstablecimiento());
            sentencia.setInt(3, c.getPuntuacion());
            sentencia.setString(4, c.getComentario());
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                return claves.next() ? claves.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar la calificación: " + e.getMessage(), e);
        }
    }
    
    public List<Calificacion> listarPorEstablecimiento(int idEstablecimiento) throws AccesoDatosException {
        String sql = "SELECT * FROM Calificacion WHERE idEstablecimiento = ? ORDER BY idCalificacion DESC";
        List<Calificacion> lista = new ArrayList<>();
        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idEstablecimiento);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Calificacion(
                    rs.getInt("idCalificacion"),
                    rs.getInt("idUsuario"),
                    rs.getInt("idEstablecimiento"),
                    rs.getInt("puntuacion"),
                    rs.getString("comentario"),
                    Fechas.formatear(rs.getTimestamp("fecha"))));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar calificaciones: " + e.getMessage(), e);
        }
    }
    
    /** Promedio de puntuación del establecimiento (0 si todavía no tiene calificaciones). */
    public double promedio(int idEstablecimiento) throws AccesoDatosException {
        String sql = "SELECT AVG(puntuacion) FROM Calificacion WHETE idEstablecimiento = ?";
        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idEstablecimiento);
            try (ResultSet rs = sentencia.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0;
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al calcular el promedio: " + e.getMessage(), e);
        }
    }
}
