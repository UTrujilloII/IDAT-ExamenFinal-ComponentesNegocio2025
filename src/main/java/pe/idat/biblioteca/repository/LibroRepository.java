package pe.idat.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.biblioteca.entity.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {
}
