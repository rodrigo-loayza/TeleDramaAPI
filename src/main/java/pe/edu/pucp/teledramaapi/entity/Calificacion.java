package pe.edu.pucp.teledramaapi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "calificacion")
@Getter
@Setter
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "estrellas", nullable = false)
    private Integer estrellas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idelenco")
    private Elenco idelenco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idobra")
    private Obra idobra;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idcliente", nullable = false)
    private Cliente idcliente;

    public Calificacion() {
    }
    public Calificacion(Integer estrellas, Obra idobra, Cliente idcliente) {
        this.estrellas = estrellas;
        this.idobra = idobra;
        this.idcliente = idcliente;
    }

    public Calificacion(Integer estrellas, Elenco idelenco, Obra idobra, Cliente idcliente) {
        this.estrellas = estrellas;
        this.idelenco = idelenco;
        this.idobra = idobra;
        this.idcliente = idcliente;
    }
}