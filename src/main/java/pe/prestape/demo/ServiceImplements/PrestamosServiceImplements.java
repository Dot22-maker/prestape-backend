package pe.prestape.demo.ServiceImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.prestape.demo.Entities.Cuotas;
import pe.prestape.demo.Entities.Pagos;
import pe.prestape.demo.Entities.Prestamos;
import pe.prestape.demo.Repository.ICuotasRepository;
import pe.prestape.demo.Repository.IPagosRepository;
import pe.prestape.demo.Repository.IPrestamosRepository;
import pe.prestape.demo.ServiceInterface.IPrestamosService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrestamosServiceImplements implements IPrestamosService {

    @Autowired
    private IPrestamosRepository pR;

    @Autowired
    private ICuotasRepository cR;

    @Autowired
    private IPagosRepository paR;

    @Override
    public List<Prestamos> list() {
        return pR.findAll();
    }

    @Override
    @Transactional
    public void insert(Prestamos prestamos) {
        // 1. Calcular el Monto Total a Devolver
        double capital = prestamos.getMontoCapital();
        double interes = prestamos.getInteres();
        double montoDevolver = Math.round((capital + (capital * (interes / 100.0))) * 100.0) / 100.0;
        prestamos.setMontoDevolver(montoDevolver);
        prestamos.setEstado("ACTIVO");

        if (prestamos.getFechaCreacion() == null) {
            prestamos.setFechaCreacion(LocalDateTime.now());
        }

        // 2. Guardar el Préstamo para generar su ID
        Prestamos prestamoGuardado = pR.save(prestamos);

        int totalCuotas = prestamoGuardado.getTotalCuotas() > 0 ? prestamoGuardado.getTotalCuotas() : 1;
        double valorCuotaIndividual = Math.round((montoDevolver / totalCuotas) * 100.0) / 100.0;

        LocalDate fechaBase = prestamoGuardado.getFechaCreacion().toLocalDate();
        String modalidad = prestamoGuardado.getModalidad() != null
                ? prestamoGuardado.getModalidad().toUpperCase()
                : "DIARIO";

        int intervaloDias = 1;
        if (modalidad.contains("SEMANAL")) {
            intervaloDias = 7;
        } else if (modalidad.contains("QUINCENAL")) {
            intervaloDias = 15;
        } else if (modalidad.contains("MENSUAL")) {
            intervaloDias = 30;
        }

        // 3. Crear cada una de las cuotas individuales reales (1 hasta N)
        for (int i = 1; i <= totalCuotas; i++) {
            Cuotas cuota = new Cuotas();
            cuota.setNumeroCuota(i);
            cuota.setMontoCuota(valorCuotaIndividual);
            cuota.setMontoPagado(0.0);
            cuota.setEstado("PENDIENTE");
            cuota.setGoogleEvent("none");
            cuota.setFechaVencimiento(fechaBase.plusDays((long) i * intervaloDias));
            cuota.setPrestamo(prestamoGuardado);

            cR.save(cuota);
        }
    }

    @Override
    public Prestamos listId(int id) {
        return pR.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void update(Prestamos prestamos) {
        // 1. Recalcular el monto total a devolver
        double capital = prestamos.getMontoCapital();
        double interes = prestamos.getInteres();
        double montoDevolver = Math.round((capital + (capital * (interes / 100.0))) * 100.0) / 100.0;
        prestamos.setMontoDevolver(montoDevolver);

        // 2. Guardar el préstamo actualizado
        Prestamos prestamoActualizado = pR.save(prestamos);

        // 3. Recalcular montos de las cuotas existentes
        List<Cuotas> cuotas = cR.buscarPorPrestamoId(prestamoActualizado.getIdPrestamo());
        int totalCuotas = prestamoActualizado.getTotalCuotas() > 0 ? prestamoActualizado.getTotalCuotas() : 1;
        double valorCuotaIndividual = Math.round((montoDevolver / totalCuotas) * 100.0) / 100.0;

        for (Cuotas c : cuotas) {
            c.setMontoCuota(valorCuotaIndividual);
            cR.save(c);
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        // 1. Buscar las cuotas asociadas al préstamo
        List<Cuotas> cuotas = cR.buscarPorPrestamoId(id);

        for (Cuotas c : cuotas) {
            // 2. Borrar primero todos los pagos asociados
            List<Pagos> pagos = paR.buscarPorCuotaId(c.getIdCuota());
            for (Pagos p : pagos) {
                paR.deleteById(p.getIdPago());
            }
            // 3. Borrar la cuota
            cR.deleteById(c.getIdCuota());
        }

        // 4. Borrar el préstamo
        pR.deleteById(id);
    }

    @Override
    public List<Prestamos> buscarPorClienteId(int idCliente) {
        return pR.buscarPorClienteId(idCliente);
    }

    @Override
    public List<Prestamos> buscarPorEstado(String estado) {
        return pR.buscarPorEstado(estado);
    }
}