package pl.kopacz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kopacz.domain.*;
import pl.kopacz.repository.ProductRepository;
import pl.kopacz.repository.ProductionItemRepository;
import pl.kopacz.service.dto.*;
import pl.kopacz.service.mapper.ProductMapper;
import pl.kopacz.service.util.RoundUtil;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.productDTOToProduct(productDTO);
        product = productRepository.save(product);
        ProductDTO result = productMapper.productToProductDTO(product);
        return result;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        log.debug("Request to get all Products");
        List<ProductDTO> result = productRepository.findAllWithEagerRelationships().stream()
            .map(productMapper::productToProductDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    @Transactional(readOnly = true)
    public ProductDTO findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        Product product = productRepository.findOneWithEagerRelationships(id);
        ProductDTO productDTO = productMapper.productToProductDTO(product);
        return productDTO;
    }

    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.delete(id);
    }

    public ProductReportDTO buildProductReport(Long productId) {
        ProductReportDTO productReport = new ProductReportDTO();
        productionItemRepository.findByProductId(productId).forEach(productionItem -> {
            productReport.setProductName(productionItem.getProductName());
            productReport.addReportItem(buildProductReportItem(productionItem));
        });
        return productReport;
    }

    private ProductReportItemDTO buildProductReportItem(ProductionItem productionItem) {
        ProductReportItemDTO productReportItem = new ProductReportItemDTO();
        productReportItem.setProductionAmount(productionItem.getAmount());
        productReportItem.setProductionDate(productionItem.getProductionDate());
        productReportItem.setUsedSpices(buildProductReportSpices(productionItem));
        return productReportItem;
    }

    private List<ProductReportSpiceDTO> buildProductReportSpices(ProductionItem productionItem) {
        BigDecimal productionItemAmount = productionItem.getAmount();
        Set<SupplyUsage> supplyUsages = productionItem.getProductionSupplyUsages().stream()
            .filter(supplyUsage -> supplyUsage.getProduct().equals(productionItem.getProduct()))
            .collect(Collectors.toSet());
        return productionItem.getProductIngredients().stream()
            .map(ingredient -> buildProductReportSpice(ingredient, supplyUsages, productionItemAmount))
            .collect(Collectors.toList());
    }

    private ProductReportSpiceDTO buildProductReportSpice(Ingredient ingredient, Set<SupplyUsage> supplyUsages,
                                                          BigDecimal productionItemAmount) {
        ProductReportSpiceDTO productReportSpice = new ProductReportSpiceDTO();
        productReportSpice.setSpiceName(ingredient.getSpiceName());
        BigDecimal recipeAmount = ingredient.getAmount().multiply(productionItemAmount).divide(BigDecimal.valueOf(100));
        productReportSpice.setRecipeAmount(RoundUtil.roundToNearest005(recipeAmount));
        productReportSpice.setSpiceUsages(buildProductReportSpiceUsages(ingredient.getSpice(), supplyUsages));
        return productReportSpice;
    }

    private List<ProductReportSpiceUsageDTO> buildProductReportSpiceUsages(Spice spice, Set<SupplyUsage> supplyUsages) {
        return supplyUsages.stream()
            .filter(supplyUsage -> supplyUsage.getSupplySpice().equals(spice))
            .map(this::buildProductReportSpiceUsage)
            .collect(Collectors.toList());
    }

    private ProductReportSpiceUsageDTO buildProductReportSpiceUsage(SupplyUsage supplyUsage) {
        ProductReportSpiceUsageDTO productReportSpiceUsage = new ProductReportSpiceUsageDTO();
        productReportSpiceUsage.setSpiceAmount(supplyUsage.getAmount());
        productReportSpiceUsage.setSerialNumber(supplyUsage.getSupplySerialNumber());
        return productReportSpiceUsage;
    }

}
