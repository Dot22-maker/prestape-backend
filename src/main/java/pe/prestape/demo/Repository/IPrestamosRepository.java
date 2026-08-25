package pe.prestape.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.prestape.demo.Entities.Prestamos;

import java.util.List;

@Repository
public interface IPrestamosRepository extends JpaRepository<Prestamos,Integer> {

    @Query("SELECT p FROM Prestamos p WHERE p.cliente.idCliente = :idCliente")
    public List<Prestamos> buscarPorClienteId(@Param("idCliente") int idCliente);

    @Query("SELECT p FROM Prestamos p WHERE p.estado = :estado")
    public List<Prestamos> buscarPorEstado(@Param("estado") String estado);

    @Query(value = "SELECT c.nombre_completo, count(pr.id_prestamo) " +
            "FROM clientes c INNER JOIN prestamos pr " +
            "ON c.id_cliente = pr.id_cliente " +
            "GROUP BY c.nombre_completo", nativeQuery = true)
    public List<String[]> totalPrestamosPorCliente();

    // Nuevas consultas para filtrar por usuario autenticado
    @Query("SELECT p FROM Prestamos p WHERE p.cliente.usuario.username = :username")
    public List<Prestamos> findByUsuarioUsername(@Param("username") String username);

    @Query("SELECT p FROM Prestamos p WHERE p.cliente.usuario.username = :username AND p.estado = :estado")
    public List<Prestamos> findByUsuarioUsernameAndEstado(@Param("username") String username, @Param("estado") String estado);

}
