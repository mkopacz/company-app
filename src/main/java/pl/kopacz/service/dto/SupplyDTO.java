package pl.kopacz.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Supply entity.
 */
public class SupplyDTO implements Serializable {

    private Long id;

    @NotNull
    private String serialNumber;

    @NotNull
    private LocalDate expirationDate;

    @NotNull
    private Double amount;


    private Long spiceId;
    
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

    public Long getSpiceId() {
        return spiceId;
    }

    public void setSpiceId(Long spiceId) {
        this.spiceId = spiceId;
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

        if ( ! Objects.equals(id, supplyDTO.id)) return false;

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
