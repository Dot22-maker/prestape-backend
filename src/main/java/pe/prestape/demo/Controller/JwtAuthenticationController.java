package pe.prestape.demo.Controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.prestape.demo.DTOs.JwtRequestDTO;
import pe.prestape.demo.DTOs.JwtResponseDTO;
import pe.prestape.demo.DTOs.UserRegisterDTO;
import pe.prestape.demo.Entities.Perfil;
import pe.prestape.demo.Entities.Roles;
import pe.prestape.demo.Entities.Users;
import pe.prestape.demo.Repository.IPerfilRepository;
import pe.prestape.demo.Repository.IUserRepository;
import pe.prestape.demo.Securitiy.JwtTokenUtil;
import pe.prestape.demo.ServiceImplements.JwtUserDetailsService;

import javax.management.relation.Role;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private IPerfilRepository peR;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private IUserRepository uR;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequestDTO authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    @PostMapping("/registro")
    @Transactional
    public ResponseEntity<String> registrarUsuario(@RequestBody UserRegisterDTO dto) {
        if (uR.buscarUsername(dto.getUsername()) > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El nombre de usuario '" + dto.getUsername() + "' ya está registrado.");
        }

        // 1. Crear Usuario
        Users u = new Users();
        u.setUsername(dto.getUsername());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setEstado(true);

        Roles rolAdmin = new Roles();
        rolAdmin.setRol("ADMIN");
        rolAdmin.setUser(u);
        u.setRoles(java.util.Arrays.asList(rolAdmin));

        Users usuarioGuardado = uR.save(u);

        // 2. Crear Perfil asociado
        Perfil perfil = new Perfil();
        perfil.setNombreCompleto(dto.getNombreCompleto());
        perfil.setEmail(dto.getEmail());
        perfil.setProvincia(dto.getProvincia() != null ? dto.getProvincia() : "Lima");
        perfil.setDistrito(dto.getDistrito() != null ? dto.getDistrito() : "Lima");
        perfil.setFecha_nacimiento(dto.getFechaNacimiento() != null ? dto.getFechaNacimiento() : LocalDate.now());
        perfil.setDni(dto.getDni() != null ? dto.getDni() : "");
        perfil.setTelefono(dto.getTelefono() != null ? dto.getTelefono() : "");
        perfil.setUsuario(usuarioGuardado);

        peR.save(perfil);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuario y perfil registrados exitosamente.");
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}