package pe.prestape.demo.Controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.prestape.demo.DTOs.ClientesDTO;
import pe.prestape.demo.Entities.Clientes;
import pe.prestape.demo.Entities.Users;
import pe.prestape.demo.Repository.IClientesRepository;
import pe.prestape.demo.Repository.IUserRepository;
import pe.prestape.demo.ServiceInterface.IClientesService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClientesController {

    @Autowired
    private IClientesService cS;

    @Autowired
    private IClientesRepository cR;

    @Autowired
    private IUserRepository uR;

    @Autowired
    private ModelMapper mM;

    private String obtenerUsernameActual() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return auth.getName();
    }

    // GET /clientes: Filtra directamente por el usuario en sesión
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public List<ClientesDTO> listar() {
        String username = obtenerUsernameActual();
        if (username == null) {
            return List.of();
        }
        return cR.findClientesByUsuarioUsername(username).stream().map(x -> {
            ClientesDTO dto = mM.map(x, ClientesDTO.class);
            if (x.getUsuario() != null) {
                dto.setIdUsuario(x.getUsuario().getId());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/mis-clientes")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public List<ClientesDTO> misClientes() {
        return listar();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> registrar(@RequestBody ClientesDTO dto) {
        Clientes c = mM.map(dto, Clientes.class);

        c.setFechaRegistro(LocalDate.now());

        if (dto.getEstado() != null && !dto.getEstado().trim().isEmpty()) {
            c.setEstado(dto.getEstado());
        } else {
            c.setEstado("ACTIVO");
        }

        String username = obtenerUsernameActual();
        if (username != null) {
            Users usuarioLogueado = uR.findByUsername(username);
            if (usuarioLogueado != null) {
                c.setUsuario(usuarioLogueado);
            }
        }

        cS.insert(c);
        return ResponseEntity.ok("Cliente registrado correctamente");
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<String> modificar(@RequestBody ClientesDTO dto) {
        Clientes existente = cS.listId(dto.getIdCliente());
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se puede modificar. No existe un cliente con el ID: " + dto.getIdCliente());
        }

        Clientes c = mM.map(dto, Clientes.class);
        c.setFechaRegistro(existente.getFechaRegistro());

        if (dto.getIdUsuario() != null) {
            Users u = new Users();
            u.setId(dto.getIdUsuario());
            c.setUsuario(u);
        } else if (existente.getUsuario() != null) {
            c.setUsuario(existente.getUsuario());
        }

        cS.update(c);
        return ResponseEntity.ok("Cliente con ID " + c.getIdCliente() + " modificado correctamente.");
    }

    @GetMapping("/estado")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> buscarPorEstado(@RequestParam("estado") String estado) {
        List<Clientes> clientes = cS.buscarPorEstado(estado);

        if (clientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontraron clientes por estado: " + estado);
        }

        List<ClientesDTO> listaDTO = clientes.stream().map(x -> {
            ClientesDTO dto = mM.map(x, ClientesDTO.class);
            if (x.getUsuario() != null) {
                dto.setIdUsuario(x.getUsuario().getId());
            }
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(listaDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
        Clientes c = cS.listId(id);

        if (c == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe un cliente con el ID: " + id);
        }

        cS.delete(id);
        return ResponseEntity.ok("Cliente con ID " + id + " dado de baja (INACTIVO) correctamente.");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> listarId(@PathVariable("id") Integer id) {
        Clientes c = cS.listId(id);

        if (c == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No existe un cliente con el ID: " + id);
        }

        ClientesDTO dto = mM.map(c, ClientesDTO.class);
        if (c.getUsuario() != null) {
            dto.setIdUsuario(c.getUsuario().getId());
        }

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/buscarDni")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> buscarPorDni(@RequestParam("dni") String dni) {
        List<Clientes> clientes = cS.buscarPorDni(dni);

        if (clientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontraron clientes por DNI: " + dni);
        }

        List<ClientesDTO> listaDTO = clientes.stream().map(x -> {
            ClientesDTO dto = mM.map(x, ClientesDTO.class);
            if (x.getUsuario() != null) {
                dto.setIdUsuario(x.getUsuario().getId());
            }
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(listaDTO);
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> buscarPorUsuarioId(@PathVariable("idUsuario") Long idUsuario) {
        List<Clientes> clientes = cS.buscarPorUsuarioId(idUsuario);

        if (clientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontraron clientes para el usuario con ID: " + idUsuario);
        }

        List<ClientesDTO> listaDTO = clientes.stream().map(x -> {
            ClientesDTO dto = mM.map(x, ClientesDTO.class);
            if (x.getUsuario() != null) {
                dto.setIdUsuario(x.getUsuario().getId());
            }
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(listaDTO);
    }
}