package pl.kopacz.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Ingredient entity.
 */
public class IngredientDTO implements Serializable {

    private Long id;

    @NotNull
    private Double amount;


    private Long spiceId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getSpiceId() {
        return spiceId;
    }

    public void setSpiceId(Long spiceId) {
        this.spiceId = spiceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IngredientDTO ingredientDTO = (IngredientDTO) o;

        if ( ! Objects.equals(id, ingredientDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "IngredientDTO{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            '}';
    }
}
