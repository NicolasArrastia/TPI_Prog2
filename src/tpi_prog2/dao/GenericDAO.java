package tpi_prog2.dao;

import java.sql.Connection;
import java.util.List;

public interface GenericDAO<T> {
    void insertar(T entidad) throws Exception;
    void actualizar(T entidad) throws Exception;
    void eliminar(int id) throws Exception;
    T getById(int id) throws Exception;
    List<T> getAll() throws Exception;

    void insertTransaccion(T entidad, Connection conn) throws Exception;
    void actualizarTransaccion(T entidad, Connection conn) throws Exception;
}
