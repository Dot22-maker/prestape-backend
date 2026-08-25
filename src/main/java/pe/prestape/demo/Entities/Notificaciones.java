package pe.prestape.demo.Entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notificaciones")
public class Notificaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private int idNotificacion;

    @Column(name = "titulo", nullable = false, length = 1000)
    private String titulo;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String mensaje;

    @Column(name = "fecha", nullable = false)
    private LocalDate fechaEnvio;

    @Column(name = "leido")
    private boolean leido;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Users usuario;

    public Notificaciones() {}

    public Notificaciones(int idNotificacion, String titulo, String mensaje, LocalDate fechaEnvio, boolean leido, Users usuario) {
        this.idNotificacion = idNotificacion;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
        this.leido = leido;
        this.usuario = usuario;
    }

    public int getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(int idNotificacion) { this.idNotificacion = idNotificacion; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDate getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDate fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public boolean isLeido() { return leido; }
    public void setLeido(boolean leido) { this.leido = leido; }

    public Users getUsuario() { return usuario; }
    public void setUsuario(Users usuario) { this.usuario = usuario; }
}