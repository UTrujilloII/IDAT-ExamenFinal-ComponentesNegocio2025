package pe.edu.idat.ms_biblioteca.service;

import org.springframework.stereotype.Service;
import pe.edu.idat.ms_biblioteca.dto.UsuarioRegistroDTO;
import pe.edu.idat.ms_biblioteca.entity.Rol;
import pe.edu.idat.ms_biblioteca.entity.Usuario;
import pe.edu.idat.ms_biblioteca.repository.RolRepository;
import pe.edu.idat.ms_biblioteca.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    public Usuario crearUsuario(UsuarioRegistroDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombre());
        usuario.setEmail(dto.email());
        usuario.setPassword(dto.password());


        Rol rol = rolRepository.findByNombre("USUARIO")
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));

        usuario.setRol(rol);
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }
}
