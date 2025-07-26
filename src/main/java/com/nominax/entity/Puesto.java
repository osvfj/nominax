package com.nominax.entity;

/**
 *
 * @author osvfj
 */

public class Puesto {
    private String id;
    private String nombre;

    public Puesto(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
