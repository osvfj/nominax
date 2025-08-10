package com.nominax.dao.impl;

import com.nominax.dao.CooperativaDAO;
import com.nominax.entity.Cooperativa;
import com.nominax.util.ManejadorArchivos;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CooperativaDAOFile implements CooperativaDAO {
    
    private static final String ARCHIVO = "cooperativas.txt";
    private static final String SEPARADOR = ";";
    
    public CooperativaDAOFile() {
      
    }
    
    @Override
    public void guardar(Cooperativa cooperativa) throws IOException {
        Optional<Cooperativa> existente = buscarPorIdEmpleado(cooperativa.getIdEmpleado());
        if (existente.isPresent()) {
            throw new IOException("Ya existe una cooperativa para el empleado ID: " + cooperativa.getIdEmpleado());
        }
        
        if (cooperativa.getId() == null) {
            cooperativa.setId(generarProximoId());
        }
        String linea = cooperativaToString(cooperativa);
        ManejadorArchivos.agregarLinea(ARCHIVO, linea);
    }
    
    @Override
    public Optional<Cooperativa> buscarPorId(Integer id) throws IOException {
        List<Cooperativa> cooperativas = obtenerTodos();
        return cooperativas.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public Optional<Cooperativa> buscarPorIdEmpleado(Integer idEmpleado) throws IOException {
        List<Cooperativa> cooperativas = obtenerTodos();
        return cooperativas.stream()
                .filter(c -> c.getIdEmpleado().equals(idEmpleado))
                .findFirst();
    }
    
    @Override
    public List<Cooperativa> obtenerTodos() throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(ARCHIVO);
        List<Cooperativa> cooperativas = new ArrayList<>();
        
        
        for (String linea : lineas) {
            cooperativas.add(stringToCooperativa(linea));
        }
        
        return cooperativas;
    }
    
    @Override
    public void actualizar(Cooperativa cooperativa) throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(ARCHIVO);
        List<String> lineasActualizadas = new ArrayList<>();
        
        boolean encontrado = false;
        for (String linea : lineas) {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length == 4 && partes[1].equals(String.valueOf(cooperativa.getIdEmpleado()))) {
                lineasActualizadas.add(cooperativaToString(cooperativa));
                encontrado = true;
            } else {
                lineasActualizadas.add(linea);
            }
        }
        
        if (!encontrado) {
            lineasActualizadas.add(cooperativaToString(cooperativa));
        }
        
        ManejadorArchivos.escribirTodasLasLineas(ARCHIVO, lineasActualizadas);
    }
    
    @Override
    public void eliminar(Integer idEmpleado) throws IOException {
        List<Cooperativa> cooperativas = obtenerTodos();
        boolean eliminado = cooperativas.removeIf(c -> c.getIdEmpleado().equals(idEmpleado));
        
        if (!eliminado) {
            throw new IOException("Cooperativa no encontrada para el empleado ID: " + idEmpleado);
        }
        
       // guardarTodos(cooperativas);
    }
    
    
    @Override
    public List<Cooperativa> buscarPorRangoDescuento(Double descuentoMin, Double descuentoMax) throws IOException {
        return obtenerTodos().stream()
                .filter(cooperativa -> {
                    Double descuento = cooperativa.getDescuento();
                    return descuento >= descuentoMin && descuento <= descuentoMax;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Cooperativa> buscarPorRangoBalance(Double balanceMin, Double balanceMax) throws IOException {
        return obtenerTodos().stream()
                .filter(cooperativa -> {
                    Double balance = cooperativa.getBalance();
                    return balance >= balanceMin && balance <= balanceMax;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Cooperativa> buscarCooperativasActivas() throws IOException {
        return obtenerTodos().stream()
                .filter(cooperativa -> cooperativa.getBalance() > 0)
                .collect(Collectors.toList());
    }
    
    private String cooperativaToString(Cooperativa cooperativa) {
        return cooperativa.getId() + SEPARADOR +
               cooperativa.getIdEmpleado() + SEPARADOR +
               cooperativa.getDescuento() + SEPARADOR +
               cooperativa.getBalance();
    }
    
    private Cooperativa stringToCooperativa(String linea) {
        String[] partes = linea.split(SEPARADOR);
        
        if (partes.length != 4) {
            throw new IllegalArgumentException("Formato de línea inválido: " + linea);
        }
        
        try {
            Integer id = Integer.parseInt(partes[0].trim());
            Integer idEmpleado = Integer.parseInt(partes[1].trim());
            Double descuento = Double.parseDouble(partes[2].trim());
            Double balance = Double.parseDouble(partes[3].trim());
            
            return new Cooperativa(id, idEmpleado, descuento, balance);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error parseando números en línea: " + linea, e);
        }
    }
    
    // Método auxiliar para generar el próximo ID
    private Integer generarProximoId() throws IOException {
        List<Cooperativa> cooperativas = obtenerTodos();
        if (cooperativas.isEmpty()) {
            return 1;
        }
        
        return cooperativas.stream()
                .mapToInt(Cooperativa::getId)
                .max()
                .orElse(0) + 1;
    }
}