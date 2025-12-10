package pe.edu.idat.biblioteca_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.idat.biblioteca_api.model.Prestamo;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    
    // Método para filtrar préstamos por usuario específico
    List<Prestamo> findByUsuarioId(Long usuarioId);
}