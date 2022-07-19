package pe.edu.pucp.teledramaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.teledramaapi.dto.TeatroaCargoDto;
import pe.edu.pucp.teledramaapi.dto.TeatrosFrecuentesDto;
import pe.edu.pucp.teledramaapi.entity.Teatro;
import pe.edu.pucp.teledramaapi.repository.TeatroRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/teatro")
public class TeatrosController {

    @Autowired
    TeatroRepository teatroRepository;

    @GetMapping(value = "/gestion")
    public ResponseEntity<HashMap<String, Object>> teatrosPorFecha(@RequestParam("idobra") String idObra,@RequestParam(value = "fecha",required = false) String fecha ) {
        HashMap<String, Object> response = new HashMap<>();
        try {

            Optional<List<Teatro>> teatros;
            if (fecha == null || fecha.equals("")) {
                teatros = teatroRepository.teatrosPorObraHorarios(Integer.parseInt(idObra));
            }else {
                teatros = teatroRepository.teatrosPorFecha(fecha, Integer.parseInt(idObra));
            }

            return teatros.map(teatritos -> {
                response.put("result", "success");
                response.put("data", teatritos);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }).orElseGet(() -> {
                response.put("result", "failure");
                response.put("data", "Fecha no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //
            });
        } catch (NumberFormatException e) {
            response.put("result", "failure");
            response.put("data", "Error de parámetros");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/teatrosFrecuentes")
    public ResponseEntity<HashMap<String, Object>> teatrosFrecuentes(@RequestParam("id") int id) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Optional<List<TeatrosFrecuentesDto>> teatros = teatroRepository.teatrosFrecuentesList(id);
            return teatros.map(teatritosFrec -> {
                response.put("result", "success");
                response.put("data", teatritosFrec);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }).orElseGet(() -> {
                response.put("result", "failure");
                response.put("data", "Teatros frecuentes no encontrados");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //
            });
        } catch (NumberFormatException e) {
            response.put("result", "failure");
            response.put("data", "Error de parámetros");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/teatrosACargo")
    public ResponseEntity<HashMap<String, Object>> teatrosACargo(@RequestParam("id") int id) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Optional<List<TeatroaCargoDto>> teatros = teatroRepository.teatroaCargoList(id);
            return teatros.map(teatritosCar -> {
                response.put("result", "success");
                response.put("data", teatritosCar);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }).orElseGet(() -> {
                response.put("result", "failure");
                response.put("data", "Teatros a cargo no encontrados");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //
            });
        } catch (NumberFormatException e) {
            response.put("result", "failure");
            response.put("data", "Error de parámetros");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
