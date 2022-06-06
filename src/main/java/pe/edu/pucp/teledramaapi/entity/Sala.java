package pe.edu.pucp.teledramaapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sala")
@Getter
@Setter
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 15)
    private String nombre;

    @Column(name = "aforo", nullable = false)
    private Integer aforo;

    @Lob
    @Column(name = "estado", nullable = false)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idteatro", nullable = false)
    @JsonBackReference
    private Teatro teatro;

}