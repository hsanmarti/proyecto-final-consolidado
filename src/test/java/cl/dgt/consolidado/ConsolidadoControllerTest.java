package cl.dgt.consolidado;

import cl.dgt.consolidado.controllers.ConsolidadoController;
import cl.dgt.consolidado.controllers.ManejadorDeErrores;
import cl.dgt.consolidado.services.ConsolidadoService;
import cl.dgt.consolidado.services.ContribuyenteNoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ConsolidadoController.class)
@Import(ManejadorDeErrores.class)
@AutoConfigureMockMvc(addFilters = false)
class ConsolidadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConsolidadoService servicio;

    @Test
    void unRutQueNoExisteDevuelve404ConCuerpo() throws Exception {
        when(servicio.delPeriodo(eq("99.999.999-9"), any(LocalDate.class), any(LocalDate.class)))
                .thenThrow(new ContribuyenteNoEncontradoException("99.999.999-9"));

        mockMvc.perform(get("/consolidados/99.999.999-9")
                        .param("desde", "2026-01-01")
                        .param("hasta", "2026-12-31"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje")
                        .value("No existe el contribuyente 99.999.999-9"));
    }
}
