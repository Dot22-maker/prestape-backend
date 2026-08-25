package pe.prestape.demo.Controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.prestape.demo.DTOs.PagosDTO;
import pe.prestape.demo.DTOs.PagosPorMetodoDTO;
import pe.prestape.demo.Entities.Cuotas;
import pe.prestape.demo.Entities.Pagos;
import pe.prestape.demo.ServiceInterface.IPagosService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pagos")
public class PagosController {

    @Autowired
    private IPagosService paS;

    @Autowired
    private ModelMapper mM;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public List<PagosDTO> listar() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return List.of();
        }
        String username = auth.getName();

        return paS.findByUsuarioUsername(username).stream().map(x -> {
            PagosDTO dto = mM.map(x, PagosDTO.class);
            if (x.getCuotas() != null) {
                dto.setIdCuota(x.getCuotas().getIdCuota());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<PagosDTO> registrar(@RequestBody PagosDTO dto) {
        Pagos p = mM.map(dto, Pagos.class);
        if (dto.getIdCuota() != 0) {
            Cuotas c = new Cuotas();
            c.setIdCuota(dto.getIdCuota());
            p.setCuotas(c);
        }
        paS.insert(p);

        PagosDTO resDTO = mM.map(p, PagosDTO.class);
        if (p.getCuotas() != null) {
            resDTO.setIdCuota(p.getCuotas().getIdCuota());
        }
        return new ResponseEntity<>(resDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> listarId(@PathVariable("id") int id) {
        Pagos p = paS.listId(id);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe un pago con el ID: " + id);
        }

        PagosDTO dto = mM.map(p, PagosDTO.class);
        if (p.getCuotas() != null) {
            dto.setIdCuota(p.getCuotas().getIdCuota());
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/cuota/{idCuota}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public List<PagosDTO> buscarPorCuotaId(@PathVariable("idCuota") int idCuota) {
        return paS.buscarPorCuotaId(idCuota).stream().map(x -> {
            PagosDTO dto = mM.map(x, PagosDTO.class);
            if (x.getCuotas() != null) {
                dto.setIdCuota(x.getCuotas().getIdCuota());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/rango-fechas")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PagosDTO> buscarPorRangoFechas(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        return paS.buscarPorRangoFechas(inicio, fin).stream().map(x -> {
            PagosDTO dto = mM.map(x, PagosDTO.class);
            if (x.getCuotas() != null) {
                dto.setIdCuota(x.getCuotas().getIdCuota());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/total-recaudado")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Double totalMontoRecaudado() {
        return paS.totalMontoRecaudado();
    }

    @GetMapping("/reporte-pagos-metodo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PagosPorMetodoDTO> reportePagosPorMetodo() {
        List<String[]> lista = paS.cantidadPagosPorMetodo();
        List<PagosPorMetodoDTO> listaDTO = new ArrayList<>();

        for (String[] columna : lista) {
            PagosPorMetodoDTO dto = new PagosPorMetodoDTO();
            dto.setMetodoPago(columna[0]);
            dto.setCantidadPagos(Long.parseLong(columna[1]));
            dto.setMontoTotal(Double.parseDouble(columna[2]));
            listaDTO.add(dto);
        }
        return listaDTO;
    }
}