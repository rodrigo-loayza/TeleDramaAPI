package pe.edu.pucp.teledramaapi.dto;

import java.time.LocalDate;

public interface FuncionDatosDto {

    Integer getIdfuncion();
    Double getCostoticket();
    Integer getDuracion();
    Integer getIdobra();
    String getNombreobra();
    String  getImagen();
    LocalDate getFechafuncion();
    String getHorafuncion();
    String getNombreteatro();
    Integer getAforo();

}
