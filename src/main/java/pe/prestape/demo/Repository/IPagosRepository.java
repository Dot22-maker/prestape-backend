package pe.prestape.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.prestape.demo.Entities.Pagos;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IPagosRepository extends JpaRepository<Pagos,Integer> {

    @Query("SELECT p FROM Pagos p WHERE p.cuotas.idCuota = :idCuota")
    public List<Pagos> buscarPorCuotaId(@Param("idCuota") int idCuota);

    @Query("SELECT p FROM Pagos p WHERE p.fechaPago BETWEEN :inicio AND :fin")
    public List<Pagos> buscarPorRangoFechas(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query(value = "SELECT COALESCE(SUM(p.monto_recibido), 0.0) FROM pagos p", nativeQuery = true)
    public Double totalMontoRecaudado();

    @Query(value = "SELECT p.metodo_pago, count(p.id_pago), COALESCE(SUM(p.monto_recibido), 0.0) " +
            "FROM pagos p " +
            "GROUP BY p.metodo_pago", nativeQuery = true)
    List<String[]> cantidadPagosPorMetodo();

    // Filtra pagos a través de la cadena pago -> cuota -> prestamo -> cliente -> usuario
    @Query("SELECT p FROM Pagos p WHERE p.cuotas.prestamo.cliente.usuario.username = :username")
    List<Pagos> findByUsuarioUsername(@Param("username") String username);
}