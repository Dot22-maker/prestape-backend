package pe.prestape.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.prestape.demo.Entities.Users;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.username = :username")
    Users findByUsername(@Param("username") String username);

    Users findOneByUsername(String username);

    @Query("SELECT COUNT(u.username) FROM Users u WHERE u.username = :username")
    int buscarUsername(@Param("username") String nombre);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO roles (rol, user_id) VALUES (:rol, :user_id)", nativeQuery = true)
    void insRol(@Param("rol") String authority, @Param("user_id") Long user_id);

    // 👈 Devuelve la lista de todos los administradores
    @Query("SELECT DISTINCT u FROM Users u JOIN u.roles r WHERE r.rol = 'ADMIN'")
    List<Users> findAllAdminUsers();
}