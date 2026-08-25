package pe.prestape.demo.ServiceInterface;

import pe.prestape.demo.Entities.Calificacion;

import java.util.List;

public interface ICalificacionesService {

    public List<Calificacion> list();
    void insert(Calificacion calificacion);
    Calificacion listId(int id);
    public void update(Calificacion calificacion);
    public void delete(int id);

    Calificacion buscarPorClienteId(int idCliente);
    List<Calificacion> findByUsuarioUsername(String username);
}