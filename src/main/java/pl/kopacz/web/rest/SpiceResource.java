package pl.kopacz.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.kopacz.service.SpiceService;
import pl.kopacz.web.rest.util.HeaderUtil;
import pl.kopacz.service.dto.SpiceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Spice.
 */
@RestController
@RequestMapping("/api")
public class SpiceResource {

    private final Logger log = LoggerFactory.getLogger(SpiceResource.class);
        
    @Inject
    private SpiceService spiceService;

    /**
     * POST  /spices : Create a new spice.
     *
     * @param spiceDTO the spiceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new spiceDTO, or with status 400 (Bad Request) if the spice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/spices")
    @Timed
    public ResponseEntity<SpiceDTO> createSpice(@Valid @RequestBody SpiceDTO spiceDTO) throws URISyntaxException {
        log.debug("REST request to save Spice : {}", spiceDTO);
        if (spiceDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("spice", "idexists", "A new spice cannot already have an ID")).body(null);
        }
        SpiceDTO result = spiceService.save(spiceDTO);
        return ResponseEntity.created(new URI("/api/spices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("spice", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /spices : Updates an existing spice.
     *
     * @param spiceDTO the spiceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated spiceDTO,
     * or with status 400 (Bad Request) if the spiceDTO is not valid,
     * or with status 500 (Internal Server Error) if the spiceDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/spices")
    @Timed
    public ResponseEntity<SpiceDTO> updateSpice(@Valid @RequestBody SpiceDTO spiceDTO) throws URISyntaxException {
        log.debug("REST request to update Spice : {}", spiceDTO);
        if (spiceDTO.getId() == null) {
            return createSpice(spiceDTO);
        }
        SpiceDTO result = spiceService.save(spiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("spice", spiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /spices : get all the spices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of spices in body
     */
    @GetMapping("/spices")
    @Timed
    public List<SpiceDTO> getAllSpices() {
        log.debug("REST request to get all Spices");
        return spiceService.findAll();
    }

    /**
     * GET  /spices/:id : get the "id" spice.
     *
     * @param id the id of the spiceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the spiceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/spices/{id}")
    @Timed
    public ResponseEntity<SpiceDTO> getSpice(@PathVariable Long id) {
        log.debug("REST request to get Spice : {}", id);
        SpiceDTO spiceDTO = spiceService.findOne(id);
        return Optional.ofNullable(spiceDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /spices/:id : delete the "id" spice.
     *
     * @param id the id of the spiceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/spices/{id}")
    @Timed
    public ResponseEntity<Void> deleteSpice(@PathVariable Long id) {
        log.debug("REST request to delete Spice : {}", id);
        spiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("spice", id.toString())).build();
    }

}
