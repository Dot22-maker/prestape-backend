package pe.prestape.demo.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prestamos")
public class Prestamos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private int idPrestamo;

    @Column(name = "monto_capital", nullable = false)
    private double montoCapital;

    @Column(name = "porcentaje_interes", nullable = false)
    private double interes;

    @Column(name = "modalidad", length = 20, nullable = false)
    private String modalidad;

    @Column(name = "total_cuotas", nullable = false)
    private int totalCuotas;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "monto_devolver", nullable = false)
    private double montoDevolver;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Clientes cliente;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        // Cálculo automático previo a la inserción
        this.montoDevolver = this.montoCapital + (this.montoCapital * (this.interes / 100.0));
    }

    public Prestamos() {
    }

    public Prestamos(int idPrestamo, double montoCapital, double interes, String modalidad, int totalCuotas, String estado, double montoDevolver, LocalDateTime fechaCreacion, Clientes cliente) {
        this.idPrestamo = idPrestamo;
        this.montoCapital = montoCapital;
        this.interes = interes;
        this.modalidad = modalidad;
        this.totalCuotas = totalCuotas;
        this.estado = estado;
        this.montoDevolver = montoDevolver;
        this.fechaCreacion = fechaCreacion;
        this.cliente = cliente;
    }

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

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }
}