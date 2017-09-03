package pl.kopacz.service.mapper;

import org.mapstruct.Mapper;
import pl.kopacz.domain.Production;
import pl.kopacz.service.dto.ProductionDTO;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductionItemMapper.class})
public interface ProductionMapper {

    ProductionDTO productionToProductionDTO(Production production);

    List<ProductionDTO> productionsToProductionDTOs(List<Production> productions);

    Production productionDTOToProduction(ProductionDTO productionDTO);

    List<Production> productionDTOsToProductions(List<ProductionDTO> productionDTOs);

}
