package pe.edu.pucp.teledramaapi.dto;

import java.time.LocalDate;

public interface FuncionesProximasDto {

    Integer getId();
    LocalDate getFecha();
    String getEstado();
    Integer getIdobra();
    Integer getIdteatro();
    String getTeatro();
    String getHora();
}
