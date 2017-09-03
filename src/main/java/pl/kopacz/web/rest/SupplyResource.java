package pl.kopacz.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kopacz.service.SupplyService;
import pl.kopacz.service.dto.SpiceDTO;
import pl.kopacz.service.dto.SupplyDTO;
import pl.kopacz.service.dto.SupplyGroupDTO;
import pl.kopacz.service.dto.SupplyGroupItemDTO;
import pl.kopacz.web.rest.util.HeaderUtil;
import pl.kopacz.web.rest.util.PaginationUtil;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.*;

/**
 * REST controller for managing Supply.
 */
@RestController
@RequestMapping("/api")
public class SupplyResource {

    private final Logger log = LoggerFactory.getLogger(SupplyResource.class);
        
    @Inject
    private SupplyService supplyService;

    /**
     * POST  /supplies : Create a new supply.
     *
     * @param supplyDTO the supplyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new supplyDTO, or with status 400 (Bad Request) if the supply has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/supplies")
    @Timed
    public ResponseEntity<SupplyDTO> createSupply(@Valid @RequestBody SupplyDTO supplyDTO) throws URISyntaxException {
        log.debug("REST request to save Supply : {}", supplyDTO);
        if (supplyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("supply", "idexists", "A new supply cannot already have an ID")).body(null);
        }
        SupplyDTO result = supplyService.save(supplyDTO);
        return ResponseEntity.created(new URI("/api/supplies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("supply", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /supplies : Updates an existing supply.
     *
     * @param supplyDTO the supplyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated supplyDTO,
     * or with status 400 (Bad Request) if the supplyDTO is not valid,
     * or with status 500 (Internal Server Error) if the supplyDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/supplies")
    @Timed
    public ResponseEntity<SupplyDTO> updateSupply(@Valid @RequestBody SupplyDTO supplyDTO) throws URISyntaxException {
        log.debug("REST request to update Supply : {}", supplyDTO);
        if (supplyDTO.getId() == null) {
            return createSupply(supplyDTO);
        }
        SupplyDTO result = supplyService.save(supplyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("supply", supplyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /supplies : get all the supplies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of supplies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/supplies")
    @Timed
    public ResponseEntity<List<SupplyGroupDTO>> getAllSupplies(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Supplies");
        Page<SupplyDTO> page = supplyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/supplies");
        List<SupplyGroupDTO> supplyGroups = groupSupplies(page.getContent());
        return new ResponseEntity<>(supplyGroups, headers, HttpStatus.OK);
    }

    private List<SupplyGroupDTO> groupSupplies(List<SupplyDTO> supplies) {
        Map<SpiceDTO, Set<SupplyGroupItemDTO>> spicesToSupplies = supplies.stream()
            .filter(supply -> supply.getAmount() > 0)
            .collect(groupingBy(SupplyDTO::getSpice, mapping(SupplyGroupItemDTO::fromSupplyDTO, toSet())));
        return spicesToSupplies.entrySet().stream()
            .map(spiceAndSupplies ->
                SupplyGroupDTO.fromSpiceAndSupplies(spiceAndSupplies.getKey(), spiceAndSupplies.getValue())
            ).collect(toList());
    }

    /**
     * GET  /supplies/:id : get the "id" supply.
     *
     * @param id the id of the supplyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the supplyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/supplies/{id}")
    @Timed
    public ResponseEntity<SupplyDTO> getSupply(@PathVariable Long id) {
        log.debug("REST request to get Supply : {}", id);
        SupplyDTO supplyDTO = supplyService.findOne(id);
        return Optional.ofNullable(supplyDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /supplies/:id : delete the "id" supply.
     *
     * @param id the id of the supplyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/supplies/{id}")
    @Timed
    public ResponseEntity<Void> deleteSupply(@PathVariable Long id) {
        log.debug("REST request to delete Supply : {}", id);
        supplyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("supply", id.toString())).build();
    }

}
