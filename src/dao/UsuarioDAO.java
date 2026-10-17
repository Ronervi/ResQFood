package dao;

import excepcion.AccesoDatosException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Estados;
import modelo.Usuario;
import util.Validador;

/**
 * Acceso a la tabla Usuario.
 */
public class UsuarioDAO {
    
    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
        rs.getInt("idUsuario"),
        rs.getString("nombre"),
        rs.getString("apellido"),
        rs.getString("email"),
        rs.getString("contraseña"),
        rs.getString("telefono"),
        Validador.valorOPorDefecto(rs.getString("estado"), Estados.ACTIVO));
    }
    
    /** Inserta el usuario y devuelve el id generado por MySQL (AUTO_INCREMENT). */
    public int registrar(Usuario usuario) throws AccesoDatosException {
        String sql = "INSERT INTO Usuario (nombre, apellido, email, contraseña, telefono, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia
                        = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                            
                            sentencia.setString(1, usuario.getNombre());
                            sentencia.setString(2, usuario.getApellido());
                            sentencia.setString(3, usuario.getEmail());
                            sentencia.setString(4, usuario.getContraseña());
                            sentencia.setString(5, usuario.getTelefono());
                            sentencia.setString(6, usuario.getEstado());
                            sentencia.executeUpdate();
                            
                            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                                return claves.next() ? claves.getInt(1) : 0;
                            }
                        } catch (SQLException e) {
                                throw new AccesoDatosException("Error al registrar el usuario: " + e.getMessage(), e);
                                }
    }
        
        public Usuario buscarPorEmail(String email) throws AccesoDatosException {
            String sql = "SELECT * FROM Usuario WHERE email = ?";
            try (Connection conexion = ConexionBD.conectar();
                    PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setString(1, email);
                try (ResultSet rs = sentencia.executeQuery()) {
                    return rs.next() ? mapear(rs) : null;
                }
            } catch (SQLException e) {
                throw new AccesoDatosException("Error al buscar el usuario: " + e.getMessage(), e);
            }
        }
        
        public Usuario buscarPorId(int idUsuario) throws AccesoDatosException {
            String sql = "SELECT * FROM Usuario WHERE idUsuario = ?";
            try (Connection conexion = ConexionBD.conectar();
            PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setInt(1, idUsuario);
                try (ResultSet rs = sentencia.executeQuery()) {
                    return rs.next() ? mapear(rs) : null;
                }
            } catch (SQLException e) {
                    throw new AccesoDatosException("Error al buscar el usuario: " + e.getMessage(), e);
                    }
        }
            
            public boolean existeEmail(String email) throws AccesoDatosException {
                return buscarPorEmail(email) != null;
            }
            
            public List<Usuario> listar() throws AccesoDatosException {
                String sql = "SELECT * FROM Usuario ORDER BY idUsuario";
                List<Usuario> usuarios = new ArrayList<>();
                try (Connection conexion = ConexionBD.conectar();
                        PreparedStatement sentencia = conexion.prepareStatement(sql);
                        ResultSet rs = sentencia.executeQuery()) {
                    while (rs.next()) {
                        usuarios.add(mapear(rs));
                    }
                    return usuarios;
                } catch (SQLException e) {
                    throw new AccesoDatosException("Error al listar usuario: " + e.getMessage(), e);
                }
            }
            
            public boolean actualizarEstado(int idUsuario, String estado) throws AccesoDatosException {
                String sql = "UPDATE Usuario SET estado = ? WHERE idUsuario = ?";
                try (Connection conexion = ConexionBD.conectar();
                        PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                    sentencia.setString(1, estado);
                    sentencia.setInt(2, idUsuario);
                    return sentencia.executeUpdate() > 0;
                } catch (SQLException e) {
                    throw new AccesoDatosException("Error al actualizar el usuario: " + e.getMessage(), e);
                }
            }
}
