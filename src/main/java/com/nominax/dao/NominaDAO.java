package com.nominax.dao;

import com.nominax.entity.Nomina;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author osvfj
 */

public interface NominaDAO {
    void guardar(Nomina nomina) throws IOException;
    Optional<Nomina> buscarPorEmpleadoYFecha(String idEmpleado, Date fecha) throws IOException;
    List<Nomina> obtenerTodas() throws IOException;
    List<Nomina> buscarPorMesYAnio(int mes, int anio) throws IOException;
    List<Nomina> buscarPorEmpleado(String idEmpleado) throws IOException;
    boolean existeNominaMesAnio(int mes, int anio) throws IOException;
    void actualizar(Nomina nomina) throws IOException;
    void eliminar(String idEmpleado, Date fecha) throws IOException;
    void eliminarPorMesYAnio(int mes, int anio) throws IOException;
}