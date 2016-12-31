package pl.kopacz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kopacz.domain.Supply;
import pl.kopacz.domain.SupplyUsage;
import pl.kopacz.repository.SupplyUsageRepository;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional
public class SupplyUsageService {

    private final Logger log = LoggerFactory.getLogger(SupplyUsageService.class);

    @Inject
    private SupplyUsageRepository supplyUsageRepository;

    public void returnSupplies(Long productionId) {
        log.debug("Request to return Supplies : {}", productionId);
        List<SupplyUsage> supplyUsages = supplyUsageRepository.findByProductionId(productionId);
        supplyUsages.forEach(supplyUsage -> {
            Supply supply = supplyUsage.getSupply();
            supply.increaseAmount(supplyUsage.getAmount());
        });
        supplyUsageRepository.delete(supplyUsages);
    }

}
