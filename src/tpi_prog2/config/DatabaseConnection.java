
package tpi_prog2.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para gestionar conexiones a la base de datos MySQL.
 *
 * Patrón: Factory con configuración estática
 * - No se puede instanciar (constructor privado)
 * - Proporciona conexiones mediante método estático getConnection()
 * - Configuración cargada una sola vez en bloque static
 *
 * Configuración por defecto:
 * - URL: jdbc:mysql://localhost:3306/dbtpi3
 * - Usuario: root
 * - Contraseña: vacía (común en desarrollo local)
 *
 * Override mediante system properties:
 * - java -Ddb.url=... -Ddb.user=... -Ddb.password=...
 */
public final class DatabaseConnection {
    /** URL de conexión JDBC. Configurable via -Ddb.url */
    private static final String URL = System.getProperty("db.url", "jdbc:mysql://localhost:3307/libreria");

    /** Usuario de la base de datos. Configurable via -Ddb.user */
    private static final String USER = System.getProperty("db.user", "root");

    /** Contraseña del usuario. Configurable via -Ddb.password */
    private static final String PASSWORD = System.getProperty("db.password", "");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            validateConfiguration();
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Error: No se encontró el driver JDBC de MySQL: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new ExceptionInInitializerError("Error en la configuración de la base de datos: " + e.getMessage());
        }
    }


    private DatabaseConnection() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no debe ser instanciada");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void validateConfiguration() {
        if (URL == null || URL.trim().isEmpty()) {
            throw new IllegalStateException("La URL de la base de datos no está configurada");
        }
        if (USER == null || USER.trim().isEmpty()) {
            throw new IllegalStateException("El usuario de la base de datos no está configurado");
        }

        if (PASSWORD == null) {
            throw new IllegalStateException("La contraseña de la base de datos no está configurada");
        }
    }
}
