package pe.edu.pucp.teledramaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.teledramaapi.dto.MontoObraReporteDto;
import pe.edu.pucp.teledramaapi.dto.ReporteDto;
import pe.edu.pucp.teledramaapi.repository.FuncionRepository;
import pe.edu.pucp.teledramaapi.repository.TeatroRepository;

import java.util.HashMap;
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
        return funcionRepository.generarReporte("20220301","20220430",null,null);
    }

    @GetMapping(value = "/monto", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MontoObraReporteDto> montoRecaudadoPorObra() {
        return teatroRepository.montoRecaudadoPorObra("20220301","20220430",null);
    }


}
