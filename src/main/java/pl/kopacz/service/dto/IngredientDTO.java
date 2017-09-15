package pl.kopacz.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class IngredientDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal amount;

    private SpiceDTO spice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public SpiceDTO getSpice() {
        return spice;
    }

    public void setSpice(SpiceDTO spice) {
        this.spice = spice;
    }

    @Override
    public String toString() {
        return "IngredientDTO{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            '}';
    }

}
