package pe.prestape.demo.ServiceInterface;

import pe.prestape.demo.Entities.Prestamos;

import java.util.List;

public interface IPrestamosService {
    List<Prestamos> list();
    void insert(Prestamos prestamos);
    Prestamos listId(int id);
    void update(Prestamos prestamos);
    void delete(int id);
    List<Prestamos> buscarPorClienteId(int idCliente);
    List<Prestamos> buscarPorEstado(String estado);
}