package pe.edu.pucp.teledramaapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "funcion")
@Getter
@Setter
public class Funcion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "fechahora", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Future(message = "Inserta una fecha valida")
    @NotNull(message = "Completar la fecha")
    private LocalDateTime fechahora;

    @Column(name = "costoticket", nullable = false)
    @Digits(integer = 4, fraction = 2, message = "Debe contener solo 2 decimales")
    @Min(value = 2, message = "Valor mínimo 2.00")
    private Double costoticket;

    @Column(name = "estado", nullable = false)
    private String estado = "activo";

    @Column(name = "aforofuncion", nullable = false)
    @Digits(integer = 3, fraction = 0, message = "Debe ser un entero")
    @Min(value = 10, message = "Valor mínimo 10")
    private Integer aforofuncion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idsala", nullable = false)
    @NotNull(message = "Debe seleccionar una sala para la obra")
    @JsonBackReference
    private Sala sala;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idobra", nullable = false)
    @JsonBackReference
    private Obra obra;

}