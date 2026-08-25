package pe.prestape.demo.ServiceImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.prestape.demo.Entities.Users;
import pe.prestape.demo.Repository.IUserRepository; // O UserRepository según cómo se llame en tu carpeta Repository

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository uR; // Verifica si tu interfaz de usuario se llama IUserRepository o UserRepository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = uR.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("Usuario no existe: %s", username));
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRol()));
        });

        return new User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
    }
}