package pl.kopacz.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.kopacz.service.ProductionItemService;
import pl.kopacz.web.rest.util.HeaderUtil;
import pl.kopacz.service.dto.ProductionItemDTO;
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
 * REST controller for managing ProductionItem.
 */
@RestController
@RequestMapping("/api")
public class ProductionItemResource {

    private final Logger log = LoggerFactory.getLogger(ProductionItemResource.class);
        
    @Inject
    private ProductionItemService productionItemService;

    /**
     * POST  /production-items : Create a new productionItem.
     *
     * @param productionItemDTO the productionItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productionItemDTO, or with status 400 (Bad Request) if the productionItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/production-items")
    @Timed
    public ResponseEntity<ProductionItemDTO> createProductionItem(@Valid @RequestBody ProductionItemDTO productionItemDTO) throws URISyntaxException {
        log.debug("REST request to save ProductionItem : {}", productionItemDTO);
        if (productionItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("productionItem", "idexists", "A new productionItem cannot already have an ID")).body(null);
        }
        ProductionItemDTO result = productionItemService.save(productionItemDTO);
        return ResponseEntity.created(new URI("/api/production-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("productionItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /production-items : Updates an existing productionItem.
     *
     * @param productionItemDTO the productionItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productionItemDTO,
     * or with status 400 (Bad Request) if the productionItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the productionItemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/production-items")
    @Timed
    public ResponseEntity<ProductionItemDTO> updateProductionItem(@Valid @RequestBody ProductionItemDTO productionItemDTO) throws URISyntaxException {
        log.debug("REST request to update ProductionItem : {}", productionItemDTO);
        if (productionItemDTO.getId() == null) {
            return createProductionItem(productionItemDTO);
        }
        ProductionItemDTO result = productionItemService.save(productionItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("productionItem", productionItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /production-items : get all the productionItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productionItems in body
     */
    @GetMapping("/production-items")
    @Timed
    public List<ProductionItemDTO> getAllProductionItems() {
        log.debug("REST request to get all ProductionItems");
        return productionItemService.findAll();
    }

    /**
     * GET  /production-items/:id : get the "id" productionItem.
     *
     * @param id the id of the productionItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productionItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/production-items/{id}")
    @Timed
    public ResponseEntity<ProductionItemDTO> getProductionItem(@PathVariable Long id) {
        log.debug("REST request to get ProductionItem : {}", id);
        ProductionItemDTO productionItemDTO = productionItemService.findOne(id);
        return Optional.ofNullable(productionItemDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /production-items/:id : delete the "id" productionItem.
     *
     * @param id the id of the productionItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/production-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductionItem(@PathVariable Long id) {
        log.debug("REST request to delete ProductionItem : {}", id);
        productionItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("productionItem", id.toString())).build();
    }

}
