package com.nominax.entity;

/**
 *
 * @author osvfj
 */



public class Departamento {
    private final int id;
    private final String descripcion;

    public Departamento(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
