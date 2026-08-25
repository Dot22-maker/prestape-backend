package pe.prestape.demo.ServiceInterface;

import pe.prestape.demo.Entities.Clientes;
import pe.prestape.demo.Entities.Perfil;

import java.util.List;

public interface IPerfilService {

    public List<Perfil> list();
    void insert(Perfil perfil);
    Perfil listId(int id);
    public void update(Perfil perfil);
    public void delete(int id);

    Perfil buscarPorUsuarioId(Long idUsuario);
    Perfil buscarPorUsername(String username);
}