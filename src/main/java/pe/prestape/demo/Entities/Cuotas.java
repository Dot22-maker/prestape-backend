package pe.prestape.demo.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "cuotas")
public class Cuotas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuota")
    private int idCuota;

    @Column(name = "numero_cuota", nullable = false)
    private int numeroCuota;

    @Column(name = "monto_cuota", nullable = false)
    private double montoCuota;

    @Column(name = "monto_pagado", nullable = false)
    private double montoPagado;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "google_event", nullable = false)
    private String googleEvent;

    @ManyToOne
    @JoinColumn(name = "id_prestamo", nullable = false)
    private Prestamos prestamo;

    public Cuotas() {
    }

    public Cuotas(int idCuota, int numeroCuota, double montoCuota, double montoPagado, LocalDate fechaVencimiento, String estado, String googleEvent, Prestamos prestamo) {
        this.idCuota = idCuota;
        this.numeroCuota = numeroCuota;
        this.montoCuota = montoCuota;
        this.montoPagado = montoPagado;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.googleEvent = googleEvent;
        this.prestamo = prestamo;
    }

    public int getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public int getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public double getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(double montoCuota) {
        this.montoCuota = montoCuota;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getGoogleEvent() {
        return googleEvent;
    }

    public void setGoogleEvent(String googleEvent) {
        this.googleEvent = googleEvent;
    }

    public Prestamos getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamos prestamo) {
        this.prestamo = prestamo;
    }
}
