package com.nominax.entity;

/**
 *
 * @author osvfj
 */


import java.util.Date;

public class Empleado {
    private String id; 
    private String nombre; 
    private String apellidoPat; 
    private String apellidoMat; 
    private String direccion; 
    private Integer telefono; 
    private Boolean sexo; 
    private Integer idDepto; 
    private Date fechaIngreso; 
    private Integer idPuesto; 
    private Boolean coop; 
    private Double salario; 

    public Empleado(String id, String nombre, String apellidoPat, 
                    String apellidoMat, String direccion, Integer telefono, 
                    Boolean sexo, Integer idDepto, Date fechaIngreso, 
                    Integer idPuesto, Boolean coop, Double salario) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoPat = apellidoPat;
        this.apellidoMat = apellidoMat;
        this.direccion = direccion;
        this.telefono = telefono;
        this.sexo = sexo;
        this.idDepto = idDepto;
        this.fechaIngreso = fechaIngreso;
        this.idPuesto = idPuesto;
        this.coop = coop;
        this.salario = salario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPat() {
        return apellidoPat;
    }

    public void setApellidoPat(String apellidoPat) {
        this.apellidoPat = apellidoPat;
    }

    public String getApellidoMat() {
        return apellidoMat;
    }

    public void setApellidoMat(String apellidoMat) {
        this.apellidoMat = apellidoMat;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public Boolean getSexo() {
        return sexo;
    }

    public void setSexo(Boolean sexo) {
        this.sexo = sexo;
    }

    public Integer getIdDepto() {
        return idDepto;
    }

    public void setIdDepto(Integer idDepto) {
        this.idDepto = idDepto;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Integer idPuesto) {
        this.idPuesto = idPuesto;
    }

    public Boolean getCoop() {
        return coop;
    }

    public void setCoop(Boolean coop) {
        this.coop = coop;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }
}
