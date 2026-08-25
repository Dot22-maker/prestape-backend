package pe.prestape.demo.Entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre_usuario", nullable = false, length = 30, unique = true)
    private String username;

    @Column(length = 200, nullable = false)
    private String password;

    private Boolean estado;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Roles> roles;

    public Users() {
    }

    public Users(Long id, String username, String password, Boolean estado, List<Roles> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.estado = estado;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }
}