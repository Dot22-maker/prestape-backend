package pe.prestape.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.prestape.demo.Entities.Cuotas;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ICuotasRepository extends JpaRepository<Cuotas, Integer> {

    @Query("SELECT COUNT(c) FROM Cuotas c WHERE c.prestamo.idPrestamo = :idPrestamo AND UPPER(c.estado) = 'PAGADO'")
    int contarCuotasPagadasPorPrestamoId(@Param("idPrestamo") int idPrestamo);

    @Query("SELECT c FROM Cuotas c WHERE c.prestamo.idPrestamo = :idPrestamo ORDER BY c.numeroCuota ASC")
    List<Cuotas> buscarPorPrestamoId(@Param("idPrestamo") int idPrestamo);

    @Query("SELECT c FROM Cuotas c WHERE c.fechaVencimiento = :fecha AND c.estado = :estado")
    List<Cuotas> buscarCuotasPorVencer(@Param("fecha") LocalDate fecha, @Param("estado") String estado);

    @Query("SELECT c FROM Cuotas c WHERE c.googleEvent = :googleEvent")
    Cuotas buscarPorGoogleEvent(@Param("googleEvent") String googleEvent);

    // Filtra cuotas a través de la cadena cuota -> prestamo -> cliente -> usuario
    @Query("SELECT c FROM Cuotas c WHERE c.prestamo.cliente.usuario.username = :username ORDER BY c.fechaVencimiento ASC")
    List<Cuotas> findByUsuarioUsername(@Param("username") String username);
}