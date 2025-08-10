package com.nominax.entity;
import java.util.Date;


/**
 *
 * @author osvfj
 */

public class Nomina {
    private String idEmpleado;
    private Date fecha;
    private Double salarioBase;
    private Double descuentoARS;
    private Double descuentoAFP;
    private Double descuentoCooperativa;
    private Double descuentoISR;
    private Double salarioNeto;
    private Boolean statusNomina;

    public Nomina() {}

    public Nomina(String idEmpleado, Date fecha, Double salarioBase, 
                  Double descuentoARS, Double descuentoAFP, 
                  Double descuentoCooperativa, Double descuentoISR, 
                  Double salarioNeto, Boolean statusNomina) {
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.salarioBase = salarioBase;
        this.descuentoARS = descuentoARS;
        this.descuentoAFP = descuentoAFP;
        this.descuentoCooperativa = descuentoCooperativa;
        this.descuentoISR = descuentoISR;
        this.salarioNeto = salarioNeto;
        this.statusNomina = statusNomina;
    }

    // Getters y setters
    public String getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(String idEmpleado) { this.idEmpleado = idEmpleado; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Double getSalarioBase() { return salarioBase; }
    public void setSalarioBase(Double salarioBase) { this.salarioBase = salarioBase; }

    public Double getDescuentoARS() { return descuentoARS; }
    public void setDescuentoARS(Double descuentoARS) { this.descuentoARS = descuentoARS; }

    public Double getDescuentoAFP() { return descuentoAFP; }
    public void setDescuentoAFP(Double descuentoAFP) { this.descuentoAFP = descuentoAFP; }

    public Double getDescuentoCooperativa() { return descuentoCooperativa; }
    public void setDescuentoCooperativa(Double descuentoCooperativa) { 
        this.descuentoCooperativa = descuentoCooperativa; 
    }

    public Double getDescuentoISR() { return descuentoISR; }
    public void setDescuentoISR(Double descuentoISR) { this.descuentoISR = descuentoISR; }

    public Double getSalarioNeto() { return salarioNeto; }
    public void setSalarioNeto(Double salarioNeto) { this.salarioNeto = salarioNeto; }

    public Boolean getStatusNomina() { return statusNomina; }
    public void setStatusNomina(Boolean statusNomina) { this.statusNomina = statusNomina; }
}