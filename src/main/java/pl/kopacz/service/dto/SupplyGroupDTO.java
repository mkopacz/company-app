package pl.kopacz.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SupplyGroupDTO implements Serializable {

    private SpiceDTO spice;
    private BigDecimal totalAmount;
    private Set<SupplyGroupItemDTO> items = new HashSet<>();

    public SpiceDTO getSpice() {
        return spice;
    }

    public void setSpice(SpiceDTO spice) {
        this.spice = spice;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Set<SupplyGroupItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<SupplyGroupItemDTO> items) {
        this.items = items;
    }

    public static SupplyGroupDTO fromSpiceAndSupplies(SpiceDTO spiceDTO, Set<SupplyGroupItemDTO> items) {
        SupplyGroupDTO supplyGroup = new SupplyGroupDTO();
        supplyGroup.spice = spiceDTO;
        supplyGroup.items = items;
        supplyGroup.totalAmount = items.stream()
            .map(SupplyGroupItemDTO::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return supplyGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SupplyGroupDTO supplyGroupDTO = (SupplyGroupDTO) o;

        if (!Objects.equals(spice.getId(), supplyGroupDTO.spice.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(spice.getId());
    }

}
