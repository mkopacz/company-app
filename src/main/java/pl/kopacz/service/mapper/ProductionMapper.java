package pl.kopacz.service.mapper;

import org.mapstruct.Mapper;
import pl.kopacz.domain.Production;
import pl.kopacz.domain.ProductionItem;
import pl.kopacz.service.dto.ProductionDTO;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductionItemMapper.class})
public interface ProductionMapper {

    ProductionDTO productionToProductionDTO(Production production);

    List<ProductionDTO> productionsToProductionDTOs(List<Production> productions);

    Production productionDTOToProduction(ProductionDTO productionDTO);

    List<Production> productionDTOsToProductions(List<ProductionDTO> productionDTOs);

    default ProductionItem productionItemFromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductionItem productionItem = new ProductionItem();
        productionItem.setId(id);
        return productionItem;
    }

}
