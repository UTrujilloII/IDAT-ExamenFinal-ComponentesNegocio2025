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
import pe.edu.idat.biblioteca.service.impl.UsuarioService; // Usar la interfaz para consistencia

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

    // Método para buscar usuario y lanzar excepción 404
    private Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con ID: " + id));
    }


    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest request) {

        // --- 1. VALIDACIÓN DE UNICIDAD (EMAIL, DNI, Y TELÉFONO) ---
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado.");
        }
        if (usuarioRepository.existsByDni(request.dni())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El DNI ya está registrado.");
        }
        if (usuarioRepository.existsByTelefono(request.telefono())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El número de teléfono ya está registrado.");
        }

        // --- 2. BUSCAR ROL Y MAPEO ---
        String rolBuscado = request.rol().toUpperCase();

        Rol rolAsignado = rolRepository.findByNombreIgnoreCase(rolBuscado)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Rol no encontrado: " + request.rol()));

        Usuario usuario = usuarioMapper.toEntity(request);

        // --- 3. ASIGNACIÓN DE CAMPOS Y ENCRIPTACIÓN ---
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

    /**
     * Reimplementación del método ListarTodos (Solución al error de compilación)
     */
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

        // --- 1. VALIDACIÓN DE UNICIDAD (PUT) ---

        // A. Validar Email
        if (usuarioRepository.findByEmail(request.email())
                .filter(u -> !u.getId().equals(id))
                .isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado por otro usuario.");
        }

        // B. Validar DNI
        if (usuarioRepository.findByDni(request.dni())
                .filter(u -> !u.getId().equals(id))
                .isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El DNI ya está registrado por otro usuario.");
        }

        // C. Validar TELÉFONO
        if (usuarioRepository.existsByTelefonoAndIdIsNot(request.telefono(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El número de teléfono ya está registrado por otro usuario.");
        }

        // --- 2. ACTUALIZACIÓN DE CAMPOS ---

        existingUser.setUsername(request.email().split("@")[0]);
        existingUser.setDni(request.dni());
        existingUser.setTelefono(request.telefono());
        existingUser.setNombre(request.nombre());
        existingUser.setEmail(request.email());

        // Solo codificar y actualizar la contraseña si se proporciona una nueva
        if (request.password() != null && !request.password().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(request.password());
            existingUser.setPassword(encodedPassword);
        }

        // --- 3. ACTUALIZACIÓN DE ROL ---
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

        // 1. Email: Validar unicidad SOLO si el email está presente Y ha cambiado
        if (request.email() != null && !request.email().isEmpty() &&
                !Objects.equals(existingUser.getEmail(), request.email())) {

            if (usuarioRepository.findByEmail(request.email())
                    .filter(u -> !u.getId().equals(id))
                    .isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado por otro usuario.");
            }
            existingUser.setEmail(request.email());
            existingUser.setUsername(request.email().split("@")[0]); // Actualizar username si el email cambia
        }

        // 2. DNI: Validar unicidad SOLO si el DNI está presente Y ha cambiado
        if (request.dni() != null && !request.dni().isEmpty() &&
                !Objects.equals(existingUser.getDni(), request.dni())) {

            if (usuarioRepository.findByDni(request.dni())
                    .filter(u -> !u.getId().equals(id))
                    .isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El DNI ya está registrado por otro usuario.");
            }
            existingUser.setDni(request.dni());
        }

        // 3. Teléfono: Validar unicidad SOLO si el Teléfono está presente Y ha cambiado
        if (request.telefono() != null && !request.telefono().isEmpty() &&
                !Objects.equals(existingUser.getTelefono(), request.telefono())) {

            if (usuarioRepository.existsByTelefonoAndIdIsNot(request.telefono(), id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El número de teléfono ya está registrado por otro usuario.");
            }
            existingUser.setTelefono(request.telefono());
        }

        // 4. Nombre
        if (request.nombre() != null && !request.nombre().isEmpty()) {
            existingUser.setNombre(request.nombre());
        }

        // 5. Contraseña
        if (request.password() != null && !request.password().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(request.password());
            existingUser.setPassword(encodedPassword);
        }

        // 6. Rol
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

        // --- 1. VERIFICACIÓN DE PRÉSTAMOS PENDIENTES (LÓGICA DE NEGOCIO) ---
        boolean tienePrestamosActivos = prestamoRepository.existsByUsuarioIdAndEstado(id, EstadoPrestamo.ACTIVO);
        boolean tienePrestamosVencidos = prestamoRepository.existsByUsuarioIdAndEstado(id, EstadoPrestamo.VENCIDO);

        if (tienePrestamosActivos || tienePrestamosVencidos) {
            // Se lanza como 400 Bad Request por error de lógica de negocio
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El usuario no se puede eliminar porque tiene préstamos pendientes (ACTIVO o VENCIDO). Primero debe devolver todos sus libros.");
        }

        // --- 2. ELIMINACIÓN DE DEPENDENCIAS ---
        // Desvincular roles (obligatorio antes de eliminar el usuario)
        usuarioAEliminar.setRoles(new HashSet<>());
        usuarioRepository.save(usuarioAEliminar);

        // Finalmente, eliminar el usuario
        usuarioRepository.delete(usuarioAEliminar);
    }
}