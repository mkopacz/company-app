package pl.kopacz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kopacz.domain.*;
import pl.kopacz.repository.ProductRepository;
import pl.kopacz.repository.ProductionItemRepository;
import pl.kopacz.service.dto.ProductDTO;
import pl.kopacz.service.dto.ProductReportDTO;
import pl.kopacz.service.dto.ProductReportSpiceDTO;
import pl.kopacz.service.dto.ProductReportSpiceUsageDTO;
import pl.kopacz.service.mapper.ProductMapper;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Product.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductionItemRepository productionItemRepository;

    @Inject
    private ProductMapper productMapper;

    /**
     * Save a product.
     *
     * @param productDTO the entity to save
     * @return the persisted entity
     */
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.productDTOToProduct(productDTO);
        product = productRepository.save(product);
        ProductDTO result = productMapper.productToProductDTO(product);
        return result;
    }

    /**
     *  Get all the products.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        log.debug("Request to get all Products");
        List<ProductDTO> result = productRepository.findAllWithEagerRelationships().stream()
            .map(productMapper::productToProductDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one product by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProductDTO findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        Product product = productRepository.findOneWithEagerRelationships(id);
        ProductDTO productDTO = productMapper.productToProductDTO(product);
        return productDTO;
    }

    /**
     *  Delete the  product by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.delete(id);
    }

    public List<ProductReportDTO> buildProductReports(Long productId) {
        return productionItemRepository.findByProductId(productId).stream()
            .map(this::buildProductReport)
            .collect(Collectors.toList());
    }

    private ProductReportDTO buildProductReport(ProductionItem productionItem) {
        ProductReportDTO productReport = new ProductReportDTO();
        productReport.setAmount(productionItem.getAmount());
        productReport.setDate(productionItem.getProductionDate());
        productReport.setSpices(buildProductReportSpices(productionItem));
        return productReport;
    }

    private Set<ProductReportSpiceDTO> buildProductReportSpices(ProductionItem productionItem) {
        Double productionItemAmount = productionItem.getAmount();
        Set<SupplyUsage> supplyUsages = productionItem.getProductionSupplyUsages();
        return productionItem.getProductIngredients().stream()
            .map(ingredient -> buildProductReportSpice(ingredient, supplyUsages, productionItemAmount))
            .collect(Collectors.toSet());
    }

    private ProductReportSpiceDTO buildProductReportSpice(Ingredient ingredient, Set<SupplyUsage> supplyUsages,
                                                          Double productionItemAmount) {
        ProductReportSpiceDTO productReportSpice = new ProductReportSpiceDTO();
        productReportSpice.setSpiceName(ingredient.getSpiceName());
        productReportSpice.setRecipieAmount(ingredient.getAmount() * productionItemAmount / 100);
        productReportSpice.setUsages(buildProductReportSpiceUsages(ingredient.getSpice(), supplyUsages));
        return productReportSpice;
    }

    private Set<ProductReportSpiceUsageDTO> buildProductReportSpiceUsages(Spice spice, Set<SupplyUsage> supplyUsages) {
        return supplyUsages.stream()
            .filter(supplyUsage -> supplyUsage.getSupplySpice().equals(spice))
            .map(this::buildProductReportSpiceUsage)
            .collect(Collectors.toSet());
    }

    private ProductReportSpiceUsageDTO buildProductReportSpiceUsage(SupplyUsage supplyUsage) {
        ProductReportSpiceUsageDTO productReportSpiceUsage = new ProductReportSpiceUsageDTO();
        productReportSpiceUsage.setSpiceAmount(supplyUsage.getAmount());
        productReportSpiceUsage.setSerialNumber(supplyUsage.getSupplySerialNumber());
        return productReportSpiceUsage;
    }

}
