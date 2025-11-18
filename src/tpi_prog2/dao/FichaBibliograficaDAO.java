package tpi_prog2.dao;

import tpi_prog2.config.DatabaseConnection;
import tpi_prog2.models.FichaBibliografica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FichaBibliograficaDAO implements GenericDAO<FichaBibliografica> {

    private static final String INSERT_SQL =
            "INSERT INTO FichaBibliografica (isbn, clasificacionDewey, estanteria, idioma, eliminado) VALUES (?, ?, ?, ?, FALSE)";

    private static final String UPDATE_SQL =
            "UPDATE FichaBibliografica SET isbn = ?, clasificacionDewey = ?, estanteria = ?, idioma = ? WHERE id_ficha = ?";

    private static final String DELETE_SQL =
            "UPDATE FichaBibliografica SET eliminado = TRUE WHERE id_ficha = ?";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM FichaBibliografica WHERE id_ficha = ? AND eliminado = FALSE";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM FichaBibliografica WHERE eliminado = FALSE";


    // -----------------------
    //  Métodos no transaccionales
    // -----------------------
    @Override
    public void insertar(FichaBibliografica ficha) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setFichaParams(stmt, ficha);
            stmt.executeUpdate();
            setGeneratedId(stmt, ficha);
        }
    }

    @Override
    public void actualizar(FichaBibliografica ficha) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setString(1, ficha.getIsbn());
            stmt.setString(2, ficha.getClasificacionDewey());
            stmt.setString(3, ficha.getEstanteria());
            stmt.setString(4, ficha.getIdioma());
            stmt.setInt(5, ficha.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("No existe ficha con ID: " + ficha.getId());
        }
    }

    // -----------------------
    //  Métodos transaccionales (reciben Connection)
    //  No hacen commit/rollback ni cierran la conexión.
    // -----------------------
    @Override
    public void insertTransaccion(FichaBibliografica ficha, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setFichaParams(stmt, ficha);
            stmt.executeUpdate();
            setGeneratedId(stmt, ficha);
        }
    }

    @Override
    public void actualizarTransaccion(FichaBibliografica ficha, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, ficha.getIsbn());
            stmt.setString(2, ficha.getClasificacionDewey());
            stmt.setString(3, ficha.getEstanteria());
            stmt.setString(4, ficha.getIdioma());
            stmt.setInt(5, ficha.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("No se pudo actualizar (transacción) ficha ID: " + ficha.getId());
        }
    }

    // -----------------------
    //  Otros
    // -----------------------
    @Override
    public void eliminar(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            if (stmt.executeUpdate() == 0) throw new SQLException("No se encontró ficha con ID: " + id);
        }
    }

    @Override
    public FichaBibliografica getById(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapToFicha(rs);
            }
        }
        return null;
    }

    @Override
    public List<FichaBibliografica> getAll() throws Exception {
        List<FichaBibliografica> fichas = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) fichas.add(mapToFicha(rs));
        }
        return fichas;
    }

    // -----------------------
    // Helpers
    // -----------------------
    private void setFichaParams(PreparedStatement stmt, FichaBibliografica f) throws SQLException {
        stmt.setString(1, f.getIsbn());
        stmt.setString(2, f.getClasificacionDewey());
        stmt.setString(3, f.getEstanteria());
        stmt.setString(4, f.getIdioma());
    }

    private void setGeneratedId(PreparedStatement stmt, FichaBibliografica ficha) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) ficha.setId(rs.getInt(1));
            else throw new SQLException("No se obtuvo ID generado para FichaBibliografica.");
        }
    }

    private FichaBibliografica mapToFicha(ResultSet rs) throws SQLException {
        return new FichaBibliografica(
                rs.getInt("id_ficha"),
                rs.getBoolean("eliminado"),
                rs.getString("isbn"),
                rs.getString("clasificacionDewey"),
                rs.getString("estanteria"),
                rs.getString("idioma")
        );
    }
}
