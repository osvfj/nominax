package com.nominax.entity;

/**
 *
 * @author osvfj
 */

public class Puesto {
    private int id;
    private String nombre;

    public Puesto(int id, String nombre) {
        validarAtributos(id, nombre);
        this.id = id;
        this.nombre = nombre;
    }

    private void validarAtributos(int id, String nombre) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del puesto es obligatorio y debe ser positivo.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del puesto es obligatorio.");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del puesto no puede ser cero o negativo.");
        }
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del puesto no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
    }
}