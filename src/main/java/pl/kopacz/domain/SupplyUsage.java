package pl.kopacz.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "supply_usage")
public class SupplyUsage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "amount", precision = 9, scale = 3, nullable = false)
    private BigDecimal amount;

    @NotNull
    @ManyToOne
    private Supply supply;

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

    public SupplyUsage amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Supply getSupply() {
        return supply;
    }

    public SupplyUsage supply(Supply supply) {
        this.supply = supply;
        return this;
    }

    public void setSupply(Supply supply) {
        this.supply = supply;
    }

    public Product getProduct() {
        return product;
    }

    public SupplyUsage product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Production getProduction() {
        return production;
    }

    public SupplyUsage production(Production production) {
        this.production = production;
        return this;
    }

    public void setProduction(Production production) {
        this.production = production;
    }

    public Spice getSupplySpice() {
        return supply.getSpice();
    }

    public String getSupplySerialNumber() {
        return supply.getSerialNumber();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SupplyUsage supplyUsage = (SupplyUsage) o;
        if (supplyUsage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, supplyUsage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "supplyUsage{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            '}';
    }

}
