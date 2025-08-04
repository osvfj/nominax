package com.nominax.dao.impl;

import com.nominax.dao.CooperativaDAO;
import com.nominax.entity.Cooperativa;
import java.io.*;
import java.util.*;

public class CooperativaDAOFile implements CooperativaDAO {
    
    private static final String ARCHIVO_COOPERATIVAS = "cooperativas.txt";
    private static final String SEPARADOR = ",";
    
    public CooperativaDAOFile() {
        crearArchivoSiNoExiste();
    }
    
    private void crearArchivoSiNoExiste() {
        File archivo = new File(ARCHIVO_COOPERATIVAS);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creando archivo de cooperativas: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void guardar(Cooperativa cooperativa) throws IOException {
        // Verificar si ya existe
        Optional<Cooperativa> existente = buscarPorIdEmpleado(cooperativa.getIdEmpleado());
        if (existente.isPresent()) {
            throw new IOException("Ya existe una cooperativa para el empleado ID: " + cooperativa.getIdEmpleado());
        }
        
        try (FileWriter fw = new FileWriter(ARCHIVO_COOPERATIVAS, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            pw.println(cooperativaToString(cooperativa));
        }
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
        List<Cooperativa> cooperativas = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_COOPERATIVAS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    try {
                        Cooperativa cooperativa = stringToCooperativa(linea);
                        cooperativas.add(cooperativa);
                    } catch (Exception e) {
                        System.err.println("Error parseando línea: " + linea + " - " + e.getMessage());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Si el archivo no existe, retornar lista vacía
            return cooperativas;
        }
        
        return cooperativas;
    }
    
    @Override
    public void actualizar(Cooperativa cooperativa) throws IOException {
        List<Cooperativa> cooperativas = obtenerTodos();
        boolean encontrado = false;
        
        for (int i = 0; i < cooperativas.size(); i++) {
            if (cooperativas.get(i).getIdEmpleado().equals(cooperativa.getIdEmpleado())) {
                cooperativas.set(i, cooperativa);
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) {
            throw new IOException("Cooperativa no encontrada para el empleado ID: " + cooperativa.getIdEmpleado());
        }
        
        guardarTodos(cooperativas);
    }
    
    @Override
    public void eliminar(Integer idEmpleado) throws IOException {
        List<Cooperativa> cooperativas = obtenerTodos();
        boolean eliminado = cooperativas.removeIf(c -> c.getIdEmpleado().equals(idEmpleado));
        
        if (!eliminado) {
            throw new IOException("Cooperativa no encontrada para el empleado ID: " + idEmpleado);
        }
        
        guardarTodos(cooperativas);
    }
    
    private void guardarTodos(List<Cooperativa> cooperativas) throws IOException {
        try (FileWriter fw = new FileWriter(ARCHIVO_COOPERATIVAS, false);
             PrintWriter pw = new PrintWriter(fw)) {
            
            for (Cooperativa cooperativa : cooperativas) {
                pw.println(cooperativaToString(cooperativa));
            }
        }
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
    
    // Método sobrecargado de guardar que genera automáticamente el ID
    public void guardarConIdAutomatico(Cooperativa cooperativa) throws IOException {
        if (cooperativa.getId() == null) {
            cooperativa.setId(generarProximoId());
        }
        guardar(cooperativa);
    }
}