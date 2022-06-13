package pe.edu.pucp.teledramaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.teledramaapi.dto.MontoObraReporteDto;
import pe.edu.pucp.teledramaapi.dto.PorcentajeAsistenciaObraDto;
import pe.edu.pucp.teledramaapi.dto.ReporteDto;
import pe.edu.pucp.teledramaapi.repository.FuncionRepository;
import pe.edu.pucp.teledramaapi.repository.TeatroRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    FuncionRepository funcionRepository;

    @Autowired
    TeatroRepository teatroRepository;

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReporteDto> generarReporte() {
        return funcionRepository.generarReporte("20220301", "20220430", null, null);
    }

    @GetMapping(value = "/monto", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MontoObraReporteDto> montoRecaudadoPorObra() {
        return teatroRepository.montoRecaudadoPorObra("20220301", "20220430", null);
    }

    // Grafico: Filtro sin obra sin teatro | Filtro sin obra con teatro
    @PostMapping(value = "/asistencia/obra",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PorcentajeAsistenciaObraDto> obtenerAsistenciaPorObra(
            @RequestParam("opcion") String opcion,
            @RequestParam("inicio") String fechainicioStr,
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


}
