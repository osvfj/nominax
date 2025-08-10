package com.nominax.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author osvfj
 */

public class ExploradorArchivos {

    public static void abrirExploradorEnRuta(String ruta) {
        try {
            File directorio = new File(ruta);

            if (directorio.exists() && directorio.isDirectory()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(directorio);
                } else {
                    System.out.println("La funcionalidad Desktop no es soportada en este sistema.");
                    JOptionPane.showMessageDialog(null, 
                        "La función de abrir el explorador no es soportada por su sistema operativo.", 
                        "Error de Compatibilidad", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("La ruta especificada no existe o no es un directorio: " + ruta);
                JOptionPane.showMessageDialog(null, 
                    "El directorio de destino no existe o no es válido:\n" + ruta, 
                    "Error de Ruta", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            System.err.println("Error de I/O al intentar abrir el explorador: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Ocurrió un error al intentar abrir el explorador de archivos.", 
                "Error de Sistema", JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException e) {
            System.err.println("Error de seguridad al intentar abrir el explorador: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Permisos insuficientes para abrir el explorador de archivos.", 
                "Error de Seguridad", JOptionPane.ERROR_MESSAGE);
        }
    }
}