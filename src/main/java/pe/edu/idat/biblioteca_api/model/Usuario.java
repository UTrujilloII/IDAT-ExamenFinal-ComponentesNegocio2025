package pe.edu.idat.biblioteca_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuarios")
@Getter @Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre de usuario es obligatorio")
    @Column(unique = true)
    private String username;

    @NotNull(message = "La contraseña es obligatoria")
    @JsonIgnore // Evita que la contraseña sea serializada en la respuesta JSON
    private String password;

    @NotNull(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    // Relación ManyToMany: Un usuario puede tener varios roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();
}