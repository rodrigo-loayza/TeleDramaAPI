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

@RestController
@CrossOrigin
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

    // Obtener monto recaudado de cada obra
    @PostMapping(value = "/monto", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> montoRecaudadoPorObra(
            @RequestParam(value = "inicio") String fechainicioStr,
            @RequestParam(value = "fin", required = false) String fechafinStr,
            @RequestParam(value = "idteatro", required = false) String idteatroStr) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            SimpleDateFormat fechaformat = new SimpleDateFormat("yyyyMMdd");
            Date fechainicio = fechaformat.parse(fechainicioStr);
            Date fechafin = (fechafinStr != null && !fechafinStr.equals("")) ? fechaformat.parse(fechafinStr) : null;
            Integer idteatro = (idteatroStr != null && !idteatroStr.equals("")) ? Integer.parseInt(idteatroStr) : null;

            response.put("result", "success");
            response.put("data", teatroRepository.montoRecaudadoPorObra(fechainicio, fechafin, idteatro));
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (NumberFormatException e) {
            response.put("result", "failure");
            response.put("data", "El idteatro debe ser un número");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ParseException e) {
            response.put("result", "failure");
            response.put("data", "La fecha debe ser en el siguiente formato: YYYYMMDD");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Grafico: Filtro sin obra sin teatro | Filtro sin obra con teatro
    @PostMapping(value = "/asistencia/obra", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> obtenerAsistenciaPorObra(
            @RequestParam(value = "opcion") String opcion,
            @RequestParam(value = "inicio") String fechainicioStr,
            @RequestParam(value = "fin", required = false) String fechafinStr,
            @RequestParam(value = "idteatro", required = false) String idteatroStr) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            SimpleDateFormat fechaformat = new SimpleDateFormat("yyyyMMdd");
            Date fechainicio = fechaformat.parse(fechainicioStr);
            Date fechafin = (fechafinStr != null && !fechafinStr.equals("")) ? fechaformat.parse(fechafinStr) : null;
            Integer idteatro = (idteatroStr != null && !idteatroStr.equals("")) ? Integer.parseInt(idteatroStr) : null;

            switch (opcion) {
                case "mas" -> {
                    response.put("result", "success");
                    response.put("data", funcionRepository.funcionesMasVistasPorcentaje(fechainicio, fechafin, idteatro));
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                case "menos" -> {
                    response.put("result", "success");
                    response.put("data", funcionRepository.funcionesMenosVistasPorcentaje(fechainicio, fechafin, idteatro));
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                default -> {
                    response.put("result", "failure");
                    response.put("data", "La opcion debe ser mas o menos");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }

        } catch (NumberFormatException e) {
            response.put("result", "failure");
            response.put("data", "El id debe ser un número");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ParseException e) {
            response.put("result", "failure");
            response.put("data", "La fecha debe ser en el siguiente formato: YYYYMMDD");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/vistas/funcion")
    public ResponseEntity<HashMap<String, Object>> funcionMasMenosVista(@RequestParam(value = "opcion", required = false) String opcion) {
        HashMap<String, Object> response = new HashMap<>();
        switch (opcion) {
            case "mas" -> {
                Optional<FuncionMasMenosVistaDto> funcionMas = funcionRepository.funcionMasVista();
                return funcionMas.map(funMas -> {
                    response.put("result", "success");
                    response.put("data", funMas);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }).orElseGet(() -> {
                    response.put("result", "failure");
                    response.put("data", "Funcion más vista no encontrada");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
            }
            case "menos" -> {
                Optional<FuncionMasMenosVistaDto> funcionMenos = funcionRepository.funcionMenosVista();
                return funcionMenos.map(funMenos -> {
                    response.put("result", "success");
                    response.put("data", funMenos);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }).orElseGet(() -> {
                    response.put("result", "failure");
                    response.put("data", "Funcion menos vista no encontrada");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
            }
            default -> {
                response.put("result", "failure");
                response.put("data", "Selecciona una opcion entre mas y menos");
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
                    response.put("data", actores);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }).orElseGet(() -> {
                    response.put("result", "failure");
                    response.put("data", "Actores mejor calificados no encontrados");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
            }
            case "director" -> {
                Optional<List<MejorElencoDto>> optDirectores = elencoRepository.elencoMejorCalificado(rol);
                return optDirectores.map(directores -> {
                    response.put("result", "success");
                    response.put("data", directores);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }).orElseGet(() -> {
                    response.put("result", "failure");
                    response.put("data", "Directores mejor calificados no encontrados");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
            }
            default -> {
                response.put("result", "failure");
                response.put("data", "El rol debe ser actor o director");
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
            response.put("data", obra);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).orElseGet(() -> {
            response.put("result", "failure");
            response.put("data", "Obra mejor calificada no encontrada");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        });
    }

    @PostMapping(value = "/reporte", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> generarReporte(
            @RequestParam(value = "inicio") String fechainicioStr,
            @RequestParam(value = "fin", required = false) String fechafinStr,
            @RequestParam(value = "idteatro", required = false) String idteatroStr,
            @RequestParam(value = "idobra", required = false) String idobraStr) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            SimpleDateFormat fechaformat = new SimpleDateFormat("yyyyMMdd");
            Date fechainicio = fechaformat.parse(fechainicioStr);
            Date fechafin = (fechafinStr != null && !fechafinStr.equals("")) ? fechaformat.parse(fechafinStr) : null;
            Integer idteatro = (idteatroStr != null && !idteatroStr.equals("")) ? Integer.parseInt(idteatroStr) : null;
            Integer idobra = (idobraStr != null && !idobraStr.equals("")) ? Integer.parseInt(idobraStr) : null;

            response.put("result", "success");
            response.put("data", funcionRepository.generarReporte(fechainicio, fechafin, idobra, idteatro));
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (NumberFormatException e) {
            response.put("result", "failure");
            response.put("data", "El id debe ser un número");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ParseException e) {
            response.put("result", "failure");
            response.put("data", "La fecha debe ser en el siguiente formato: yyyyMMdd");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
