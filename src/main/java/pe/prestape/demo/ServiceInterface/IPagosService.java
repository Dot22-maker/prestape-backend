package pe.prestape.demo.ServiceInterface;

import pe.prestape.demo.Entities.Calificacion;
import pe.prestape.demo.Entities.Pagos;

import java.time.LocalDate;
import java.util.List;

public interface IPagosService {

    public List<Pagos> list();
    void insert(Pagos pagos);
    Pagos listId(int id);

    List<Pagos> buscarPorCuotaId(int idCuota);
    List<Pagos> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);
    Double totalMontoRecaudado();
    List<String[]> cantidadPagosPorMetodo();
    List<Pagos> findByUsuarioUsername(String username);
}