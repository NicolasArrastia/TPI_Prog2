package tpi_prog2.main;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import tpi_prog2.config.DatabaseConnection;

public class TestConexion {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Conexión exitosa a la base de datos");

                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("Usuario conectado: " + metaData.getUserName());
                System.out.println("Base de datos: " + conn.getCatalog());
                System.out.println("URL: " + metaData.getURL());
                System.out.println("Driver: " + metaData.getDriverName() + " v" + metaData.getDriverVersion());
            } else {
                System.out.println("No se pudo establecer la conexión.");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
