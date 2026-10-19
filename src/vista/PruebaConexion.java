package vista;

import dao.ConexionBD;
import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {
    
    public static void main(String[] args) {
        
        try (Connection conexion = ConexionBD.conectar()) {
            
            System.out.println(
            "Conexión a MySQL establecida correctamente."
            );
        } catch (SQLException e) {
            
            System.out.println(
            "No se pudo conectar a la base de datos."
            );
            
            System.out.println("Error: " + e.getMessage());
        }
    }
}
