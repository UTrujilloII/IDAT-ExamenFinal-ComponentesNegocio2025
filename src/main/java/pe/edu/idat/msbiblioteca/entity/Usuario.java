package pe.edu.idat.msbiblioteca.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidad que representa un usuario del sistema de biblioteca.
 * Puede tener roles de ADMIN o USUARIO con diferentes privilegios.
 *
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Entity
@Table(name = "usuario")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Usuario
{
    /**
     * Identificador único del usuario (clave primaria)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario único para login
     */
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    /**
     * Contraseña encriptada del usuario
     */
    @Column(nullable = false)
    private String password;

    /**
     * Indica si la cuenta del usuario está habilitada
     */
    private boolean enabled = true;

    /**
     * Nombre completo del usuario
     */
    @Column(length = 100)
    private String nombreCompleto;

    /**
     * Correo electrónico del usuario
     */
    @Column(unique = true, length = 100)
    private String email;

    /**
     * Teléfono de contacto del usuario
     */
    @Column(length = 20)
    private String telefono;

    /**
     * Dirección del usuario
     */
    @Column(length = 200)
    private String direccion;

    /**
     * Fecha de registro del usuario en el sistema
     */
    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro = LocalDate.now();

    /**
     * Roles asignados al usuario (ADMIN, USUARIO)
     * Relación muchos a muchos con la entidad Rol
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "idusuario"),
            inverseJoinColumns = @JoinColumn(name = "idrol")
    )
    private Set<Rol> roles = new HashSet<>();

    /**
     * Lista de préstamos realizados por el usuario
     * Relación uno a muchos: un usuario puede tener muchos préstamos
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Prestamo> prestamos;

    /**
     * Verifica si el usuario tiene un rol específico
     *
     * @param nombreRol Nombre del rol a verificar
     * @return true si el usuario tiene el rol, false en caso contrario
     */
    public boolean tieneRol(String nombreRol) {
        return roles.stream()
                .anyMatch(rol -> rol.getNombre().equalsIgnoreCase(nombreRol));
    }
}
