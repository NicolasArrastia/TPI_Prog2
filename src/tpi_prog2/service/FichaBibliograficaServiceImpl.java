package tpi_prog2.service;

import tpi_prog2.dao.GenericDAO;
import tpi_prog2.models.FichaBibliografica;

import java.sql.Connection;
import java.util.List;

public class FichaBibliograficaServiceImpl implements GenericService<FichaBibliografica> {

    private final GenericDAO<FichaBibliografica> fichaDAO;

    public FichaBibliograficaServiceImpl(GenericDAO<FichaBibliografica> fichaDAO) {
        if (fichaDAO == null) throw new IllegalArgumentException("FichaBibliograficaDAO no puede ser null");
        this.fichaDAO = fichaDAO;
    }

    @Override
    public void insertar(FichaBibliografica ficha) throws Exception {
        validateFicha(ficha);
        fichaDAO.insertar(ficha);
    }

    @Override
    public void actualizar(FichaBibliografica ficha) throws Exception {
        validateFicha(ficha);
        if (ficha.getId() <= 0) throw new IllegalArgumentException("El ID debe ser mayor a 0 para actualizar");
        fichaDAO.actualizar(ficha);
    }

    public void insertar(FichaBibliografica ficha, Connection conn) throws Exception {
        validateFicha(ficha);
        fichaDAO.insertTransaccion(ficha, conn);
    }

    public void actualizar(FichaBibliografica ficha, Connection conn) throws Exception {
        validateFicha(ficha);
        if (ficha.getId() <= 0) throw new IllegalArgumentException("El ID debe ser mayor a 0 para actualizar");
        fichaDAO.actualizarTransaccion(ficha, conn);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("ID inválido");
        fichaDAO.eliminar(id);
    }

    @Override
    public FichaBibliografica getById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("ID inválido");
        return fichaDAO.getById(id);
    }

    @Override
    public List<FichaBibliografica> getAll() throws Exception {
        return fichaDAO.getAll();
    }

    private void validateFicha(FichaBibliografica ficha) {
        if (ficha == null) throw new IllegalArgumentException("Ficha null");
        if (ficha.getIsbn() == null || ficha.getIsbn().isBlank())
            throw new IllegalArgumentException("ISBN no puede estar vacío");
        if (ficha.getIdioma() == null || ficha.getIdioma().isBlank())
            throw new IllegalArgumentException("Idioma no puede estar vacío");
    }
}
