package pe.edu.idat.biblioteca.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.idat.biblioteca.entity.Usuario;
import pe.edu.idat.biblioteca.repository.UsuarioRepository;

@Service // ¡CRÍTICO! Esto registra la clase como el bean de UserDetailsService.
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // En tu aplicación, el 'username' se mapea al campo 'email' de la Entidad Usuario.

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email: " + email
                ));



        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("El usuario se encuentra inactivo.");
        }

        return usuario;
    }
}