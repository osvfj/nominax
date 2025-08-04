package com.nominax.dao;

import com.nominax.entity.Empleado;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author osvfj
 */

public interface EmpleadoDAO {
    Optional<Empleado> buscarPorId(String id) throws IOException;
    List<Empleado> obtenerTodos() throws IOException;
    void guardar(Empleado empleado) throws IOException;
    void actualizar(Empleado empleado) throws IOException;
    void eliminar(String id) throws IOException;
}