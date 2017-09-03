package pl.kopacz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kopacz.domain.Production;
import pl.kopacz.domain.SupplyUsage;
import pl.kopacz.exception.InsufficientSpiceException;
import pl.kopacz.repository.ProductionRepository;
import pl.kopacz.service.dto.ProductionDTO;
import pl.kopacz.service.mapper.ProductionMapper;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductionService {

    private final Logger log = LoggerFactory.getLogger(ProductionService.class);

    @Inject
    private ProductionRepository productionRepository;

    @Inject
    private ProductionMapper productionMapper;

    @Inject
    private SupplyService supplyService;

    public ProductionDTO save(ProductionDTO productionDTO) throws InsufficientSpiceException {
        log.debug("Request to save Production : {}", productionDTO);
        Production production = productionMapper.productionDTOToProduction(productionDTO);
        if (production.getId() != null) {
            supplyService.returnSupplies(production.getId());
        }

        productionRepository.loadProducts(production);
        Set<SupplyUsage> supplyUsages = supplyService.useSupplies(production);
        production.setSupplyUsages(supplyUsages);

        production = productionRepository.save(production);
        ProductionDTO result = productionMapper.productionToProductionDTO(production);
        return result;
    }

    @Transactional(readOnly = true)
    public List<ProductionDTO> findAll() {
        log.debug("Request to get all Productions");
        List<ProductionDTO> result = productionRepository.findAll().stream()
            .map(productionMapper::productionToProductionDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    @Transactional(readOnly = true)
    public ProductionDTO findOne(Long id) {
        log.debug("Request to get Production : {}", id);
        Production production = productionRepository.findOne(id);
        ProductionDTO productionDTO = productionMapper.productionToProductionDTO(production);
        return productionDTO;
    }

    public void delete(Long id) {
        log.debug("Request to delete Production : {}", id);
        supplyService.returnSupplies(id);
        productionRepository.delete(id);
    }

}
