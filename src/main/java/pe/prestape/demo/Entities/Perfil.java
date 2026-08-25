package pe.prestape.demo.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "perfil")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private int idPerfil;

    @Column(name = "nombre_completo", length = 100, nullable = false)
    private String nombreCompleto;

    @Column(name = "email", length = 150, nullable = false)
    private String email;

    @Column(name = "provincia", length = 150, nullable = false)
    private String provincia;

    @Column(name = "distrito", length = 100, nullable = false)
    private String distrito;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fecha_nacimiento;

    @Column(name = "dni", length = 10, nullable = false)
    private String dni;

    @Column(name = "telefono", length = 15, nullable = false)
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Users usuario;


    public Perfil() {
    }

    public Perfil(int idPerfil, String nombreCompleto, String email, String provincia, String distrito, LocalDate fecha_nacimiento, String dni, String telefono, Users usuario) {
        this.idPerfil = idPerfil;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.provincia = provincia;
        this.distrito = distrito;
        this.fecha_nacimiento = fecha_nacimiento;
        this.dni = dni;
        this.telefono = telefono;
        this.usuario = usuario;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Users getUsuario() {
        return usuario;
    }

    public void setUsuario(Users usuario) {
        this.usuario = usuario;
    }
}