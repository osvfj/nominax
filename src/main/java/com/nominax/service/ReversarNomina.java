package com.nominax.service;

import com.nominax.dao.CooperativaDAO;
import com.nominax.dao.NominaDAO;
import com.nominax.entity.Cooperativa;
import com.nominax.entity.Nomina;
import com.nominax.dao.impl.CooperativaDAOFile;
import com.nominax.dao.impl.EmpleadoDAOFile;
import com.nominax.dao.impl.NominaDAOFile;
import com.nominax.util.ManejadorArchivos;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author osvfj
 */

public final class ReversarNomina {
    
    private static final NominaDAO nominaDAO = new NominaDAOFile();
    private static final CooperativaDAO cooperativaDAO = new CooperativaDAOFile();
    private static final EmpleadoDAOFile empleadoDAO = new EmpleadoDAOFile();

    public void reversarNomina(int mes, int anio) throws IOException {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }
        
        if (anio < 1900 || anio > 2100) {
            throw new IllegalArgumentException("El año debe estar entre 1900 y 2100.");
        }
        
        List<Nomina> nominasAReversar = nominaDAO.buscarPorMesYAnio(mes, anio);
        
        if (nominasAReversar.isEmpty()) {
            throw new RuntimeException("No se encontraron registros de nómina para " + 
                                     String.format("%02d/%d", mes, anio) + ".");
        }
        
        revertirCooperativas(nominasAReversar);
        
        eliminarDirectorioDeNominasDelMes(mes, anio);
        
        nominaDAO.eliminarPorMesYAnio(mes, anio);
    }
    
    private void revertirCooperativas(List<Nomina> nominas) throws IOException {
        Map<String, Double> descuentosPorEmpleado = new HashMap<>();
        
        for (Nomina nomina : nominas) {
            String idEmpleado = nomina.getIdEmpleado();
            Double descuento = nomina.getDescuentoCooperativa();
            
            if (descuento != null && descuento > 0) {
                descuentosPorEmpleado.merge(idEmpleado, descuento, Double::sum);
            }
        }
        
        for (Map.Entry<String, Double> entrada : descuentosPorEmpleado.entrySet()) {
            String idEmpleado = entrada.getKey();
            Double montoARevertir = entrada.getValue();
            
            Optional<Cooperativa> cooperativaOpcional = 
                cooperativaDAO.buscarPorIdEmpleado(Integer.valueOf(idEmpleado));
            
            if (cooperativaOpcional.isPresent()) {
                Cooperativa cooperativa = cooperativaOpcional.get();
                Double nuevoBalance = cooperativa.getBalance() - montoARevertir;
                cooperativa.setBalance(Math.max(0.0, nuevoBalance));
                cooperativaDAO.actualizar(cooperativa);
            }
        }
    }
    
    private void eliminarDirectorioDeNominasDelMes(int mes, int anio) {
        String rutaDirectorioPdfMensual = ManejadorArchivos.obtenerRutaBase() + File.separator + 
                                   "nominas" + File.separator + 
                                   anio + File.separator + 
                                   String.format("%02d", mes);
        
        File directorioMensual = new File(rutaDirectorioPdfMensual);
        
        if (directorioMensual.exists() && directorioMensual.isDirectory()) {
            eliminarContenidoDirectorio(directorioMensual);
            
            if (!directorioMensual.delete()) {
                throw new RuntimeException("Advertencia: No se pudo eliminar el directorio de PDFs " + 
                                 directorioMensual.getAbsolutePath() + ".");
            } else {
                File directorioPadreAnual = directorioMensual.getParentFile();
                if (directorioPadreAnual != null) {
                    eliminarDirectorioVacio(directorioPadreAnual);
                }
            }
        }
    }

    private void eliminarContenidoDirectorio(File directorio) {
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isDirectory()) {
                    eliminarContenidoDirectorio(archivo); 
                    if (!archivo.delete()) {
                        throw new RuntimeException("Advertencia: No se pudo eliminar el subdirectorio " + 
                                         archivo.getAbsolutePath());
                    }
                } else {
                    if (!archivo.delete()) {
                        throw new RuntimeException("Advertencia: No se pudo eliminar el archivo " + 
                                         archivo.getAbsolutePath());
                    }
                }
            }
        }
    }
    
    private void eliminarDirectorioVacio(File directorio) {
        if (directorio != null && directorio.exists() && directorio.isDirectory()) {
            File[] archivos = directorio.listFiles();
            if (archivos == null || archivos.length == 0) {
                if (!directorio.delete()) {
                    throw new RuntimeException("Advertencia: No se pudo eliminar el directorio vacío " + 
                                     directorio.getAbsolutePath());
                }
            }
        }
    }
}