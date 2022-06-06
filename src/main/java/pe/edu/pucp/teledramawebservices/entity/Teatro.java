package pe.edu.pucp.teledramawebservices.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "teatro")
@Getter
@Setter
public class Teatro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank
    @Column(name = "nombre", nullable = false, length = 45)
    @Size(min = 0, max = 45, message = "Nombre puede tener máximo 45 caracteres")
    private String nombre;

    @Column(name = "imagen", nullable = false, length = 300)
    private String imagen;

    @NotBlank
    @Column(name = "direccion", nullable = false, length = 100)
    @Size(min = 0, max = 100, message = "Dirección puede tener máximo 100 caracteres")
    private String direccion;

    @NotBlank
    @Column(name = "coordenadas", nullable = false, length = 50)
    private String coordenadas;

    @NumberFormat
    @NotNull
    @Column(name = "cantidadsalas", nullable = false)
    private Integer cantidadsalas;


    @Column(name = "estado", nullable = false)
    @NotNull(message = "Estado no debe estar vacío")
    private String estado;

    @NotBlank
    @Column(name = "horasemana", nullable = false, length = 45)
    private String horasemana;

    @NotBlank
    @Column(name = "horafindesemana", nullable = false, length = 45)
    private String horafindesemana;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "iddistrito", nullable = false)
    @NotNull(message = "Distrito no debe estar vacío")
    private Distrito distrito;

    @ManyToMany(mappedBy = "teatrosPorEmpleado")
    private List<Empleado> empleados;

}