package pe.prestape.demo.DTOs;


import java.time.LocalDateTime;

public class PrestamosDTO {

    private int idPrestamo;
    private double montoCapital;
    private double interes;
    private String modalidad;
    private int totalCuotas;
    private String estado;
    private double montoDevolver;
    private LocalDateTime fechaCreacion;
    private int idCliente;

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public double getMontoCapital() {
        return montoCapital;
    }

    public void setMontoCapital(double montoCapital) {
        this.montoCapital = montoCapital;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public int getTotalCuotas() {
        return totalCuotas;
    }

    public void setTotalCuotas(int totalCuotas) {
        this.totalCuotas = totalCuotas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getMontoDevolver() {
        return montoDevolver;
    }

    public void setMontoDevolver(double montoDevolver) {
        this.montoDevolver = montoDevolver;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
