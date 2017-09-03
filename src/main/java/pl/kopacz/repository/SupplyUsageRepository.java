package pl.kopacz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kopacz.domain.SupplyUsage;

import java.util.List;

@SuppressWarnings("unused")
public interface SupplyUsageRepository extends JpaRepository<SupplyUsage, Long> {

    List<SupplyUsage> findByProductionId(Long productionId);

}
