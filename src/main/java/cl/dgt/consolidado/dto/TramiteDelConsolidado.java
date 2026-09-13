package cl.dgt.consolidado.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TramiteDelConsolidado(
        Long id,
        String tipo,
        String estado,
        LocalDate fecha,
        BigDecimal montoDeclarado) {
}
