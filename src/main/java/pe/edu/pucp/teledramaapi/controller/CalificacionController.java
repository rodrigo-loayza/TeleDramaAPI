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

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @CrossOrigin("http://localhost:8080")
    @PostMapping(value = "/guardar", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE + "; charset=utf-8"})
    public ResponseEntity guardarCalificacion(@RequestParam(value = "listaE") String listaE,
                                              @RequestParam(value = "listaO") String listaO,
                                              @RequestParam(value = "cliente") String idClienteStr
    ) {
        try {
            idClienteStr = idClienteStr.replaceAll("\"", "");
            Integer idCliente = Integer.parseInt(idClienteStr);

            if (!clienteRepository.existsById(idCliente)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Cliente cliente = clienteRepository.findById(idCliente).get();
            Map<String, String> listaElenco = new ObjectMapper().readValue(listaE, HashMap.class);
            Map<String, String> listaObra = new ObjectMapper().readValue(listaO, HashMap.class);

            Integer idObra = null;
            Integer estrellasObra = null;
            for (String idObraStr : listaObra.keySet()) {
                idObra = Integer.parseInt(idObraStr);
                estrellasObra = Integer.parseInt(listaObra.get(idObraStr));
                break;
            }


            if (idObra == null || !obraRepository.existsById(idObra)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Obra obra = obraRepository.findById(idObra).get();

            Calificacion calificacionObra = new Calificacion();
            calificacionObra.setIdobra(obra);
            calificacionObra.setEstrellas(estrellasObra);
            calificacionObra.setIdcliente(cliente);

            calificacionRepository.save(calificacionObra);

            for (String elencoIdStr : listaElenco.keySet()) {
                Integer idElenco = Integer.parseInt(elencoIdStr);
                Integer estrellasElenco = Integer.parseInt(listaElenco.get(elencoIdStr));

                if (elencoRepository.existsById(idElenco)) {
                    Elenco elenco = elencoRepository.findById(idElenco).get();

                    Calificacion calificacionElenco = new Calificacion();
                    calificacionElenco.setIdobra(obra);
                    calificacionElenco.setIdelenco(elenco);
                    calificacionElenco.setEstrellas(estrellasElenco);
                    calificacionElenco.setIdcliente(cliente);

                    calificacionRepository.save(calificacionElenco);
                }
            }

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
