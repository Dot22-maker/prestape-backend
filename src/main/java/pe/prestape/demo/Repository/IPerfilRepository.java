package pe.prestape.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.prestape.demo.Entities.Perfil;

@Repository
public interface IPerfilRepository extends JpaRepository<Perfil,Integer> {

    @Query("SELECT p FROM Perfil p WHERE p.usuario.id = :idUsuario")
    public Perfil buscarPorUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query("SELECT p FROM Perfil p WHERE p.usuario.username = :username")
    public Perfil buscarPorUsername(@Param("username") String username);
}