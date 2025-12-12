package pe.edu.idat.ms_biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.idat.ms_biblioteca.entity.Libro;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long>
{
    Libro findByTitulo(String titulo);

    Libro findByCodigo(String codigo);
}
