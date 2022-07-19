package pe.edu.pucp.teledramaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.teledramaapi.entity.Sala;
import pe.edu.pucp.teledramaapi.repository.SalaRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/sala")
public class SalaController {

    @Autowired
    private SalaRepository salaRepository;

    @GetMapping(value = "/gestion/teatro/{idteatro}")
    public ResponseEntity<HashMap<String, Object>> salasActivasPorTeatro(@PathVariable("idteatro") String idStr) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Optional<List<Sala>> optSalas = salaRepository.salasPorIdteatroYEstado(Integer.parseInt(idStr), "activo");
            return optSalas.map(salas -> {
                response.put("result", "success");
                response.put("data", salas);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }).orElseGet(() -> {
                response.put("result", "failure");
                response.put("data", "Teatro no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            });
        } catch (NumberFormatException e) {
            response.put("result", "failure");
            response.put("data", "Id de teatro debe ser un número entero");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
