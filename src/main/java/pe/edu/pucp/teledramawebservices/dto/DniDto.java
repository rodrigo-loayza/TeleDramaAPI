package pe.edu.pucp.teledramawebservices.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DniDto {
    private String document;
    public DniDto(String document) {
        this.document = document;
    }
}
