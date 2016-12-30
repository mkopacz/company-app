package pl.kopacz.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "supply")
public class Supply implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @ManyToOne
    private Spice spice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Supply serialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public Supply expirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Double getAmount() {
        return amount;
    }

    public Supply amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Spice getSpice() {
        return spice;
    }

    public Supply spice(Spice spice) {
        this.spice = spice;
        return this;
    }

    public void setSpice(Spice spice) {
        this.spice = spice;
    }

    public void lowerAmount(Double amount) {
        this.amount -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Supply supply = (Supply) o;
        if (supply.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, supply.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Supply{" +
            "id=" + id +
            ", serialNumber='" + serialNumber + "'" +
            ", expirationDate='" + expirationDate + "'" +
            ", amount='" + amount + "'" +
            '}';
    }

}
