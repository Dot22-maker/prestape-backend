package pe.prestape.demo.Controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.prestape.demo.DTOs.PrestamosDTO;
import pe.prestape.demo.Entities.Clientes;
import pe.prestape.demo.Entities.Prestamos;
import pe.prestape.demo.Repository.IPrestamosRepository;
import pe.prestape.demo.ServiceInterface.IPrestamosService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prestamos")
public class PrestamosController {

    @Autowired
    private IPrestamosService pS;

    @Autowired
    private IPrestamosRepository pR;

    @Autowired
    private ModelMapper mM;

    // GET /prestamos es el endpoint que consume el frontend: se filtra por el
    // usuario autenticado (a través de prestamo -> cliente -> usuario) para que
    // cada usuario vea solo los préstamos de sus propios clientes.
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public List<PrestamosDTO> listar() {
        String username = obtenerUsernameActual();
        if (username == null) {
            return List.of();
        }
        return pR.findByUsuarioUsername(username).stream().map(x -> {
            PrestamosDTO dto = mM.map(x, PrestamosDTO.class);
            if (x.getCliente() != null) {
                dto.setIdCliente(x.getCliente().getIdCliente());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/mis-prestamos")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public List<PrestamosDTO> misPrestamos() {
        // Endpoint explícito equivalente a GET /prestamos (se mantiene por compatibilidad)
        return listar();
    }

    private String obtenerUsernameActual() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return auth.getName();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public void registrar(@RequestBody PrestamosDTO dto) {
        Prestamos p = mM.map(dto, Prestamos.class);
        if (dto.getIdCliente() != 0) {
            Clientes c = new Clientes();
            c.setIdCliente(dto.getIdCliente());
            p.setCliente(c);
        }
        pS.insert(p);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> modificar(@RequestBody PrestamosDTO dto) {
        Prestamos existente = pS.listId(dto.getIdPrestamo());
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe un préstamo con el ID: " + dto.getIdPrestamo());
        }

        Prestamos p = mM.map(dto, Prestamos.class);
        if (dto.getIdCliente() != 0) {
            Clientes c = new Clientes();
            c.setIdCliente(dto.getIdCliente());
            p.setCliente(c);
        }

        pS.update(p);
        return ResponseEntity.ok("Préstamo con ID " + dto.getIdPrestamo() + " modificado correctamente.");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
        Prestamos p = pS.listId(id);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe un préstamo con el ID: " + id);
        }
        pS.delete(id);
        return ResponseEntity.ok("Préstamo con ID " + id + " eliminado correctamente.");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> listarId(@PathVariable("id") Integer id) {
        Prestamos p = pS.listId(id);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe un préstamo con el ID: " + id);
        }
        PrestamosDTO dto = mM.map(p, PrestamosDTO.class);
        if (p.getCliente() != null) {
            dto.setIdCliente(p.getCliente().getIdCliente());
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public List<PrestamosDTO> buscarPorClienteId(@PathVariable("idCliente") int idCliente) {
        return pS.buscarPorClienteId(idCliente).stream().map(x -> {
            PrestamosDTO dto = mM.map(x, PrestamosDTO.class);
            if (x.getCliente() != null) {
                dto.setIdCliente(x.getCliente().getIdCliente());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/estado")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PrestamosDTO> buscarPorEstado(@RequestParam("estado") String estado) {
        return pS.buscarPorEstado(estado).stream().map(x -> {
            PrestamosDTO dto = mM.map(x, PrestamosDTO.class);
            if (x.getCliente() != null) {
                dto.setIdCliente(x.getCliente().getIdCliente());
            }
            return dto;
        }).collect(Collectors.toList());
    }
}