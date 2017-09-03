package pl.kopacz.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ProductReportDTO implements Serializable {

    private LocalDate date;
    private Double amount;
    private Set<ProductReportSpiceDTO> spices = new HashSet<>();

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Set<ProductReportSpiceDTO> getSpices() {
        return spices;
    }

    public void setSpices(Set<ProductReportSpiceDTO> spices) {
        this.spices = spices;
    }

}