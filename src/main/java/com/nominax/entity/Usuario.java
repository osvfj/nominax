package com.nominax.entity;

/**
 *
 * @author osvfj
 */

public class Usuario {
    private String usuario;
    private String password;
    private int nivelDeAcceso;
    private String nombre;
    private String apellidos;
    private String email;

    public Usuario(String usuario, String password, int nivelDeAcceso, String nombre, String apellidos, String email) {
       this.validarAtributos(usuario, password, nivelDeAcceso, nombre, apellidos);
       this.usuario = usuario;
       this.password = password;
       this.nivelDeAcceso = nivelDeAcceso;
       this.nombre = nombre;
       this.apellidos = apellidos;
       this.email = email;
    }

    public Usuario(String usuario, String password, int nivelDeAcceso, String nombre, String apellidos) {
        this(usuario, password, nivelDeAcceso, nombre, apellidos, null);
    }
    
     private void validarAtributos(String usuario, String password, int nivelDeAcceso, String nombre, String apellidos) {
        if (usuario == null || usuario.isEmpty()) {
            throw new IllegalArgumentException("El usuario es obligatorio.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        if (nombre == null || password.isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (apellidos == null || password.isEmpty()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios.");
        }
        if (nivelDeAcceso < 0 || nivelDeAcceso > 1) {
            throw new IllegalArgumentException("El nivel de acceso debe ser 0 (usuario normal) o 1 (administrador).");
        }
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNivelDeAcceso() {
        return nivelDeAcceso;
    }

    public void setNivelDeAcceso(int nivelDeAcceso) {
        this.nivelDeAcceso = nivelDeAcceso;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
