package pl.kopacz.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class ProductionItemDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal amount;

    private ProductDTO product;

    private Long productionId;

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

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public Long getProductionId() {
        return productionId;
    }

    public void setProductionId(Long productionId) {
        this.productionId = productionId;
    }

    @Override
    public String toString() {
        return "ProductionItemDTO{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            '}';
    }

}
