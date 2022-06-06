package pe.edu.pucp.teledramaapi.dto;

import java.time.LocalDateTime;

public interface FuncionesPorObraDto {
    Integer getIdfuncion();
    Double getCostoticket();
    LocalDateTime getFechahora();
    Integer getCantidadtickets();
    String getAforo();
    String getNombreteatro();
    Integer getIdsala();
    String getNombresala();
    String getEstado();
}

