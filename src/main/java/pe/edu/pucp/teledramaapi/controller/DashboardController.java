package pe.edu.pucp.teledramaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.teledramaapi.dto.*;
import pe.edu.pucp.teledramaapi.repository.ElencoRepository;
import pe.edu.pucp.teledramaapi.repository.FuncionRepository;
import pe.edu.pucp.teledramaapi.repository.ObraRepository;
import pe.edu.pucp.teledramaapi.repository.TeatroRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private FuncionRepository funcionRepository;

    @Autowired
    private TeatroRepository teatroRepository;

    @Autowired
    private ObraRepository obraRepository;

    @Autowired
    private ElencoRepository elencoRepository;

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReporteDto> generarReporte() {
        return funcionRepository.generarReporte("20220301", "20220430", null, null);
    }

    @GetMapping(value = "/monto", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MontoObraReporteDto> montoRecaudadoPorObra() {
        return teatroRepository.montoRecaudadoPorObra("20220301", "20220430", null);
    }

    // Grafico: Filtro sin obra sin teatro | Filtro sin obra con teatro
    @PostMapping(value = "/asistencia/obra", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PorcentajeAsistenciaObraDto> obtenerAsistenciaPorObra(
            @RequestParam(value = "opcion", required = true) String opcion,
            @RequestParam(value = "inicio", required = false) String fechainicioStr,
            @RequestParam(value = "fin", required = false) String fechafinStr,
            @RequestParam(value = "idteatro", required = false) String idteatroStr) {
        try {
            SimpleDateFormat fechaformat = new SimpleDateFormat("yyyyMMdd");
            Date fechainicio = fechaformat.parse(fechainicioStr);
            Date fechafin = null;
            if (fechafinStr != null) {
                fechafin = fechaformat.parse(fechafinStr);
            }
            Integer idteatro = null;
            if (idteatroStr != null) {
                idteatro = Integer.parseInt(idteatroStr);
            }
            if (opcion.equalsIgnoreCase("mas")) {
                return funcionRepository.funcionesMasVistasPorcentaje(fechainicio, fechafin, idteatro);
            } else if (opcion.equalsIgnoreCase("menos")) {
                return funcionRepository.funcionesMenosVistasPorcentaje(fechainicio, fechafin, idteatro);
            }

        } catch (NumberFormatException e) {
            System.out.println("estado : error");
            System.out.println("msg : El id debe ser un número");
        } catch (ParseException e) {
            System.out.println("estado : error");
            System.out.println("msg : La fecha debe ser en el siguiente formato: YYYYMMDD");
        }
        return null;
    }

    @GetMapping("/vistas/funcion")
    public ResponseEntity<HashMap<String, Object>> funcionMasMenosVista(@RequestParam(value = "opcion", required = false) String opcion) {
        HashMap<String, Object> response = new HashMap<>();
        switch (opcion) {
            case "mas" -> {
                Optional<FuncionMasMenosVistaDto> funcionMas = funcionRepository.funcionMasVista();
                return funcionMas.map(funMas -> {
                    response.put("result", "success");
                    response.put("funcion", funMas);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }).orElseGet(() -> {
                    response.put("result", "failure");
                    response.put("msg", "Funcion más vista no encontrada");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
            }
            case "menos" -> {
                Optional<FuncionMasMenosVistaDto> funcionMenos = funcionRepository.funcionMenosVista();
                return funcionMenos.map(funMenos -> {
                    response.put("result", "success");
                    response.put("funcion", funMenos);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }).orElseGet(() -> {
                    response.put("result", "failure");
                    response.put("msg", "Funcion menos vista no encontrada");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
            }
            default -> {
                response.put("result", "failure");
                response.put("msg", "Selecciona una opcion entre mas y menos");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping("/calificacion/elenco")
    public ResponseEntity<HashMap<String, Object>> mejorElenco(@RequestParam(value = "rol", required = false) String rol) {
        HashMap<String, Object> response = new HashMap<>();
        switch (rol) {
            case "actor" -> {
                Optional<List<MejorElencoDto>> optActores = elencoRepository.elencoMejorCalificado(rol);
                return optActores.map(actores -> {
                    response.put("result", "success");
                    response.put("elenco", actores);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }).orElseGet(() -> {
                    response.put("result", "failure");
                    response.put("msg", "Actores mejor calificados no encontrados");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
            }
            case "director" -> {
                Optional<List<MejorElencoDto>> optDirectores = elencoRepository.elencoMejorCalificado(rol);
                return optDirectores.map(directores -> {
                    response.put("result", "success");
                    response.put("elenco", directores);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }).orElseGet(() -> {
                    response.put("result", "failure");
                    response.put("msg", "Directores mejor calificados no encontrados");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
            }
            default -> {
                response.put("result", "failure");
                response.put("msg", "El rol debe ser actor o director");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping("/calificacion/obra")
    public ResponseEntity<HashMap<String, Object>> obraMejorCalificada() {
        HashMap<String, Object> response = new HashMap<>();
        Optional<ObraMejorCalificadaDto> optObra = obraRepository.obraMejorCalificada();
        return optObra.map(obra -> {
            response.put("result", "success");
            response.put("obra", obra);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).orElseGet(() -> {
            response.put("result", "failure");
            response.put("msg", "Obra mejor calificada no encontrada");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        });
    }

}
