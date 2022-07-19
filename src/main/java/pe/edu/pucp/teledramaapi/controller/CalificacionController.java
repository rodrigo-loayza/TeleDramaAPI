package pe.edu.pucp.teledramaapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.teledramaapi.entity.Calificacion;
import pe.edu.pucp.teledramaapi.entity.Cliente;
import pe.edu.pucp.teledramaapi.entity.Elenco;
import pe.edu.pucp.teledramaapi.entity.Obra;
import pe.edu.pucp.teledramaapi.repository.CalificacionRepository;
import pe.edu.pucp.teledramaapi.repository.ClienteRepository;
import pe.edu.pucp.teledramaapi.repository.ElencoRepository;
import pe.edu.pucp.teledramaapi.repository.ObraRepository;

import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping(value = "/calificacion")
public class CalificacionController {

    @Autowired
    CalificacionRepository calificacionRepository;

    @Autowired
    ObraRepository obraRepository;

    @Autowired
    ElencoRepository elencoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @PostMapping(value = "/guardar", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE + "; charset=utf-8"})
    public ResponseEntity guardarCalificacion(
            @RequestParam(value = "cliente") String idClienteStr,
            @RequestParam(value = "listaE") String listaE,
            @RequestParam(value = "listaO") String listaO
    ) {
        try {
            idClienteStr = idClienteStr.replaceAll("\"", "");
            Integer idCliente = Integer.parseInt(idClienteStr);

            if (!clienteRepository.existsById(idCliente)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Cliente cliente = clienteRepository.findById(idCliente).orElse(null);
            HashMap<String, String> listaElenco = new ObjectMapper().readValue(listaE, HashMap.class);
            HashMap<String, String> listaObra = new ObjectMapper().readValue(listaO, HashMap.class);

            Integer idObra = null;
            Integer estrellasObra = null;
            for (String idObraStr : listaObra.keySet()) {
                idObra = Integer.parseInt(idObraStr.equals("") ? "0" : idObraStr);
                estrellasObra = Integer.parseInt(listaObra.get(idObraStr));
                break;
            }

            if (idObra == null || !obraRepository.existsById(idObra)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }

            Obra obra = obraRepository.findById(idObra).orElse(null);

            if (estrellasObra != 0) calificacionRepository.save(new Calificacion(estrellasObra, obra, cliente));

            for (String elencoIdStr : listaElenco.keySet()) {
                Integer idElenco = Integer.parseInt(elencoIdStr);
                Integer estrellasElenco = Integer.parseInt(listaElenco.get(elencoIdStr));

                if (elencoRepository.existsById(idElenco)) {
                    Elenco elenco = elencoRepository.findById(idElenco).orElse(null);

                    calificacionRepository.save(new Calificacion(estrellasElenco, elenco, obra, cliente));
                }
            }

            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

}
