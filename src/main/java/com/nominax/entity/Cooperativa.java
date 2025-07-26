package com.nominax.entity;

/**
 *
 * @author osvfj
 */

public class Cooperativa {
    private String id;
    private Double porcentajeDesc;
    private Double balanceAcum;

    public Cooperativa(String id, Double porcentajeDesc) {
        this.id = id;
        this.porcentajeDesc = porcentajeDesc;
        this.balanceAcum = null;
    }

    public Cooperativa(String id, Double porcentajeDesc, Double balanceAcum) {
        this.id = id;
        this.porcentajeDesc = porcentajeDesc;
        this.balanceAcum = balanceAcum;
    }

    public String getId() {
        return id;
    }

    public Double getPorcentajeDesc() {
        return porcentajeDesc;
    }

    public Double getBalanceAcum() {
        return balanceAcum;
    }

    public void setBalanceAcum(Double balanceAcum) {
        this.balanceAcum = balanceAcum;
    }
}
