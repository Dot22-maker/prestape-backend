package pe.prestape.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.prestape.demo.Entities.Calificacion;
import java.util.List;

@Repository
public interface ICalificacionRepository extends JpaRepository<Calificacion, Integer> {

    @Query("SELECT c FROM Calificacion c LEFT JOIN FETCH c.cliente")
    List<Calificacion> listarTodoConCliente();

    @Query("SELECT c FROM Calificacion c WHERE c.cliente.idCliente = :idCliente")
    Calificacion findByClienteId(@Param("idCliente") int idCliente);

    // Filtra calificaciones a través de la cadena calificacion -> cliente -> usuario
    @Query("SELECT c FROM Calificacion c WHERE c.cliente.usuario.username = :username")
    List<Calificacion> findByUsuarioUsername(@Param("username") String username);
}