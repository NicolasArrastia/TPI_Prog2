package tpi_prog2.dao;

import tpi_prog2.config.DatabaseConnection;
import tpi_prog2.models.Libro;
import tpi_prog2.models.FichaBibliografica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO implements GenericDAO<Libro> {
    private static final String INSERT_SQL = "INSERT INTO libros (titulo, autor, editorial, anio_edicion, ficha_id) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL = "UPDATE libros SET titulo = ?, autor = ?, editorial = ?, anio_edicion = ?, ficha_id = ? "
            +
            "WHERE id = ?";

    private static final String DELETE_SQL = "UPDATE libros SET eliminado = TRUE WHERE id = ?";

    private static final String SELECT_BY_ID_SQL = "SELECT l.id, l.titulo, l.autor, l.editorial, l.anio_edicion, l.ficha_id, "
            +
            "f.id AS f_id, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma, f.eliminado AS f_eliminado " +
            "FROM libros l LEFT JOIN ficha_bibliografica f ON l.ficha_id = f.id " +
            "WHERE l.id = ? AND l.eliminado = FALSE";

    private static final String SELECT_ALL_SQL = "SELECT l.id, l.titulo, l.autor, l.editorial, l.anio_edicion, l.ficha_id, "
            +
            "f.id AS f_id, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma, f.eliminado AS f_eliminado " +
            "FROM libros l LEFT JOIN ficha_bibliografica f ON l.ficha_id = f.id " +
            "WHERE l.eliminado = FALSE";


    private final FichaBibliograficaDAO fichaDAO;

    public LibroDAO(FichaBibliograficaDAO fichaDAO) {
        if (fichaDAO == null) {
            throw new IllegalArgumentException("FichaDAO no puede ser null");
        }
        this.fichaDAO = fichaDAO;
    }

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
    public void insertTransaccion(Libro libro, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
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
            if (rows == 0) {
                throw new SQLException("No se pudo actualizar el libro con ID: " + libro.getId());
            }
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new SQLException("No se encontró libro con ID: " + id);
            }
        }
    }

    @Override
    public Libro getById(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLibro(rs);
                }
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

            while (rs.next()) {
                libros.add(mapResultSetToLibro(rs));
            }
        }

        return libros;
    }

    private void setLibroParameters(PreparedStatement stmt, Libro libro) throws SQLException {
        stmt.setString(1, libro.getTitulo());
        stmt.setString(2, libro.getAutor());
        stmt.setString(3, libro.getEditorial());
        stmt.setInt(4, libro.getAnioEdicion());
        setFichaId(stmt, 5, libro.getFichaBibliografica());
    }

    private void setFichaId(PreparedStatement stmt, int index, FichaBibliografica ficha) throws SQLException {
        if (ficha != null && ficha.getId() > 0) {
            stmt.setInt(index, ficha.getId());
        } else {
            stmt.setNull(index, Types.INTEGER);
        }
    }

    private void setGeneratedId(PreparedStatement stmt, Libro libro) throws SQLException {
        try (ResultSet keys = stmt.getGeneratedKeys()) {
            if (keys.next()) {
                libro.setId(keys.getInt(1));
            } else {
                throw new SQLException("No se obtuvo ID generado para Libro.");
            }
        }
    }

    private Libro mapResultSetToLibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        libro.setId(rs.getInt("id"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setEditorial(rs.getString("editorial"));
        libro.setAnioEdicion(rs.getInt("anio_edicion"));

        int fichaId = rs.getInt("ficha_id");
        if (fichaId > 0 && !rs.wasNull()) {
            FichaBibliografica ficha = new FichaBibliografica(
                    rs.getInt("f_id"),
                    rs.getBoolean("f_eliminado"),
                    rs.getString("isbn"),
                    rs.getString("clasificacion_dewey"),
                    rs.getString("estanteria"),
                    rs.getString("idioma"));
            libro.setFichaBibliografica(ficha);
        }

        return libro;
    }
}