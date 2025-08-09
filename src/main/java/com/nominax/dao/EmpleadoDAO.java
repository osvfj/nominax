package com.nominax.dao;

import com.nominax.entity.Empleado;
import java.io.IOException;
import java.util.Date;
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
    
      
    List<Empleado> buscarPorDepartamento(Integer idDepartamento) throws IOException;
    List<Empleado> buscarPorPuesto(Integer idPuesto) throws IOException;
    List<Empleado> buscarPorSexo(Boolean sexo) throws IOException;
    List<Empleado> buscarPorCooperativa(Boolean esMiembroCooperativa) throws IOException;
    List<Empleado> buscarPorRangoFechaIngreso(Date fechaInicio, Date fechaFin) throws IOException;
    List<Empleado> buscarPorRangoSalario(Double salarioMin, Double salarioMax) throws IOException;
    List<Empleado> buscarPorNombre(String nombre) throws IOException;
    long contarPorDepartamento(Integer idDepartamento) throws IOException;
    long contarPorPuesto(Integer idPuesto) throws IOException;
}