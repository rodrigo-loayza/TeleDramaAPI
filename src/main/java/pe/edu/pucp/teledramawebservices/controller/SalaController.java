package pe.edu.pucp.teledramawebservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.teledramawebservices.entity.Sala;
import pe.edu.pucp.teledramawebservices.repository.SalaRepository;
import pe.edu.pucp.teledramawebservices.repository.TeatroRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sala")
public class SalaController {

    @Autowired
    private TeatroRepository teatroRepository;

    @Autowired
    private SalaRepository salaRepository;

    @GetMapping(value = "/teatro/{idteatro}")
    public ResponseEntity<HashMap<String, Object>> salasActivasPorTeatro(@PathVariable("idteatro") String idStr) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            assert teatroRepository.existsById(Integer.parseInt(idStr));
            Optional<List<Sala>> optSalas = salaRepository.salasPorIdteatroYEstado(Integer.parseInt(idStr), "activo");
            return optSalas.map(salas -> {
                response.put("result", "success");
                response.put("salas", salas);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }).orElseGet(() -> {
                response.put("msg", "Teatro no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            });
        } catch (NumberFormatException e) {
            response.put("msg", "Id de teatro debe ser un número entero");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


}
