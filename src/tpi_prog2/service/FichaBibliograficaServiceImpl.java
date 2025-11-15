package tpi_prog2.service;

import tpi_prog2.dao.GenericDAO;
import tpi_prog2.models.FichaBibliografica;

import java.util.List;

public class FichaBibliograficaServiceImpl implements GenericService<FichaBibliografica> {

    private final GenericDAO<FichaBibliografica> fichaDAO;

    public FichaBibliograficaServiceImpl(GenericDAO<FichaBibliografica> fichaDAO) {
        if (fichaDAO == null) {
            throw new IllegalArgumentException("FichaBibliograficaDAO no puede ser null");
        }
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
        if (ficha.getId() <= 0) {
            throw new IllegalArgumentException("El ID de la ficha debe ser mayor a 0 para actualizar");
        }
        fichaDAO.actualizar(ficha);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        fichaDAO.eliminar(id);
    }

    @Override
    public FichaBibliografica getById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        return fichaDAO.getById(id);
    }

    @Override
    public List<FichaBibliografica> getAll() throws Exception {
        return fichaDAO.getAll();
    }

    private void validateFicha(FichaBibliografica ficha) {
        if (ficha == null) {
            throw new IllegalArgumentException("La ficha bibliográfica no puede ser null");
        }

        if (ficha.getIsbn() == null || ficha.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede estar vacío");
        }

        if (ficha.getIdioma() == null || ficha.getIdioma().trim().isEmpty()) {
            throw new IllegalArgumentException("El idioma no puede estar vacío");
        }

    }
}
