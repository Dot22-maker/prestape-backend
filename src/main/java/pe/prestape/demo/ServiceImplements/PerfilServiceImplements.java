package pe.prestape.demo.ServiceImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.prestape.demo.Entities.Perfil;
import pe.prestape.demo.Repository.IPerfilRepository;
import pe.prestape.demo.ServiceInterface.IPerfilService;

import java.util.List;

@Service
public class PerfilServiceImplements implements IPerfilService {

    @Autowired
    private IPerfilRepository peR;

    @Override
    public List<Perfil> list() {
        return peR.findAll();
    }

    @Override
    public void insert(Perfil perfil) {
        peR.save(perfil);

    }

    @Override
    public Perfil listId(int id) {
        return peR.findById(id).orElse(null);
    }

    @Override
    public void update(Perfil perfil) {
        peR.save(perfil);

    }

    @Override
    public void delete(int id) {
        peR.deleteById(id);

    }

    @Override
    public Perfil buscarPorUsuarioId(Long idUsuario) {
        return peR.buscarPorUsuarioId(idUsuario);
    }

    @Override
    public Perfil buscarPorUsername(String username) {
        return peR.buscarPorUsername(username);
    }
}