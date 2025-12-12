package pe.edu.idat.biblioteca.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioRequest;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioResponse;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioPatchRequest;
import pe.edu.idat.biblioteca.entity.Usuario;
import pe.edu.idat.biblioteca.entity.Rol;
import pe.edu.idat.biblioteca.entity.EstadoPrestamo;
import pe.edu.idat.biblioteca.mappers.UsuarioMapper;
import pe.edu.idat.biblioteca.repository.UsuarioRepository;
import pe.edu.idat.biblioteca.repository.RolRepository;
import pe.edu.idat.biblioteca.repository.PrestamoRepository;
import pe.edu.idat.biblioteca.service.impl.UsuarioService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final PrestamoRepository prestamoRepository;

    private Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con ID: " + id));
    }
    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado.");
        }
        if (usuarioRepository.existsByDni(request.dni())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El DNI ya está registrado.");
        }
        if (usuarioRepository.existsByTelefono(request.telefono())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El número de teléfono ya está registrado.");
        }
        String rolBuscado = request.rol().toUpperCase();

        Rol rolAsignado = rolRepository.findByNombreIgnoreCase(rolBuscado)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Rol no encontrado: " + request.rol()));

        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setUsername(request.email().split("@")[0]);
        String encodedPassword = passwordEncoder.encode(request.password());
        usuario.setPassword(encodedPassword);

        usuario.setDni(request.dni());
        usuario.setTelefono(request.telefono());

        Set<Rol> roles = new HashSet<>();
        roles.add(rolAsignado);
        usuario.setRoles(roles);

        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(saved);
    }
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request) {
        // Usamos el método interno para buscar y lanzar 404
        Usuario existingUser = buscarUsuarioPorId(id);
        if (usuarioRepository.findByEmail(request.email())
                .filter(u -> !u.getId().equals(id))
                .isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado por otro usuario.");
        }

        //  Validar DNI
        if (usuarioRepository.findByDni(request.dni())
                .filter(u -> !u.getId().equals(id))
                .isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El DNI ya está registrado por otro usuario.");
        }

        //  Validar TELÉFONO
        if (usuarioRepository.existsByTelefonoAndIdIsNot(request.telefono(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El número de teléfono ya está registrado por otro usuario.");
        }
        existingUser.setUsername(request.email().split("@")[0]);
        existingUser.setDni(request.dni());
        existingUser.setTelefono(request.telefono());
        existingUser.setNombre(request.nombre());
        existingUser.setEmail(request.email());
        if (request.password() != null && !request.password().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(request.password());
            existingUser.setPassword(encodedPassword);
        }
        String rolBuscado = request.rol().toUpperCase();
        Rol nuevoRol = rolRepository.findByNombreIgnoreCase(rolBuscado)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Rol no encontrado: " + request.rol()));

        Set<Rol> roles = new HashSet<>();
        roles.add(nuevoRol);
        existingUser.setRoles(roles);

        Usuario saved = usuarioRepository.save(existingUser);
        return usuarioMapper.toResponse(saved);
    }

    @Override
    public UsuarioResponse actualizarParcialUsuario(Long id, UsuarioPatchRequest request) {
        Usuario existingUser = buscarUsuarioPorId(id);
        if (request.email() != null && !request.email().isEmpty() &&
                !Objects.equals(existingUser.getEmail(), request.email())) {

            if (usuarioRepository.findByEmail(request.email())
                    .filter(u -> !u.getId().equals(id))
                    .isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado por otro usuario.");
            }
            existingUser.setEmail(request.email());
            existingUser.setUsername(request.email().split("@")[0]);
        }
        if (request.dni() != null && !request.dni().isEmpty() &&
                !Objects.equals(existingUser.getDni(), request.dni())) {

            if (usuarioRepository.findByDni(request.dni())
                    .filter(u -> !u.getId().equals(id))
                    .isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El DNI ya está registrado por otro usuario.");
            }
            existingUser.setDni(request.dni());
        }
        if (request.telefono() != null && !request.telefono().isEmpty() &&
                !Objects.equals(existingUser.getTelefono(), request.telefono())) {

            if (usuarioRepository.existsByTelefonoAndIdIsNot(request.telefono(), id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El número de teléfono ya está registrado por otro usuario.");
            }
            existingUser.setTelefono(request.telefono());
        }
        if (request.nombre() != null && !request.nombre().isEmpty()) {
            existingUser.setNombre(request.nombre());
        }
        if (request.password() != null && !request.password().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(request.password());
            existingUser.setPassword(encodedPassword);
        }
        if (request.rol() != null && !request.rol().isEmpty()) {
            String rolBuscado = request.rol().toUpperCase();
            Rol nuevoRol = rolRepository.findByNombreIgnoreCase(rolBuscado)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Rol no encontrado: " + request.rol()));

            Set<Rol> roles = new HashSet<>();
            roles.add(nuevoRol);
            existingUser.setRoles(roles);
        }

        Usuario saved = usuarioRepository.save(existingUser);
        return usuarioMapper.toResponse(saved);
    }

    @Override
    public void eliminarUsuario(Long id) {
        Usuario usuarioAEliminar = buscarUsuarioPorId(id);
        boolean tienePrestamosActivos = prestamoRepository.existsByUsuarioIdAndEstado(id, EstadoPrestamo.ACTIVO);
        boolean tienePrestamosVencidos = prestamoRepository.existsByUsuarioIdAndEstado(id, EstadoPrestamo.VENCIDO);

        if (tienePrestamosActivos || tienePrestamosVencidos) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El usuario no se puede eliminar porque tiene préstamos pendientes (ACTIVO o VENCIDO). Primero debe devolver todos sus libros.");
        }
        usuarioAEliminar.setRoles(new HashSet<>());
        usuarioRepository.save(usuarioAEliminar);
        usuarioRepository.delete(usuarioAEliminar);
    }
}