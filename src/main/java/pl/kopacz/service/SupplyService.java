package pl.kopacz.service;

import pl.kopacz.domain.Supply;
import pl.kopacz.repository.SupplyRepository;
import pl.kopacz.service.dto.SupplyDTO;
import pl.kopacz.service.mapper.SupplyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Supply.
 */
@Service
@Transactional
public class SupplyService {

    private final Logger log = LoggerFactory.getLogger(SupplyService.class);
    
    @Inject
    private SupplyRepository supplyRepository;

    @Inject
    private SupplyMapper supplyMapper;

    /**
     * Save a supply.
     *
     * @param supplyDTO the entity to save
     * @return the persisted entity
     */
    public SupplyDTO save(SupplyDTO supplyDTO) {
        log.debug("Request to save Supply : {}", supplyDTO);
        Supply supply = supplyMapper.supplyDTOToSupply(supplyDTO);
        supply = supplyRepository.save(supply);
        SupplyDTO result = supplyMapper.supplyToSupplyDTO(supply);
        return result;
    }

    /**
     *  Get all the supplies.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<SupplyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Supplies");
        Page<Supply> result = supplyRepository.findAll(pageable);
        return result.map(supply -> supplyMapper.supplyToSupplyDTO(supply));
    }

    /**
     *  Get one supply by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SupplyDTO findOne(Long id) {
        log.debug("Request to get Supply : {}", id);
        Supply supply = supplyRepository.findOne(id);
        SupplyDTO supplyDTO = supplyMapper.supplyToSupplyDTO(supply);
        return supplyDTO;
    }

    /**
     *  Delete the  supply by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Supply : {}", id);
        supplyRepository.delete(id);
    }
}
