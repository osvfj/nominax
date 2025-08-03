package com.nominax.dao;

import com.nominax.entity.Departamento;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author osvfj
 */

public interface DepartamentoDAO {
    Optional<Departamento> buscarPorId(int id) throws IOException;
    List<Departamento> obtenerTodos() throws IOException;
    void guardar(Departamento departamento) throws IOException;
    void eliminar(int id) throws IOException;
}