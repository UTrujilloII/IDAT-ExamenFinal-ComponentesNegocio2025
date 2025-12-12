package pe.edu.idat.msbiblioteca.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.idat.msbiblioteca.entity.Usuario;
import pe.edu.idat.msbiblioteca.repository.UsuarioRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService
{
    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Cargando usuario: {}", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no existe: " + username));

        // Los roles ya tienen el prefijo ROLE_ en la BD, no agregar de nuevo
        var authorities = usuario.getRoles()
                .stream()
                .map(r -> {
                    String roleName = r.getNombre();
                    // Si el rol ya tiene ROLE_, no agregarlo de nuevo
                    String authority = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
                    log.debug("Authority creado: {}", authority);
                    return new SimpleGrantedAuthority(authority);
                })
                .collect(Collectors.toSet());

        log.info("Usuario {} cargado con authorities: {}", username, authorities);

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .disabled(!usuario.isEnabled())
                .authorities(authorities)
                .build();
    }
}
