package pe.prestape.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.prestape.demo.Entities.Notificaciones;
import pe.prestape.demo.Entities.Users;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface INotificacionesRepository extends JpaRepository<Notificaciones, Integer> {

    @Query("SELECT n FROM Notificaciones n WHERE n.usuario.id = :idUsuario ORDER BY n.fechaEnvio DESC")
    List<Notificaciones> buscarPorUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query("SELECT n FROM Notificaciones n WHERE n.usuario.username = :username ORDER BY n.fechaEnvio DESC")
    List<Notificaciones> findByUsuarioUsername(@Param("username") String username);

    // Comprueba si este admin ya recibió esta alerta hoy
    boolean existsByTituloAndFechaEnvioAndUsuario(String titulo, LocalDate fechaEnvio, Users usuario);
}