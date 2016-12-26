package pl.kopacz.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ProductionItemDTO implements Serializable {

    private Long id;

    @NotNull
    private Double amount;

    private ProductDTO product;

    private Long productionId;

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
