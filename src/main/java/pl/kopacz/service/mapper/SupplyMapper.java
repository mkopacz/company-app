package pl.kopacz.service.mapper;

import org.mapstruct.Mapper;
import pl.kopacz.domain.Supply;
import pl.kopacz.service.dto.SupplyDTO;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SpiceMapper.class})
public interface SupplyMapper {

    SupplyDTO supplyToSupplyDTO(Supply supply);

    List<SupplyDTO> suppliesToSupplyDTOs(List<Supply> supplies);

    Supply supplyDTOToSupply(SupplyDTO supplyDTO);

    List<Supply> supplyDTOsToSupplies(List<SupplyDTO> supplyDTOs);

}
