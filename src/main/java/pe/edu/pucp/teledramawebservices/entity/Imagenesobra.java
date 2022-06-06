package pe.edu.pucp.teledramawebservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "imagenesobra")
@Getter
@Setter
public class Imagenesobra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "imagen", nullable = false, length = 500)
    private String imagen;

    @ManyToOne
    @JoinColumn(name="idobra")
    private Obra obra;


}