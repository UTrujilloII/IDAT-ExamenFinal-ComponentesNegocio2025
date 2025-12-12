package com.biblioteca.biblioteca_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ------------------------------------------------------------
    // DATOS PERSONALES
    // ------------------------------------------------------------

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    @Email(message = "El correo debe ser válido")
    @NotBlank(message = "El email no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false)
    private String password; // se almacena HASHEADA

    // ------------------------------------------------------------
    // ROLES
    // ------------------------------------------------------------

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Rol> roles = new HashSet<>();

    // ------------------------------------------------------------
    // PRESTAMOS
    // ------------------------------------------------------------

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Prestamo> prestamos = new ArrayList<>();


    // ------------------------------------------------------------
    // MÉTODOS UTILES (opcionales pero recomendados)
    // ------------------------------------------------------------

    /** Agrega un rol sin duplicarlo. */
    public void addRol(Rol rol) {
        roles.add(rol);
    }

    /** Remueve un rol del usuario. */
    public void removeRol(Rol rol) {
        roles.remove(rol);
    }
}
