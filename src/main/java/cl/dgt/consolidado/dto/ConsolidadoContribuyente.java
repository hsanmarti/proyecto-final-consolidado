package cl.dgt.consolidado.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ConsolidadoContribuyente(
        String rut,
        String razonSocial,
        LocalDate desde,
        LocalDate hasta,
        List<TramiteDelConsolidado> tramites,
        BigDecimal totalDeclarado) {
}
