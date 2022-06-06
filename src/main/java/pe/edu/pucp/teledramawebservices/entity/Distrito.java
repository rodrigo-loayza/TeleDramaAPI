package pe.edu.pucp.teledramawebservices.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "distrito")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
@Getter
@Setter
public class Distrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Min(value = 0, message = "Distrito no puede estar vacío")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

}