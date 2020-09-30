package poc.pucminas.repository;

import poc.pucminas.domain.Plano;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Plano entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {
}
