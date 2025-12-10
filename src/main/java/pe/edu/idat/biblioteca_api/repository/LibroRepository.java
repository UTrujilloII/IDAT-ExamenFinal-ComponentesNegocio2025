package pe.edu.idat.biblioteca_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.idat.biblioteca_api.model.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    // Aquí podrías agregar: Optional<Libro> findByIsbn(String isbn);
    // Pero con el básico nos basta por ahora.
}