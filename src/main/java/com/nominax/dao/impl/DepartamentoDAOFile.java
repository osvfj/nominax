package com.nominax.dao.impl;
        
        
import com.nominax.dao.DepartamentoDAO;
import com.nominax.entity.Departamento;
import com.nominax.util.ManejadorArchivos;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



public class DepartamentoDAOFile implements DepartamentoDAO {

    private static final String NOMBRE_ARCHIVO = "departamentos.txt";
    private static final String SEPARADOR = ";";

    @Override
    public Optional<Departamento> buscarPorId(int id) throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(NOMBRE_ARCHIVO);
        return lineas.stream()
            .map(this::parsearLineaADepartamento)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(departamento -> departamento.getId() == id)
            .findFirst();
    }

    @Override
    public List<Departamento> obtenerTodos() throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(NOMBRE_ARCHIVO);
        return lineas.stream()
            .map(this::parsearLineaADepartamento)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    @Override
    public void guardar(Departamento departamento) throws IOException {
        List<Departamento> departamentosActuales = obtenerTodos();

        boolean encontrado = false;
        for (int i = 0; i < departamentosActuales.size(); i++) {
            if (departamentosActuales.get(i).getId() == departamento.getId()) {
                departamentosActuales.set(i, departamento);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            departamentosActuales.add(departamento);
        }

        List<String> lineasA_Persistir = departamentosActuales.stream()
            .map(this::convertirDepartamentoALinea)
            .collect(Collectors.toList());

        ManejadorArchivos.escribirTodasLasLineas(NOMBRE_ARCHIVO, lineasA_Persistir);
    }

    @Override
    public void eliminar(int id) throws IOException {
        List<Departamento> departamentosActuales = obtenerTodos();
        List<Departamento> listaActualizada = departamentosActuales.stream()
            .filter(departamento -> departamento.getId() != id)
            .collect(Collectors.toList());

        List<String> lineasA_Persistir = listaActualizada.stream()
            .map(this::convertirDepartamentoALinea)
            .collect(Collectors.toList());

        ManejadorArchivos.escribirTodasLasLineas(NOMBRE_ARCHIVO, lineasA_Persistir);
    }

    private Optional<Departamento> parsearLineaADepartamento(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            return Optional.empty();
        }
        String[] partes = linea.split(SEPARADOR);
        if (partes.length == 2) {
            try {
                int id = Integer.parseInt(partes[0].trim());
                String descripcion = partes[1].trim();
                return Optional.of(new Departamento(id, descripcion));
            } catch (NumberFormatException e) {
                System.err.println("Error de formato de número al parsear ID de departamento en línea: " + linea + " - " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("Error de validación al crear Departamento desde línea: " + linea + " - " + e.getMessage());
            }
        } else {
            System.err.println("Línea de departamento con formato incorrecto, se esperaban 2 partes: " + linea);
        }
        return Optional.empty();
    }

    private String convertirDepartamentoALinea(Departamento departamento) {
        return String.join(SEPARADOR,
            String.valueOf(departamento.getId()),
            departamento.getDescripcion()
        );
    }
}