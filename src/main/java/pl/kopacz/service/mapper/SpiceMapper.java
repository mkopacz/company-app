package pl.kopacz.service.mapper;

import org.mapstruct.Mapper;
import pl.kopacz.domain.Spice;
import pl.kopacz.service.dto.SpiceDTO;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface SpiceMapper {

    SpiceDTO spiceToSpiceDTO(Spice spice);

    List<SpiceDTO> spicesToSpiceDTOs(List<Spice> spices);

    Spice spiceDTOToSpice(SpiceDTO spiceDTO);

    List<Spice> spiceDTOsToSpices(List<SpiceDTO> spiceDTOs);

}
