package pe.prestape.demo.ServiceImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.prestape.demo.Entities.Clientes;
import pe.prestape.demo.Repository.IClientesRepository;
import pe.prestape.demo.ServiceInterface.IClientesService;

import java.util.List;

@Service
public class ClientesServiceImplements implements IClientesService {

    @Autowired
    private IClientesRepository clR;

    @Override
    public List<Clientes> list() {
        return clR.findAll();
    }

    @Override
    public void insert(Clientes clientes) {
        clR.save(clientes);

    }

    @Override
    public Clientes listId(int id) {
        return clR.findById(id).orElse(null);
    }

    @Override
    public void update(Clientes clientes) {
        clR.save(clientes);

    }

    @Override
    public void delete(int id) {
        Clientes c = clR.findById(id).orElse(null);
        if (c != null) {
            c.setEstado("INACTIVO");
            clR.save(c);
        }
    }

    @Override
    public List<Clientes> buscarPorDni(String dni) {
        return clR.buscarPorDni(dni);
    }

    @Override
    public List<Clientes> buscarPorUsuarioId(Long idUsuario) {
        return clR.buscarPorUsuarioId(idUsuario); // <--- Conectado al repo
    }

    @Override
    public List<Clientes> buscarPorEstado(String estado) {
        return clR.buscarPorEstado(estado); // <--- Llama al método del repositorio
    }
}
