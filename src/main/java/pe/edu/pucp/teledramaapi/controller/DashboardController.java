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
    public List<MontoObraReporteDto> montoRecaudadoPorObra(
            @RequestParam(value = "inicio") String fechainicioStr,
            @RequestParam(value = "fin", required = false) String fechafinStr,
            @RequestParam(value = "idteatro", required = false) String idteatroStr) {

        try {
            SimpleDateFormat fechaformat = new SimpleDateFormat("yyyyMMdd");
            Date fechainicio = fechaformat.parse(fechainicioStr);
            Date fechafin = (fechafinStr != null && !fechafinStr.equals("")) ? fechaformat.parse(fechafinStr) : null;
            Integer idteatro = (idteatroStr != null && !idteatroStr.equals("")) ? Integer.parseInt(idteatroStr) : null;

            return teatroRepository.montoRecaudadoPorObra(fechainicio, fechafin, idteatro);

        } catch (NumberFormatException e) {
            System.out.println("Monto recaudado obra: \nestado : error \nmsg : El idteatro debe ser un número");
        } catch (ParseException e) {
            System.out.println("Monto recaudado obra: \nestado : error \nmsg : La fecha debe ser en el siguiente formato: YYYYMMDD");
        }
        return null;
    }

    // Grafico: Filtro sin obra sin teatro | Filtro sin obra con teatro
    @PostMapping(value = "/asistencia/obra", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PorcentajeAsistenciaObraDto> obtenerAsistenciaPorObra(
            @RequestParam(value = "opcion") String opcion,
            @RequestParam(value = "inicio") String fechainicioStr,
            @RequestParam(value = "fin", required = false) String fechafinStr,
            @RequestParam(value = "idteatro", required = false) String idteatroStr) {

        try {
            SimpleDateFormat fechaformat = new SimpleDateFormat("yyyyMMdd");
            Date fechainicio = fechaformat.parse(fechainicioStr);
            Date fechafin = (fechafinStr != null && !fechafinStr.equals("")) ? fechaformat.parse(fechafinStr) : null;
            Integer idteatro = (idteatroStr != null && !idteatroStr.equals("")) ? Integer.parseInt(idteatroStr) : null;

            if (opcion.equalsIgnoreCase("mas")) {
                return funcionRepository.funcionesMasVistasPorcentaje(fechainicio, fechafin, idteatro);
            } else if (opcion.equalsIgnoreCase("menos")) {
                return funcionRepository.funcionesMenosVistasPorcentaje(fechainicio, fechafin, idteatro);
            }

        } catch (NumberFormatException e) {
            System.out.println("Asistencia obra: \nestado : error \nmsg : El id debe ser un número");
        } catch (ParseException e) {
            System.out.println("Asistencia obra: \nestado : error \nmsg : La fecha debe ser en el siguiente formato: YYYYMMDD");
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

    @PostMapping(value = "/reporte", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReporteDto> generarReporte(
            @RequestParam(value = "inicio") String fechainicioStr,
            @RequestParam(value = "fin", required = false) String fechafinStr,
            @RequestParam(value = "idteatro", required = false) String idteatroStr,
            @RequestParam(value = "idobra", required = false) String idobraStr) {

        try {
            SimpleDateFormat fechaformat = new SimpleDateFormat("yyyyMMdd");
            Date fechainicio = fechaformat.parse(fechainicioStr);
            Date fechafin = (fechafinStr != null && !fechafinStr.equals("")) ? fechaformat.parse(fechafinStr) : null;
            Integer idteatro = (idteatroStr != null && !idteatroStr.equals("")) ? Integer.parseInt(idteatroStr) : null;
            Integer idobra = (idobraStr != null && !idobraStr.equals("")) ? Integer.parseInt(idobraStr) : null;

            return funcionRepository.generarReporte(fechainicio, fechafin, idobra, idteatro);

        } catch (NumberFormatException e) {
            System.out.println("Reporte: \nestado : error \nmsg : El id debe ser un número");
        } catch (ParseException e) {
            System.out.println("Reporte: \nestado : error \nmsg : La fecha debe ser en el siguiente formato: yyyyMMdd");
        }

        return null;
    }

}
