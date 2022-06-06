package pe.edu.pucp.teledramaapi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "compra")
@Getter
@Setter
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "imagenqr", nullable = false, length = 500)
    private String imagenqr;

    @Column(name = "cantidadtickets", nullable = false)
    private Integer cantidadtickets;

    @Column(name = "fechacompra", nullable = false)
    private Instant fechacompra;

    @Lob
    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "fechacambio")
    private Instant fechacambio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idfuncion", nullable = false)
    private Funcion idfuncion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idcliente", nullable = false)
    private Cliente idcliente;

}