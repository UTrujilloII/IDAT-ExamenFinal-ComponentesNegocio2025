package pe.edu.idat.biblioteca.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioRequest;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioResponse;
import pe.edu.idat.biblioteca.entity.Prestamo;
import pe.edu.idat.biblioteca.entity.Rol;
import pe.edu.idat.biblioteca.entity.Usuario;
import pe.edu.idat.biblioteca.mappers.UsuarioMapper;
import pe.edu.idat.biblioteca.repository.PrestamoRepository;
import pe.edu.idat.biblioteca.repository.RolRepository;
import pe.edu.idat.biblioteca.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioServiceImpl implements UsuarioService {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    private Rol getRol(String nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new NoSuchElementException("Rol '" + nombre + "' no encontrado."));
    }

    @Override
    @Transactional
    public UsuarioResponse registrarUsuario(UsuarioRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email ya está registrado: " + request.email());
        }

        Usuario usuario = usuarioMapper.toEntity(request);

        // Asignación de Contraseña y Rol por defecto (USER)
        usuario.setPassword(passwordEncoder.encode("defaultpassword"));
        usuario.setRol(getRol("USER"));

        usuario.setActivo(true);
        // @PrePersist en la Entidad Usuario se encarga de fechaRegistro y ultimaActualizacion.

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(savedUsuario);
    }

    @Override
    @Transactional
    public UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request) {
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + id));

        if (!existingUsuario.getEmail().equals(request.email()) &&
                usuarioRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email " + request.email() + " ya está en uso por otro usuario.");
        }

        existingUsuario.setNombre(request.nombre());
        existingUsuario.setApellido(request.apellido());
        existingUsuario.setEmail(request.email());
        existingUsuario.setUltimaActualizacion(LocalDateTime.now());

        Usuario updatedUsuario = usuarioRepository.save(existingUsuario);
        return usuarioMapper.toResponse(updatedUsuario);
    }

    @Override
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponse obtenerUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional
    public void desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + id));

        if (!usuario.getActivo()) {
            throw new RuntimeException("El usuario con ID " + id + " ya está inactivo.");
        }

        // Lógica de Negocio: Verificar si tiene préstamos activos antes de desactivar
        boolean tienePrestamosActivos = prestamoRepository.findByUsuarioIdAndEstado(id, Prestamo.EstadoPrestamo.ACTIVO).isEmpty();

        if (!tienePrestamosActivos) {
            throw new RuntimeException("No se puede desactivar el usuario porque tiene préstamos ACTIVO pendientes de devolver.");
        }

        usuario.setActivo(false);
        usuario.setUltimaActualizacion(LocalDateTime.now());
        usuarioRepository.save(usuario);

    }
    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerUsuarioPorEmail(String email) {
        // Usa el método del repositorio que busca por email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con email: " + email));

        // Mapea la entidad encontrada a la respuesta DTO
        return usuarioMapper.toResponse(usuario);
    }
}
