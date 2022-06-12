package pe.edu.pucp.teledramaapi.dto;

import java.time.LocalDateTime;

public interface FuncionMasMenosVistaPorObraDto {

    Integer getIdobra();
    String getNombre();
    String getFotoprincipal();
    LocalDateTime getFechahora();
    Integer getIdsala();
    String getSala();
    Integer getIdteatro();
    String getTeatro();
    Integer getAsistencia();

}
