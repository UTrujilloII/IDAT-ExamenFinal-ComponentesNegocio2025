package pe.edu.idat.msbiblioteca.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entidad que representa un rol del sistema (por ejemplo ROLE_ADMIN, ROLE_USER).
 *
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Entity
@Table(name = "rol")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Rol
{
    /**
     * Identificador único del rol (clave primaria)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del rol (ROLE_ADMIN, ROLE_USUARIO)
     * Debe ser único en el sistema
     */
    @Column(unique = true, nullable = false, length = 50)
    private String nombre;

    /**
     * Constructor para crear rol solo con nombre
     *
     * @param nombre Nombre del rol
     */
    public Rol(String nombre) {
        this.nombre = nombre;
    }
}
