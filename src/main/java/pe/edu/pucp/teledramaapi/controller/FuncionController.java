package pe.edu.pucp.teledramaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.teledramaapi.dto.FuncionDatosDto;
import pe.edu.pucp.teledramaapi.dto.FuncionesProximasDto;
import pe.edu.pucp.teledramaapi.dto.HorasFuncionDto;
import pe.edu.pucp.teledramaapi.entity.Funcion;
import pe.edu.pucp.teledramaapi.repository.FuncionRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/funcion")
public class FuncionController {

    @Autowired
    FuncionRepository funcionRepository;

    @GetMapping(value = "/gestion")
    public ResponseEntity<HashMap<String, Object>> funcionPorObra(@RequestParam("idobra") String idObra,
                                                                  @RequestParam(value = "idteatro",required = false) String idteatro) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Optional<List<FuncionesProximasDto>> funciones;

            if (idteatro == null || idteatro.equals("")) {
                funciones = funcionRepository.funcionesProximasId(Integer.parseInt(idObra));
            }else {
                funciones = funcionRepository.funcionesProximasPorTeatro(Integer.parseInt(idObra),Integer.parseInt(idteatro));
            }

            return funciones.map(funcioncitas -> {
                response.put("result", "success");
                response.put("funciones", funcioncitas);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }).orElseGet(() -> {
                response.put("msg", "Funcion no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //
            });
        } catch (NumberFormatException e) {
            response.put("msg", "Error de parámetros");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/gestion/horas")
    public ResponseEntity<HashMap<String, Object>> horasFuncionPorObra(@RequestParam("idobra") String idObra, @RequestParam("fecha") String fecha, @RequestParam("idteatro") String idteatro) {
        HashMap<String, Object> response = new HashMap<>();
        try {
//            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//            Optional<List<HorasFuncionDto>> hora = funcionRepository.horasFuncionesPorTeatro(dateFormat.parse(fecha), Integer.parseInt(idObra), Integer.parseInt(idteatro));
            Optional<List<HorasFuncionDto>> hora = funcionRepository.horasFuncionesPorTeatro(fecha, Integer.parseInt(idObra), Integer.parseInt(idteatro));
            return hora.map(horitas -> {
                response.put("result", "success");
                response.put("horitas", horitas);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }).orElseGet(() -> {
                response.put("msg", "Funcion no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //
            });
        } catch (NumberFormatException e) {
            response.put("msg", "Error de parámetros");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/gestion/detalle")
    public ResponseEntity<HashMap<String, Object>> detalleFuncion(@RequestParam("idfuncion") String idfuncion) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Optional<FuncionDatosDto> funcion = funcionRepository.detalleFuncion(Integer.parseInt(idfuncion));
            if (funcion.isPresent()) {
                response.put("result", "success");
                response.put("funcioncita", funcion.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("msg", "Funcion no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //
            }
        } catch (NumberFormatException e) {
            response.put("msg", "Error de parámetros");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
