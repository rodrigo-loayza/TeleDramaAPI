package pe.edu.pucp.teledramaapi.dto;

import java.time.LocalDateTime;

public interface FuncionMasMenosVistaDto {

    Integer getIdobra();
    String getNombre();
    String getMiniatura();
    LocalDateTime getFechahora();
    Integer getIdsala();
    String getSala();
    Integer getIdteatro();
    String getTeatro();
    Integer getAsistencia();

}
