package pe.prestape.demo.Controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.prestape.demo.DTOs.NotificacionesDTO;
import pe.prestape.demo.Entities.Notificaciones;
import pe.prestape.demo.ServiceInterface.INotificacionesService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionesController {

    @Autowired
    private INotificacionesService nS;

    @Autowired
    private ModelMapper mM;

    // GET /notificaciones es el endpoint que consume el frontend: se filtra por
    // el usuario autenticado para que cada usuario vea solo sus propias notificaciones.
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public List<NotificacionesDTO> listar() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return List.of();
        }
        String username = auth.getName();

        return nS.findByUsuarioUsername(username).stream().map(x -> {
            NotificacionesDTO dto = mM.map(x, NotificacionesDTO.class);
            dto.setDescripcion(x.getMensaje());
            dto.setFecha(x.getFechaEnvio());
            if (x.getUsuario() != null) {
                dto.setIdUsuario(x.getUsuario().getId());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> listarId(@PathVariable("id") Integer id) {
        Notificaciones n = nS.listId(id);
        if (n == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe una notificación con el ID: " + id);
        }
        NotificacionesDTO dto = mM.map(n, NotificacionesDTO.class);
        dto.setDescripcion(n.getMensaje());
        dto.setFecha(n.getFechaEnvio());
        if (n.getUsuario() != null) {
            dto.setIdUsuario(n.getUsuario().getId());
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> buscarPorUsuarioId(@PathVariable("idUsuario") Long idUsuario) {
        List<Notificaciones> lista = nS.buscarPorUsuarioId(idUsuario);
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontraron notificaciones para el usuario con ID: " + idUsuario);
        }

        List<NotificacionesDTO> listaDTO = lista.stream().map(x -> {
            NotificacionesDTO dto = mM.map(x, NotificacionesDTO.class);
            dto.setDescripcion(x.getMensaje());
            dto.setFecha(x.getFechaEnvio());
            if (x.getUsuario() != null) {
                dto.setIdUsuario(x.getUsuario().getId());
            }
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(listaDTO);
    }
}