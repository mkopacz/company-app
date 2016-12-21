package pl.kopacz.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.kopacz.domain.Product;
import pl.kopacz.domain.Production;
import pl.kopacz.domain.ProductionItem;
import pl.kopacz.service.dto.ProductionItemDTO;

import java.util.List;

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
