package pl.kopacz.repository;

import pl.kopacz.domain.Supply;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Supply entity.
 */
@SuppressWarnings("unused")
public interface SupplyRepository extends JpaRepository<Supply,Long> {

}
