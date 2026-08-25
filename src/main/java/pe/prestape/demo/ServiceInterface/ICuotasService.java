package pe.prestape.demo.ServiceInterface;

import pe.prestape.demo.Entities.Cuotas;

import java.time.LocalDate;
import java.util.List;

public interface ICuotasService {
    List<Cuotas> list();
    void insert(Cuotas cuotas);
    Cuotas listId(int id);
    List<Cuotas> buscarPorPrestamoId(int idPrestamo);
    List<Cuotas> buscarCuotasPorVencer(LocalDate fecha, String estado);
    Cuotas buscarPorGoogleEvent(String googleEvent);
    List<Cuotas> findByUsuarioUsername(String username);
}