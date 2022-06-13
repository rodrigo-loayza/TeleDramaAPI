package pe.edu.pucp.teledramaapi.dto;

import java.time.LocalDateTime;

public interface ReporteDto {
    Integer getIdobra();
    String getNombre();
    Integer getIdfuncion();
    LocalDateTime getFechahora();
    Integer getIdsala();
    String getSala();
    Integer getIdteatro();
    String getTeatro();
    Double getPrecioticket();
    Integer getAsistencia();
    Double getPorcentaje();
    Double getMonto();
}
