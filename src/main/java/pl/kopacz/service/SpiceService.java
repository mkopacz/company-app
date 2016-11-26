package pl.kopacz.service;

import pl.kopacz.domain.Spice;
import pl.kopacz.repository.SpiceRepository;
import pl.kopacz.service.dto.SpiceDTO;
import pl.kopacz.service.mapper.SpiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Spice.
 */
@Service
@Transactional
public class SpiceService {

    private final Logger log = LoggerFactory.getLogger(SpiceService.class);
    
    @Inject
    private SpiceRepository spiceRepository;

    @Inject
    private SpiceMapper spiceMapper;

    /**
     * Save a spice.
     *
     * @param spiceDTO the entity to save
     * @return the persisted entity
     */
    public SpiceDTO save(SpiceDTO spiceDTO) {
        log.debug("Request to save Spice : {}", spiceDTO);
        Spice spice = spiceMapper.spiceDTOToSpice(spiceDTO);
        spice = spiceRepository.save(spice);
        SpiceDTO result = spiceMapper.spiceToSpiceDTO(spice);
        return result;
    }

    /**
     *  Get all the spices.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<SpiceDTO> findAll() {
        log.debug("Request to get all Spices");
        List<SpiceDTO> result = spiceRepository.findAll().stream()
            .map(spiceMapper::spiceToSpiceDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one spice by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SpiceDTO findOne(Long id) {
        log.debug("Request to get Spice : {}", id);
        Spice spice = spiceRepository.findOne(id);
        SpiceDTO spiceDTO = spiceMapper.spiceToSpiceDTO(spice);
        return spiceDTO;
    }

    /**
     *  Delete the  spice by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Spice : {}", id);
        spiceRepository.delete(id);
    }
}
