package tpi_prog2.dao;

import tpi_prog2.config.DatabaseConnection;
import tpi_prog2.models.Libro;
import tpi_prog2.models.FichaBibliografica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO implements GenericDAO<Libro> {

    private static final String INSERT_SQL =
            "INSERT INTO Libro (titulo, autor, editorial, anioEdicion, id_ficha) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE Libro SET titulo = ?, autor = ?, editorial = ?, anioEdicion = ?, id_ficha = ? WHERE id_libro = ?";

    private static final String DELETE_SQL =
            "UPDATE Libro SET eliminado = TRUE WHERE id_libro = ?";

    private static final String SELECT_BY_ID_SQL =
            "SELECT l.id_libro, l.titulo, l.autor, l.editorial, l.anioEdicion, l.id_ficha, " +
            "f.id_ficha AS f_id, f.isbn, f.clasificacionDewey, f.estanteria, f.idioma, f.eliminado AS f_eliminado " +
            "FROM Libro l LEFT JOIN FichaBibliografica f ON l.id_ficha = f.id_ficha " +
            "WHERE l.id_libro = ? AND l.eliminado = FALSE";

    private static final String SELECT_ALL_SQL =
            "SELECT l.id_libro, l.titulo, l.autor, l.editorial, l.anioEdicion, l.id_ficha, " +
            "f.id_ficha AS f_id, f.isbn, f.clasificacionDewey, f.estanteria, f.idioma, f.eliminado AS f_eliminado " +
            "FROM Libro l LEFT JOIN FichaBibliografica f ON l.id_ficha = f.id_ficha " +
            "WHERE l.eliminado = FALSE";

   

    // -----------------------
    // Métodos NO transaccionales
    // -----------------------
    @Override
    public void insertar(Libro libro) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setLibroParameters(stmt, libro);
            stmt.executeUpdate();
            setGeneratedId(stmt, libro);
        }
    }

    @Override
    public void actualizar(Libro libro) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getEditorial());
            stmt.setInt(4, libro.getAnioEdicion());
            setFichaId(stmt, 5, libro.getFichaBibliografica());
            stmt.setInt(6, libro.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("No existe libro con ID: " + libro.getId());
        }
    }

    // -----------------------
    // Métodos transaccionales
    // -----------------------
    @Override
    public void insertTransaccion(Libro libro, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setLibroParameters(stmt, libro);
            stmt.executeUpdate();
            setGeneratedId(stmt, libro);
        }
    }

    @Override
    public void actualizarTransaccion(Libro libro, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getEditorial());
            stmt.setInt(4, libro.getAnioEdicion());
            setFichaId(stmt, 5, libro.getFichaBibliografica());
            stmt.setInt(6, libro.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("No se pudo actualizar (transacción) libro ID: " + libro.getId());
        }
    }

    // -----------------------
    // Otros CRUD
    // -----------------------
    @Override
    public void eliminar(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            if (stmt.executeUpdate() == 0) throw new SQLException("No se encontró libro con ID: " + id);
        }
    }

    @Override
    public Libro getById(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToLibro(rs);
            }
        }
        return null;
    }

    @Override
    public List<Libro> getAll() throws Exception {
        List<Libro> libros = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) libros.add(mapResultSetToLibro(rs));
        }
        return libros;
    }

    // -----------------------
    // Helpers
    // -----------------------
    private void setLibroParameters(PreparedStatement stmt, Libro libro) throws SQLException {
        stmt.setString(1, libro.getTitulo());
        stmt.setString(2, libro.getAutor());
        stmt.setString(3, libro.getEditorial());
        stmt.setInt(4, libro.getAnioEdicion());
        setFichaId(stmt, 5, libro.getFichaBibliografica());
    }

    private void setFichaId(PreparedStatement stmt, int index, FichaBibliografica ficha) throws SQLException {
        if (ficha != null && ficha.getId() > 0) stmt.setInt(index, ficha.getId());
        else stmt.setNull(index, Types.INTEGER);
    }

    private void setGeneratedId(PreparedStatement stmt, Libro libro) throws SQLException {
        try (ResultSet keys = stmt.getGeneratedKeys()) {
            if (keys.next()) libro.setId(keys.getInt(1));
            else throw new SQLException("No se obtuvo ID generado para Libro.");
        }
    }

    private Libro mapResultSetToLibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        libro.setId(rs.getInt("id_libro"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setEditorial(rs.getString("editorial"));
        libro.setAnioEdicion(rs.getInt("anioEdicion"));

        int fichaId = rs.getInt("id_ficha");
        if (fichaId > 0 && !rs.wasNull()) {
            FichaBibliografica ficha = new FichaBibliografica(
                    rs.getInt("f_id"),
                    rs.getBoolean("f_eliminado"),
                    rs.getString("isbn"),
                    rs.getString("clasificacionDewey"),
                    rs.getString("estanteria"),
                    rs.getString("idioma")
            );
            libro.setFichaBibliografica(ficha);
        }
        return libro;
    }
}
