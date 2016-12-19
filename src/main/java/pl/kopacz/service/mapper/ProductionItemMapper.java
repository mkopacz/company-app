package pl.kopacz.service.mapper;

import pl.kopacz.domain.*;
import pl.kopacz.service.dto.ProductionItemDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ProductionItem and its DTO ProductionItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductionItemMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "production.id", target = "productionId")
    ProductionItemDTO productionItemToProductionItemDTO(ProductionItem productionItem);

    List<ProductionItemDTO> productionItemsToProductionItemDTOs(List<ProductionItem> productionItems);

    @Mapping(source = "productId", target = "product")
    @Mapping(source = "productionId", target = "production")
    ProductionItem productionItemDTOToProductionItem(ProductionItemDTO productionItemDTO);

    List<ProductionItem> productionItemDTOsToProductionItems(List<ProductionItemDTO> productionItemDTOs);

    default Product productFromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }

    default Production productionFromId(Long id) {
        if (id == null) {
            return null;
        }
        Production production = new Production();
        production.setId(id);
        return production;
    }
}
