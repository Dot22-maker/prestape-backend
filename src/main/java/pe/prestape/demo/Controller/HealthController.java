package pe.prestape.demo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint liviano para "mantener despierto" el servicio de Render.
 *
 * No toca la base de datos a proposito: solo queremos que Render vea
 * trafico entrante y no apague el contenedor por inactividad. Si este
 * endpoint tambien consultara Neon, estarias gastando tus horas de
 * computo gratis de Neon las 24 horas del dia (el plan free de Neon
 * solo trae 100 horas de computo al mes: mantenerlo despierto siempre
 * se te acabaria en unos 4 dias).
 *
 * Uso: configura un servicio externo (cron-job.org, UptimeRobot,
 * GitHub Actions con cron, etc.) para pegarle un GET a
 * https://prestape-backend.onrender.com/health cada 10-14 minutos.
 *
 * IMPORTANTE: esto NO esta oficialmente soportado por Render en el
 * plan free y no es 100% garantizado (ver seccion final de mi
 * respuesta). Es un parche gratuito, no la solucion definitiva.
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}