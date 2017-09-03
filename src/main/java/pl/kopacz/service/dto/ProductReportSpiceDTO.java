package pl.kopacz.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ProductReportSpiceDTO implements Serializable {

    private String spiceName;
    private Double recipieAmount;
    private Double usedAmount;
    private Set<ProductReportSpiceUsageDTO> usages = new HashSet<>();

    public String getSpiceName() {
        return spiceName;
    }

    public void setSpiceName(String spiceName) {
        this.spiceName = spiceName;
    }

    public Double getRecipieAmount() {
        return recipieAmount;
    }

    public void setRecipieAmount(Double recipieAmount) {
        this.recipieAmount = recipieAmount;
    }

    public Double getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(Double usedAmount) {
        this.usedAmount = usedAmount;
    }

    public Set<ProductReportSpiceUsageDTO> getUsages() {
        return usages;
    }

    public void setUsages(Set<ProductReportSpiceUsageDTO> usages) {
        this.usages = usages;
        this.usedAmount = usages.stream()
            .mapToDouble(ProductReportSpiceUsageDTO::getSpiceAmount)
            .sum();
    }

}
