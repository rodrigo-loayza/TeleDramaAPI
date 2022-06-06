package pe.edu.pucp.teledramawebservices.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatosDniDto {
    private String dni;
    private String name;
    private String mothers_lastname;
    private String fathers_lastname;
    private String fullname;
    private String verification_code;
    private String updated_at;
    private String info;
}
