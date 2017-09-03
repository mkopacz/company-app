package pl.kopacz.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class SupplyGroupItemDTO implements Serializable {

    private Long id;

    @NotNull
    private String serialNumber;

    @NotNull
    private LocalDate expirationDate;

    @NotNull
    private Double amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public static SupplyGroupItemDTO fromSupplyDTO(SupplyDTO supplyDTO) {
        SupplyGroupItemDTO supplyGroupItemDTO = new SupplyGroupItemDTO();
        supplyGroupItemDTO.id = supplyDTO.getId();
        supplyGroupItemDTO.serialNumber = supplyDTO.getSerialNumber();
        supplyGroupItemDTO.expirationDate = supplyDTO.getExpirationDate();
        supplyGroupItemDTO.amount = supplyDTO.getAmount();
        return supplyGroupItemDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SupplyGroupItemDTO supplyDTO = (SupplyGroupItemDTO) o;

        if (!Objects.equals(id, supplyDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
