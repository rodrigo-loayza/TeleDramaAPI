package pe.edu.pucp.teledramaapi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "elenco")
@Getter
@Setter
public class Elenco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "foto", nullable = false, length = 100)
    private String foto;

    @Column(name = "correo", nullable = false, length = 50)
    @Email(message = "Ingrese un email válido")
    @NotBlank(message = "Debe ingresar un email")
    private String correo;

    @Column(name = "nombre", nullable = false, length = 50)
    @Size(min = 3, max = 30, message = "Nombre debe tener entre 3 y 30 caracteres")
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 50)
    @Size(min = 3, max = 50, message = "Apellido debe tener entre 3 y 50 caracteres")
    private String apellido;

    @Column(name = "rol", nullable = false)
    @NotBlank(message = "Debe elegir un rol para el artista")
    private String rol;

    @Column(name = "telefeno", length = 9)
    @Digits(integer = 9, fraction = 0, message = "Debe ser un número entero")
    private String telefeno;

    @ManyToMany(mappedBy = "elencoPorObra")
    private List<Obra> obras;

}