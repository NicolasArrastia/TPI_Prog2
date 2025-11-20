package tpi_prog2.service;

import java.sql.Connection;
import tpi_prog2.config.DatabaseConnection;
import tpi_prog2.models.Libro;
import tpi_prog2.dao.LibroDAO;

import java.util.List;

public class LibroServiceImpl implements GenericService<Libro> {

    private final LibroDAO libroDAO;
    private final FichaBibliograficaServiceImpl fichaService;

    public LibroServiceImpl(LibroDAO libroDAO, FichaBibliograficaServiceImpl fichaService) {
        if (libroDAO == null) throw new IllegalArgumentException("LibroDAO no puede ser null");
        if (fichaService == null) throw new IllegalArgumentException("FichaBibliograficaServiceImpl no puede ser null");
        this.libroDAO = libroDAO;
        this.fichaService = fichaService;
    }

    @Override
    public void insertar(Libro libro) throws Exception {
        validateLibro(libro);
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection(false);

            if (libro.getFichaBibliografica() != null) {
                if (libro.getFichaBibliografica().getId() == 0) {
                    fichaService.insertar(libro.getFichaBibliografica(), conn);
                } else {
                    fichaService.actualizar(libro.getFichaBibliografica(), conn);
                }
            }

            libroDAO.insertTransaccion(libro, conn);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); System.out.println("Rollback");} catch (Exception ignore) {}
            throw e; 
        } finally {
            if (conn != null) try { conn.close(); } catch (Exception ignore) {}
        }
    }

    @Override
    public void actualizar(Libro libro) throws Exception {
        validateLibro(libro);
        if (libro.getId() <= 0) throw new IllegalArgumentException("El ID del libro debe ser > 0 para actualizar");

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection(false);

            if (libro.getFichaBibliografica() != null) {
                if (libro.getFichaBibliografica().getId() == 0) {
                    fichaService.insertar(libro.getFichaBibliografica(), conn);
                } else {
                    fichaService.actualizar(libro.getFichaBibliografica(), conn);
                }
            }

            libroDAO.actualizarTransaccion(libro, conn);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (Exception ignore) {}
            throw e;
        } finally {
            if (conn != null) try { conn.close(); } catch (Exception ignore) {}
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser mayor a 0");
        libroDAO.eliminar(id);
    }

    @Override
    public Libro getById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser mayor a 0");
        return libroDAO.getById(id);
    }

    @Override
    public List<Libro> getAll() throws Exception {
        return libroDAO.getAll();
    }

    private void validateLibro(Libro libro) {
        if (libro == null) throw new IllegalArgumentException("El libro no puede ser null");
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty())
            throw new IllegalArgumentException("El título no puede estar vacío");
    }

    public FichaBibliograficaServiceImpl getFichaService() {
        return fichaService;
    }
}
