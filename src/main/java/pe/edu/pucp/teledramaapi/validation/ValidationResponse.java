package pe.edu.pucp.teledramaapi.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationResponse {

    private String status;
    private List<ErrorMessage> errorMessageList;

}
