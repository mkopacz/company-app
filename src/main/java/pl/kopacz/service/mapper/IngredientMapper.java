package pl.kopacz.service.mapper;

import pl.kopacz.domain.*;
import pl.kopacz.service.dto.IngredientDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Ingredient and its DTO IngredientDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IngredientMapper {

    @Mapping(source = "spice.id", target = "spiceId")
    IngredientDTO ingredientToIngredientDTO(Ingredient ingredient);

    List<IngredientDTO> ingredientsToIngredientDTOs(List<Ingredient> ingredients);

    @Mapping(source = "spiceId", target = "spice")
    @Mapping(target = "products", ignore = true)
    Ingredient ingredientDTOToIngredient(IngredientDTO ingredientDTO);

    List<Ingredient> ingredientDTOsToIngredients(List<IngredientDTO> ingredientDTOs);

    default Spice spiceFromId(Long id) {
        if (id == null) {
            return null;
        }
        Spice spice = new Spice();
        spice.setId(id);
        return spice;
    }
}
