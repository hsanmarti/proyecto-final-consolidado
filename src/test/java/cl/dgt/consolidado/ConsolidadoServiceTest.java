package cl.dgt.consolidado;

import cl.dgt.consolidado.dto.ConsolidadoContribuyente;
import cl.dgt.consolidado.entities.Contribuyente;
import cl.dgt.consolidado.entities.Tramite;
import cl.dgt.consolidado.repositories.ContribuyenteRepository;
import cl.dgt.consolidado.repositories.TramiteRepository;
import cl.dgt.consolidado.services.ConsolidadoService;
import cl.dgt.consolidado.services.ContadorDeConsolidados;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsolidadoServiceTest {

    @Mock
    private ContribuyenteRepository contribuyentes;

    @Mock
    private TramiteRepository tramites;

    @Test
    void elTotalSumaTodosLosMontosIncluidoUnRechazado() {
        String rut = "76.111.111-1";
        LocalDate desde = LocalDate.of(2026, 1, 1);
        LocalDate hasta = LocalDate.of(2026, 12, 31);

        when(contribuyentes.findByRut(rut))
                .thenReturn(Optional.of(new Contribuyente(rut, "Comercial Andes Ltda.")));
        when(tramites.delPeriodo(eq(rut), any(), any())).thenReturn(List.of(
                tramite(1L, "F29", "PAGADO", new BigDecimal("1200000.00")),
                tramite(2L, "F22", "PENDIENTE", new BigDecimal("3400000.00")),
                tramite(3L, "F29", "RECHAZADO", new BigDecimal("500000.00"))));

        ConsolidadoService servicio = new ConsolidadoService(
                contribuyentes, tramites, new ContadorDeConsolidados(new SimpleMeterRegistry()));

        ConsolidadoContribuyente resultado = servicio.delPeriodo(rut, desde, hasta);

        assertEquals(0, new BigDecimal("5100000.00").compareTo(resultado.totalDeclarado()));
        assertEquals(3, resultado.tramites().size());
    }

    private static Tramite tramite(Long id, String tipo, String estado, BigDecimal monto) {
        return new Tramite(id, tipo, estado, LocalDate.of(2026, 3, 1), monto, "SCL-CEN");
    }
}
