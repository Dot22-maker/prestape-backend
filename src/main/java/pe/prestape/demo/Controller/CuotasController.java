package pe.prestape.demo.Controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.prestape.demo.DTOs.CuotasDTO;
import pe.prestape.demo.Entities.Cuotas;
import pe.prestape.demo.Entities.Prestamos;
import pe.prestape.demo.ServiceInterface.ICuotasService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cuotas")
public class CuotasController {

    @Autowired
    private ICuotasService cuS;

    @Autowired
    private ModelMapper mM;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public List<CuotasDTO> listar() {
        String username = obtenerUsernameActual();
        if (username == null) {
            return List.of();
        }
        return cuS.findByUsuarioUsername(username).stream().map(x -> {
            CuotasDTO dto = mM.map(x, CuotasDTO.class);
            if (x.getPrestamo() != null) {
                dto.setIdPrestamo(x.getPrestamo().getIdPrestamo());
            }
            return dto;
        }).collect(Collectors.toList());
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
    public ResponseEntity<CuotasDTO> registrar(@RequestBody CuotasDTO dto) {
        Cuotas c = mM.map(dto, Cuotas.class);

        // Evita violación de constraint NOT NULL en google_event
        if (c.getGoogleEvent() == null || c.getGoogleEvent().trim().isEmpty()) {
            c.setGoogleEvent("none");
        }

        if (dto.getIdPrestamo() != 0) {
            Prestamos p = new Prestamos();
            p.setIdPrestamo(dto.getIdPrestamo());
            c.setPrestamo(p);
        }

        cuS.insert(c);

        CuotasDTO resDTO = mM.map(c, CuotasDTO.class);
        if (c.getPrestamo() != null) {
            resDTO.setIdPrestamo(c.getPrestamo().getIdPrestamo());
        }
        return new ResponseEntity<>(resDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> listarId(@PathVariable("id") Integer id) {
        Cuotas c = cuS.listId(id);
        if (c == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe una cuota con el ID: " + id);
        }
        CuotasDTO dto = mM.map(c, CuotasDTO.class);
        if (c.getPrestamo() != null) {
            dto.setIdPrestamo(c.getPrestamo().getIdPrestamo());
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/prestamo/{idPrestamo}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public List<CuotasDTO> buscarPorPrestamoId(@PathVariable("idPrestamo") int idPrestamo) {
        return cuS.buscarPorPrestamoId(idPrestamo).stream().map(x -> {
            CuotasDTO dto = mM.map(x, CuotasDTO.class);
            if (x.getPrestamo() != null) {
                dto.setIdPrestamo(x.getPrestamo().getIdPrestamo());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/por-vencer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<CuotasDTO> buscarCuotasPorVencer(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("estado") String estado) {
        return cuS.buscarCuotasPorVencer(fecha, estado).stream().map(x -> {
            CuotasDTO dto = mM.map(x, CuotasDTO.class);
            if (x.getPrestamo() != null) {
                dto.setIdPrestamo(x.getPrestamo().getIdPrestamo());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/google-event/{googleEvent}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> buscarPorGoogleEvent(@PathVariable("googleEvent") String googleEvent) {
        Cuotas c = cuS.buscarPorGoogleEvent(googleEvent);
        if (c == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró cuota para el evento: " + googleEvent);
        }
        CuotasDTO dto = mM.map(c, CuotasDTO.class);
        if (c.getPrestamo() != null) {
            dto.setIdPrestamo(c.getPrestamo().getIdPrestamo());
        }
        return ResponseEntity.ok(dto);
    }
}