package pe.prestape.demo.ServiceInterface;

import pe.prestape.demo.Entities.Calificacion;
import pe.prestape.demo.Entities.Clientes;

import java.util.List;

public interface IClientesService {

    public List<Clientes> list();
    void insert(Clientes clientes);
    Clientes listId(int id);
    public void update(Clientes clientes);
    public void delete(int id);

    List<Clientes> buscarPorDni(String dni);
    List<Clientes> buscarPorUsuarioId(Long idUsuario);
    List<Clientes> buscarPorEstado(String estado);
}
