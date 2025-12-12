package pe.edu.idat.ms_biblioteca.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Prestamo> prestamos;

}
