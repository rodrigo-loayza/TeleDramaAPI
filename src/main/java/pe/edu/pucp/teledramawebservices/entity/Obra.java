package pe.edu.pucp.teledramawebservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "obra")
@Getter
@Setter
public class Obra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    @Size(max = 50, message = "Debe contener menos de 50 caracteres")
    @NotBlank(message = "Completar el nombre de la obra")
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    @Size(min = 3, max = 500, message = "Debe contener entre 3 y 500 caracteres")
    @NotBlank(message = "Completar el nombre de la obra")
    private String descripcion;

    @Column(name = "restriccionedad", nullable = false)
    @NotBlank(message = "Debe seleccionar la restricción")
    private String restriccionedad;

    @Column(name = "duracion", nullable = false)
    @Digits(integer = 4, fraction = 0, message = "Debe ser un número entero")
    @Max(value = 350, message = "Debe ser menor o igual a 350")
    @Min(value = 30, message = "Debe ser mayor o igual a 30")
    @NotNull(message = "Indicar la duración de la obra")
    private Integer duracion;

    @Column(name = "fotoprincipal", nullable = false, length = 500)
    private String fotoprincipal;

    @ManyToMany
    @JoinTable(name = "genero_obra",
            joinColumns = @JoinColumn(name = "idobra"),
            inverseJoinColumns = @JoinColumn(name = "idgenero"))
    @NotNull(message = "Debe seleccionar al menos un género")
    private List<Genero> generosPorObra;

    @ManyToMany
    @JoinTable(name = "obra_elenco",
            joinColumns = @JoinColumn(name = "idobra"),
            inverseJoinColumns = @JoinColumn(name = "idelenco"))
    private List<Elenco> elencoPorObra;

    @OneToMany(mappedBy = "obra")
    private List<Imagenesobra> imagenesobras;



}