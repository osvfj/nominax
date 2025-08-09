package com.nominax.dao.impl;

import com.nominax.dao.EmpleadoDAO;
import com.nominax.entity.Empleado;
import com.nominax.util.ManejadorArchivos;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author osvfj
 */
public class EmpleadoDAOFile implements EmpleadoDAO {
    private static final String ARCHIVO = "empleados.txt";
    private static final String SEPARADOR = ";";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Optional<Empleado> buscarPorId(String id) throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(ARCHIVO);
        
        for (String linea : lineas) {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 12 && partes[0].equals(id)) {
                return Optional.of(parsearEmpleado(partes));
            }
        }
        
        return Optional.empty();
    }

    @Override
    public List<Empleado> obtenerTodos() throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(ARCHIVO);
        List<Empleado> empleados = new ArrayList<>();
        
        for (String linea : lineas) {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 12) {
                empleados.add(parsearEmpleado(partes));
            }
        }
        
        return empleados;
    }

    @Override
    public void guardar(Empleado empleado) throws IOException {
        List<Empleado> empleadosActuales = obtenerTodos(); 

        empleadosActuales.add(empleado); 
        
        List<String> lineasaAguardar = empleadosActuales.stream()
            .map(this::convertirEmpleadoALinea) 
            .collect(Collectors.toList());

        ManejadorArchivos.escribirTodasLasLineas(ARCHIVO, lineasaAguardar);
    }

    @Override
    public void actualizar(Empleado empleado) throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(ARCHIVO);
        List<String> lineasActualizadas = new ArrayList<>();
        
        boolean encontrado = false;
        for (String linea : lineas) {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 12 && partes[0].equals(empleado.getId())) {
                lineasActualizadas.add(convertirEmpleadoALinea(empleado));
                encontrado = true;
            } else {
                lineasActualizadas.add(linea);
            }
        }
        
        if (!encontrado) {
            lineasActualizadas.add(convertirEmpleadoALinea(empleado));
        }
        
        ManejadorArchivos.escribirTodasLasLineas(ARCHIVO, lineasActualizadas);
    }

    @Override
    public void eliminar(String id) throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(ARCHIVO);
        List<String> lineasFiltradas = new ArrayList<>();
        
        for (String linea : lineas) {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length < 12 || !partes[0].equals(id)) {
                lineasFiltradas.add(linea);
            }
        }
        
        ManejadorArchivos.escribirTodasLasLineas(ARCHIVO, lineasFiltradas);
    }
    
    @Override
    public List<Empleado> buscarPorDepartamento(Integer idDepartamento) throws IOException {
        return obtenerTodos().stream()
                .filter(empleado -> empleado.getIdDepto().equals(idDepartamento))
                .collect(Collectors.toList());
    }

    @Override
    public List<Empleado> buscarPorPuesto(Integer idPuesto) throws IOException {
        return obtenerTodos().stream()
                .filter(empleado -> empleado.getIdPuesto().equals(idPuesto))
                .collect(Collectors.toList());
    }

    @Override
    public List<Empleado> buscarPorSexo(Boolean sexo) throws IOException {
        return obtenerTodos().stream()
                .filter(empleado -> empleado.getSexo().equals(sexo))
                .collect(Collectors.toList());
    }

    @Override
    public List<Empleado> buscarPorCooperativa(Boolean esMiembroCooperativa) throws IOException {
        return obtenerTodos().stream()
                .filter(empleado -> empleado.getCoop().equals(esMiembroCooperativa))
                .collect(Collectors.toList());
    }
    @Override
    public List<Empleado> buscarPorRangoFechaIngreso(Date fechaInicio, Date fechaFin) throws IOException {
        return obtenerTodos().stream()
                .filter(empleado -> {
                    Date fechaIngreso = empleado.getFechaIngreso();
                    return !fechaIngreso.before(fechaInicio) && !fechaIngreso.after(fechaFin);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Empleado> buscarPorRangoSalario(Double salarioMin, Double salarioMax) throws IOException {
        return obtenerTodos().stream()
                .filter(empleado -> {
                    Double salario = empleado.getSalario();
                    return salario >= salarioMin && salario <= salarioMax;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Empleado> buscarPorNombre(String nombre) throws IOException {
        String nombreBusqueda = nombre.toLowerCase();
        return obtenerTodos().stream()
                .filter(empleado -> 
                    empleado.getNombre().toLowerCase().contains(nombreBusqueda) ||
                    empleado.getApellidoPat().toLowerCase().contains(nombreBusqueda) ||
                    empleado.getApellidoMat().toLowerCase().contains(nombreBusqueda)
                )
                .collect(Collectors.toList());
    }

    @Override
    public long contarPorDepartamento(Integer idDepartamento) throws IOException {
        return buscarPorDepartamento(idDepartamento).size();
    }

    @Override
    public long contarPorPuesto(Integer idPuesto) throws IOException {
        return buscarPorPuesto(idPuesto).size();
    }
    
    
    private Empleado parsearEmpleado(String[] partes) {
        try {
            String id = partes[0];
            String nombre = partes[1];
            String apellidoPat = partes[2];
            String apellidoMat = partes[3];
            String direccion = partes[4];
            Long telefono = Long.parseLong(partes[5]);
            Boolean sexo = Boolean.parseBoolean(partes[6]);
            Integer idDepto = Integer.parseInt(partes[7]);
            Date fechaIngreso = dateFormat.parse(partes[8]);
            Integer idPuesto = Integer.parseInt(partes[9]);
            Boolean coop = Boolean.parseBoolean(partes[10]);
            Double salario = Double.parseDouble(partes[11]);
            
            return new Empleado(id, nombre, apellidoPat, apellidoMat, direccion,
                    telefono, sexo, idDepto, fechaIngreso, idPuesto, coop, salario);
        } catch (ParseException | NumberFormatException e) {
            throw new RuntimeException("Error al parsear empleado: " + e.getMessage(), e);
        }
    }

    private String convertirEmpleadoALinea(Empleado empleado) {
        return String.join(SEPARADOR,
                empleado.getId(),
                empleado.getNombre(),
                empleado.getApellidoPat(),
                empleado.getApellidoMat(),
                empleado.getDireccion(),
                empleado.getTelefono().toString(),
                empleado.getSexo().toString(),
                empleado.getIdDepto().toString(),
                dateFormat.format(empleado.getFechaIngreso()),
                empleado.getIdPuesto().toString(),
                empleado.getCoop().toString(),
                empleado.getSalario().toString()
        );
    }
}