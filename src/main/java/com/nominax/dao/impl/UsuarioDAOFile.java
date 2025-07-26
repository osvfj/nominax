package com.nominax.dao.impl;

/**
 *
 * @author osvfj
 */

import com.nominax.dao.UsuarioDAO;
import com.nominax.entity.Usuario;
import com.nominax.util.ManejadorArchivos;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mindrot.jbcrypt.BCrypt;


public class UsuarioDAOFile implements UsuarioDAO {

    private static final String NOMBRE_ARCHIVO = "c:/nominax/usuarios.txt";
    private static final String SEPARADOR = ";";

    @Override
    public Optional<Usuario> buscarPorUsuario(String usuario) throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(NOMBRE_ARCHIVO);
        return lineas.stream()
            .map(this::parsearLineaAUsuario) // Intenta convertir cada línea a un objeto Usuario
            .filter(Optional::isPresent) // Filtra las líneas que no pudieron ser parseadas
            .map(Optional::get) // Obtiene el objeto Usuario del Optional
            .filter(user -> user.getUsuario().equalsIgnoreCase(usuario)) // Filtra por login
            .findFirst(); // Devuelve el primer usuario que coincida (o vacío si no hay)
    }

    @Override
    public List<Usuario> obtenerTodos() throws IOException {
        List<String> lineas = ManejadorArchivos.leerTodasLasLineas(NOMBRE_ARCHIVO);
        return lineas.stream()
            .map(this::parsearLineaAUsuario)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }
    
    @Override
    public void guardar(Usuario usuario) throws IOException {
        List<Usuario> usuariosActuales = obtenerTodos(); 
        
        boolean encontrado = false;
        // Busca si el usuario ya existe para actualizarlo
        for (int i = 0; i < usuariosActuales.size(); i++) {
            if (usuariosActuales.get(i).getUsuario().equalsIgnoreCase(usuario.getUsuario())) {
                String hashedPassword = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt(10));
                usuario.setPassword(hashedPassword);
                usuariosActuales.set(i, usuario); 
                encontrado = true;
                break;
            }
        }
        

        if (!encontrado) {
            String hashedPassword = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt(10));
            usuario.setPassword(hashedPassword);
            usuariosActuales.add(usuario); 
        }
        
        List<String> lineasaAguardar = usuariosActuales.stream()
            .map(this::convertirUsuarioALinea) 
            .collect(Collectors.toList());

        ManejadorArchivos.escribirTodasLasLineas(NOMBRE_ARCHIVO, lineasaAguardar);
    }

    @Override
    public void eliminar(String usuario) throws IOException {
        List<Usuario> usuariosActuales = obtenerTodos();
        List<Usuario> listaActualizada = usuariosActuales.stream()
            .filter(user -> !user.getUsuario().equalsIgnoreCase(usuario))
            .collect(Collectors.toList());

        List<String> lineasaAguardar = listaActualizada.stream()
            .map(this::convertirUsuarioALinea)
            .collect(Collectors.toList());

        ManejadorArchivos.escribirTodasLasLineas(NOMBRE_ARCHIVO, lineasaAguardar);
    }

    @Override
    public Optional<Usuario> validarCredenciales(String usuario, String password) throws IOException {
        return buscarPorUsuario(usuario)
            .filter(user ->  BCrypt.checkpw(password, user.getPassword())); 
    }

   
    private Optional<Usuario> parsearLineaAUsuario(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            return Optional.empty();
        }
        String[] partes = linea.split(SEPARADOR);
        if (partes.length == 6) {
            try {
                int nivelAcceso = Integer.parseInt(partes[2]);

                Usuario usuario = new Usuario(
                    partes[0], // usuario
                    partes[1], // password
                    nivelAcceso, // nivelAcceso
                    partes[3], // nombre
                    partes[4], // apellidos
                    partes[5]  // email
                );
                return Optional.of(usuario);
            } catch (NumberFormatException e) {
                System.err.println("Error de formato de número al parsear línea de usuario: " + linea + " - " + e.getMessage());
            }
        } else {
            System.err.println("Línea de usuario con formato incorrecto, se esperaban 6 partes: " + linea);
        }
        return Optional.empty();
    }

   
    private String convertirUsuarioALinea(Usuario usuario) {
        return String.join(SEPARADOR,
            usuario.getUsuario(),
            usuario.getPassword(),
            String.valueOf(usuario.getNivelDeAcceso()),
            usuario.getNombre(),
            usuario.getApellidos(),
            usuario.getEmail()
        );
    }
}