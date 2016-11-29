package pl.kopacz.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.kopacz.domain.Ingredient;
import pl.kopacz.domain.Spice;
import pl.kopacz.service.dto.IngredientDTO;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface IngredientMapper {

    @Mapping(source = "spice.id", target = "spiceId")
    IngredientDTO ingredientToIngredientDTO(Ingredient ingredient);

    List<IngredientDTO> ingredientsToIngredientDTOs(List<Ingredient> ingredients);

    @Mapping(source = "spiceId", target = "spice")
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
