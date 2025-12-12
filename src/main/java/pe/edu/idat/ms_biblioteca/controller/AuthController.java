package pe.edu.idat.ms_biblioteca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import pe.edu.idat.ms_biblioteca.dto.*;
import pe.edu.idat.ms_biblioteca.entity.Rol;
import pe.edu.idat.ms_biblioteca.entity.Usuario;
import pe.edu.idat.ms_biblioteca.repository.UsuarioRepository;
import pe.edu.idat.ms_biblioteca.repository.RolRepository;
import pe.edu.idat.ms_biblioteca.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder encoder;

    // 🔥 Registro
    @PostMapping("/register")
    public String register(@RequestBody UsuarioRegistroDTO dto) {

        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            return "El correo ya está registrado";
        }

        Rol rol = rolRepository.findByNombre("USUARIO")
                .orElseThrow(() -> new RuntimeException("Error: Rol USUARIO no encontrado."));

        Usuario u = new Usuario();
        u.setNombre(dto.nombre());
        u.setEmail(dto.email());
        u.setPassword(encoder.encode(dto.password()));
        u.setRol(rol);

        usuarioRepository.save(u);

        return "Usuario registrado correctamente";
    }


    // 🔥 Login
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        Usuario user = usuarioRepository.findByEmail(dto.email()).orElseThrow();

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRol().getNombre()
        );

        return new LoginResponseDTO(token);
    }
}
