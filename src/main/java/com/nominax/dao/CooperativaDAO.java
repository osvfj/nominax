package com.nominax.dao;

import com.nominax.entity.Cooperativa;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author osvfj
 */
public interface CooperativaDAO {
    void guardar(Cooperativa cooperativa) throws IOException;
    Optional<Cooperativa> buscarPorId(Integer id) throws IOException;
    Optional<Cooperativa> buscarPorIdEmpleado(Integer idEmpleado) throws IOException;
    List<Cooperativa> obtenerTodos() throws IOException;
    void actualizar(Cooperativa cooperativa) throws IOException;
    void eliminar(Integer idEmpleado) throws IOException;
    
    List<Cooperativa> buscarPorRangoDescuento(Double descuentoMin, Double descuentoMax) throws IOException;
    List<Cooperativa> buscarPorRangoBalance(Double balanceMin, Double balanceMax) throws IOException;
    List<Cooperativa> buscarCooperativasActivas() throws IOException;
}
