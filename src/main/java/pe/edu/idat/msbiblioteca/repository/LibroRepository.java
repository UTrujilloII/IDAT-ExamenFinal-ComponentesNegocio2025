package pe.edu.idat.msbiblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.idat.msbiblioteca.entity.Libro;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Libro.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas sobre libros.
 *
 * @author Jonathan Jiménez
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    /**
     * Busca un libro por su código ISBN
     *
     * @param isbn Código ISBN del libro
     * @return Optional conteniendo el libro si existe
     */
    Optional<Libro> findByIsbn(String isbn);

    /**
     * Busca libros por título (búsqueda parcial, no sensible a mayúsculas)
     *
     * @param titulo Título o parte del título del libro
     * @return Lista de libros que coinciden con el título
     */
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    /**
     * Busca libros por autor (búsqueda parcial, no sensible a mayúsculas)
     *
     * @param autor Nombre del autor o parte del nombre
     * @return Lista de libros del autor especificado
     */
    List<Libro> findByAutorContainingIgnoreCase(String autor);

    /**
     * Busca libros por categoría
     *
     * @param categoria Categoría del libro
     * @return Lista de libros de la categoría especificada
     */
    List<Libro> findByCategoria(String categoria);

    /**
     * Busca libros disponibles (con copias disponibles > 0)
     *
     * @return Lista de libros disponibles para préstamo
     */
    @Query("SELECT l FROM Libro l WHERE l.copiasDisponibles > 0 AND l.estado = 'DISPONIBLE'")
    List<Libro> findLibrosDisponibles();

    /**
     * Busca libros por estado (DISPONIBLE, AGOTADO, EN_MANTENIMIENTO)
     *
     * @param estado Estado del libro
     * @return Lista de libros con el estado especificado
     */
    List<Libro> findByEstado(String estado);

    /**
     * Busca libros por título, autor o ISBN (búsqueda global)
     *
     * @param keyword Palabra clave para buscar
     * @return Lista de libros que coinciden con la búsqueda
     */
    @Query("SELECT l FROM Libro l WHERE " +
           "LOWER(l.titulo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.autor) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.isbn) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.categoria) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Libro> buscarLibros(@Param("keyword") String keyword);

    /**
     * Obtiene las categorías distintas de libros registrados
     *
     * @return Lista de categorías únicas
     */
    @Query("SELECT DISTINCT l.categoria FROM Libro l WHERE l.categoria IS NOT NULL ORDER BY l.categoria")
    List<String> findAllCategorias();

    /**
     * Cuenta la cantidad total de libros registrados
     *
     * @return Número total de libros
     */
    @Query("SELECT COUNT(l) FROM Libro l")
    Long contarTotalLibros();

    /**
     * Cuenta la cantidad de libros disponibles
     *
     * @return Número de libros disponibles
     */
    @Query("SELECT COUNT(l) FROM Libro l WHERE l.copiasDisponibles > 0 AND l.estado = 'DISPONIBLE'")
    Long contarLibrosDisponibles();
}

