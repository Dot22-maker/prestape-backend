package pe.prestape.demo.Controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.prestape.demo.DTOs.PerfilDTO;
import pe.prestape.demo.Entities.Perfil;
import pe.prestape.demo.Entities.Users;
import pe.prestape.demo.ServiceInterface.IPerfilService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/perfiles")
public class PerfilController {

    @Autowired
    private IPerfilService pS;

    @Autowired
    private ModelMapper mM;

    // Solo el ADMIN puede ver la lista general de todos los perfiles
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PerfilDTO> listar() {
        return pS.list().stream().map(x -> {
            PerfilDTO dto = mM.map(x, PerfilDTO.class);
            if (x.getUsuario() != null) {
                dto.setIdUsuario(x.getUsuario().getId());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // Tanto ADMIN como CLIENTE pueden crear su perfil
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public void registrar(@RequestBody PerfilDTO dto) {
        Perfil p = mM.map(dto, Perfil.class);
        if (dto.getIdUsuario() != null) {
            Users u = new Users();
            u.setId(dto.getIdUsuario());
            p.setUsuario(u);
        }
        pS.insert(p);
    }

    // ADMIN o el CLIENTE dueño pueden actualizar su información
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<String> modificar(@PathVariable("id") Integer id, @RequestBody PerfilDTO dto) {
        Perfil existente = pS.listId(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se puede modificar. No existe un perfil con el ID: " + id);
        }

        Perfil p = mM.map(dto, Perfil.class);
        p.setIdPerfil(id);

        if (dto.getIdUsuario() != null) {
            Users u = new Users();
            u.setId(dto.getIdUsuario());
            p.setUsuario(u);
        }

        pS.update(p);
        return ResponseEntity.ok("Perfil con ID " + id + " modificado correctamente.");
    }

    // Consultar perfil por su propio ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> listarId(@PathVariable("id") int id) {
        Perfil p = pS.listId(id);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe un perfil con el ID: " + id);
        }
        PerfilDTO dto = mM.map(p, PerfilDTO.class);
        if (p.getUsuario() != null) {
            dto.setIdUsuario(p.getUsuario().getId());
        }
        return ResponseEntity.ok(dto);
    }

    // Endpoint clave para el frontend: Cargar los datos del usuario logueado
    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> buscarPorUsuarioId(@PathVariable("idUsuario") Long idUsuario) {
        Perfil p = pS.buscarPorUsuarioId(idUsuario);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró un perfil para el usuario con ID: " + idUsuario);
        }
        PerfilDTO dto = mM.map(p, PerfilDTO.class);
        if (p.getUsuario() != null) {
            dto.setIdUsuario(p.getUsuario().getId());
        }
        return ResponseEntity.ok(dto);
    }
}