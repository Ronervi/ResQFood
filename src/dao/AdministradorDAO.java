package dao;

import excepcion.AccesoDatosException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Administrador;
import modelo.Estados;
import util.Validador;

/**
 * Acceso a la tabla Administrador.
 */
public class AdministradorDAO {
    
    public Administrador buscarPorEmail(String email) throws AccesoDatosException {
        String sql = "SELECT * FROM Administrador WHERE email = ?";
        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, email);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new Administrador(
                rs.getInt("idAdministrador"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getString("contrasena"),
                Validador.valorOPorDefecto(rs.getString("estado"), Estados.ACTIVO));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar el administrador: " + e.getMessage(), e);
        }
    }
}
