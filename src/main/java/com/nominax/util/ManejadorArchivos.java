package com.nominax.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author osvfj
 */

public final class ManejadorArchivos {

    static String userHome = System.getProperty("user.home");
    static String nombreDelDirectorio = "nominax";

    private ManejadorArchivos() {
        
    }
    
    public static String obtenerRutaBase() {
        return userHome + File.separator + nombreDelDirectorio;
    }

    public static void crearDirectorioSiNoExiste(String rutaDirectorio) {
        File directorio = new File(obtenerRutaBase(), rutaDirectorio);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }
    
    public static boolean isDirectorioInicializado() {
        File directorioBase = new File(userHome + File.separator + nombreDelDirectorio);
        return directorioBase.exists() && directorioBase.isDirectory();
    }

    public static List<String> leerTodasLasLineas(String rutaArchivo) throws IOException {
        File archivo = new File(
            obtenerRutaBase(),
            rutaArchivo
        );

        if (!archivo.exists()) {
            File directorioPadre = archivo.getParentFile();
            if (directorioPadre != null) {
                directorioPadre.mkdirs();
            }
            return Collections.emptyList();
        }

        List<String> lineas = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                lineas.add(linea);
            }
        }
        return lineas;
    }

    public static void escribirTodasLasLineas(
        String rutaArchivo,
        List<String> lineas
    ) throws IOException {
        File archivo = new File(
            userHome + File.separator + nombreDelDirectorio,
            rutaArchivo
        );

        File directorioPadre = archivo.getParentFile();
        if (directorioPadre != null) {
            directorioPadre.mkdirs();
        }

        try (
            BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo, false))
        ) {
            for (int i = 0; i < lineas.size(); i++) {
                escritor.write(lineas.get(i));
                if (i < lineas.size() - 1) {
                    escritor.newLine();
                }
            }
        }
    }

    public static void agregarLinea(String rutaArchivo, String linea)
        throws IOException {
        File archivo = new File(
            userHome + File.separator + nombreDelDirectorio,
            rutaArchivo
        );

        File directorioPadre = archivo.getParentFile();
        if (directorioPadre != null) {
            directorioPadre.mkdirs();
        }

        try (
            BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo, true))
        ) {
            escritor.write(linea);
            escritor.newLine();
        }
    }
}