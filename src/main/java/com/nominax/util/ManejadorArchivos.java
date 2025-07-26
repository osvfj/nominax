package com.nominax.util;

/**
 *
 * @author osvfj
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

public final class ManejadorArchivos {

    private  ManejadorArchivos() {
    }

  
    public static List<String> leerTodasLasLineas(String rutaArchivo) throws IOException {
        Path ruta = Paths.get(rutaArchivo);

        if (!Files.exists(ruta)) {
            Files.createDirectories(ruta.getParent());
            return Collections.emptyList();
        }

        return Files.readAllLines(ruta);
    }


    public static void escribirTodasLasLineas(String rutaArchivo, List<String> lineas) throws IOException {
        Path ruta = Paths.get(rutaArchivo);
        Files.createDirectories(ruta.getParent());

        Files.write(ruta, lineas, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static void agregarLinea(String rutaArchivo, String linea) throws IOException {
        Path ruta = Paths.get(rutaArchivo);

        Files.createDirectories(ruta.getParent());

        String lineaConSalto = linea + System.lineSeparator();

        Files.write(ruta, lineaConSalto.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}