package pl.kopacz.repository;

import pl.kopacz.domain.Production;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Production entity.
 */
@SuppressWarnings("unused")
public interface ProductionRepository extends JpaRepository<Production,Long> {

}
