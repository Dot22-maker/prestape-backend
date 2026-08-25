package pe.prestape.demo.ServiceImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.prestape.demo.Entities.Cuotas;
import pe.prestape.demo.Repository.ICuotasRepository;
import pe.prestape.demo.ServiceInterface.ICuotasService;

import java.time.LocalDate;
import java.util.List;

@Service
public class CuotasServiceImplements implements ICuotasService {

    @Autowired
    private ICuotasRepository cuR;

    @Override
    public List<Cuotas> list() {
        return cuR.findAll();
    }

    @Override
    public void insert(Cuotas cuotas) {
        if (cuotas.getMontoPagado() == 0.0) {
            cuotas.setMontoPagado(0.0);
        }
        if (cuotas.getEstado() == null || cuotas.getEstado().isEmpty()) {
            cuotas.setEstado("PENDIENTE");
        }
        cuR.save(cuotas);
    }

    @Override
    public Cuotas listId(int id) {
        return cuR.findById(id).orElse(null);
    }

    @Override
    public List<Cuotas> buscarPorPrestamoId(int idPrestamo) {
        return cuR.buscarPorPrestamoId(idPrestamo);
    }

    @Override
    public List<Cuotas> buscarCuotasPorVencer(LocalDate fecha, String estado) {
        return cuR.buscarCuotasPorVencer(fecha, estado);
    }

    @Override
    public Cuotas buscarPorGoogleEvent(String googleEvent) {
        return cuR.buscarPorGoogleEvent(googleEvent);
    }

    @Override
    public List<Cuotas> findByUsuarioUsername(String username) {
        return cuR.findByUsuarioUsername(username);
    }
}