package pe.prestape.demo.ServiceImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.prestape.demo.Entities.Calificacion;
import pe.prestape.demo.Repository.ICalificacionRepository;
import pe.prestape.demo.ServiceInterface.ICalificacionesService;

import java.util.List;

@Service
public class CalificacionesServiceImplements implements ICalificacionesService {

    @Autowired
    private ICalificacionRepository caR;

    @Override
    public List<Calificacion> list() {
        return caR.findAll();
    }

    @Override
    public void insert(Calificacion calificacion) {
        caR.save(calificacion);

    }

    @Override
    public Calificacion listId(int id) {
        return caR.findById(id).orElse(null);
    }

    @Override
    public void update(Calificacion calificacion) {
        caR.save(calificacion);

    }

    @Override
    public void delete(int id) {
        caR.deleteById(id);
    }

    @Override
    public Calificacion buscarPorClienteId(int idCliente) {
        return caR.findByClienteId(idCliente);
    }

    @Override
    public List<Calificacion> findByUsuarioUsername(String username) {
        return caR.findByUsuarioUsername(username);
    }
}