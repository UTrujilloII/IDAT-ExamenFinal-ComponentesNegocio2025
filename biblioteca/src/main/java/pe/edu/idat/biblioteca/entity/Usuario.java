package pe.edu.idat.biblioteca.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre",length = 50,nullable = false)
    private String nombre;

    @Column(name = "apellido",length = 50,nullable = false)
    private String apellido;

    @Column(name = "telefono",length = 9,nullable = false)
    private String telefono;

    @Column(name = "email",length = 100,nullable = false,unique = true)
    private String email;

    @Column(name = "password",length = 100,nullable = false)
    private String password;

    @Column(name = "enabled",nullable = false)
    private Boolean enabled = true;

    @Column(name = "fecha_creacion",nullable = false)
    private LocalDate fechaCreacion;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles=new HashSet<>();

}
