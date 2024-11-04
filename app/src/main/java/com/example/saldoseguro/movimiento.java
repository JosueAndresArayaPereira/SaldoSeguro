package com.example.saldoseguro;
public class movimiento {
    private String categoria;
    private double cantidad;
    private String fecha;
    private String cuenta;
    private String tipoMovimiento;

    // Constructor
    public movimiento(String categoria, double cantidad, String fecha, String cuenta, String tipoMovimiento) {
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.cuenta = cuenta;
        this.tipoMovimiento = tipoMovimiento;
    }

    // Getters y setters (si es necesario)
    public String getCategoria() {
        return categoria;
    }

    public double getCantidad() {
        return cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public String getCuenta() {
        return cuenta;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }
}
