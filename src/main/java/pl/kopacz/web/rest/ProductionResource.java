package pl.kopacz.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kopacz.exception.InsufficientSpiceException;
import pl.kopacz.service.ProductionService;
import pl.kopacz.service.dto.ProductionDTO;
import pl.kopacz.web.rest.util.HeaderUtil;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Production.
 */
@RestController
@RequestMapping("/api")
public class ProductionResource {

    private final Logger log = LoggerFactory.getLogger(ProductionResource.class);

    @Inject
    private ProductionService productionService;

    /**
     * POST  /productions : Create a new production.
     *
     * @param productionDTO the productionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productionDTO, or with status 400 (Bad Request) if the production has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/productions")
    @Timed
    public ResponseEntity<ProductionDTO> createProduction(@Valid @RequestBody ProductionDTO productionDTO)
        throws URISyntaxException, InsufficientSpiceException {

        log.debug("REST request to save Production : {}", productionDTO);
        if (productionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("production", "idexists", "A new production cannot already have an ID")).body(null);
        }
        ProductionDTO result = productionService.save(productionDTO);
        return ResponseEntity.created(new URI("/api/productions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("production", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /productions : Updates an existing production.
     *
     * @param productionDTO the productionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productionDTO,
     * or with status 400 (Bad Request) if the productionDTO is not valid,
     * or with status 500 (Internal Server Error) if the productionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/productions")
    @Timed
    public ResponseEntity<ProductionDTO> updateProduction(@Valid @RequestBody ProductionDTO productionDTO)
        throws URISyntaxException, InsufficientSpiceException {

        log.debug("REST request to update Production : {}", productionDTO);
        if (productionDTO.getId() == null) {
            return createProduction(productionDTO);
        }
        ProductionDTO result = productionService.save(productionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("production", productionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /productions : get all the productions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productions in body
     */
    @GetMapping("/productions")
    @Timed
    public List<ProductionDTO> getAllProductions() {
        log.debug("REST request to get all Productions");
        return productionService.findAll();
    }

    /**
     * GET  /productions/:id : get the "id" production.
     *
     * @param id the id of the productionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/productions/{id}")
    @Timed
    public ResponseEntity<ProductionDTO> getProduction(@PathVariable Long id) {
        log.debug("REST request to get Production : {}", id);
        ProductionDTO productionDTO = productionService.findOne(id);
        return Optional.ofNullable(productionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /productions/:id : delete the "id" production.
     *
     * @param id the id of the productionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/productions/{id}")
    @Timed
    public ResponseEntity<Void> deleteProduction(@PathVariable Long id) {
        log.debug("REST request to delete Production : {}", id);
        productionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("production", id.toString())).build();
    }

    @ExceptionHandler(InsufficientSpiceException.class)
    private ResponseEntity<String> handleInsufficientSpiceException() {
        return ResponseEntity.badRequest()
            .contentType(MediaType.TEXT_PLAIN)
            .body("Brak przypraw dla tej produkcji.");
    }

}
