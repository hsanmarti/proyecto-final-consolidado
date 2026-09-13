package cl.dgt.consolidado.controllers;

import cl.dgt.consolidado.dto.ConsolidadoContribuyente;
import cl.dgt.consolidado.services.ConsolidadoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class ConsolidadoController {

    private final ConsolidadoService servicio;

    public ConsolidadoController(ConsolidadoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/consolidados/{rut}")
    public ConsolidadoContribuyente consolidado(
            @PathVariable String rut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return servicio.delPeriodo(rut, desde, hasta);
    }
}
