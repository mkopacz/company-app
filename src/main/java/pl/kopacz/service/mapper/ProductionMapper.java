package pl.kopacz.service.mapper;

import pl.kopacz.domain.*;
import pl.kopacz.service.dto.ProductionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Production and its DTO ProductionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductionMapper {

    ProductionDTO productionToProductionDTO(Production production);

    List<ProductionDTO> productionsToProductionDTOs(List<Production> productions);

    @Mapping(target = "productionItems", ignore = true)
    Production productionDTOToProduction(ProductionDTO productionDTO);

    List<Production> productionDTOsToProductions(List<ProductionDTO> productionDTOs);
}
