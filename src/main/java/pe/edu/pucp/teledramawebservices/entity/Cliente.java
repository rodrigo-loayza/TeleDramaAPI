package pe.edu.pucp.teledramawebservices.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "cliente")
@Getter
@Setter
public class Cliente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "nombre", nullable = false, length = 50)
    @Size(min = 3, max = 30, message = "Nombre debe tener entre 3 y 30 caracteres")
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 50)
    @Size(min = 3, max = 50, message = "Apellido debe tener entre 3 y 50 caracteres")
    private String apellido;

    @Column(name = "correo", nullable = false, length = 50, unique = true)
    @Email(message = "Ingrese un email válido")
    @NotBlank(message = "Debe ingresar un email")
    private String correo;

    @Column(name = "dni", nullable = false, length = 8)
    @Digits(integer = 8, fraction = 0, message = "Debe ser un número")
    @Size(min = 8, max = 8, message = "DNI debe tener 8 dígitos")
    private String dni;

    @Column(name = "contrasena", nullable = false, length = 32)
    @NotBlank(message = "Debe ingresar una contraseña")
    private String contrasena;

    @Column(name = "telefono", nullable = false, length = 9)
    @Digits(integer = 9, fraction = 0, message = "Debe ser un número")
    @Size(min = 9, max = 9, message = "Telefono debe tener 9 dígitos")
    private String telefono;

    @Column(name = "fechanacimiento", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "No puede ser una fecha del futuro")
    @NotNull(message = "Ingresar fecha de nacimiento")
    private LocalDate fechanacimiento;

    @Column(name = "enabled")
    private boolean estado = true;

    @Column(name = "direccion", length = 100)
    @Size(min = 0, max = 100, message = "Dirección puede tener máximo 100 caracteres")
    private String direccion;

    @Column(name = "image", length = 200, insertable = false)
    private String image = "https://teledrama.blob.core.windows.net/imagenes/profile_pic_default.png";

    @Column(name = "rol", nullable = false, length = 45, insertable = false)
    private String rol = "cliente";

    private String rsttoken;
}