package pl.kopacz.service.mapper;

import pl.kopacz.domain.*;
import pl.kopacz.service.dto.SupplyDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Supply and its DTO SupplyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SupplyMapper {

    @Mapping(source = "spice.id", target = "spiceId")
    SupplyDTO supplyToSupplyDTO(Supply supply);

    List<SupplyDTO> suppliesToSupplyDTOs(List<Supply> supplies);

    @Mapping(source = "spiceId", target = "spice")
    Supply supplyDTOToSupply(SupplyDTO supplyDTO);

    List<Supply> supplyDTOsToSupplies(List<SupplyDTO> supplyDTOs);

    default Spice spiceFromId(Long id) {
        if (id == null) {
            return null;
        }
        Spice spice = new Spice();
        spice.setId(id);
        return spice;
    }
}
