/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nominax.entity;
import java.util.Date;


/**
 *
 * @author osvfj
 */


public class Nomina {
    private Integer idNomina;
    private String idEmpleado;
    private Date fechaNomina;
    private Double salarioBruto;
    private Double valorAFP;
    private Double valorARS;
    private Double valorCoop;
    private Double valorISR;
    private Double sueldoNeto;
    private Boolean statusNomina;

    public Nomina(Integer idNomina, String idEmpleado) {
        this.idNomina = idNomina;
        this.idEmpleado = idEmpleado;
    }

    public Nomina(Integer idNomina, String idEmpleado, Date fechaNomina, Double salarioBruto, 
                  Double valorAFP, Double valorARS, Double valorCoop, Double valorISR, 
                  Double sueldoNeto, Boolean statusNomina) {
        this.idNomina = idNomina;
        this.idEmpleado = idEmpleado;
        this.fechaNomina = fechaNomina;
        this.salarioBruto = salarioBruto;
        this.valorAFP = valorAFP;
        this.valorARS = valorARS;
        this.valorCoop = valorCoop;
        this.valorISR = valorISR;
        this.sueldoNeto = sueldoNeto;
        this.statusNomina = statusNomina;
    }

    public Integer getIdNomina() {
        return idNomina;
    }

    public void setIdNomina(Integer idNomina) {
        this.idNomina = idNomina;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Date getFechaNomina() {
        return fechaNomina;
    }

    public void setFechaNomina(Date fechaNomina) {
        this.fechaNomina = fechaNomina;
    }

    public Double getSalarioBruto() {
        return salarioBruto;
    }

    public void setSalarioBruto(Double salarioBruto) {
        this.salarioBruto = salarioBruto;
    }

    public Double getValorAFP() {
        return valorAFP;
    }

    public void setValorAFP(Double valorAFP) {
        this.valorAFP = valorAFP;
    }

    public Double getValorARS() {
        return valorARS;
    }

    public void setValorARS(Double valorARS) {
        this.valorARS = valorARS;
    }

    public Double getValorCoop() {
        return valorCoop;
    }

    public void setValorCoop(Double valorCoop) {
        this.valorCoop = valorCoop;
    }

    public Double getValorISR() {
        return valorISR;
    }

    public void setValorISR(Double valorISR) {
        this.valorISR = valorISR;
    }

    public Double getSueldoNeto() {
        return sueldoNeto;
    }

    public void setSueldoNeto(Double sueldoNeto) {
        this.sueldoNeto = sueldoNeto;
    }

    public Boolean getStatusNomina() {
        return statusNomina;
    }

    public void setStatusNomina(Boolean statusNomina) {
        this.statusNomina = statusNomina;
    }
}
