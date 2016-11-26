package pl.kopacz.service.mapper;

import pl.kopacz.domain.*;
import pl.kopacz.service.dto.SpiceDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Spice and its DTO SpiceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpiceMapper {

    SpiceDTO spiceToSpiceDTO(Spice spice);

    List<SpiceDTO> spicesToSpiceDTOs(List<Spice> spices);

    Spice spiceDTOToSpice(SpiceDTO spiceDTO);

    List<Spice> spiceDTOsToSpices(List<SpiceDTO> spiceDTOs);
}
