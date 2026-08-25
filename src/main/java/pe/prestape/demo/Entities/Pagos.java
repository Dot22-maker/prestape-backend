package pe.prestape.demo.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "pagos")
public class Pagos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private int idPago;

    @Column(name = "monto_recibido", nullable = false)
    private double montoRecibido;

    @Column(name = "metodo_pago", length = 20, nullable = false)
    private String metodoPago;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @ManyToOne
    @JoinColumn(name = "id_cuota", nullable = false)
    private Cuotas cuotas;

    @PrePersist
    public void prePersist() {
        this.fechaPago = LocalDate.now();
    }

    public Pagos() {
    }

    public Pagos(int idPago, double montoRecibido, String metodoPago, LocalDate fechaPago, Cuotas cuotas) {
        this.idPago = idPago;
        this.montoRecibido = montoRecibido;
        this.metodoPago = metodoPago;
        this.fechaPago = fechaPago;
        this.cuotas = cuotas;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public double getMontoRecibido() {
        return montoRecibido;
    }

    public void setMontoRecibido(double montoRecibido) {
        this.montoRecibido = montoRecibido;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Cuotas getCuotas() {
        return cuotas;
    }

    public void setCuotas(Cuotas cuotas) {
        this.cuotas = cuotas;
    }
}
