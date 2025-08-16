package com.nominax.dao.impl;

import com.nominax.dao.NominaDAO;
import com.nominax.entity.Nomina;
import com.nominax.util.ManejadorArchivos;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author osvfj
 */

public class NominaDAOFile implements NominaDAO {
    private static final String ARCHIVO = "nominas.txt";
    private static final String SEPARADOR = ";";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void guardar(Nomina nomina) throws IOException {
        List<Nomina> nominasActuales = obtenerTodas();
        nominasActuales.add(nomina);
        
        List<String> lineasAGuardar = nominasActuales.stream()
            .map(this::convertirNominaALinea)
            .collect(Collectors.toList());

        ManejadorArchivos.escribirTodasLasLineas(ARCHIVO, lineasAGuardar);
    }

    @Override
    public Optional<Nomina> buscarPorEmpleadoYFecha(String idEmpleado, Date fecha) throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(ARCHIVO);
        
        for (String linea : lineas) {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 9 && partes[0].equals(idEmpleado)) {
                try {
                    Date fechaNomina = dateFormat.parse(partes[1]);
                    Calendar calFecha = Calendar.getInstance();
                    calFecha.setTime(fecha);
                    Calendar calNomina = Calendar.getInstance();
                    calNomina.setTime(fechaNomina);
                    
                    if (calFecha.get(Calendar.MONTH) == calNomina.get(Calendar.MONTH) &&
                        calFecha.get(Calendar.YEAR) == calNomina.get(Calendar.YEAR)) {
                        return Optional.of(parsearNomina(partes));
                    }
                } catch (ParseException e) {
                    continue;
                }
            }
        }
        
        return Optional.empty();
    }

    @Override
    public List<Nomina> obtenerTodas() throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(ARCHIVO);
        List<Nomina> nominas = new ArrayList<>();
        
        for (String linea : lineas) {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 9) {
                nominas.add(parsearNomina(partes));
            }
        }
        
        return nominas;
    }

    @Override
    public List<Nomina> buscarPorMesYAnio(int mes, int anio) throws IOException {
        return obtenerTodas().stream()
            .filter(nomina -> {
                Calendar cal = Calendar.getInstance();
                cal.setTime(nomina.getFecha());
                return cal.get(Calendar.MONTH) == mes - 1 && cal.get(Calendar.YEAR) == anio;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<Nomina> buscarPorEmpleado(String idEmpleado) throws IOException {
        return obtenerTodas().stream()
            .filter(nomina -> nomina.getIdEmpleado().equals(idEmpleado))
            .collect(Collectors.toList());
    }

    @Override
    public boolean existeNominaMesAnio(int mes, int anio) throws IOException {
        return !buscarPorMesYAnio(mes, anio).isEmpty();
    }

    @Override
    public void actualizar(Nomina nomina) throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(ARCHIVO);
        List<String> lineasActualizadas = new ArrayList<>();
        
        boolean encontrado = false;
        for (String linea : lineas) {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 9 && partes[0].equals(nomina.getIdEmpleado())) {
                try {
                    Date fechaNomina = dateFormat.parse(partes[1]);
                    Calendar calFecha = Calendar.getInstance();
                    calFecha.setTime(nomina.getFecha());
                    Calendar calNomina = Calendar.getInstance();
                    calNomina.setTime(fechaNomina);
                    
                    if (calFecha.get(Calendar.MONTH) == calNomina.get(Calendar.MONTH) &&
                        calFecha.get(Calendar.YEAR) == calNomina.get(Calendar.YEAR)) {
                        lineasActualizadas.add(convertirNominaALinea(nomina));
                        encontrado = true;
                    } else {
                        lineasActualizadas.add(linea);
                    }
                } catch (ParseException e) {
                    lineasActualizadas.add(linea);
                }
            } else {
                lineasActualizadas.add(linea);
            }
        }
        
        if (!encontrado) {
            lineasActualizadas.add(convertirNominaALinea(nomina));
        }
        
        ManejadorArchivos.escribirTodasLasLineas(ARCHIVO, lineasActualizadas);
    }

    @Override
    public void eliminar(String idEmpleado, Date fecha) throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(ARCHIVO);
        List<String> lineasFiltradas = new ArrayList<>();
        
        for (String linea : lineas) {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 9 && partes[0].equals(idEmpleado)) {
                try {
                    Date fechaNomina = dateFormat.parse(partes[1]);
                    Calendar calFecha = Calendar.getInstance();
                    calFecha.setTime(fecha);
                    Calendar calNomina = Calendar.getInstance();
                    calNomina.setTime(fechaNomina);
                    
                    if (calFecha.get(Calendar.MONTH) == calNomina.get(Calendar.MONTH) &&
                        calFecha.get(Calendar.YEAR) == calNomina.get(Calendar.YEAR)) {
                        continue; // Omitir esta línea (eliminar)
                    }
                } catch (ParseException e) {
                    // Si hay error de parsing, mantener la línea
                }
            }
            lineasFiltradas.add(linea);
        }
        
        ManejadorArchivos.escribirTodasLasLineas(ARCHIVO, lineasFiltradas);
    }

    @Override
    public void eliminarPorMesYAnio(int mes, int anio) throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(ARCHIVO);
        List<String> lineasFiltradas = new ArrayList<>();
        
        for (String linea : lineas) {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 9) {
                try {
                    Date fechaPago = dateFormat.parse(partes[1]);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaPago);
                    
                    boolean esDelPeriodo = cal.get(Calendar.MONTH) + 1 == mes && 
                                         cal.get(Calendar.YEAR) == anio;
                    System.out.println();
                    
                    if (!esDelPeriodo) {
                        lineasFiltradas.add(linea);
                    }
                } catch (ParseException e) {
                    lineasFiltradas.add(linea);
                }
            } else {
                lineasFiltradas.add(linea);
            }
        }
        
        ManejadorArchivos.escribirTodasLasLineas(ARCHIVO, lineasFiltradas);
    }
    
    private Nomina parsearNomina(String[] partes) {
        try {
            String idEmpleado = partes[0];
            Date fecha = dateFormat.parse(partes[1]);
            Double salarioBase = Double.parseDouble(partes[2]);
            Double descuentoARS = Double.parseDouble(partes[3]);
            Double descuentoAFP = Double.parseDouble(partes[4]);
            Double descuentoCooperativa = Double.parseDouble(partes[5]);
            Double descuentoISR = Double.parseDouble(partes[6]);
            Double salarioNeto = Double.parseDouble(partes[7]);
            Boolean statusNomina = Boolean.parseBoolean(partes[8]);
            
            return new Nomina(idEmpleado, fecha, salarioBase, descuentoARS, 
                            descuentoAFP, descuentoCooperativa, descuentoISR, 
                            salarioNeto, statusNomina);
        } catch (ParseException | NumberFormatException e) {
            throw new RuntimeException("Error al parsear nómina: " + e.getMessage(), e);
        }
    }

    private String convertirNominaALinea(Nomina nomina) {
        return String.join(SEPARADOR,
            nomina.getIdEmpleado(),
            dateFormat.format(nomina.getFecha()),
            nomina.getSalarioBase().toString(),
            nomina.getDescuentoARS().toString(),
            nomina.getDescuentoAFP().toString(),
            nomina.getDescuentoCooperativa().toString(),
            nomina.getDescuentoISR().toString(),
            nomina.getSalarioNeto().toString(),
            nomina.getStatusNomina().toString()
        );
    }
}