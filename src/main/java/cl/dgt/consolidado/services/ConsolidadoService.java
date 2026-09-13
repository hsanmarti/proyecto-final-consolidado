package cl.dgt.consolidado.services;

import cl.dgt.consolidado.dto.ConsolidadoContribuyente;
import cl.dgt.consolidado.dto.TramiteDelConsolidado;
import cl.dgt.consolidado.entities.Contribuyente;
import cl.dgt.consolidado.entities.Tramite;
import cl.dgt.consolidado.repositories.ContribuyenteRepository;
import cl.dgt.consolidado.repositories.TramiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ConsolidadoService {

    private final ContribuyenteRepository contribuyentes;
    private final TramiteRepository tramites;
    private final ContadorDeConsolidados contador;

    public ConsolidadoService(ContribuyenteRepository contribuyentes,
                              TramiteRepository tramites,
                              ContadorDeConsolidados contador) {
        this.contribuyentes = contribuyentes;
        this.tramites = tramites;
        this.contador = contador;
    }

    @Transactional(readOnly = true)
    public ConsolidadoContribuyente delPeriodo(String rut, LocalDate desde, LocalDate hasta) {
        Contribuyente contribuyente = contribuyentes.findByRut(rut)
                .orElseThrow(() -> new ContribuyenteNoEncontradoException(rut));

        List<TramiteDelConsolidado> delPeriodo = tramites.delPeriodo(rut, desde, hasta).stream()
                .map(ConsolidadoService::aDto)
                .toList();

        BigDecimal total = delPeriodo.stream()
                .map(TramiteDelConsolidado::montoDeclarado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        contador.emitidos().increment();

        return new ConsolidadoContribuyente(
                contribuyente.getRut(),
                contribuyente.getRazonSocial(),
                desde,
                hasta,
                delPeriodo,
                total);
    }

    private static TramiteDelConsolidado aDto(Tramite t) {
        return new TramiteDelConsolidado(
                t.getId(), t.getTipo(), t.getEstado(), t.getFecha(), t.getMonto());
    }
}
