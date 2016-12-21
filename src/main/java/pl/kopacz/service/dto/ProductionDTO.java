package pl.kopacz.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ProductionDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime datetime;

    private Set<ProductionItemDTO> productionItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(ZonedDateTime datetime) {
        this.datetime = datetime;
    }

    public Set<ProductionItemDTO> getProductionItems() {
        return productionItems;
    }

    public void setProductionItems(Set<ProductionItemDTO> productionItems) {
        this.productionItems = productionItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductionDTO productionDTO = (ProductionDTO) o;

        if ( ! Objects.equals(id, productionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductionDTO{" +
            "id=" + id +
            ", datetime='" + datetime + "'" +
            '}';
    }

}
