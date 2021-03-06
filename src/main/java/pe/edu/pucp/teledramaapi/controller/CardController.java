package pe.edu.pucp.teledramaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.teledramaapi.entity.Cardcredential;
import pe.edu.pucp.teledramaapi.repository.CardRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/card")
public class CardController {

    @Autowired
    CardRepository cardRepository;

    @GetMapping("/lista")
    public ResponseEntity<HashMap<String, Object>> listaTarjetas() {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Optional<List<Cardcredential>> lista = Optional.of(cardRepository.findAll());
            return lista.map(cards-> {
                response.put("result", "success");
                response.put("data", cards);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }).orElseGet(() -> {
                response.put("result", "failure");
                response.put("data", "No encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //
            });
        } catch (NumberFormatException e) {
            response.put("result", "failure");
            response.put("data", "Error de parámetros");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}