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
import pe.prestape.demo.ServiceInterface.IPagosService;

import java.time.LocalDate;
import java.util.List;

@Service
public class PagosServiceImplements implements IPagosService {

    @Autowired
    private IPagosRepository paR;

    @Autowired
    private ICuotasRepository cuR;

    @Autowired
    private IPrestamosRepository prR;

    @Override
    public List<Pagos> list() {
        return paR.findAll();
    }

    @Override
    @Transactional
    public void insert(Pagos pagos) {
        // 1. Guardar el registro del pago
        paR.save(pagos);

        if (pagos.getCuotas() != null && pagos.getCuotas().getIdCuota() != 0) {
            Cuotas cuota = cuR.findById(pagos.getCuotas().getIdCuota()).orElse(null);

            if (cuota != null) {
                // 2. Actualizar monto pagado de la cuota individual
                double nuevoMontoPagado = Math.round((cuota.getMontoPagado() + pagos.getMontoRecibido()) * 100.0) / 100.0;
                cuota.setMontoPagado(nuevoMontoPagado);

                // Si esta cuota individual se cubrió, pasa a PAGADO
                if (nuevoMontoPagado >= (cuota.getMontoCuota() - 0.05)) {
                    cuota.setEstado("PAGADO");
                } else {
                    cuota.setEstado("PENDIENTE");
                }
                cuR.save(cuota);

                // 3. Evaluar el estado global del Préstamo contra el montoDevolver total
                Prestamos prestamo = cuota.getPrestamo();
                if (prestamo == null && cuota.getPrestamo() != null) {
                    prestamo = prR.findById(cuota.getPrestamo().getIdPrestamo()).orElse(null);
                }

                if (prestamo != null) {
                    List<Cuotas> cuotasPrestamo = cuR.buscarPorPrestamoId(prestamo.getIdPrestamo());

                    // Sumar el dinero real amortizado de todas las cuotas
                    double totalAmortizado = cuotasPrestamo.stream()
                            .mapToDouble(Cuotas::getMontoPagado)
                            .sum();

                    // Contar cuotas pagadas
                    long cuotasPagadasCount = cuotasPrestamo.stream()
                            .filter(c -> "PAGADO".equalsIgnoreCase(c.getEstado()))
                            .count();

                    boolean totalCubierto = totalAmortizado >= (prestamo.getMontoDevolver() - 0.10);
                    boolean cuotasCompletas = prestamo.getTotalCuotas() > 0 && cuotasPagadasCount >= prestamo.getTotalCuotas();

                    if (totalCubierto || cuotasCompletas) {
                        prestamo.setEstado("FINALIZADO");
                    } else {
                        prestamo.setEstado("ACTIVO");
                    }
                    prR.save(prestamo);
                }
            }
        }
    }

    @Override
    public Pagos listId(int id) {
        return paR.findById(id).orElse(null);
    }

    @Override
    public List<Pagos> buscarPorCuotaId(int idCuota) {
        return paR.buscarPorCuotaId(idCuota);
    }

    @Override
    public List<Pagos> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return paR.buscarPorRangoFechas(inicio, fin);
    }

    @Override
    public Double totalMontoRecaudado() {
        return paR.totalMontoRecaudado();
    }

    @Override
    public List<String[]> cantidadPagosPorMetodo() {
        return paR.cantidadPagosPorMetodo();
    }

    @Override
    public List<Pagos> findByUsuarioUsername(String username) {
        return paR.findByUsuarioUsername(username);
    }
}