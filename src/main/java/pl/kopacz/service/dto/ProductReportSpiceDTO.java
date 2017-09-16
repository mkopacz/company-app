package pl.kopacz.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ProductReportSpiceDTO implements Serializable {

    private String spiceName;
    private BigDecimal recipieAmount;
    private BigDecimal usedAmount;
    private Set<ProductReportSpiceUsageDTO> usages = new HashSet<>();

    public String getSpiceName() {
        return spiceName;
    }

    public void setSpiceName(String spiceName) {
        this.spiceName = spiceName;
    }

    public BigDecimal getRecipieAmount() {
        return recipieAmount;
    }

    public void setRecipieAmount(BigDecimal recipieAmount) {
        this.recipieAmount = recipieAmount;
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public Set<ProductReportSpiceUsageDTO> getUsages() {
        return usages;
    }

    public void setUsages(Set<ProductReportSpiceUsageDTO> usages) {
        this.usages = usages;
        this.usedAmount = usages.stream()
            .map(ProductReportSpiceUsageDTO::getSpiceAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
