package pe.prestape.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.prestape.demo.Entities.Perfil;
import pe.prestape.demo.ServiceInterface.IPerfilService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuariosController {

    @Autowired
    private IPerfilService pS;

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No hay una sesión válida. Vuelve a iniciar sesión.");
        }

        String username = auth.getName();
        Perfil perfil = pS.buscarPorUsername(username);

        Map<String, Object> body = new HashMap<>();

        if (perfil != null) {
            String nombreCompleto = perfil.getNombreCompleto() != null ? perfil.getNombreCompleto().trim() : "";
            String nombre = nombreCompleto;
            String apellidos = "";
            int espacio = nombreCompleto.indexOf(' ');
            if (espacio > 0) {
                nombre = nombreCompleto.substring(0, espacio);
                apellidos = nombreCompleto.substring(espacio + 1).trim();
            }

            body.put("nombre", nombre);
            body.put("apellidos", apellidos);
            body.put("nombreCompleto", nombreCompleto);
            body.put("email", perfil.getEmail() != null ? perfil.getEmail() : "");
            body.put("telefono", perfil.getTelefono() != null ? perfil.getTelefono() : "");
            body.put("dni", perfil.getDni() != null ? perfil.getDni() : "");
            body.put("provincia", perfil.getProvincia() != null ? perfil.getProvincia() : "Lima");
            body.put("distrito", perfil.getDistrito() != null ? perfil.getDistrito() : "Lima");
            body.put("username", username);
        } else {
            // Evita el error 404 devolviendo estructura válida con el username del JWT
            body.put("nombre", username);
            body.put("apellidos", "");
            body.put("nombreCompleto", username);
            body.put("email", "");
            body.put("telefono", "");
            body.put("dni", "");
            body.put("provincia", "Lima");
            body.put("distrito", "Lima");
            body.put("username", username);
        }

        return ResponseEntity.ok(body);
    }
}