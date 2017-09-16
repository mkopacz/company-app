package pl.kopacz.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class SupplyDTO implements Serializable {

    private Long id;

    @NotNull
    private String serialNumber;

    @NotNull
    private LocalDate expirationDate;

    @NotNull
    private BigDecimal amount;

    private SpiceDTO spice;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SupplyDTO supplyDTO = (SupplyDTO) o;

        if (!Objects.equals(id, supplyDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SupplyDTO{" +
            "id=" + id +
            ", serialNumber='" + serialNumber + "'" +
            ", expirationDate='" + expirationDate + "'" +
            ", amount='" + amount + "'" +
            '}';
    }

}
