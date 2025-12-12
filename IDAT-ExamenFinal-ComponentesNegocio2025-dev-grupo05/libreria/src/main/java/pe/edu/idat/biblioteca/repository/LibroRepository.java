package pe.edu.idat.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.idat.biblioteca.entity.Libro;
import pe.edu.idat.biblioteca.entity.Prestamo;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByActivoTrue();

    List<Libro> findByTituloContainingIgnoreCaseAndActivoTrue(String titulo);

    List<Libro> findByAutorContainingIgnoreCaseAndActivoTrue(String autor);

    List<Libro> findByCategoriaAndActivoTrue(String categoria);


}