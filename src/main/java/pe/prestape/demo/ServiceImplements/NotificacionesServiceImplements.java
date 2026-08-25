package pe.prestape.demo.ServiceImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.prestape.demo.Entities.Notificaciones;
import pe.prestape.demo.Repository.INotificacionesRepository;
import pe.prestape.demo.ServiceInterface.INotificacionesService;

import java.util.List;

@Service
public class NotificacionesServiceImplements implements INotificacionesService {

    @Autowired
    private INotificacionesRepository nR;

    @Override
    public List<Notificaciones> list() {
        return nR.findAll();
    }

    @Override
    public Notificaciones listId(int id) {
        return nR.findById(id).orElse(null);
    }

    @Override
    public List<Notificaciones> buscarPorUsuarioId(Long idUsuario) {
        return nR.buscarPorUsuarioId(idUsuario);
    }

    @Override
    public List<Notificaciones> findByUsuarioUsername(String username) {
        return nR.findByUsuarioUsername(username);
    }
}