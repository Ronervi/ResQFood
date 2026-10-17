package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Conexiones a MySQL. Los datos de conexión (incluida la contraseña)
 * Se leen del archivo config.properties, que NO se sube a GitHub.
 */
public class ConexionBD {
    
    private static final String ARCHIVO_CONFIG = "config.properties";
    private static Properties config;
    
    private static synchronized Properties cargarConfiguracion() throws SQLException {
        if (config == null) {
            File archivo = new File(ARCHIVO_CONFIG);  
            if (!archivo.exists()) {
                throw new SQLException("No se encontró el archivo '" + ARCHIVO_CONFIG
                + "' en la carpeta del proyecto. Copiá 'config.properties.example' "
                + "como 'config.properties' y completá tu contraseña de MySQL.");
            }
            Properties propiedades = new Properties();
            try (Reader lector = new InputStreamReader(
            new FileInputStream(archivo), StandardCharsets.UTF_8)) {
                propiedades.load(lector);
            } catch (IOException e) {
                throw new SQLException("No se pudo leer el archivo '" + ARCHIVO_CONFIG + "'.", e);
            }
            config = propiedades;
        }
        return config;
    }
    
    public static Connection conectar() throws SQLException {
        Properties p = cargarConfiguracion();
        String url = p.getProperty("db.url", "jdbc:mysql://localhost:3306/resqfood");
        String usuario = p.getProperty("db.usuario", "root");
        String contraseña = p.getProperty("db.password", "");
        return DriverManager.getConnection(url, usuario, contraseña);
    }
}
