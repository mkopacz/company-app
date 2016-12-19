package pl.kopacz.service;

import pl.kopacz.domain.ProductionItem;
import pl.kopacz.repository.ProductionItemRepository;
import pl.kopacz.service.dto.ProductionItemDTO;
import pl.kopacz.service.mapper.ProductionItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ProductionItem.
 */
@Service
@Transactional
public class ProductionItemService {

    private final Logger log = LoggerFactory.getLogger(ProductionItemService.class);
    
    @Inject
    private ProductionItemRepository productionItemRepository;

    @Inject
    private ProductionItemMapper productionItemMapper;

    /**
     * Save a productionItem.
     *
     * @param productionItemDTO the entity to save
     * @return the persisted entity
     */
    public ProductionItemDTO save(ProductionItemDTO productionItemDTO) {
        log.debug("Request to save ProductionItem : {}", productionItemDTO);
        ProductionItem productionItem = productionItemMapper.productionItemDTOToProductionItem(productionItemDTO);
        productionItem = productionItemRepository.save(productionItem);
        ProductionItemDTO result = productionItemMapper.productionItemToProductionItemDTO(productionItem);
        return result;
    }

    /**
     *  Get all the productionItems.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ProductionItemDTO> findAll() {
        log.debug("Request to get all ProductionItems");
        List<ProductionItemDTO> result = productionItemRepository.findAll().stream()
            .map(productionItemMapper::productionItemToProductionItemDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one productionItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ProductionItemDTO findOne(Long id) {
        log.debug("Request to get ProductionItem : {}", id);
        ProductionItem productionItem = productionItemRepository.findOne(id);
        ProductionItemDTO productionItemDTO = productionItemMapper.productionItemToProductionItemDTO(productionItem);
        return productionItemDTO;
    }

    /**
     *  Delete the  productionItem by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductionItem : {}", id);
        productionItemRepository.delete(id);
    }
}
