package com.nominax.dao.impl;

import com.nominax.dao.PuestoDAO;
import com.nominax.entity.Puesto;
import com.nominax.util.ManejadorArchivos;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author osvfj
 */

public class PuestoDAOFile implements PuestoDAO {

    private static final String NOMBRE_ARCHIVO = "puestos.txt";
    private static final String SEPARADOR = ";";

    
    @Override
    public Optional<Puesto> buscarPorId(int id) throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(NOMBRE_ARCHIVO);
        return lineas.stream()
            .map(this::parsearLineaAPuesto)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(puesto -> puesto.getId() == id)
            .findFirst();
    }

    
    @Override
    public List<Puesto> obtenerTodos() throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(NOMBRE_ARCHIVO);
        return lineas.stream()
            .map(this::parsearLineaAPuesto)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    
    @Override
    public void guardar(Puesto puesto) throws IOException {
        List<Puesto> puestosActuales = obtenerTodos();

        boolean encontrado = false;
        for (int i = 0; i < puestosActuales.size(); i++) {
            if (puestosActuales.get(i).getId() == puesto.getId()) {
                puestosActuales.set(i, puesto);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            puestosActuales.add(puesto);
        }

        List<String> lineasA_Persistir = puestosActuales.stream()
            .map(this::convertirPuestoALinea)
            .collect(Collectors.toList());

        ManejadorArchivos.escribirTodasLasLineas(NOMBRE_ARCHIVO, lineasA_Persistir);
    }

    
    @Override
    public void eliminar(int id) throws IOException {
        List<Puesto> puestosActuales = obtenerTodos();
        List<Puesto> listaActualizada = puestosActuales.stream()
            .filter(puesto -> puesto.getId() != id)
            .collect(Collectors.toList());

        List<String> lineasA_Persistir = listaActualizada.stream()
            .map(this::convertirPuestoALinea)
            .collect(Collectors.toList());

        ManejadorArchivos.escribirTodasLasLineas(NOMBRE_ARCHIVO, lineasA_Persistir);
    }

    private Optional<Puesto> parsearLineaAPuesto(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            return Optional.empty();
        }
        String[] partes = linea.split(SEPARADOR);
        if (partes.length == 2) {
            try {
                int id = Integer.parseInt(partes[0].trim());
                String nombre = partes[1].trim();
                return Optional.of(new Puesto(id, nombre));
            } catch (NumberFormatException e) {
                System.err.println("Error de formato de número al parsear ID de puesto en línea: " + linea + " - " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("Error de validación al crear Puesto desde línea: " + linea + " - " + e.getMessage());
            }
        } else {
            System.err.println("Línea de puesto con formato incorrecto, se esperaban 2 partes usando '" + SEPARADOR + "': " + linea);
        }
        return Optional.empty();
    }

    private String convertirPuestoALinea(Puesto puesto) {
        return String.join(SEPARADOR,
            String.valueOf(puesto.getId()),
            puesto.getNombre()
        );
    }
}