package pe.edu.pucp.teledramaapi.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public interface FuncionDatosDto {

    Integer getIdfuncion();
    Double getCostoticket();
    Integer getIdobra();
    String getNombreobra();
    String  getImagen();
    LocalDate getFechafuncion();
    String getHorafuncion();
    String getNombreteatro();
    Integer getAforo();

}
