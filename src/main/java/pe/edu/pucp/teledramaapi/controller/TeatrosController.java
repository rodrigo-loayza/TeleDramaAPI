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
    public ResponseEntity<HashMap<String, Object>> teatrosPorFecha(@RequestParam("idobra") String idObra,@RequestParam("fecha") String fecha ) {
        HashMap<String, Object> response = new HashMap<>();
        try {
//            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//            Optional<List<Teatro>> teatros = teatroRepository.teatrosPorFecha( dateFormat.parse(fecha), Integer.parseInt(idObra));
            Optional<List<Teatro>> teatros = teatroRepository.teatrosPorFecha(fecha, Integer.parseInt(idObra));
            return teatros.map(teatritos -> {
                response.put("result", "success");
                response.put("teatros", teatritos);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }).orElseGet(() -> {
                response.put("msg", "Fecha no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //
            });
        } catch (NumberFormatException e) {
            response.put("msg", "Error de parámetros");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/teatrosFrecuentes")
    public List<TeatrosFrecuentesDto> teatrosFrecuentes(@RequestParam("id") int id){
        return  teatroRepository.teatrosFrecuentesList(id);
    }

    @GetMapping(value = "/teatroaCargo")
    public List<TeatroaCargoDto> teatroaCargo(@RequestParam("id") int id){
        return teatroRepository.teatroaCargoList(id);
    }

}
