package com.proyecto.idat.ms_gestion_ventas.service.impl;

import com.proyecto.idat.ms_gestion_ventas.dto.usuario.*;
import com.proyecto.idat.ms_gestion_ventas.entity.Usuario;
import com.proyecto.idat.ms_gestion_ventas.exception.ReglaNegocioException;
import com.proyecto.idat.ms_gestion_ventas.mappers.UsuarioMapper;
import com.proyecto.idat.ms_gestion_ventas.repository.RolRepository;
import com.proyecto.idat.ms_gestion_ventas.repository.UsuarioRepository;
import com.proyecto.idat.ms_gestion_ventas.repository.PrestamoRepository; // <-- NUEVO
import com.proyecto.idat.ms_gestion_ventas.security.BloqueoTokenService;
import com.proyecto.idat.ms_gestion_ventas.security.JwtUtils;
import com.proyecto.idat.ms_gestion_ventas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final BloqueoTokenService bloqueoTokenService;
    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final PrestamoRepository prestamoRepository;

    @Override
    public Map<String, Object> desbloquearUsuario(String username) {

        boolean bloqueoUsuarioLogin = bloqueoTokenService.estaBloqueado(username);
        boolean bloqueoUsuarioToken = jwtUtils.estaBloqueadoPorToken(username);

        if (bloqueoUsuarioLogin) {
            bloqueoTokenService.desbloquear(username);
        }

        if (bloqueoUsuarioToken) {
            jwtUtils.desbloquearBloqueoTokenUsuario(username);
        }

        Map<String, Object> body = new HashMap<>();
        if (bloqueoUsuarioLogin || bloqueoUsuarioToken) {
            body.put("mensaje", "Usuario desbloqueado con éxito (login y/o token)");
        } else {
            body.put("mensaje", "El usuario no tenía un bloqueo activo, no fue necesario desbloquearlo");
        }
        body.put("username", username);

        return body;
    }

    @Override
    public Map<String, Object> crearUsuarioPorAdmin(AdminCrearUsuarioRequest request) {

        if (usuarioRepository.existsByUsername(request.username())) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con el username '" + request.username() + "'"
            );
        }

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con el email '" + request.email() + "'"
            );
        }

        String rolNombre = request.rol().trim().toUpperCase();

        var rol = rolRepository.findByNombre(rolNombre)
                .orElseThrow(() -> new ReglaNegocioException(
                        "Rol no válido. Solo se permite ADMIN o USUARIO"
                ));

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setNombreCompleto(request.nombreCompleto());
        usuario.setEmail(request.email());
        usuario.setRoles(Set.of(rol));

        Usuario guardado = usuarioRepository.save(usuario);

        UsuarioResponse response = usuarioMapper.toResponse(guardado);

        Map<String, Object> body = new HashMap<>();
        String mensajeRol = "Usuario con rol " + rolNombre + " creado con éxito";
        body.put("mensaje", mensajeRol);
        body.put("usuario", response);

        return body;
    }

    @Override
    public Map<String, Object> listarTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioResponse> respuesta = usuarios.stream()
                .map(usuarioMapper::toResponse)
                .toList();

        Map<String, Object> body = new HashMap<>();
        body.put("totalUsuarios", respuesta.size());
        body.put("usuarios", respuesta);

        return body;
    }

    @Override
    public Map<String, Object> listarUsuariosPorRol(String rolNombre) {

        List<Usuario> usuarios = usuarioRepository.findAll();

        List<UsuarioResponse> filtrados = usuarios.stream()
                .filter(u -> u.getRoles().stream()
                        .anyMatch(r -> rolNombre.equalsIgnoreCase(r.getNombre())))
                .map(usuarioMapper::toResponse)
                .toList();

        Map<String, Object> body = new HashMap<>();
        body.put("totalUsuarios", filtrados.size());
        body.put("usuarios", filtrados);

        return body;
    }

    @Override
    public Map<String, Object> listarBloqueos() {

        Map<String, Long> bloqueosLogin = bloqueoTokenService.obtenerUsuariosBloqueados();

        Map<String, Object> bloqueosLoginBody = new HashMap<>();
        bloqueosLoginBody.put("tipo", "INTENTOS_LOGIN");
        bloqueosLoginBody.put("cantidad", bloqueosLogin.size());
        bloqueosLoginBody.put("usuarios", bloqueosLogin);

        Map<String, Long> bloqueosToken = jwtUtils.obtenerUsuariosBloqueadosPorToken();

        Map<String, Object> bloqueosTokenBody = new HashMap<>();
        bloqueosTokenBody.put("tipo", "TOKENS_INVALIDOS");
        bloqueosTokenBody.put("cantidad", bloqueosToken.size());
        bloqueosTokenBody.put("usuarios", bloqueosToken);

        Map<String, Object> body = new HashMap<>();
        body.put("bloqueosLogin", bloqueosLoginBody);
        body.put("bloqueosToken", bloqueosTokenBody);

        return body;
    }

    @Override
    public UsuarioResponse actualizarMiCuenta(String usernameActual,
                                              ActualizarMiCuentaRequest request) {

        Usuario usuario = usuarioRepository.findByUsername(usernameActual)
                .orElseThrow(() -> new ReglaNegocioException(
                        "No se encontró el usuario autenticado en el sistema"
                ));

        if (!passwordEncoder.matches(request.passwordActual(), usuario.getPassword())) {
            throw new ReglaNegocioException("La contraseña actual no es correcta");
        }

        if (!usuario.getUsername().equals(request.username())
                && usuarioRepository.existsByUsername(request.username())) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con el username '" + request.username() + "'"
            );
        }

        if (!usuario.getEmail().equals(request.email())
                && usuarioRepository.existsByEmail(request.email())) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con el email '" + request.email() + "'"
            );
        }

        usuario.setUsername(request.username());
        usuario.setNombreCompleto(request.nombreCompleto());
        usuario.setEmail(request.email());

        if (request.nuevaPassword() != null && !request.nuevaPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.nuevaPassword()));
        }

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(guardado);
    }

    @Override
    public UsuarioResponse actualizarUsuarioPorAdmin(Long idUsuario,
                                                     ActualizarUsuarioRequest request) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe un usuario con id " + idUsuario
                ));

        if (usuarioRepository.existsByUsernameAndIdUsuarioNot(request.username(), idUsuario)) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con el username '" + request.username() + "'"
            );
        }

        if (usuarioRepository.existsByEmailAndIdUsuarioNot(request.email(), idUsuario)) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con el email '" + request.email() + "'"
            );
        }

        usuario.setUsername(request.username());
        usuario.setNombreCompleto(request.nombreCompleto());
        usuario.setEmail(request.email());

        if (request.password() != null && !request.password().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.password()));
        }

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(guardado);
    }

    @Override
    public Map<String, Object> eliminarUsuarioPorAdmin(Long idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe un usuario con id " + idUsuario
                ));

        if (usuario.getUsername().equalsIgnoreCase("admin")) {
            throw new ReglaNegocioException("No se permite eliminar la cuenta principal de ADMIN");
        }

        //  mensaje de la FK
        boolean tienePrestamos = !prestamoRepository.findByUsuario(usuario).isEmpty();
        if (tienePrestamos) {
            throw new ReglaNegocioException(
                    "No se puede eliminar el usuario porque tiene préstamos registrados. " +
                            "Primero elimina o gestiona sus préstamos."
            );
        }

        usuarioRepository.delete(usuario);

        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Usuario eliminado correctamente");
        body.put("idEliminado", idUsuario);

        return body;
    }
}
