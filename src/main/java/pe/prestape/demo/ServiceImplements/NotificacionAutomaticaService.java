package pe.prestape.demo.ServiceImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.prestape.demo.Entities.Cuotas;
import pe.prestape.demo.Entities.Notificaciones;
import pe.prestape.demo.Entities.Users;
import pe.prestape.demo.Repository.ICuotasRepository;
import pe.prestape.demo.Repository.INotificacionesRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificacionAutomaticaService {

    @Autowired
    private ICuotasRepository cuotasRepository;

    @Autowired
    private INotificacionesRepository notificacionesRepository;

    @Scheduled(cron = "0 0 8 * * ?", zone = "America/Lima")
    @Transactional
    public void verificarVencimientosYNotificar() {
        LocalDate hoy = LocalDate.now();

        List<Cuotas> cuotasDeHoy = cuotasRepository.buscarCuotasPorVencer(hoy, "PENDIENTE");
        if (cuotasDeHoy == null || cuotasDeHoy.isEmpty()) {
            return;
        }

        for (Cuotas cuota : cuotasDeHoy) {
            if (cuota.getPrestamo() != null && cuota.getPrestamo().getCliente() != null) {
                var cliente = cuota.getPrestamo().getCliente();
                Users usuarioDuenio = cliente.getUsuario();

                // Si el cliente no tiene usuario asignado, se omite
                if (usuarioDuenio == null) {
                    continue;
                }

                String titulo = "Vencimiento de Cuota - " + cliente.getNombreCompleto();

                // Evitar notificaciones duplicadas en el mismo día para el mismo usuario
                if (notificacionesRepository.existsByTituloAndFechaEnvioAndUsuario(titulo, hoy, usuarioDuenio)) {
                    continue;
                }

                String mensaje = cliente.getNombreCompleto() + " tiene una cuota pendiente de S/ " +
                        cuota.getMontoCuota() + " que vence el día de hoy.";

                Notificaciones noti = new Notificaciones();
                noti.setTitulo(titulo);
                noti.setMensaje(mensaje);
                noti.setFechaEnvio(hoy);
                noti.setLeido(false);
                noti.setUsuario(usuarioDuenio);

                notificacionesRepository.save(noti);
            }
        }
    }
}