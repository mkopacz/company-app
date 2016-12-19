package pl.kopacz.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


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
    public String toString() {
        return "IngredientDTO{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            '}';
    }

}
