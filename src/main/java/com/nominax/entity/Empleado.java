package com.nominax.entity;

import java.util.Date;

public class Empleado {
    private String id;
    private String nombre;
    private String apellidoPat;
    private String apellidoMat;
    private String direccion;
    private Long telefono;
    private Boolean sexo;
    private Integer idDepto;
    private Date fechaIngreso;
    private Integer idPuesto;
    private Boolean coop;
    private Double salario;

    public Empleado(String id, String nombre, String apellidoPat,
                    String apellidoMat, String direccion, Long telefono,
                    Boolean sexo, Integer idDepto, Date fechaIngreso,
                    Integer idPuesto, Boolean coop, Double salario) {
        validarAtributos(id, nombre, apellidoPat, apellidoMat, direccion, telefono,
                         sexo, idDepto, fechaIngreso, idPuesto, coop, salario);

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

    private void validarAtributos(String id, String nombre, String apellidoPat,
                                  String apellidoMat, String direccion, Long telefono,
                                  Boolean sexo, Integer idDepto, Date fechaIngreso,
                                  Integer idPuesto, Boolean coop, Double salario) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del empleado es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del empleado es obligatorio.");
        }
        if (apellidoPat == null || apellidoPat.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido paterno es obligatorio.");
        }
        if (apellidoMat == null || apellidoMat.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido materno es obligatorio.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }
        if (telefono == null || telefono <= 0) {
            throw new IllegalArgumentException("El teléfono es obligatorio y debe ser un número válido.");
        }
        if (sexo == null) {
            throw new IllegalArgumentException("El sexo es obligatorio.");
        }
        if (idDepto == null || idDepto <= 0) {
            throw new IllegalArgumentException("El ID de departamento es obligatorio y debe ser un número válido.");
        }
        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso es obligatoria.");
        }
        if (idPuesto == null || idPuesto <= 0) {
            throw new IllegalArgumentException("El ID de puesto es obligatorio y debe ser un número válido.");
        }
        if (coop == null) {
            throw new IllegalArgumentException("El atributo 'coop' es obligatorio.");
        }
        if (salario == null || salario <= 0) {
            throw new IllegalArgumentException("El salario es obligatorio y debe ser un número positivo.");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del empleado no puede ser nulo o vacío.");
        }
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del empleado no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
    }

    public String getApellidoPat() {
        return apellidoPat;
    }

    public void setApellidoPat(String apellidoPat) {
        if (apellidoPat == null || apellidoPat.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido paterno no puede ser nulo o vacío.");
        }
        this.apellidoPat = apellidoPat;
    }

    public String getApellidoMat() {
        return apellidoMat;
    }

    public void setApellidoMat(String apellidoMat) {
        if (apellidoMat == null || apellidoMat.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido materno no puede ser nulo o vacío.");
        }
        this.apellidoMat = apellidoMat;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede ser nula o vacía.");
        }
        this.direccion = direccion;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        if (telefono == null || telefono <= 0) {
            throw new IllegalArgumentException("El teléfono no puede ser nulo o negativo.");
        }
        this.telefono = telefono;
    }

    public Boolean getSexo() {
        return sexo;
    }

    public void setSexo(Boolean sexo) {
        if (sexo == null) {
            throw new IllegalArgumentException("El sexo no puede ser nulo.");
        }
        this.sexo = sexo;
    }

    public Integer getIdDepto() {
        return idDepto;
    }

    public void setIdDepto(Integer idDepto) {
        if (idDepto == null || idDepto <= 0) {
            throw new IllegalArgumentException("El ID de departamento no puede ser nulo o negativo.");
        }
        this.idDepto = idDepto;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser nula.");
        }
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Integer idPuesto) {
        if (idPuesto == null || idPuesto <= 0) {
            throw new IllegalArgumentException("El ID de puesto no puede ser nulo o negativo.");
        }
        this.idPuesto = idPuesto;
    }

    public Boolean getCoop() {
        return coop;
    }

    public void setCoop(Boolean coop) {
        if (coop == null) {
            throw new IllegalArgumentException("El atributo 'coop' no puede ser nulo.");
        }
        this.coop = coop;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        if (salario == null || salario <= 0) {
            throw new IllegalArgumentException("El salario no puede ser nulo o negativo.");
        }
        this.salario = salario;
    }
}