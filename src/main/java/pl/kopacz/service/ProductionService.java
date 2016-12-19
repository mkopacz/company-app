package pl.kopacz.service;

import pl.kopacz.domain.Production;
import pl.kopacz.repository.ProductionRepository;
import pl.kopacz.service.dto.ProductionDTO;
import pl.kopacz.service.mapper.ProductionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Production.
 */
@Service
@Transactional
public class ProductionService {

    private final Logger log = LoggerFactory.getLogger(ProductionService.class);
    
    @Inject
    private ProductionRepository productionRepository;

    @Inject
    private ProductionMapper productionMapper;

    /**
     * Save a production.
     *
     * @param productionDTO the entity to save
     * @return the persisted entity
     */
    public ProductionDTO save(ProductionDTO productionDTO) {
        log.debug("Request to save Production : {}", productionDTO);
        Production production = productionMapper.productionDTOToProduction(productionDTO);
        production = productionRepository.save(production);
        ProductionDTO result = productionMapper.productionToProductionDTO(production);
        return result;
    }

    /**
     *  Get all the productions.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ProductionDTO> findAll() {
        log.debug("Request to get all Productions");
        List<ProductionDTO> result = productionRepository.findAll().stream()
            .map(productionMapper::productionToProductionDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one production by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ProductionDTO findOne(Long id) {
        log.debug("Request to get Production : {}", id);
        Production production = productionRepository.findOne(id);
        ProductionDTO productionDTO = productionMapper.productionToProductionDTO(production);
        return productionDTO;
    }

    /**
     *  Delete the  production by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Production : {}", id);
        productionRepository.delete(id);
    }
}
