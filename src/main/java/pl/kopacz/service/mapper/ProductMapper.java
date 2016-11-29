package pl.kopacz.service.mapper;

import org.mapstruct.Mapper;
import pl.kopacz.domain.Ingredient;
import pl.kopacz.domain.Product;
import pl.kopacz.service.dto.ProductDTO;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class})
public interface ProductMapper {

    ProductDTO productToProductDTO(Product product);

    List<ProductDTO> productsToProductDTOs(List<Product> products);

    Product productDTOToProduct(ProductDTO productDTO);

    List<Product> productDTOsToProducts(List<ProductDTO> productDTOs);

    default Ingredient ingredientFromId(Long id) {
        if (id == null) {
            return null;
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        return ingredient;
    }

}
