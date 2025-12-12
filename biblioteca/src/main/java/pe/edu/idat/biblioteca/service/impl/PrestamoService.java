package pe.edu.idat.biblioteca.service.impl; // <<< AJUSTA ESTE PAQUETE SI TU INTERFACE ESTÁ EN OTRO LUGAR

import pe.edu.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoResponse;

import java.util.List;

/**
 * Define las operaciones de negocio relacionadas con la gestión de préstamos.
 */
public interface PrestamoService {

    /**
     * Crea uno o más registros de préstamo a partir de una única solicitud.
     * @param request DTO que contiene el ID de usuario, la lista de libros y fechas.
     * @return Lista de DTOs de respuesta con los IDs generados y detalles de cada préstamo creado.
     */
    List<PrestamoResponse> crearPrestamo(PrestamoRequest request);

    /**
     * Procesa la devolución de un único ítem de préstamo y actualiza el stock.
     * @param id ID del registro de préstamo a devolver.
     */
    void devolverPrestamo(Long id);

    /**
     * Obtiene el historial de préstamos de un usuario específico.
     * @param username Nombre de usuario para buscar el historial.
     * @return Lista de DTOs de respuesta con el historial de préstamos.
     */
    List<PrestamoResponse> obtenerHistorial(String username);

    /**
     * Obtiene los detalles de un préstamo específico por su ID.
     * @param id ID del préstamo.
     * @return DTO de respuesta del préstamo.
     */
    PrestamoResponse obtenerPrestamoPorId(Long id);

    /**
     * Lista todos los préstamos registrados en el sistema.
     * @return Lista de DTOs de respuesta de todos los préstamos.
     */
    List<PrestamoResponse> listarTodosLosPrestamos();
}