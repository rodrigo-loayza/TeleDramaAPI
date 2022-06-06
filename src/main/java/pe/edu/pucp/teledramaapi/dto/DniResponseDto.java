package pe.edu.pucp.teledramaapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DniResponseDto {
    private boolean success;
    private DatosDniDto data;
}
