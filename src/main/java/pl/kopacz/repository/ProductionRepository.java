package pl.kopacz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kopacz.domain.Production;
import pl.kopacz.repository.custom.ProductionRepositoryCustom;

@SuppressWarnings("unused")
public interface ProductionRepository extends JpaRepository<Production, Long>, ProductionRepositoryCustom {

}
