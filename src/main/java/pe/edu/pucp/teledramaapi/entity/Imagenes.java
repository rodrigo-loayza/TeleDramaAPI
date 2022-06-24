package pe.edu.pucp.teledramaapi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "imagenes")
@Getter
@Setter
public class Imagenes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "imagen", nullable = false, length = 500)
    private String imagen;

    @ManyToOne
    @JoinColumn(name="idobra")
    private Obra obra;

    @ManyToOne
    @JoinColumn(name="idteatro")
    private Teatro teatro;


}