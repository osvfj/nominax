package com.nominax.entity;

import java.util.Objects;

/**
 *
 * @author osvfj
 */


public class Cooperativa {
    private Integer id;
    private Integer idEmpleado;
    private Double descuento;
    private Double balance;
    
    public Cooperativa(Integer id, Integer idEmpleado, Double descuento, Double balance) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.descuento = descuento;
        this.balance = balance;
    }
    
    public Cooperativa(Integer idEmpleado, Double descuento) {
        this.idEmpleado = idEmpleado;
        this.descuento = descuento;
        this.balance = 0.0; 
    }
    
    public Cooperativa() {
        this.balance = 0.0;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getIdEmpleado() {
        return idEmpleado;
    }
    
    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    
    public Double getDescuento() {
        return descuento;
    }
    
    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }
    
    public Double getBalance() {
        return balance;
    }
    
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    
    @Override
    public String toString() {
        return "Cooperativa{" +
                "id=" + id +
                ", idEmpleado=" + idEmpleado +
                ", descuento=" + descuento +
                ", balance=" + balance +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Cooperativa that = (Cooperativa) o;
        return Objects.equals(id, that.id) && 
               Objects.equals(idEmpleado, that.idEmpleado);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, idEmpleado);
    }
}