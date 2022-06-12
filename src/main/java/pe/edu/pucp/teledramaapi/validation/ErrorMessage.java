package pe.edu.pucp.teledramaapi.validation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessage {

    private String fieldName;
    private String message;

    public ErrorMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

}
