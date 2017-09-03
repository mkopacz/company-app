package pl.kopacz.service.mapper;

import org.mapstruct.Mapper;
import pl.kopacz.domain.Ingredient;
import pl.kopacz.service.dto.IngredientDTO;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SpiceMapper.class})
public interface IngredientMapper {

    IngredientDTO ingredientToIngredientDTO(Ingredient ingredient);

    List<IngredientDTO> ingredientsToIngredientDTOs(List<Ingredient> ingredients);

    Ingredient ingredientDTOToIngredient(IngredientDTO ingredientDTO);

    List<Ingredient> ingredientDTOsToIngredients(List<IngredientDTO> ingredientDTOs);

}
