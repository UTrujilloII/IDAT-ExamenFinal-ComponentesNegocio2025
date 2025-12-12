package com.proyecto.idat.ms_gestion_ventas.service;

import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.ActualizarPrestamoAdminRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.ActualizarPrestamoUsuarioRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.PrestamoRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.PrestamoResponse;
import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.PrestamoUsuarioRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PrestamoService {

    // ADMIN registra préstamo para cualquier usuario
    PrestamoResponse registrarPrestamo(PrestamoRequest request);

    // USUARIO se presta un libro para sí mismo
    PrestamoResponse registrarPrestamoUsuario(PrestamoUsuarioRequest request,
                                              UserDetails usuarioActual);

    // USUARIO devuelve uno de SUS préstamos
    PrestamoResponse devolverPrestamoUsuario(Long idPrestamo,
                                             UserDetails usuarioActual);

    // ADMIN puede marcar como devuelto cualquier préstamo
    PrestamoResponse devolverPrestamoAdmin(Long idPrestamo);

    // Historial del usuario autenticado (ADMIN o USUARIO)
    List<PrestamoResponse> historialUsuario(UserDetails usuarioActual);

    // ADMIN ve TODOS los préstamos
    List<PrestamoResponse> listarTodos();

    // Actualizar préstamo del propio usuario
    PrestamoResponse actualizarPrestamoUsuario(Long idPrestamo,
                                               String usernameActual,
                                               ActualizarPrestamoUsuarioRequest request);

    // Actualizar préstamo de cualquier usuario (solo ADMIN)
    PrestamoResponse actualizarPrestamoAdmin(Long idPrestamo,
                                             ActualizarPrestamoAdminRequest request);

    // Eliminar / cancelar préstamo del propio usuario
    void eliminarPrestamoUsuario(Long idPrestamo, String usernameActual);

    // Eliminar préstamo de un usuario (solo ADMIN, con idUsuario + idPrestamo)
    void eliminarPrestamoAdmin(Long idUsuario, Long idPrestamo);
}
