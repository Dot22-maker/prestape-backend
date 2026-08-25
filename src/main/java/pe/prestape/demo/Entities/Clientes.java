package pe.prestape.demo.Entities;

import jakarta.persistence.*;
import lombok.Cleanup;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private int idCliente;

    @Column(name = "nombre_completo", length = 150, nullable = false)
    private String nombreCompleto;

    @Column(name = "telefono", length = 15, nullable = false)
    private String telefono;

    @Column(name = "dni", length = 8)
    private String dni;

    @Column(name = "genero", length = 10, nullable = false)
    private String genero;

    @Column(name = "direccion", length = 100)
    private String direccion;

    @Column(name = "nacionalidad", length = 100, nullable = false)
    private String nacionalidad;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "notas", length = 200)
    private String notas;

    @Column(name = "estado", nullable = false)
    private String estado;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "id_usuario", nullable = true)
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private Users usuario;

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDate.now();
    }
    public Clientes() {
    }

    public Clientes(int idCliente, String nombreCompleto, String telefono, String dni, String genero, String direccion, String nacionalidad, LocalDate fechaRegistro, String notas, String estado, Users usuario) {
        this.idCliente = idCliente;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.dni = dni;
        this.genero = genero;
        this.direccion = direccion;
        this.nacionalidad = nacionalidad;
        this.fechaRegistro = fechaRegistro;
        this.notas = notas;
        this.estado = estado;
        this.usuario = usuario;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Users getUsuario() {
        return usuario;
    }

    public void setUsuario(Users usuario) {
        this.usuario = usuario;
    }
}
