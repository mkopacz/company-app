package pl.kopacz.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "production_item")
public class ProductionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @ManyToOne
    private Product product;

    @NotNull
    @ManyToOne
    private Production production;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ProductionItem amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public ProductionItem product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Production getProduction() {
        return production;
    }

    public ProductionItem production(Production production) {
        this.production = production;
        return this;
    }

    public void setProduction(Production production) {
        this.production = production;
    }

    public String getProductName() {
        return product.getName();
    }

    public Set<Ingredient> getProductIngredients() {
        return product.getIngredients();
    }

    public LocalDate getProductionDate() {
        return production.getDate();
    }

    public Set<SupplyUsage> getProductionSupplyUsages() {
        return production.getSupplyUsages();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductionItem productionItem = (ProductionItem) o;
        if (productionItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, productionItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductionItem{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            '}';
    }

}
