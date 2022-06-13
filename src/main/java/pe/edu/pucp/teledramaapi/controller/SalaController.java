package pe.edu.pucp.teledramaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.teledramaapi.entity.Funcion;
import pe.edu.pucp.teledramaapi.entity.Sala;
import pe.edu.pucp.teledramaapi.repository.FuncionRepository;
import pe.edu.pucp.teledramaapi.repository.SalaRepository;
import pe.edu.pucp.teledramaapi.validation.ErrorMessage;
import pe.edu.pucp.teledramaapi.validation.ValidationResponse;

import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/sala")
public class SalaController {

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private FuncionRepository funcionRepository;

    @GetMapping(value = "/gestion/teatro/{idteatro}")
    public ResponseEntity<HashMap<String, Object>> salasActivasPorTeatro(@PathVariable("idteatro") String idStr) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Optional<List<Sala>> optSalas = salaRepository.salasPorIdteatroYEstado(Integer.parseInt(idStr), "activo");
            return optSalas.map(salas -> {
                response.put("result", "success");
                response.put("salas", salas);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }).orElseGet(() -> {
                response.put("result", "failure");
                response.put("msg", "Teatro no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // o no tiene salas xd
            });
        } catch (NumberFormatException e) {
            response.put("result", "failure");
            response.put("msg", "Id de teatro debe ser un número entero");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/gestion/guardar", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ValidationResponse nuevaFuncion(@Valid Funcion funcion, BindingResult bindingResult) {
        ValidationResponse validationResponse = new ValidationResponse();

        // En caso no haya cambios al editar, no se analizan los errores ni se guardan cambios.
        Funcion funcionDB = null;
        boolean cambioFecha = false;
        if (funcion.getId() != null) {
            funcionDB = funcionRepository.findById(funcion.getId()).orElse(new Funcion());
            // Para validar que no se pueda editar una función inactiva
            if (funcionDB.getEstado().equals("inactivo")) {
                validationResponse.setStatus("INVALID");
                return validationResponse;
            }
            boolean cambioCosto = !(funcionDB.getCostoticket().compareTo(funcion.getCostoticket()) == 0);
            cambioFecha = !funcionDB.getFechahora().isEqual(funcion.getFechahora());
            if (!cambioCosto && !cambioFecha) {
                validationResponse.setStatus("NO_CHANGES");
                return validationResponse;
            }
        }

        // Si se usa el tipo de variable como clases y no se completa es null, pero por el initbinder si no se completa es 0.
        boolean aforoInvalido = false;
        if (funcion.getAforofuncion() != 0 && funcion.getSala().getAforo() != 0)
            aforoInvalido = funcion.getAforofuncion() > funcion.getSala().getAforo();

        // La explicación de la validación está en el query de FuncionRepository =)
        boolean fechaInvalida = false;
        if (funcion.getFechahora() != null) {
            Integer idfuncion = funcion.getId() != null ? funcion.getId() : 0;
            fechaInvalida = funcionRepository.validarFechaFuncion(funcion.getObra().getId(), funcion.getFechahora(), idfuncion).size() != 0;
        }

        if (bindingResult.hasErrors() || aforoInvalido || fechaInvalida) {
            validationResponse.setStatus("FAIL"); // status: "ERROR"

            if (aforoInvalido)
                bindingResult.addError(new FieldError("funcion", "aforofuncion", "Sobrepasa el máximo"));
            if (fechaInvalida)
                bindingResult.addError(new FieldError("funcion", "fechahora", "Hay cruce de fechas"));
            // Tal vez podriamos implementar que se muestre la fecha con la que se cruza, pq la retorna el query

            List<ErrorMessage> errorMessages = new ArrayList<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                // Al editar, para no marcar fecha pasada o cruce, solo se toman en cuenta sus errores si hay cambio
                if (!cambioFecha && fieldError.getField().equals("fechahora") && funcion.getId() != null) continue;
                errorMessages.add(new ErrorMessage(fieldError.getField(), fieldError.getDefaultMessage()));
            }
            validationResponse.setErrorMessageList(errorMessages); // errorMessages: {"..."}

            // Es decir que se estaba editando y solo habían errores de fecha que eran porque no había cambio,
            // lo que significa que hay edición exitosa
            if (errorMessages.size() == 0 && funcion.getId() != null) {
                validationResponse.setStatus("SUCCESS");
                assert funcionDB != null;
                funcionDB.setCostoticket(funcion.getCostoticket()); // Al guardar el normal hay error por las validaciones
                funcionRepository.save(funcionDB);
            }
        } else {
            validationResponse.setStatus("SUCCESS");
            funcionRepository.save(funcion);
        }

        return validationResponse;
    }

    @InitBinder("funcion")
    // Init binder pq messages.properties no funciona con ajax (al menos no encontré la forma :( )
    public void validadorFuncion(WebDataBinder binder) {
        PropertyEditorSupport integerValidator = new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    this.setValue(Integer.parseInt(text));
                } catch (NumberFormatException e) {
                    this.setValue(0);
                }
            }
        };
        PropertyEditorSupport doubleValidator = new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    this.setValue(Double.parseDouble(text));
                } catch (NumberFormatException e) {
                    this.setValue(0);
                }
            }
        };
        PropertyEditorSupport dateValidator = new PropertyEditorSupport() {
            // Para casos de insertar fechas inválidas, como un año de 5 dígitos
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    this.setValue(LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                } catch (DateTimeParseException e) {
                    this.setValue(LocalDateTime.now().minusDays(1)); // Se pone fecha pasada en caso de error
                    // para usar el mensaje del @Future
                }
            }
        };
        binder.registerCustomEditor(Integer.class, "aforofuncion", integerValidator);
        binder.registerCustomEditor(Double.class, "costoticket", doubleValidator);
        binder.registerCustomEditor(LocalDateTime.class, "fechahora", dateValidator);

    }
}
