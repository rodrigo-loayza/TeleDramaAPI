package pe.edu.pucp.teledramaapi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cardcredential")
@Getter
@Setter
public class Cardcredential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "cardnumber", nullable = false, length = 19)
    private String cardnumber;

    @Column(name = "expirationdate", nullable = false, length = 5)
    private String expirationdate;

    @Column(name = "securitycode", nullable = false, length = 3)
    private String securitycode;

    @Column(name = "typecard", length = 15)
    private String typecard;

    @Column(name = "nombre", length = 10)
    private String nombre;

    @Column(name = "apellido", length = 10)
    private String apellido;

}