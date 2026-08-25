package pe.prestape.demo.ServiceInterface;

import pe.prestape.demo.Entities.Notificaciones;

import java.util.List;

public interface INotificacionesService {

    List<Notificaciones> list();

    Notificaciones listId(int id);

    List<Notificaciones> buscarPorUsuarioId(Long idUsuario);
    List<Notificaciones> findByUsuarioUsername(String username);
}