package pe.prestape.demo.Controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.prestape.demo.DTOs.CalificacionDTO;
import pe.prestape.demo.Entities.Calificacion;
import pe.prestape.demo.Entities.Clientes;
import pe.prestape.demo.ServiceInterface.ICalificacionesService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/calificaciones")
@CrossOrigin(origins = "*")
public class CalificacionController {

    @Autowired
    private ICalificacionesService caS;

    @Autowired
    private ModelMapper mM;

    // GET /calificaciones es el endpoint que consume el frontend: se filtra por el
    // usuario autenticado (calificacion -> cliente -> usuario).
    @GetMapping
    public List<CalificacionDTO> listar() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return List.of();
        }
        String username = auth.getName();

        return caS.findByUsuarioUsername(username).stream().map(x -> {
            CalificacionDTO dto = new CalificacionDTO();
            dto.setIdCalificacion(x.getIdCalificacion());
            dto.setCalificacion(x.getCalificacion());
            if (x.getCliente() != null) {
                dto.setIdCliente(x.getCliente().getIdCliente());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody CalificacionDTO dto) {
        if (dto.getIdCliente() == 0) {
            return ResponseEntity.badRequest().body("El idCliente es requerido");
        }

        // 1. Buscar si ya existe calificación para este cliente en la BD
        Calificacion existente = caS.buscarPorClienteId(dto.getIdCliente());

        if (existente != null) {
            // Si ya existe, actualizamos la nota existente (evita el error de llave duplicada)
            existente.setCalificacion(dto.getCalificacion());
            caS.update(existente);
            return ResponseEntity.ok("Calificación actualizada con éxito");
        } else {
            // Si no existe, creamos el nuevo registro
            Calificacion nueva = mM.map(dto, Calificacion.class);
            Clientes cliente = new Clientes();
            cliente.setIdCliente(dto.getIdCliente());
            nueva.setCliente(cliente);

            caS.insert(nueva);
            return ResponseEntity.ok("Calificación registrada con éxito");
        }
    }

    @PutMapping
    public ResponseEntity<?> modificar(@RequestBody CalificacionDTO dto) {
        Calificacion c = mM.map(dto, Calificacion.class);
        c.setIdCalificacion(dto.getIdCalificacion());

        if (dto.getIdCliente() != 0) {
            Clientes cliente = new Clientes();
            cliente.setIdCliente(dto.getIdCliente());
            c.setCliente(cliente);
        }

        caS.update(c);
        return ResponseEntity.ok(Map.of("message", "Calificación modificada con éxito"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") int id) {
        caS.delete(id);
        return ResponseEntity.ok(Map.of("message", "Calificación eliminada"));
    }

    @GetMapping("/{id}")
    public CalificacionDTO listarId(@PathVariable("id") int id) {
        Calificacion c = caS.listId(id);
        if (c == null) return null;
        CalificacionDTO dto = mM.map(c, CalificacionDTO.class);
        if (c.getCliente() != null) {
            dto.setIdCliente(c.getCliente().getIdCliente());
        }
        return dto;
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> buscarPorClienteId(@PathVariable("idCliente") int idCliente) {
        Calificacion c = caS.buscarPorClienteId(idCliente);
        if (c == null) {
            return ResponseEntity.ok(Map.of());
        }
        CalificacionDTO dto = mM.map(c, CalificacionDTO.class);
        if (c.getCliente() != null) {
            dto.setIdCliente(c.getCliente().getIdCliente());
        }
        return ResponseEntity.ok(dto);
    }
}