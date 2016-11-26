package pl.kopacz.repository;

import pl.kopacz.domain.Spice;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Spice entity.
 */
@SuppressWarnings("unused")
public interface SpiceRepository extends JpaRepository<Spice,Long> {

}
