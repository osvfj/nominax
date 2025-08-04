package com.nominax.dao;

import com.nominax.entity.Puesto;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author osvfj
 */

public interface PuestoDAO {
    Optional<Puesto> buscarPorId(int id) throws IOException;
    List<Puesto> obtenerTodos() throws IOException;
    void guardar(Puesto puesto) throws IOException;
    void eliminar(int id) throws IOException;
}