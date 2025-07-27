package com.nominax.dao;

/**
 *
 * @author osvfj
 */


import com.nominax.entity.Usuario;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface UsuarioDAO {
    Optional<Usuario> buscarPorUsuario(String usuario) throws IOException;
    List<Usuario> obtenerTodos() throws IOException;
    void guardar(Usuario usuario) throws IOException;
    void actualizar(Usuario usuario, boolean cambiarPassword) throws IOException;
    void eliminar(String usuario) throws IOException;
    Optional<Usuario> validarCredenciales(String usuario, String password) throws IOException;
}