package pe.prestape.demo.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "calificacion")
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calificacion")
    private int idCalificacion;

    @Column(name = "calificacion", nullable = false)
    private double calificacion;

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "id_cliente")
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private Clientes cliente;

    public Calificacion() {
    }

    public Calificacion(int idCalificacion, double calificacion, Clientes cliente) {
        this.idCalificacion = idCalificacion;
        this.calificacion = calificacion;
        this.cliente = cliente;
    }

    public int getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(int idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }
}
