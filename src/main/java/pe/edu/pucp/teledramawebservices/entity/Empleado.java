package pe.edu.pucp.teledramawebservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity

@Table(name = "empleado")
@Getter
@Setter
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 50)
    @Size(min = 3, max = 30, message = "Nombre debe tener entre 3 y 30 caracteres")
    @NotNull(message = "Ingresar nombre")
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 50)
    @Size(min = 3, max = 30, message = "Apellido debe tener entre 3 y 30 caracteres")
    @NotNull(message = "Ingresar apellido")
    private String apellido;

    @Column(name = "correo", nullable = false, length = 50)
    @Size(min = 3, max = 30, message = "Correo debe tener entre 3 y 30 caracteres")
    @NotNull(message = "Ingresar correo")
    @Email
    private String correo;

    @Column(name = "contrasena")
    private String contrasena = "$2a$10$jzeb5RKOyW2DrnarFZfsJ.HQVgdERtf404H7TX1vVcJ/hZEI3Wkoy";

    @Column(name = "enabled", nullable = false)
    private boolean estado = true;

    @Column(name = "dni", length = 8)
    @Digits(integer = 8, fraction = 0, message = "Debe ser un número")
    @Size(min = 8, max = 8, message = "DNI debe tener 8 dígitos")
    private String dni;

    @Lob
    @Column(name = "rol", nullable = false)
    @NotNull
    private String rol;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "teatro_empleado",
            joinColumns = @JoinColumn(name = "idempleado"),
            inverseJoinColumns = @JoinColumn(name = "idteatro"))
    @NotNull
    private List<Teatro> teatrosPorEmpleado;

    private String rsttoken;

}