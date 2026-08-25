package pe.prestape.demo.Entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rol", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_usuario", "tipo_rol"})})
public class Roles implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long id;

    @Column(name = "tipo_rol", length = 30, nullable = false)
    private String rol;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Users user;

    public Roles() {
    }

    public Roles(Long id, String rol, Users user) {
        this.id = id;
        this.rol = rol;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}