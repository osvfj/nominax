package com.nominax.entity;

/**
 *
 * @author osvfj
 */



public class Departamento {
    private int id;
    private String descripcion;

    public Departamento(int id, String descripcion) {
        
        this.validarAtributos(id, descripcion);
        this.id = id;
        this.descripcion = descripcion;
    }
    
      private void validarAtributos(int id, String descripcion) {
        if(descripcion == null || descripcion.isEmpty()){
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    public void setId(int id) {
        this.id = id; 
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
