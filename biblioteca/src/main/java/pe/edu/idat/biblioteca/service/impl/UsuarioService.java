package pe.edu.idat.biblioteca.service.impl; // Nota: He corregido el paquete, asumo que 'impl' no va en la interfaz

import java.util.List;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioRequest;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioResponse;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioPatchRequest; // ¡NUEVO IMPORT!

// Nota: Asegúrate de que esta interfaz NO esté en el paquete 'impl'
public interface UsuarioService {
    UsuarioResponse crearUsuario(UsuarioRequest request);
    List<UsuarioResponse> listarTodos();
    UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request);

    // --- NUEVA FIRMA PARA EL MÉTODO PATCH ---
    UsuarioResponse actualizarParcialUsuario(Long id, UsuarioPatchRequest request);
    // ----------------------------------------

    void eliminarUsuario(Long id);
}