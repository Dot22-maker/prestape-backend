package pe.prestape.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.prestape.demo.Entities.Clientes;

import java.util.List;

@Repository
public interface IClientesRepository extends JpaRepository<Clientes,Integer> {

    @Query("SELECT c FROM Clientes c WHERE c.dni = :dni")
    public List<Clientes> buscarPorDni(@Param("dni") String dni);

    @Query("SELECT c FROM Clientes c WHERE c.usuario.id = :idUsuario")
    public List<Clientes> buscarPorUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query("SELECT c FROM Clientes c WHERE c.estado = :estado")
    public List<Clientes> buscarPorEstado(@Param("estado") String estado);

    // Nueva consulta para filtrar por usuario autenticado (username)
    @Query("SELECT c FROM Clientes c WHERE c.usuario.username = :username")
    public List<Clientes> findClientesByUsuarioUsername(@Param("username") String username);

}
