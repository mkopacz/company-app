package pl.kopacz.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "production")
public class Production implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "datetime", nullable = false)
    private ZonedDateTime datetime;

    @NotNull
    @OneToMany(mappedBy = "production", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductionItem> productionItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDatetime() {
        return datetime;
    }

    public Production datetime(ZonedDateTime datetime) {
        this.datetime = datetime;
        return this;
    }

    public void setDatetime(ZonedDateTime datetime) {
        this.datetime = datetime;
    }

    public Set<ProductionItem> getProductionItems() {
        return productionItems;
    }

    public Production productionItems(Set<ProductionItem> productionItems) {
        this.productionItems = productionItems;
        return this;
    }

    public Production addProductionItem(ProductionItem productionItem) {
        productionItems.add(productionItem);
        productionItem.setProduction(this);
        return this;
    }

    public Production removeProductionItem(ProductionItem productionItem) {
        productionItems.remove(productionItem);
        productionItem.setProduction(null);
        return this;
    }

    public void setProductionItems(Set<ProductionItem> productionItems) {
        productionItems.forEach(this::addProductionItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Production production = (Production) o;
        if (production.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, production.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Production{" +
            "id=" + id +
            ", datetime='" + datetime + "'" +
            '}';
    }

}
