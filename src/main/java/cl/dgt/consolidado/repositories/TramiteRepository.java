package cl.dgt.consolidado.repositories;

import cl.dgt.consolidado.entities.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TramiteRepository extends JpaRepository<Tramite, Long> {

    @Query("select t from Tramite t join t.contribuyente c " +
           "where c.rut = :rut and t.fecha between :desde and :hasta " +
           "order by t.fecha, t.id")
    List<Tramite> delPeriodo(@Param("rut") String rut,
                             @Param("desde") LocalDate desde,
                             @Param("hasta") LocalDate hasta);
}
